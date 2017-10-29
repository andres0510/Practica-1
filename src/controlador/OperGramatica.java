



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import Lista.ListaP;
import Lista.NodoLg;

/*
 */
public class OperGramatica {
    
    
    
    public static ListaP graToLista(String grama){
        int i=1,j,n =grama.length();
        ListaP lista=new ListaP();
        NodoLg padre=new NodoLg("*");
        String aux,aux2,aux3;
        char token;
        while(i<n){
            aux="";
            token=grama.charAt(i);
            while(token!='>'){
                aux=aux+Character.toString(grama.charAt(i));
                i++;
                token=grama.charAt(i);
            }                                       
                         
           
            j=i+2;                                                // Contador que me llevara al lado derecho ce la gramatica
            aux2=Character.toString(grama.charAt(j));
            lista.insertarNodo(aux, 'n', false, padre);            //Inserta el lado izquierdo de la gramatica
            while(!"\n".equals(aux2)){
                aux3="";
                switch(aux2){                                      //Este while controla el lado derecho de la gramatica
                    case "<": j++;
                              token=grama.charAt(j);
                              while(token!='>'){
                                 aux3=aux3+Character.toString(grama.charAt(j));
                                 j++;
                                 token=grama.charAt(j);
                                }                
                              if(grama.charAt(j+1)=='\n')           //Si es un NT y es final de linea envia true
                                  lista.insertarNodo(aux2, 'n', true, null);
                              else
                                  lista.insertarNodo(aux2, 'n', false, null); //De lo contrario envia falso
                              j++;
                              break;
                    case "/": lista.insertarNodo(aux, 't', true, null);  //Sera nuestro nulo en la gramatica
                              j++;
                              break;
                    default:  if(grama.charAt(j+1)=='\n')                 //Si es un T y es final de linea envia Verdadero
                                  lista.insertarNodo(aux2, 't', true, null);
                              else                                          //De lo contrario envia falso
                                  lista.insertarNodo(aux2, 't', false, null);
                              j++;
                              break; 
                                      
                }
                
                aux2=Character.toString(grama.charAt(j));          //Actualiza el Dato a procesar
            
            }
            
            i=j+2; //Lleva la i al proximo renglon de la gramatica
           
        }
        
        return lista;
        
    }
    
    public static void recorrer(NodoLg p){
        if(p!=null) {
            System.out.println(p.getDato());
             if(p.getLigaH()!=null){
                recorrer(p.getLigaH().getLigaD());
                
            }
            recorrer(p.getLigaD());
                
            
            
           
            
        
        
    }  
        
    
    }
    
    
    
    
    
}
