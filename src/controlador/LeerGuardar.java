package controlador;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LeerGuardar {
    JFileChooser fc;
    FileInputStream archivoTxt;
    DataInputStream entrada;
    BufferedReader br;
    String texto="";        
    
    public String leerTxt() throws IOException{                             
        fc = new JFileChooser(); 
        
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);                                                   //Buscar solo archivos de texto
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Archivos Txt","txt"));
        fc.setAcceptAllFileFilterUsed(false);                                                       
        fc.showOpenDialog(fc);                                                                              //Mostrar selector de archivos
        try{                                                                                                //Leer el archivo de texto
                String nombreArchivo = fc.getSelectedFile().getAbsolutePath();
                archivoTxt = new FileInputStream(nombreArchivo);
                entrada = new DataInputStream(archivoTxt);
                br = new BufferedReader(new InputStreamReader(entrada,"iso-8859-1"));                       //Admitir caracteres especiales
                String aux;
                while((aux=br.readLine())!= null){
                    if(!aux.equals(""))
                        texto = texto + aux +'\n';
                }
                entrada.close();
                texto = texto.replaceAll(" ", "");                                                          //Eliminar espacios en la gramatica
                return texto;
        } catch(FileNotFoundException e){                                                                   //Error al cargar el archivo
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al leer el archivo");
            return null;
        }
    }
    
    public boolean esLetra(int ascii){                                                                      //Identificar si el caracter es una letra
        if(ascii<65 || (ascii>90 && ascii < 97) || ascii > 122){
            return false;                        
        }
        return true;
    }
    
    public boolean esNumero(int ascii){                                                                     //Identificar si el caracter es numero
        if(ascii<48 && ascii>57){
            return false;
        }
        return true;
    }
    
    public boolean validarGram(String gram){                                                                //Verificar la validez la de gramatica                                                      
        char token='\n';                                                                                
        char tokenAux;                                                                                      //Token analizado anteriormente
        int largo = gram.length();
        boolean llaveAbierta=false;                                                                         //Identificar si hay un "<" sin cerrar
        boolean derecha=false;                                                                              //Identificar si está en la parte derecha de la gramatica
        int i=0;
        int ascii;
        int asciiTokenAux;                                                                                  //Para verificar el último carater en el case default
        while(i<largo){
            tokenAux=token;
            token = gram.charAt(i);
            ascii = gram.codePointAt(i);
            switch(tokenAux){
                case '\n':  if(token != '<' && token != '\n'){
                                return false;
                            }                              
                            if(token == '<'){
                                llaveAbierta = true;
                            }
                            derecha=false;
                            break;
                case '<' :  if(!esLetra(ascii)){                                                            //En un no terminal solo se admiten letras
                                return false;                        
                            }
                            break;
                case '>' :  if(!llaveAbierta){
                                return false;
                            }
                            if(!derecha){                                                                   //Si está a la izquierda, solo puede pasar a la derecha al cerrar el NT
                                if(token != '='){
                                    return false;     
                                }
                            } else{                                                                         
                                if(!esLetra(ascii) && !esNumero(ascii) && token != '\n' && token != '<'){   //Solo se permiten letras, números, abrir un NT o salto de línea
                                    return false;                        
                                }
                            }
                            llaveAbierta=false;
                            if(token == '<'){
                                llaveAbierta = true;
                            }
                            break;
                case '=' :  if(derecha){                                                                    //Si ya se encuentra a la derecha
                                return false;
                            }
                            if(token != '<' && !esLetra(ascii) && !esNumero(ascii) && token != '/'){
                                return false;
                            }
                            if(token == '<'){
                                llaveAbierta = true;
                            }
                            derecha=true;
                            break;
                case '/' :  if(token != '\n'){                                                              //Si es la secuencia nula solo se permite pasar a otra línea
                                return false;
                            }
                            break;
                default :   asciiTokenAux = (int) tokenAux;
                            if(token == '<' && llaveAbierta){
                                    return false;
                                }
                            if(esNumero(asciiTokenAux)){
                                if(!esNumero(ascii) && !esLetra(ascii) && token != '\n' && token != '<'){
                                    return false;
                                }
                            } else{
                                if(esLetra(asciiTokenAux)){
                                    if(token == '>' && !llaveAbierta){
                                        return false;
                                    }
                                    if(token == '>'){
                                        llaveAbierta=false;
                                    }
                                    if(esNumero(ascii) && llaveAbierta){
                                        return false;
                                    }
                                    if(!derecha){
                                        if(!esLetra(ascii) && token != '>'){
                                            return false;
                                        }
                                    } else{
                                        if(!esNumero(ascii) && !esLetra(ascii) && token != '\n' && token != '<' && token != '>'){
                                            return false;
                                        }
                                        if(token == '\n' && llaveAbierta){
                                            return false;
                                        }
                                    }
                                } else{
                                    return false;
                                }                            
                            }
                            if(token == '<'){
                                llaveAbierta = true;
                            }
                            break;
            }
            i++;
        }
        if(llaveAbierta || (!derecha && token != '\n')){                                                    //Si hay una llave abierta o si la izquierda está incompleta
            return false;
        }
        return true;
    }
    
    public void guardarTxt(JTextArea txtGcode) throws IOException{                             
         try{
       JFileChooser filechooser = new JFileChooser();
       FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto", "txt", "gcode");
       filechooser.setFileFilter(filter);
       filechooser.showSaveDialog(filechooser);
       File guarda = filechooser.getSelectedFile();

       if(guarda != null){
      try ( 
      /*guardamos el archivo y le damos el formato directamente*/ 
          FileWriter save = new FileWriter(guarda+".txt")) {
          PrintWriter pw = null;
          pw = new PrintWriter(save);
          pw.println(txtGcode.getText());

          
      }
       }
   }catch(IOException ex){
   JOptionPane.showMessageDialog(null,"Su archivo no se ha guardado",
   "Advertencia",JOptionPane.WARNING_MESSAGE);}
         
   }

    
    
}
