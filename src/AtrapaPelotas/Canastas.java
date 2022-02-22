package AtrapaPelotas;

import java.awt.Image;


public class Canastas extends Sprite {

    public Canastas(Image m, int u, int v, int w, int h, int du, int dv)
    {
        super(m, u, v, 5*w, h, du, dv);
    }

    public void mueveDerecha(int w)
    {
        if (x + ancho + Math.abs(dx) <= w) {
            x += Math.abs(dx);
        }
    }

    public void mueveIzquierda()
    {
        if (x - Math.abs(dx) >= 0) {
            x -= Math.abs(dx);
        }
    }
}
