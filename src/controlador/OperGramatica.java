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
        int i=0,j,n =grama.length();
        ListaP lista=new ListaP();
        NodoLg padre=new NodoLg("*");
        String aux,aux2;
        while(i<n){                                            //Recorre hasta la longitud total -1 para no tener en cuenta el ultimo salto de linea
            aux=Character.toString(grama.charAt(i+1));             //Este while controla el lado izquierdo de la gramatica
           
            j=i+4;                                                // Contador que me llevara al lado derecho ce la gramatica
            aux2=Character.toString(grama.charAt(j));
            lista.insertarNodo(aux, 'n', false, padre);            //Inserta el lado izquierdo de la gramatica
            while(!"\n".equals(aux2)){
                
                switch(aux2){                                      //Este while controla el lado derecho de la gramatica
                    case "<": j++;
                              aux2=Character.toString(grama.charAt(j));
                              if(grama.charAt(j+2)=='\n')           //Si es un NT y es final de linea envia true
                                  lista.insertarNodo(aux2, 'n', true, null);
                              else
                                  lista.insertarNodo(aux2, 'n', false, null); //De lo contrario envia falso
                              j++;
                              break;
                    case ">": j++;
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
            
            i=j+1; //Lleva la i al proximo renglon de la gramatica
           
        }
        
        return lista;
        
    }
    
    
}
