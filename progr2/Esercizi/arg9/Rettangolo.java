package arg9;

// Classe fornita dall'esercizio

// Rettangolo.java
import java.awt.*;
import javax.swing.*;

public class Rettangolo extends Figura {
    // Lunghezza della base del rettangolo, in pixels
    private int base;
    // Altezza del rettangolo, in pixels
    private int altezza;

    // Costruttore
    public Rettangolo(int base, int altezza) {
        this.base = base;
        this.altezza = altezza;
    }

    // Disegna il rettangolo con il contesto grafico @g, centrato nell'origine
    @Override
    public void draw(Graphics g) {
        int b = base / 2;
        int h = altezza / 2;
        g.drawLine(b, h, -b, h);
        g.drawLine(-b, h, -b, -h);
        g.drawLine(-b, -h, b, -h);
        g.drawLine(b, -h, b, h);
    }
}
