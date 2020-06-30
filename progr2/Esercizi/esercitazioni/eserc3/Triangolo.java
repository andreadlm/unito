package esercitazioni.eserc3;

import java.awt.*;

public class Triangolo implements Figura {
    int xBase;
    int yBase;
    int b;
    int h;
    Color color;

    public Triangolo(int xBase, int yBase, int b, int h, Color color) {
        this.xBase = xBase;
        this.yBase = yBase;
        this.b = b;
        this.h = h;
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawLine((xBase-b)/2, yBase, (xBase+b)/2, yBase);
        g.drawLine((xBase+b)/2, yBase, xBase, yBase + h);
        g.drawLine(xBase, yBase + h, (xBase-b)/2, yBase);
    }
    
}