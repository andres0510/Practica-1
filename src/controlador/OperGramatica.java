package controlador;

import Lista.ListaP;
import Lista.NodoLg;
import java.util.ArrayList;

public class OperGramatica {
    
    private boolean formEsp=true;
    private static ArrayList<String> listaVivos=new ArrayList<String>();
    private static ArrayList<String> listaAlcan=new ArrayList<String>();
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
    
    public boolean noTermDefinidos(NodoLg p, ListaP lista){                         //Busca si todos los NT tienen por lo menos una producción
        NodoLg main=lista.getRaiz();
        boolean encontrado=false;
        if(p!=null){
            if(p.getTipo()=='n'){
                while(main.getLigaD()!=null){
                    main=main.getLigaD();
                    if(main.getDato().equals(p.getDato())){
                        encontrado = true;
                    }
                }
            } else{
                encontrado=true;
            }
            if(p.getLigaH()!=null && encontrado){
                encontrado = noTermDefinidos(p.getLigaH(), lista);
            }
            if(encontrado){
                encontrado = noTermDefinidos(p.getLigaD(), lista);
            }
            return encontrado;   
        }
        return true;
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
    
    public boolean tieneSalida(ListaP lista){                                       //Verifica si la gramática posee los λ que permitan dejar de procesar
        int cantNT = totalEstados(lista);
        boolean b;
        boolean v[] = new boolean[cantNT];                                          //Vector para identificar si un NT posee una producción con λ
        String t[] = new String[cantNT];                                            //Vector de NT para compararlos con su respectivo booleano
        t = vectorNoTerminales(lista, t);
        v = vectorBooleanos(lista, v);
        b=tieneSecNula(v);
        if(!b){                                                                     //Si no tiene secuencias nulas entonces no tiene cómo parar
            return false;
        }
        b=buscaSalida(lista, v, t);
        return b;
    }
    
    private boolean buscaSalida(ListaP lista, boolean[] v, String nt[]) {           //Busca una salida para cada uno de los NT de la lista, buscando si van a otros NT que tienen salida
        int i=0;
        int j;
        boolean cambios=false;                                                      //Detecta si el vector de booleanos ha sufrido cambios, para seguir operando en caso de que sí
        NodoLg pos=lista.getRaiz();                                                 //Recorre la línea principal de la lista generalizada, los NT
        NodoLg cabezas;                                                             //Recorre los nodos cabeza que pos pueda tener
        NodoLg term;                                                                //Recorre los T de la lista
        NodoLg cabT;                                                                //Recorre las posibles cabezas que puedan tener los T de la lista
        NodoLg noTerm;                                                              //Recorre los NT no padres de la lista
        while(i<v.length){                                                          //Mientras no sea el final de la lista
            pos=pos.getLigaD();
            if(v[i]==false){                                                        //Si el NT aún no tiene salida
                cabezas=pos;
                while(cabezas.getLigaH()!=null && v[i]==false){                     //Recorre las cabezas debajo de pos
                    cabezas=cabezas.getLigaH();
                    term=cabezas.getLigaD();
                    noTerm=term.getLigaD();
                    j=buscaPosicionNT(nt, noTerm.getDato());
                    if(v[j]==true){                                                 //Si el terminal al que va tiene salida, entonces i tiene salida
                        v[i]=true;
                        cambios=true;
                    }
                    if(term.getLigaH()!=null && v[i]==false){                       //Si el terminal tiene hijos, los recorre de igual forma
                        cabT=term;
                        while(cabT.getLigaH()!=null && v[i]==false){
                            cabT=cabT.getLigaH();
                            noTerm=cabT.getLigaD();
                            j=buscaPosicionNT(nt, noTerm.getDato());
                            if(v[j]==true){
                                v[i]=true;
                                cambios=true;
                            }
                        }
                    }
                }
            }
            i++;
        }
        if(vectorVerdadero(v)){                                                     //Si todos los NT tienen salida devuelve true
            return true;
        }
        if(cambios){                                                                //Si el vector sufrió cambios, entonces todavía se puede buscar salida
            return buscaSalida(lista, v, nt);
        }
        return false;                                                               //Si no ha sufrido cambios y el vector aún no está lleno con 'true' entonces no tiene salida
    }
    
    public String[] vectorNoTerminales(ListaP lista, String vector[]){              //Ayuda a pasar el los NT de la gramática a un vector
        NodoLg pos = lista.getRaiz();
        int i=0;
        do{
            pos=pos.getLigaD();
            vector[i] = pos.getDato();
            i++;
        }while(pos.getLigaD()!=null);
        return vector;
    }
    
    public boolean[] vectorBooleanos(ListaP lista, boolean vector[]){               //Ayuda a llenar el vector de booleanos creado en tieneSalida()
        NodoLg pos=lista.getRaiz();
        NodoLg aux;
        int i=0;
        do{
            pos=pos.getLigaD();
            aux=pos;
            do{
                aux=aux.getLigaH();
                if(aux.getLigaD().getDato().equals("/")){
                    vector[i]=true;
                }           
            }while(aux.getLigaH()!=null);
            i++;
        }while(pos.getLigaD()!=null);
        
        return vector;
    }
    
    public boolean tieneSecNula(boolean vec[]){                                     //Verifica si el vector de booleanos creado en tieneSalida(), tiene algún valor en true
        int i=0;
        while(i<vec.length){
            if(vec[i]==true){
                return true;
            }
            i++;
        }
        return false;
    }
    
    public boolean vectorVerdadero(boolean vec[]){                                  //Verifica si el vector tiene todos sus elementos en true
        int i=0;
        while(i<vec.length){
            if(vec[i]==false){
                return false;
            }
            i++;
        }
        return true;
    }
    
    public int buscaPosicionNT(String[] vec, String dato){                          //Busca el índice i del NT en el vector creado en buscaSalida()
        int i=0;
        do{
            if(vec[i].equals(dato)){
                return i;
            }
            i++;
        }while(i<vec.length);
        return -1;
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
   
   //Estos ultimos dos me retornan la lista de los vivos
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
    
    public static int totalEstados(ListaP lista){
        int i=0;
        NodoLg p=lista.getPrimer().getLigaD();
        while(!lista.isEnd(p)){
            i++;
            p=p.getLigaD();
        }
        return i;
    }
    
    //Metodos para los que se pueden alcanzar
    
    public static ArrayList<String> listaAlcan(ListaP lista){
        boolean b;
        int j=0,estados=totalEstados(lista);
        listaAlcan.add(lista.getPrimer().getLigaD().getDato());
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
                if(l.getTipo()=='n'){
                    if(list.indexOf(l.getDato())==-1){
                        list.add(l.getDato());
                    }
                }
                if(l.getLigaH()!=null){
                    busqueAlcan(l.getLigaH(), lista, list);  
                }
                l=l.getLigaD();           
            }
            if(hijo.getLigaH()!=null){
                busqueAlcan(hijo.getLigaH(), lista, list);
            }
        }
    }
    
    //Luego teniendo la lista de alcanzables se realiza el metodo para los inalcanzables
    public static ArrayList<String> listaInalcanzables(ListaP lista){
        NodoLg primero=lista.getPrimer().getLigaD();
        ArrayList<String> listaInal=new ArrayList<String>();
        while(!lista.isEnd(primero)){
            if(listaAlcan.indexOf(primero.getDato())==-1){
                listaInal.add(primero.getDato());
            }
            primero=primero.getLigaD();
        }
        return listaInal;
        
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
            if(!bandera.equals("")){
                grama=grama+bandera;
            }
            l=hijo.getLigaD();
            while(!lista.isEnd(l)){
                if(l.getTipo()=='n'){
                    grama=grama+"<"+l.getDato()+">";
                }
                if(l.getTipo()=='t'){
                    grama=grama+l.getDato();
                }
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
   
    public boolean reconocerHilera(ListaP lista, String hilera){
        
    
        return false;
    }
    
}
