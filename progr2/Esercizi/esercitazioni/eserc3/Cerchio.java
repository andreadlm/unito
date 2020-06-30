package esercitazioni.eserc3;

import java.awt.*;

// Classe fornita per consegna

public class Cerchio implements Figura {
    private int raggio;
    private int x;
    private int y;
    private Color color;

    public Cerchio(int raggio, int x, int y, Color color){ 
        this.raggio = raggio;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawOval(x-raggio, y-raggio, 2*raggio, 2*raggio);
    }
}