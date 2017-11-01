package controlador;

import Lista.ListaP;
import Lista.NodoLg;
import java.util.ArrayList;

public class OperGramatica {
    private boolean formEsp=true;
    private static ArrayList<String> listaVivos=new ArrayList<String>();
    private static ArrayList<String> listaAlcan=new ArrayList<String>();
    private static ArrayList<String> listaEntradas=new ArrayList<String>();
   
    private static String [][] matrizAuto;
    private static String grama="";
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
           /* if(!p.getDato().equals("*")){
                System.out.println(p.getTipo());
                System.out.println(p.isFinDeLinea());
                System.out.println("");
            }*/
            
            if(p.getLigaH()!=null){
                recorrer(p.getLigaH());
            }
            recorrer(p.getLigaD());
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
   /////////////////////////////////////////////////////////////////////////////// 
   //Metodos para encontrar los no terminales vivos de la gramatica 
    
    //Estos dos primeros metodos buscan cuales No terminales tienen solo cadenas de terminales a su derecha
    public static void listaTermi(ListaP lista){
        boolean b;
        int j=0;
        NodoLg primero=lista.getPrimer().getLigaD();        
        while(!lista.isEnd(primero)){
            b=busqueTerminales(primero.getLigaH(), primero, lista);
            if(b){
                listaVivos.add(j, primero.getDato());
                j++;
            }
            primero=primero.getLigaD();
        }
              
    }
    
   public static boolean busqueTerminales(NodoLg hijo, NodoLg padre, ListaP lista){
       NodoLg l;
       if(hijo!=null){
           if((hijo.getDato().equals("*")) && (padre.getTipo()=='t') && (hijo.getLigaD()==null))
               return true;
           l=hijo.getLigaD();
           while((!lista.isEnd(l)) && (l.getTipo()=='t')){
               if(l.getLigaH()!=null){
                  if(busqueTerminales(l.getLigaH(),l, lista))
                      return true;              
               }               
               l=l.getLigaD();           
           }
           
           if(lista.isEnd(l))
               return true;
           if(hijo.getLigaH()!=null)
               return busqueTerminales(hijo.getLigaH(), padre, lista);   
           
       }
       return false;
   }
   
   //Estos ultimos dos me retornan la lista de los vivos y me va completando el conjunto con los mismo
    public static ArrayList<String> listaNomu(ListaP lista){
        boolean b;
        int j=0;
                       
        while(j<listaVivos.size()){
            
            NodoLg primero=lista.getPrimer().getLigaD();
            while(!lista.isEnd(primero)){
                if(listaVivos.indexOf(primero.getDato())!=-1)
                    primero=primero.getLigaD();
                else{
                    b=busqueNoMu(primero.getLigaH(), lista, listaVivos);
                    if(b)
                        listaVivos.add(primero.getDato());
                    primero=primero.getLigaD();
                }
            }
            j++;        
        }
        return listaVivos;
        
    }
    
    public static boolean busqueNoMu(NodoLg hijo, ListaP lista, ArrayList<String> list){
       NodoLg l;
       int j=0;
       if(hijo!=null){
           l=hijo.getLigaD();
           while((j==0) && !(lista.isEnd(l))){
               if(l.getTipo()=='n')
                   if(list.indexOf(l.getDato())==-1)
                       j=1;
               if(l.getLigaH()!=null)
                  return busqueNoMu(l.getLigaH(), lista, list);     
                              
               l=l.getLigaD();           
           }
           
           if(j==0)
               return true;
           if(hijo.getLigaH()!=null)
               return busqueNoMu(hijo.getLigaH(), lista, list);   
           
       }
       return false;
   }
  
        //Metodo para encontrar los no vivos
    
    // Con este metodo hallo los muertos teniendo la lista de vivos 
    public static ArrayList<String> listaMuertos(ListaP lista){
        NodoLg primero=lista.getPrimer().getLigaD();
        ArrayList<String> listaMuer=new ArrayList<String>();
        while(!lista.isEnd(primero)){
            if(listaVivos.indexOf(primero.getDato())==-1)
                listaMuer.add(primero.getDato());
            primero=primero.getLigaD();
        }
        return listaMuer;
        
    }
    
       
    
    
    //Metodos para los que se pueden alcanzar
    
    public static ArrayList<String> listaAlcan(ListaP lista){
        boolean b;
        int j=0,estados=listaEsta(lista).size();
        listaAlcan.add(lista.getPrimer().getLigaD().getDato()); //Agrega el primer no terminal de la gramatica dado que este es siempre alcanzable
        while(j<listaAlcan.size()){
            NodoLg primero=lista.getPrimer().getLigaD();
            while(!lista.isEnd(primero)){
                    if(listaAlcan.indexOf(primero.getDato())==-1)
                        primero=primero.getLigaD();
                    else{
                    busqueAlcan(primero.getLigaH(), lista, listaAlcan);
                    primero=primero.getLigaD();
                    }
                
            }
            if(listaAlcan.size()==estados)
                j=listaAlcan.size();
            else
                j++;        
        }
        return listaAlcan;
        
    }
    
    public static void busqueAlcan(NodoLg hijo, ListaP lista, ArrayList<String> list){
       NodoLg l;
       if(hijo!=null){ 
           l=hijo.getLigaD();
           while(!lista.isEnd(l)){
               if(l.getTipo()=='n')
                   if(list.indexOf(l.getDato())==-1)
                       list.add(l.getDato());
               if(l.getLigaH()!=null)
                       busqueAlcan(l.getLigaH(), lista, list);     
                  l=l.getLigaD();           
           }
           if(hijo.getLigaH()!=null)
               busqueAlcan(hijo.getLigaH(), lista, list);           
           
   }   
    }
     //Luego teniendo la lista de alcanzables se realiza el metodo para los inalcanzables
    public static ArrayList<String> listaInalcanzables(ListaP lista){
        NodoLg primero=lista.getPrimer().getLigaD();
        ArrayList<String> listaInal=new ArrayList<String>();
        while(!lista.isEnd(primero)){
            if(listaAlcan.indexOf(primero.getDato())==-1)
                listaInal.add(primero.getDato());
            primero=primero.getLigaD();
        }
        return listaInal;
        
    }
    
    
  /////////////////////////////
    
    //Estos Metodos siguientes seran necesarios para la construccion del automata de la gramatica en forma especial ingresada
       public static ArrayList<String> listaEntrad(ListaP lista){
        NodoLg primero=lista.getPrimer().getLigaD();
        while(!lista.isEnd(primero)){
                    
                    busqueEntra(primero.getLigaH(), listaEntradas);
                    primero=primero.getLigaD();
                          
            } 
        
        return listaEntradas;
        
    }
       
   public static void busqueEntra(NodoLg hijo, ArrayList<String> list){
       NodoLg l;
       if(hijo!=null){ 
           l=hijo.getLigaD();
               if((l.getTipo()=='t')&&(!l.getDato().equals("/")))
                   if(list.indexOf(l.getDato())==-1)
                       list.add(l.getDato());
                  
           if(hijo.getLigaH()!=null)
               busqueEntra(hijo.getLigaH(), list);           
           
   }   
    }  
   
    public static ArrayList<String> listaEsta(ListaP lista){
        ArrayList<String> listaaux=new ArrayList<String>();
        NodoLg p=lista.getPrimer().getLigaD();
        while(!lista.isEnd(p)){
            listaaux.add(p.getDato());
            p=p.getLigaD();
        }
        return listaaux;
    }
    
    //Retorna la matriz que representa el automata
    public static String [][] autoMatriz(ListaP lista){
        ArrayList estados=listaEsta(lista);
        ArrayList entradas=listaEntrad(lista);
        int filas=estados.size()+1;
        int columnas=entradas.size()+2;
        NodoLg primer=lista.getPrimer().getLigaD();
        NodoLg l = null;
        matrizAuto=new String[filas][columnas];
        int i=1,k;
        while(i<estados.size()+1){
            matrizAuto[i][0]=(String) estados.get(i-1);
            i++;
        }
        i=1;
        while(i<entradas.size()+1){
            matrizAuto[0][i]=(String) estados.get(i-1);
            i++;        
        }
        i=1;
        while(!lista.isEnd(primer)){
            k=0;
            l=primer.getLigaH();
            while((k<entradas.size())){
                while((l!=null)){
                        lleneMatriz(i,k+1,l,(String)entradas.get(k),'n',entradas.size());
                        l=l.getLigaH();
                    }
               l=primer.getLigaH();
               k++;
            }
            
            primer=primer.getLigaD();
            i++;
        }
                    
       return matrizAuto;     
            
    }
    
    public static void lleneMatriz(int fila, int columna, NodoLg hijo, String entrada, char bandera, int aceptacion){
    
        if(hijo!=null){
            
            NodoLg l=hijo.getLigaD();
            if(l.getDato().equals("/"))                
                matrizAuto[fila][aceptacion+1]="1";
            
            
            if((l.getDato().equals(entrada))&&(bandera=='n')){
                matrizAuto[fila][columna]=l.getLigaD().getDato();
                if(l.getLigaH()!=null)
                    lleneMatriz(fila,columna,l.getLigaH(),entrada,'t',aceptacion);                             
            }
            if(bandera=='t'){
                matrizAuto[fila][columna]=matrizAuto[fila][columna]+","+l.getDato();
                if(l.getLigaH()!=null)
                    lleneMatriz(fila,columna,l.getLigaH(),entrada,'t',aceptacion);
                
                
            } 
             
            
            }
      
    }
    
    
    
   
    
    //Los siguiente dos metodos me escriben la gramatica de lista a String
    public static String toGrama(ListaP lista){
        NodoLg primero=lista.getPrimer().getLigaD();
            while(!lista.isEnd(primero)){
                    grama=grama+"<"+primero.getDato()+">";
                    agregar(primero.getLigaH(), lista, "<"+primero.getDato()+">", "");
                    primero=primero.getLigaD();
                }
            
                    
        
        return grama;
        
    }
    public static void agregar(NodoLg hijo, ListaP lista, String padre, String bandera){
        if(hijo!=null){
            NodoLg l;
            grama=grama+"=";
            if(!bandera.equals(""))
                grama=grama+bandera;
            
            l=hijo.getLigaD();
            while(!lista.isEnd(l)){
                if(l.getTipo()=='n')
                    grama=grama+"<"+l.getDato()+">";
                if(l.getTipo()=='t')
                    grama=grama+l.getDato();
                
                l=l.getLigaD();
            }
            grama=grama+"\n";
            l=hijo.getLigaD();
            while(!lista.isEnd(l)){
                if(l.getLigaH()!=null){
                    grama=grama+padre;
                    agregar(l.getLigaH(), lista, padre, l.getDato());
                    
                }
                    
                l=l.getLigaD();            
            }
            if(hijo.getLigaH()!=null){
                grama=grama+padre;
                agregar(hijo.getLigaH(), lista, padre, bandera);
            }
        }
    
    }
   
}
