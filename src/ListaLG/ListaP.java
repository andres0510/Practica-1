/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ListaLG;


public class ListaP {
    
     private NodoLg raiz,posicion;
      public ListaP(){ //Constructor
        NodoLg x=new NodoLg('*');//Nodo cabeza del arbol "r" significa Raiz
        posicion=raiz=x;//Ambos apuntadores dirigidos al nodo cabeza
    }
      public NodoLg getLast(){ //Obtener ultimo
        return posicion;
    }
    public NodoLg getRaiz(){
        return raiz;
    }
   public void insertarNodo(String n, NodoLg padre){ //Retorna un nodo
       
            if(padre.getLigaH()==null){
            if(buscarDato()){
                posicion=posicion.getLigaH();
                return;
            }
            else{
                conectarNodo(n, 'n');
                agregarNodoCabeza();
            
            }
                
            
            } 
        
        
    }
   
   public void conectarNodo(String dato, char tipo){
       NodoLg dat=new NodoLg(dato);
       dat.setTipo(tipo);
       posicion.setLigaD(dat);
       dat.setLigaI(posicion);
       posicion=dat;
   }
   public void agregarNodoCabeza(){
       NodoLg nodocabeza=new NodoLg("*");
       posicion.setLigaH(nodocabeza);
       posicion=nodocabeza;
   }
    
}
