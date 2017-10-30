package Lista;

import java.util.ArrayList;

public class ListaP {
    private final NodoLg raiz;
    private NodoLg posicion;
    private NodoLg posAux;
    private NodoLg primer;
    private NodoLg ultimo;

    public NodoLg getRaiz() {
        return raiz;
    }
    
    public ListaP(){                                                                        //Constructor
        NodoLg x = new NodoLg("*");                                                         //Nodo cabeza de la lista
        posicion=raiz=primer=ultimo=x;                                                      //Ambos apuntadores dirigidos al nodo cabeza
    }

    public NodoLg getPrimer() {
        return primer;
    }
    public boolean isEnd(NodoLg l){
        return l==null;
    }
    
    public boolean esVacia(ListaP lista){
        NodoLg p=lista.getPrimer().getLigaD();
        return p==null;
    }
    
    public void setPrimer(NodoLg primer) {
        this.primer = primer;
    }

    public NodoLg getUltimo() {
        return ultimo;
    }

    public void setUltimo(NodoLg ultimo) {
        this.ultimo = ultimo;
    }

    public void conectarNodo(String dato, char tipo, boolean fin){                          //Conecta un nuevo nodo hacia la derecha
        NodoLg dat=new NodoLg(dato);
        dat.setTipo(tipo);
        dat.setFinDeLinea(fin);
        posicion.setLigaD(dat);
        dat.setLigaI(posicion);
        posicion=dat;
    }
   
    public void agregarNodoCabeza(){                                                        //Conecta un nodo cabeza como hijo de un nodo cualquiera
        while(posicion.getLigaH()!=null){                                                    //Si existen más nodos cabeza hacia abajo
            posicion = posicion.getLigaH();
        }
        NodoLg nodocabeza=new NodoLg("*");
        posicion.setLigaH(nodocabeza);
        posicion=nodocabeza;
    }

    public void insertarNodo(String dato, char tipo, boolean finDeLinea, boolean padre){    //Hace las validaciones para insertar T y NT a la lista
        if(padre==false){                                                                   //Si no tiene padre entonces se encuentra en la parte izquierda de la producción
            if(encontrarDato(dato)){                                                        //Si encuentra que el NT ya existe, entonces actualiza posicion para agregar ahí
                posicion=posicion.getLigaH();
            }
            else{                                                                           //Si no existe el NT, entonces lo agrega y le añade un nodo cabeza para empezar a agregar ahí
                conectarNodo(dato, tipo, finDeLinea);
                agregarNodoCabeza();
            }
        } else{                                                                             //Si tiene padre es de la parte derecha de la producción
            if(posicion.getLigaD() == null){                                                //Si no hay datos insertados a la derecha
                if(posicion.isFinDeLinea()){                                                //Si la línea termina ahí, entonces añade nodo cabeza para seguir escribiendo
                    agregarNodoCabeza();
                    conectarNodo(dato, tipo, finDeLinea);
                } else{                                                                     //Si aún no termina de escribir la línea entonces sigue añadiendo hacia la derecha
                    conectarNodo(dato, tipo, finDeLinea);
                }
            }else{                                                                          //Si ya hay datos a la derecha
                if(posicion.getLigaD().getDato().equals(dato)){                             //Compara si el dato a ingresar es igual al que ya está guardado y actualiza posición
                    posicion=posicion.getLigaD();
                    if(finDeLinea && posicion.getLigaD()!=null){                            //Si la producción que se está escribiendo es más corta que la existente
                        reacomodarNodo();
                    }
                } else{                                                                     //Si es diferente, entonces añade una sub-línea
                    agregarNodoCabeza();
                    conectarNodo(dato, tipo, finDeLinea);
                }
            }
        }
    }
   
    public boolean encontrarDato(String d){                                                 //Recorre la línea principal desde la raíz para buscar un NT
        posicion=raiz;
        while(posicion.getLigaD() != null){
            posicion=posicion.getLigaD();
            if(posicion.getDato().equals(d)){
               return true;
            }
        }
        return false;
    }

    private void reacomodarNodo() {                                                         //Realiza el reacomodo del nodo cuando la producción que se está escribiendo es más corta que la
        posAux=posicion.getLigaD();                                                         //ya existente en la lista
        posicion.setFinDeLinea(true);
        posicion.setLigaD(null);
        agregarNodoCabeza();
        posicion.setLigaD(posAux);
        posAux.setLigaI(posicion);
    }
    
    public void desconectar(ListaP lista, ArrayList<String> conjunto){  //Metodo que desconecta los no terminales que dan paso a la simplificacion
                                                                         //De la gramatica
        NodoLg p=lista.getPrimer().getLigaD();
        while(!lista.isEnd(p)){
            if(conjunto.indexOf(p.getDato())!=-1){
               p.getLigaI().setLigaD(p.getLigaD());
               p.getLigaD().setLigaI(p.getLigaI());
            }
           p=p.getLigaD();
            
        
        }
    }
   
}
