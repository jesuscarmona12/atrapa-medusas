package AtrapaPelotas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JOptionPane;


public class PintaPanel extends javax.swing.JPanel implements Runnable {

    private Thread hilo;
    private Image bobImagen, fondo, lanzadorImagen, balaImagen, pelotaImagen, vidaImagen, 
            pelota2Imagen, instImagen,patricioImagen,BobyPatImagen, calamardoImagen;
    public Canastas BobEsponja;
    private ArrayList<Lanzador> lanzador;
    private ArrayList<Pelota> vida;
    private ArrayList<Pelota> pelotas;
    private ArrayList<Pelota> pelotaRey;
    private Clip clipPelota, clipMusica;
    private int puntuacion = 0, puntuacionfinal = 0;
    private int vidas = 5;
    private int personaje = 1;
    private int record;
    private String srecord;
 

    public enum Status {
        Esperando, Jugando, Perdio, Pausa, Seleccion
    }
    private Status status = Status.Esperando;
    private final Object lockVida = new Object();
    private final Object lockLanzador = new Object();
    private final Object lockPelotas = new Object();
    private final Object lockPelotasRey = new Object();
    private final Object lockStatus = new Object();
    
    
    

    /**
     * Creates new form PintaPanel
     */
    public PintaPanel()
    {
        initComponents();
        setLayout(null);
        
        Bob.setEnabled(false);
        patricio.setEnabled(false);
        calamardo.setEnabled(false);
        lanzador = new ArrayList<>();
        vida = new ArrayList<>();
        pelotas = new ArrayList<>();
        pelotaRey = new ArrayList<>();

        // Abre las imagenes
        bobImagen = Toolkit.getDefaultToolkit().getImage("img/bobImagen.png");
        fondo = Toolkit.getDefaultToolkit().getImage("img/Fondo.jpeg");
        lanzadorImagen = Toolkit.getDefaultToolkit().getImage("img/lanzador.png");   
        pelotaImagen = Toolkit.getDefaultToolkit().getImage("img/pelota.png");
        vidaImagen = Toolkit.getDefaultToolkit().getImage("img/vida.png");
        pelota2Imagen = Toolkit.getDefaultToolkit().getImage("img/rey.png");
        instImagen = Toolkit.getDefaultToolkit().getImage("img/inst.jpg");
        patricioImagen = Toolkit.getDefaultToolkit().getImage("img/patricio.png");
        BobyPatImagen = Toolkit.getDefaultToolkit().getImage("img/BobyPat.png");
        calamardoImagen = Toolkit.getDefaultToolkit().getImage("img/calamardoImagen.png");

        // Espera las imagenes
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(bobImagen, 0);
        tracker.addImage(fondo, 0);
        tracker.addImage(lanzadorImagen, 0);
        tracker.addImage(vidaImagen, 0);
        tracker.addImage(pelotaImagen, 0);
        tracker.addImage(pelota2Imagen, 0);
        tracker.addImage(instImagen, 0);
        tracker.addImage(patricioImagen, 0);
        tracker.addImage(BobyPatImagen, 0);
        tracker.addImage(calamardoImagen, 0);

        try {
            tracker.waitForID(0);
        }
        catch (InterruptedException e) {
            System.out.println(e);
        }

        try {
            AudioInputStream input1 = AudioSystem.getAudioInputStream(
                    new File("sonidos/pelota.wav"));
            clipPelota = AudioSystem.getClip();
            clipPelota.open(input1);

            AudioInputStream input2 = AudioSystem.getAudioInputStream(
                    new File("sonidos/musica.wav"));
            clipMusica = AudioSystem.getClip();
            clipMusica.open(input2);
            
            
            
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public Status getStatus()
    {
        Status valor;

        synchronized (lockStatus) {
            valor = status;
        }

        return valor;
    }

    public void setStatus(Status valor)
    {
        synchronized (lockStatus) {
            status = valor;
        }
    }

    @Override
    public void paint(Graphics g)
    {
        // Pinta el fondo
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), null);
        
        if (getStatus() == Status.Esperando) {
            
           g.drawImage(BobyPatImagen, 150, 330, 150, 166, null);
            g.setColor(Color.black);       
            g.setFont(new Font("Arial", Font.BOLD, 20));
            
            g.drawString("Instrucciones:",
                    getWidth() / 18, getHeight() / 2 - 190);
            g.drawString("- Para moverte utiliza el mousue.",
                    getWidth() / 18, getHeight() / 2 - 170);
            g.drawString("- Atrapa todas las medusas rosas que puedas.",
                    getWidth() / 18, getHeight() / 2 - 145);
            g.drawString("- Perderás una vida por cada medusa ",
                    getWidth() / 18, getHeight() / 2 - 120);
            g.drawString("no atrapada (5 vidas iniciales).",
                    getWidth() / 18, getHeight() / 2 - 100);
            
            g.drawString("- Perderas una vida por cada medusa ",
                    getWidth() / 18, getHeight() / 2 - 80);
            g.drawString("azul atrapada.",
                    getWidth() / 18, getHeight() / 2 - 60);
            
             g.drawString("- Presiona la tecla 'p' para pausar ",
                    getWidth() / 18, getHeight() / 2 - 40);
            g.drawString("y espacio para reanudar.",
                    getWidth() / 18, getHeight() / 2 - 20);
            g.setColor(Color.red);
            
            g.drawString("Presiona espacio para continuar",
                    getWidth() / 5 -10, getHeight() / 2 + 180);
            
            
            
            return;
        }
        if (getStatus() == Status.Seleccion){
             File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;
            //Lee record de archivo de texto
            try {
               //Abre archivo de texto
               archivo = new File ("record.txt");
               fr = new FileReader (archivo);
               br = new BufferedReader(fr);

               // Lectura del fichero
               
               
               
                   srecord = br.readLine();
                   record = Integer.parseInt(srecord);
      
              
            }
            
            catch(Exception e){
               e.printStackTrace();
            }
            try{                    
            if( null != fr ){   
               fr.close();     
            }                  
             }catch (Exception e2){ 
                e2.printStackTrace();
             }
            //Posicion de botones de selección de personaje
            Bob.setBounds(50,300,160,87);
            patricio.setBounds(250,300,180,87);
            calamardo.setBounds(150,400,180,87);
            Bob.setEnabled(true);
            patricio.setEnabled(true);
            calamardo.setEnabled(true);
            setOpaque(false);
            super.paint(g);
            g.setFont(new Font("Arial", Font.BOLD, 23));
            g.setColor(Color.black);
            g.drawString("Para comenzar a jugar selecciona",
                    getWidth() / 18, getHeight() / 2 - 110);
            g.drawString("un personaje:",
                    getWidth() / 18, getHeight() / 2 - 80);
            if (record < 1500){
            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.setColor(Color.red);
            g.drawString("(bloqueado)",
                    200, 500);
            }
            return;
        }
        //Estado de pausa mientras se juega
        if (getStatus() == Status.Pausa) {
             Bob.setEnabled(false);
           patricio.setEnabled(false);
           calamardo.setEnabled(false);
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Juego en pausa.",
                    getWidth() / 18, getHeight() / 2 - 170);
           
            g.drawString("Presiona espacio para continuar ",
                    getWidth() / 18, getHeight() / 2 - 120);
           
            return;
        }

        if (getStatus() == Status.Perdio) {
            clipMusica.stop();
           Bob.setEnabled(false);
           patricio.setEnabled(false);
           calamardo.setEnabled(false);
            puntuacion = 0; // Se reinicia la puntuación.
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("¡HAZ  PERDIDO!",
                    getWidth() / 5, getHeight() / 2 - 150);
            g.setColor(Color.blue);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Agotaste todas tus vidas.",
                    getWidth() / 5, getHeight() / 2 - 90);
            g.setColor(Color.blue);
            g.drawString("Puntuación final: " + puntuacionfinal,
                    getWidth() / 5, getHeight() / 2 - 47);
            if (puntuacionfinal < record)
            g.drawString("RECORD : " + record,
                    getWidth() / 5, getHeight() / 2 - 17);
            else{
                g.drawString("RECORD : " + puntuacionfinal,
                    getWidth() / 5, getHeight() / 2 - 17);
            }
            
 
            g.setColor(Color.black);
            g.drawString("Presiona una tecla",
                    getWidth() / 5, getHeight() / 2 + 30);
            g.drawString("o el ratón",
                    getWidth() / 5, getHeight() / 2 + 60);
            g.drawString("para recomenzar de nuevo.",
                    getWidth() / 5, getHeight() / 2 + 90);
            
            FileWriter fichero = null;
            PrintWriter pw = null;
            
           
            //Guarda nuevo record en el archivo de texto
            if (record < puntuacionfinal){ 
            try
        {
            fichero = new FileWriter("record.txt");
            pw = new PrintWriter(fichero);
            
           
            pw.println(puntuacionfinal);
            
            

        } catch (Exception e) {
            e.printStackTrace();
        }
            try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
            }
            
            return;
        }
         

        // Pinta la canasta
        if (BobEsponja != null) {
            BobEsponja.pinta(g);
        }

        // Pinta los lanzadores
        synchronized (lockLanzador) {
            for (Lanzador a : lanzador) {
                a.pinta(g);
            }
        }    

        // Pinta las pelotas
        synchronized (lockPelotas) {
            for (Pelota m : pelotas) {
                m.pinta(g);
            }
        }
        
        //Pinta las vidas
        synchronized (lockVida) {
            for (Pelota m : vida) {
                m.pinta(g);
            }
        }
        
        synchronized (lockPelotasRey) {
            for (Pelota m : pelotaRey) {
                m.pinta(g);
            }
        }
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.setColor(Color.black);
            g.drawString("Vidas: " + vidas,
            getWidth() -50-25, getHeight() / 2 + 324);
            g.setColor(Color.black);
            g.drawString("Puntos: " + puntuacion,
            getWidth()/20, getHeight() / 2 + 324);
    }

    @Override
    public void run()
    {
        while (hilo != null) {

            
            if (getStatus() == Status.Jugando){
                //Mueve botones fuera del rango del juego
                Bob.setBounds(800,800,160,87);
                patricio.setBounds(800,800,180,87);
                calamardo.setBounds(800,800,180,87);
                Bob.setEnabled(false);
                patricio.setEnabled(false);
                calamardo.setEnabled(false);
            
        }
            //System.out.println("antes status: " + status);
            if (getStatus() == Status.Esperando
                    || getStatus() == Status.Perdio || getStatus() == Status.Pausa ||getStatus() == Status.Seleccion ) {
                continue;
            }

            //System.out.println("despues status: " + status);
            // mover los lanzadores
            for (Lanzador a : lanzador) {
                a.mueve2();
                if (a.lanzaPelotas()) {
                    Pelota m = new Pelota(pelotaImagen, (int) (Math.random() * 445) +1, a.y,
                            55, 60, 0, (getHeight() / 100)*((int) (Math.random() * 4) +2));

                    synchronized (lockPelotas) {
                        pelotas.add(m);
                    }

                    clipPelota.stop();
                    clipPelota.setFramePosition(0);
                    clipPelota.start();
                }
                if (a.lanzaVidas()) {
                    Pelota m = new Pelota(vidaImagen, (int) (Math.random() * 460) +1, a.y,
                            25, 25, 0, (getHeight() / 100)*((int) (Math.random() * 6) +3));

                    synchronized (lockVida) {
                        vida.add(m);
                    }
      
                }
                if (a.lanzaPelotas2()) {
                    Pelota m = new Pelota(pelota2Imagen, (int) (Math.random() * 440) +1, a.y,
                            60, 60, 0, (getHeight() / 100)*((int) (Math.random() * 3) +1));

                    synchronized (lockPelotasRey) {
                        pelotaRey.add(m);
                    }
      
                }
            }
            
            if (getStatus() == Status.Jugando){   
                //Inicia canción durante el juego
                clipMusica.start();
                FloatControl volume = (FloatControl) clipMusica.getControl(FloatControl.Type.MASTER_GAIN);
                 volume.setValue(-20);

                
            }
 

            // mover las pelotas
            synchronized (lockPelotas) {
                for (Pelota m : pelotas) {
                    
                    m.mueve();
                }
            }
            // mover vidas
            synchronized (lockVida) {
                for (Pelota m : vida) {
                    
                    m.mueve();
                }
            }
            
            //Mover medusas rey
            synchronized (lockPelotasRey) {
                for (Pelota m : pelotaRey) {
                    
                    m.mueve();
                }
            }

          
            int i = 0;
            

            // borrar las pelotas atrapadas o caidas.
            
            while (i < pelotas.size()) {
                Pelota m = pelotas.get(i);
                
                if (m.y > getHeight() || m.intersecta(BobEsponja)) {
                    synchronized (lockPelotas) {
                        pelotas.remove(i);
                        if(m.intersecta(BobEsponja)){
                            puntuacion++;
                        }
                        if (m.y > getHeight()){
                            vidas--;
                            
                    }
                        if (vidas < 0 || vidas == 0){
                             status = Status.Perdio;
                             vidas = 5; //Se reinician las vidas al perder.               
                             puntuacionfinal = puntuacion;
                             
                             
                        }
                        
                    }
                }
                else {
                    i++;
                }
                
                
            }
            i = 0; 
            // Se borran las pelotas despues de perder todas las vidas.
            if (status == Status.Perdio){
            while (i < pelotas.size()) {
                Pelota m = pelotas.get(i);
                
                if (!(m.y > getHeight() || m.intersecta(BobEsponja))) {
                    synchronized (lockPelotas) {
                        pelotas.remove(i);
      
                    }
                }
                else {
                    i++;
                }
                
                
            }
            }
            i=0;
            //Se borran vidas restantes al perder
             if (status == Status.Perdio){
            while (i < vida.size()) {
                Pelota m = vida.get(i);
                
                if (!(m.y > getHeight() || m.intersecta(BobEsponja))) {
                    synchronized (lockVida) {
                        vida.remove(i);
         
                        
                    }
                }
                else {
                    i++;
                }
                
                
            }
            }
            i=0;
            // Se borran las vidas al caer o intersectar
            while (i < vida.size()) {
                Pelota h = vida.get(i);
                
                if (h.y > getHeight() ||h.intersecta(BobEsponja)) {
                    synchronized (lockVida) {
                        vida.remove(i);
                        // Se aumenta una vida si se atrapa un corazon
                        if(h.intersecta(BobEsponja)){
                            vidas++;
                        }

                        
                    }
                }
                else {
                    i++;
                }
                
                
            }
            // Se borran las pelotas azules
            i=0;
            while (i < pelotaRey.size()) {
                Pelota r = pelotaRey.get(i);
                
                if (r.y > getHeight() ||r.intersecta(BobEsponja)) {
                    synchronized (lockPelotasRey) {
                        pelotaRey.remove(i);
                        // Se aumenta una vida si se atrapa un corazon
                        if(r.intersecta(BobEsponja)){
                            vidas--;
                        }

                        
                    }
                }
                else {
                    i++;
                }
                
                
            }
            
            // se borran las pelotas azules despues de perder.
            i=0;
             if (status == Status.Perdio){
            while (i < pelotaRey.size()) {
                Pelota r = pelotaRey.get(i);
                
                if (!(r.y > getHeight() || r.intersecta(BobEsponja))) {
                    synchronized (lockPelotasRey) {
                        pelotaRey.remove(i);
                        
                        
                        
                    }
                }
                else {
                    i++;
                }
                
                
            }
             }
           
            
            for (Lanzador a : lanzador) {
                if (a.y + a.alto > getHeight()) {
                    System.out.println("juego finalizado");
              
                }
            }



            repaint();

            try {
                Thread.sleep(60);
            }
            catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }



    public void recomienza()
    {   
           //Selección de personaje
        if (personaje ==1){
        BobEsponja = new Canastas(bobImagen, getWidth() / 2, 18 * getHeight() / 20,
                17, 72, getWidth() / 20, 0);
        }
        if (personaje ==2){
        BobEsponja = new Canastas(patricioImagen, getWidth() / 2, 18 * getHeight() / 20,
                17, 72, getWidth() / 20, 0);
        }
        if (personaje ==3){
        BobEsponja = new Canastas(calamardoImagen, getWidth() / 2, 18 * getHeight() / 20,
                22, 77, getWidth() / 20, 0);
        }
        

        synchronized (lockLanzador) {
            lanzador.clear();
        }

        int ancho = getWidth();
        int alto = getHeight();
        int w, h = 1;
        for (int renglon = 0; renglon < 1; renglon++) {
            w = 1;
            for (int col = 0; col < 1; col++) {
                int x = ancho * w / 10;
                int y = alto * h / 15;
                Lanzador bola = new Lanzador(lanzadorImagen, (int) (Math.random() * 400) + 1, y-10, 20, 20, 0, 1);

                synchronized (lockLanzador) {
                    lanzador.add(bola);
                }

                w += 2;
            }
            h += 2;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        patricio = new javax.swing.JButton();
        Bob = new javax.swing.JButton();
        calamardo = new javax.swing.JButton();

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        patricio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AtrapaPelotas/kisspng-patrick-star-coloring-book-squidward-tentacles-pla-patrick-star-5b1fbac0331413.2347789515288060802092 (1).png"))); // NOI18N
        patricio.setText("Patricio");
        patricio.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        patricio.setDefaultCapable(false);
        patricio.setEnabled(false);
        patricio.setFocusPainted(false);
        patricio.setFocusable(false);
        patricio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patricioActionPerformed(evt);
            }
        });

        Bob.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AtrapaPelotas/kisspng-bob-esponja-drawing-cartoon-spongebob-squarepants-5b0d49d7c28340.9671935615275975277967 (1).png"))); // NOI18N
        Bob.setText("Bob");
        Bob.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Bob.setDefaultCapable(false);
        Bob.setEnabled(false);
        Bob.setFocusPainted(false);
        Bob.setFocusable(false);
        Bob.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BobActionPerformed(evt);
            }
        });

        calamardo.setIcon(new javax.swing.ImageIcon("C:\\Users\\JESUSANGELCARMONAALV\\Desktop\\CatchBall\\img\\kisspng-squidward-tentacles-patrick-star-mr-krabs-karen-s-2199x1648px-squidward-wallpaper-wallpapersafari-5bc53eb3891b19.3345821115396532995616 (2).png")); // NOI18N
        calamardo.setText("Calamardo");
        calamardo.setEnabled(false);
        calamardo.setFocusable(false);
        calamardo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calamardoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(patricio)
                        .addGap(26, 26, 26)
                        .addComponent(Bob, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(calamardo)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(49, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Bob, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(patricio))
                .addGap(18, 18, 18)
                .addComponent(calamardo, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Bob.getAccessibleContext().setAccessibleParent(Bob);
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseMoved(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseMoved
    {//GEN-HEADEREND:event_formMouseMoved
        if (evt.getX() + BobEsponja.ancho < getWidth()) {
            BobEsponja.x = evt.getX();
            repaint();
        }
        if (evt.getY() + BobEsponja.ancho < getHeight()) {
            BobEsponja.y = evt.getY();
            repaint();
        }
    }//GEN-LAST:event_formMouseMoved

    private void formComponentResized(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_formComponentResized
    {//GEN-HEADEREND:event_formComponentResized
        recomienza();

        hilo = new Thread(this);
        hilo.start();
    }//GEN-LAST:event_formComponentResized

    private void formMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_formMouseClicked
    {//GEN-HEADEREND:event_formMouseClicked
   
        if (getStatus() == Status.Perdio) {
            setStatus(Status.Seleccion);
            recomienza();
        }
        if (getStatus() == Status.Pausa) {
            setStatus(Status.Jugando);
            
        }
        
       
    }//GEN-LAST:event_formMouseClicked

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
     
      // TODO add your handling code here:
    }//GEN-LAST:event_formKeyPressed
//Botones de selección de personaje
    private void BobActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BobActionPerformed
        personaje = 1;
         Bob.setEnabled(false);
           patricio.setEnabled(false);
         recomienza();
        status = Status.Jugando;
    }//GEN-LAST:event_BobActionPerformed

    private void patricioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patricioActionPerformed
        personaje = 2;
        recomienza(); 
         Bob.setEnabled(false);
           patricio.setEnabled(false);
        status = Status.Jugando;
    }//GEN-LAST:event_patricioActionPerformed

    private void calamardoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calamardoActionPerformed
       if (record >= 1500){
        personaje = 3;
        recomienza();
        status = Status.Jugando;
       }
       else{
           JOptionPane.showMessageDialog(null, "Personaje Bloqueado (se requiere hacer 1500 puntos para desbloquear).");
    
 


       }
        
    
    }//GEN-LAST:event_calamardoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Bob;
    private javax.swing.JButton calamardo;
    private javax.swing.JButton patricio;
    // End of variables declaration//GEN-END:variables
}
