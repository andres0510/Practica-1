package controlador;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LeerGuardar {
    JFileChooser fc;
    FileInputStream archivoTxt;
    DataInputStream entrada;
    BufferedReader br;
    String texto="";        
    
    public String leerTxt() throws IOException{                             
        fc = new JFileChooser();     
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);                                           //Buscar solo archivos de texto
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Archivos Txt","txt"));
        fc.setAcceptAllFileFilterUsed(false);                                                       
        fc.showOpenDialog(fc);                                                                      //Mostrar selector de archivos
        try{                                                                                        //Leer el archivo de texto
                String nombreArchivo = fc.getSelectedFile().getAbsolutePath();
                archivoTxt = new FileInputStream(nombreArchivo);
                entrada = new DataInputStream(archivoTxt);
                br = new BufferedReader(new InputStreamReader(entrada,"iso-8859-1"));               //Admitir caracteres especiales
                String aux;
                while((aux=br.readLine())!= null){
                    texto = texto + aux + "\n";
                }
                entrada.close();
                texto = texto.replaceAll(" ", "");                                                  //Eliminar espacios en la gramatica
                return texto;
        } catch(FileNotFoundException e){                                                           //Error al cargar el archivo
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al leer el archivo");
            return null;
        }
    }
    
    public boolean esLetra(int ascii){                                                              //Identificar si el caracter es una letra
        if(ascii<65 || (ascii>90 && ascii < 97) || ascii > 122){
            return false;                        
        }
        return true;
    }
    
    public boolean esNumero(int ascii){                                                             //Identificar si el caracter es numero
        if(ascii<48 && ascii>57){
            return false;
        }
        return true;
    }
    
    public boolean validarGram(String gram){                                                        //Verificar la validez la de gramatica                                                      
        char token='\n';                                                                            //Token analizado anteriormente
        char tokenAux;
        int largo = gram.length();
        boolean llaveAbierta=false;                                                                 //Identificar si hay un "<" sin cerrar
        boolean derecha=false;                                                                      //Identificar si está en la parte derecha de la gramatica
        boolean hayNoTerm=false;
        int i=0;
        int ascii;
        int asciiTokenAux;                                                                          //Para verificar el último carater en el case default
        while(i<largo){
            tokenAux=token;
            token = gram.charAt(i);
            ascii = gram.codePointAt(i);
            switch(tokenAux){
                case '\n':  if(token != '<' && token != '\n' && !esLetra(ascii) && !esNumero(ascii)){
                                return false;
                            }                              
                            if(token == '<'){
                                llaveAbierta = true;
                            }
                            derecha=false;
                            hayNoTerm=false;
                            break;
                case '<' :  if(!derecha){
                                hayNoTerm=true;
                            }
                            if(!esLetra(ascii)){                                                            //En un no terminal solo se admiten letras
                                return false;                        
                            }
                            break;
                case '>' :  if (!llaveAbierta){
                                return false;
                            }
                            if (!derecha){                                                                  //Si está a la izquierda solo puede pasar a la derecha o abrir otro no terminal
                                if(token != '=' && token != '<' && !esLetra(ascii) && !esNumero(ascii)){
                                    return false;     
                                }
                            } else{                                                                         //Si está a la derecha
                                if(!esLetra(ascii) && !esNumero(ascii) && token != '\n' && token != '<'){   //Solo se permiten letras, números, abrir un no terminal o salto de línea
                                    return false;                        
                                }
                            }
                            llaveAbierta=false;
                            if(token == '<'){
                                llaveAbierta = true;
                            }
                            break;
                case '=' :  if (derecha){                                                                   //Si ya se encuentra a la derecha
                                return false;
                            }
                            if(!hayNoTerm){
                                return false;
                            }
                            if (token != '<' && !esLetra(ascii) && !esNumero(ascii) && token != '/'){
                                return false;
                            }
                            if(token == '<'){
                                llaveAbierta = true;
                            }
                            derecha=true;
                            break;
                case '/' :  if (token != '\n'){                                                             //Si es la secuencia nula solo se permite pasar a otra línea
                                return false;
                            }
                            break;
                default :   asciiTokenAux = (int) tokenAux;
                            if (token == '<' && llaveAbierta){
                                    return false;
                                }
                            if(esNumero(asciiTokenAux)){
                                if(!derecha){
                                    if(!esNumero(ascii) && token != '=' && token != '<'){
                                        return false;
                                    }
                                } else{
                                    if(!esNumero(ascii) && token != '\n' && token != '<'){
                                        return false;
                                    }
                                }
                            } else{
                                if(esLetra(asciiTokenAux)){
                                    if (token == '>' && !llaveAbierta){
                                        return false;
                                    }
                                    if (token == '>'){
                                        llaveAbierta=false;
                                    }
                                    if (esNumero(ascii) && llaveAbierta){
                                        return false;
                                    }
                                    if(!derecha){
                                        if(!esNumero(ascii) && !esLetra(ascii) && token != '=' && token != '<' && token != '>'){
                                            return false;
                                        }
                                        if(token == '=' && llaveAbierta){
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
        if(llaveAbierta || (!derecha && token != '\n')){
            return false;
        }
        return true;
    }
    
}
