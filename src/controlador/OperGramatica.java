package controlador;

import Lista.ListaP;
import Lista.NodoLg;
import java.util.Stack;

public class OperGramatica {
    
    public static ListaP graToLista(String grama){                                  //Convertir la gramática String a lista generalizada
        int i=1;
        //int j;
        int largo=grama.length();
        String dato;
        String aux;
        char token;
        boolean finDeLinea=false;
        ListaP lista=new ListaP();
        while(i<largo){
            dato="";
            token=grama.charAt(i);
            while(token!='>'){
                dato=dato+Character.toString(token);
                i++;
                token=grama.charAt(i);
            }
            i=i+2;
            lista.insertarNodo(dato, 'n', false, false);
            aux=Character.toString(grama.charAt(i));
            while(!aux.equals("\n")){
                dato="";
                switch(aux){
                    case "<":   i++;
                                token=grama.charAt(i);
                                while(token!='>'){
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
                    case "/":   finDeLinea=true;
                                lista.insertarNodo(aux, 't', finDeLinea, true);
                                i++;
                                break;
                    default:    if(grama.charAt(i+1)=='\n'){
                                    finDeLinea=true;
                                }
                                lista.insertarNodo(aux, 't', finDeLinea, true);
                                i++;
                                break;            
                }
                aux=Character.toString(grama.charAt(i));
            }
            finDeLinea=false;
            i=i+2;
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
    
}
