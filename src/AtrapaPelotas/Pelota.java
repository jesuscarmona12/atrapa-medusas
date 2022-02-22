package AtrapaPelotas;

import java.awt.Image;
import java.awt.Rectangle;


public class Pelota extends Sprite {

    public Pelota(Image m, int u, int v, int w, int h, int du, int dv)
    {
        super(m, u, v, w, h, du, dv);
    }

    public boolean intersecta(Canastas nave)
    {
        Rectangle r1 = new Rectangle(x, y, ancho, alto);
        Rectangle r2 = new Rectangle(nave.x, nave.y, nave.ancho, nave.alto);

        return r1.intersects(r2);
    }
}
