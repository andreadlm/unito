package arg9;

import java.awt.*;
import javax.swing.*;

public class RettangoloColorato extends Rettangolo {
    
    // Colore del rettangolo
    private java.awt.Color colore;
    
    // Costruttore
    public RettangoloColorato(int base, int altezza, java.awt.Color colore) {
        super(base, altezza);
        this.colore = colore;
    }

    // Disegna il rettangolo colorato con il contesto grafico @g
    @Override
    public void draw(Graphics g) {
        java.awt.Color tmp = g.getColor();

        g.setColor(colore);
        super.draw(g);

        g.setColor(tmp);
    }
}