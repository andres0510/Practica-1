package controlador;
import java.awt.Color;
import javax.swing.*;

public class ControladorInterfaz {

    public void iniciarBotones(JButton val, JButton grdA, JButton autD, JButton simp){      //Configuracion por defecto de los botones
        val.setEnabled(false);
        grdA.setEnabled(false);
        autD.setEnabled(false);
        simp.setEnabled(false); 
    }
    
    public void iniciarText(JTextField txt1, JTextArea txt2, JTextArea txt3){               //Configuracion por defecto de los cuadros de texto
        txt1.setEditable(false);
        txt1.setBackground(new Color(236,236,236));
        txt2.setEditable(false);
        txt2.setBackground(new Color(236,236,236));
        txt3.setEditable(false);
        txt3.setBackground(new Color(236,236,236));
    }
}