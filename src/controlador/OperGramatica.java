package controlador;

import Lista.ListaP;
import Lista.NodoLg;

public class OperGramatica {
    private boolean formEsp=true;
    
    public static ListaP graToLista(String grama){                                  //Convertir la gramática String a lista generalizada
        int i=1;
        int largo=grama.length();
        String dato;
        String aux;
        char token;
        boolean finDeLinea=false;
        ListaP lista=new ListaP();
        while(i<largo){                                                             //Recorrer el String completo
            dato="";
            token=grama.charAt(i);
            while(token!='>'){                                                      //Concatenar el nombre del N, en caso de que tenga más de un token
                dato=dato+Character.toString(token);
                i++;
                token=grama.charAt(i);
            }
            i=i+2;
            lista.insertarNodo(dato, 'n', false, false);                            //Insertar el N a la lista
            aux=Character.toString(grama.charAt(i));
            while(!aux.equals("\n")){                                               //Recorrer el lado derecho de la producción
                dato="";
                switch(aux){
                    case "<":   i++;
                                token=grama.charAt(i);
                                while(token!='>'){                                  //Concatenar el nombre del N
                                    dato=dato+Character.toString(token);
                                    i++;
                                    token=grama.charAt(i);
                                }
                                if(grama.charAt(i+1)=='\n'){
                                    finDeLinea=true;
                                }
                                lista.insertarNodo(dato, 'n', finDeLinea, true);
                                i++;
                                break;
                    case "/":   finDeLinea=true;                                    //Representa nuestro λ
                                lista.insertarNodo(aux, 't', finDeLinea, true);
                                i++;
                                break;
                    default:    if(grama.charAt(i+1)=='\n'){                        //En caso de ser terminales los agrega uno por uno
                                    finDeLinea=true;
                                }
                                lista.insertarNodo(aux, 't', finDeLinea, true);
                                i++;
                                break;            
                }
                aux=Character.toString(grama.charAt(i));                            //Autualizar el token a evaluar
            }
            finDeLinea=false;
            i=i+2;                                                                  //Al saltar línea, i+1 siempre será '<', se salta entonces a i+2
        }
        return lista;
    }
    
    public static void recorrer(NodoLg p){
        if(p!=null){
            System.out.println(p.getDato());
            if(!p.getDato().equals("*")){
                System.out.println(p.getTipo());
                System.out.println(p.isFinDeLinea());
                System.out.println("");
            }
            recorrer(p.getLigaD());
            if(p.getLigaH()!=null){
                recorrer(p.getLigaH());
            }
        }
    }
    
    public boolean formaEspecial(ListaP lista){                                     //Ayuda a determinar si la gramática es de la forma especial y así poder hacer el autómata
        NodoLg pos = lista.getRaiz().getLigaD();
        recorreColumna(pos, 't');                                                   //Recorre los hijos
        if(pos.getLigaD()!=null){                                                   //Si hay más N hacia la derecha, realiza el mismo proceso
            recorreFila(pos.getLigaD(), 'x');
        }
        return formEsp;
    }
    
    public void recorreFila(NodoLg p, char tipo){                                   //Ayuda a recorrer la lista hacia la derecha en el método formaEspecial()
        NodoLg pos = p;
        if(tipo!='x'){                                                              //x será el tipo que indica que no se encuentra buscando nada
            if(pos.getTipo()==tipo){                                                
                if(tipo=='t'){                                                      //Si coinciden el tipo buscado y encontrado con un T
                    if(pos.getLigaD()==null){
                        if(!pos.getDato().equals("/")){                             //Si solo hay un T, entonces no es de la forma especial, a menos que ese T sea λ
                            formEsp=false;
                        }
                    } else{
                        recorreFila(pos.getLigaD(), 'n');                           //En caso contrario, busca el N faltante a la producción hacia la derecha y en los hijos(si tiene)
                    }
                    if(pos.getLigaH()!=null){
                        recorreColumna(pos, 'n');
                    }
                }
                if(tipo=='n'){                                                      //Si coinciden el tipo buscado y encontrado con un N
                    if(pos.getLigaD()!=null || pos.getLigaH()!=null){               //Si ya encontró la estructura <N>=T<N> y aún no acaba la hilera, entonces no es de la forma especial
                        formEsp=false;
                    }
                }
            } else{                                                                 //Si no coincide el tipo buscado y el encontrado, entonces no es de la forma especial
                formEsp=false;
            }
        } else{                                                                     //el apuntador ha vuelto a la línea principal, realiza el mismo proceso para el siguiente padre
            recorreColumna(pos, 't');
            if(pos.getLigaD()!=null){
                recorreFila(pos.getLigaD(), 'x');
            }
        }
    }
    
    public void recorreColumna(NodoLg p, char tipo){                                //Ayuda a recorrer los hijos en el método formaEspecial()
        NodoLg pos = p;
        do{                                                                         //Recorre todos los nodos cabeza hijos de p
            pos=pos.getLigaH();
            recorreFila(pos.getLigaD(), tipo);
        }while(pos.getLigaH()!=null);
    }
    
}
