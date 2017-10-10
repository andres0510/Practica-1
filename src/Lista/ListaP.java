package Lista;

public class ListaP {
    private final NodoLg raiz;
    private NodoLg posicion;
    private NodoLg primer;
    private NodoLg ultimo;
    
    public ListaP(){                                                                        //Constructor
        NodoLg x = new NodoLg("*");                                                         //Nodo cabeza de la lista
        posicion=raiz=primer=ultimo=x;                                                                    //Ambos apuntadores dirigidos al nodo cabeza
    }

    public NodoLg getPrimer() {
        return primer;
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
       NodoLg nodocabeza=new NodoLg("*");
       posicion.setLigaH(nodocabeza);
       posicion=nodocabeza;
    }

    public void insertarNodo(String dato, char tipo, boolean finDeLinea, NodoLg padre){     //Hace las validaciones para insertar T y NT a la lista
        if(padre!=null){                                                                    //Si el nodo no tiene padre entonces pertenece a la izquierda de la gramática
            if(encontrarDato(dato)){                                                        //Si encuentra que el NT ya existe, entonces actualiza posicion para agregar ahí
                posicion=posicion.getLigaH();
            }
            else{                                                                           //Si no existe el NT, entonces lo agrega y le añade un nodo cabeza para empezar a agregar ahí
                conectarNodo(dato, tipo, finDeLinea);
                agregarNodoCabeza();
            }
        } else{                                                                             //El dato que se va a insertar a la lista hace parte de la derecha de la gramática
            if(posicion.getLigaD() == null){                                                //Si no hay datos insertados a la derecha
                if(finDeLinea){                                                             //Si la línea termina ahí, entonces añade nodo cabeza para seguir escribiendo
                    //
                    agregarNodoCabeza();
                    conectarNodo(dato, tipo, finDeLinea);
                } else{                                                                     //Si aún no termina de escribir la línea entonces sigue añadiendo hacia la derecha
                    conectarNodo(dato, tipo, finDeLinea);
                }
            }else{                                                                          //Si ya hay datos a la derecha
                if(posicion.getLigaD().getDato().equals(dato)){                             //Compara si el dato a ingresar es igual al que ya está guardado y actualiza posicion
                    posicion=posicion.getLigaD();
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
   
}
