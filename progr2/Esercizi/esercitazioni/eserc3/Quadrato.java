package esercitazioni.eserc3;

import java.awt.*;

// Classe fornita per consegna

public class Quadrato implements Figura {
    private int lato;
    private int x;
    private int y;
    private Color color = null;

    public Quadrato(int lato, int x, int y, Color color){ 
        this.lato = lato;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        int m = lato / 2;
        g.drawLine( x+m, y+m, x-m, y+m); //disegno primo lato su g
        g.drawLine( x-m, y+m, x-m, y-m); //disegno secondo lato su g
        g.drawLine( x-m, y-m, x+m, y-m); //disegno terzo   lato su g 
        g.drawLine( x+m, y-m, x+m, y+m); //disegno quarto  lato su g
    }
}