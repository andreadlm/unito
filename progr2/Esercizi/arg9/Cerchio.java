package arg9;

import java.awt.*;
import javax.swing.*;

public class Cerchio extends Figura {

    // Raggio del cerchio, in pixel
    private int raggio;

    // Costruttore
    public Cerchio(int raggio) { this.raggio = raggio; }

    // Diseghna il cerchio con il contesto grafico @g
    @Override
    public void draw(Graphics g) {
        g.drawOval(-raggio, -raggio, 2*raggio, 2*raggio);
    }
    
}