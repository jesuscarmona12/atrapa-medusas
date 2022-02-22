package AtrapaPelotas;

import java.awt.Graphics;
import java.awt.Image;


public class Sprite {
    public int x, y;    // posicion
    public int ancho, alto;
    public int dx, dy;  // velocidad
    private final Image imagen;

    public Sprite(Image m, int u, int v, int w, int h, int du, int dv)
    {
        imagen = m;
        x = u;
        y = v;
        ancho = w;
        alto = h;
        dx = du;
        dy = dv;
    }

    public void mueve()
    {
        x += dx;
        y += dy;
    }
    public void mueve2()
    {
        x += dx;
        
    }
    

    public void pinta(Graphics g)
    {
        g.drawImage(imagen, x, y, ancho, alto, null);
    }
}
