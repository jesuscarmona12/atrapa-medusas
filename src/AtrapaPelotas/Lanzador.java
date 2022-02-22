package AtrapaPelotas;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

public class Lanzador extends Sprite {
    private Random random,random2,random3 ;
    private long ultimoDisparo;
    private long ultimoDisparo2;
    private long ultimoDisparo3;
    private long tiempoEntreDisparos = (int) (Math.random() * 320) + 70;  // milisegundos
    private long tiempoEntreDisparos2 = (int) (Math.random() * 4800) + 1500;
    private long tiempoEntreDisparos3 = (int) (Math.random() * 2400) + 1100;// milisegundos
    
    public Lanzador(Image m, int u, int v, int w, int h, int du, int dv)
    {
        super(m, u, v-60, w, h, du, dv);
        random = new Random();
        random2 = new Random();
        random3 = new Random();
        ultimoDisparo = System.currentTimeMillis();
        ultimoDisparo2 = System.currentTimeMillis();
        ultimoDisparo3 = System.currentTimeMillis();
    }

    public boolean intersecta(Pelota m)
    {
        Rectangle r1 = new Rectangle(x, y, ancho, alto);
        Rectangle r2 = new Rectangle(m.x, m.y, m.ancho, m.alto);

        return r1.intersects(r2);
    }

    public boolean lanzaPelotas()
    {
        if (System.currentTimeMillis() - ultimoDisparo > tiempoEntreDisparos) {
            ultimoDisparo = System.currentTimeMillis();
            return random.nextFloat() < 0.5;
        }
        return false;
    }
    public boolean lanzaPelotas2()
    {
        if (System.currentTimeMillis() - ultimoDisparo3 > tiempoEntreDisparos3) {
            ultimoDisparo3 = System.currentTimeMillis();
            return random3.nextFloat() < 0.4;
        }
        return false;
    }
    
    public boolean lanzaVidas()
    {
        if (System.currentTimeMillis() - ultimoDisparo2 > tiempoEntreDisparos2) {
            ultimoDisparo2 = System.currentTimeMillis();
            return random2.nextFloat() < 0.3;
        }
        return false;
    }
}
