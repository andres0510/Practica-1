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
    //Perra
    //Andres gay
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
                //Activar Botones en la interfaz aquí
                return texto;
        } catch(FileNotFoundException e){                                                           //Error al cargar el archivo
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al leer el archivo");
            return null;
        }
    }
    
}
