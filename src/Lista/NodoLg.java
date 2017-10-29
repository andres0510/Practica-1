package Lista;

public class NodoLg {
    
    /*
        Configuración del nodo con:
        1. Dato
        2. Tipo (T o NT)
        3. Tres ligas (izquierda, derecha, hijo)
        4. Fin de línea
    */
    
    private String dato;
    private char tipo;
    private NodoLg ligaD;
    private NodoLg ligaI;
    private NodoLg ligaH;
    private boolean finDeLinea;

    public NodoLg(String dato) {
        this.dato = dato;
        ligaD=ligaI=ligaH=null;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public NodoLg getLigaD() {
        return ligaD;
    }

    public void setLigaD(NodoLg ligaD) {
        this.ligaD = ligaD;
    }

    public NodoLg getLigaI() {
        return ligaI;
    }

    public void setLigaI(NodoLg ligaI) {
        this.ligaI = ligaI;
    }

    public NodoLg getLigaH() {
        return ligaH;
    }

    public void setLigaH(NodoLg ligaH) {
        this.ligaH = ligaH;
    }

    public boolean isFinDeLinea() {
        return finDeLinea;
    }

    public void setFinDeLinea(boolean finDeLinea) {
        this.finDeLinea = finDeLinea;
    }
    
}
