package arg9;

import java.awt.*;
import javax.swing.*;
import java.lang.Math;

public class PoligonoRegolare extends Figura {
    
    // Numero di lati del poligono
    private int nLati;
    // Raggio del cerchio nel quale il poligono viene inscritto, in pixel
    private int raggio;

    // Costruttore
    public PoligonoRegolare(int nLati, int raggio) { 
        this.nLati = nLati;
        this.raggio = raggio;
    }

    // Disegna il poligono regolare con il contesto grafico @g
    @Override
    public void draw(Graphics g) {
        for(int i = 0; i < nLati; i++)
            g.drawLine((int)(raggio * Math.cos((2 * Math.PI * (i % nLati)) / nLati)), 
                       (int)(raggio * Math.sin((2 * Math.PI * (i % nLati)) / nLati)), 
                       (int)(raggio * Math.cos((2 * Math.PI * ((i + 1) % nLati)) / nLati)), 
                       (int)(raggio * Math.sin((2 * Math.PI * ((i + 1) % nLati)) / nLati)));
    }
}