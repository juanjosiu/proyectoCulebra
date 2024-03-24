/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.nibbles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;

/**
 *
 * @author Carlos
 */
public class Tablero extends JPanel implements ActionListener{

    private final int ANCHO_TABLERO = 600;
    private final int ALTURA_TABLERO = 600;
    private final int TAM_PUNTO = 10;
    private final int RAND_POS = 59;
    private final int RETRASO = 140;
    private final int TODOS_PUNTOS = 3600;
    
    private final int x[] = new int[TODOS_PUNTOS];
    private final int y[] = new int[TODOS_PUNTOS];
    
    private Image punto;
    private Image cabeza;
    private Image manzana;
    
    private Timer timer;
    private int puntos;
    private int manzana_x;
    private int manzana_y;
    
    private boolean enJuego = true;
    private boolean dirDerecha = true;
    private boolean dirArriba = false;
    private boolean dirIzquierda = false;
    private boolean dirAbajo = false;
    private int puntaje = 0;
    
    public Tablero(){
        inicializarTablero();
    }
    
    public void inicializarTablero(){
        addKeyListener(new AdaptadorTeclado());
        setBackground(Color.black);
        setFocusable(true);
        
        setPreferredSize(new Dimension(ANCHO_TABLERO, ALTURA_TABLERO));
        cargarImagenes();
        inicializarJuego();
    }
    
    private void cargarImagenes(){
        ImageIcon iiPunto = new ImageIcon("src/main/java/com/mycompany/nibbles/recursos/dot.png");
        punto = iiPunto.getImage();
        
        ImageIcon iiCabeza = new ImageIcon("src/main/java/com/mycompany/nibbles/recursos/head.png");
        cabeza = iiCabeza.getImage();
        
        ImageIcon iiManzana = new ImageIcon("src/main/java/com/mycompany/nibbles/recursos/apple.png");
        manzana = iiManzana.getImage();
    }
    
    private void inicializarJuego(){
        puntaje = 0;
        puntos = 3;
        for(int i=0; i<puntos; i++){
            x[i] = 50 -i * 10;
            y[i] = 50;
        }
        
        posicionarManzana();
        enJuego = true;
        timer = new Timer(RETRASO, this);
        timer.start();
    }
    
    private void posicionarManzana(){
        int r = (int)(Math.random() * RAND_POS);
        manzana_x = ((r * TAM_PUNTO));
        
        r = (int)(Math.random() * RAND_POS);
        manzana_y = ((r * TAM_PUNTO));
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        pintar(g);
    }
    
    private void pintar(Graphics g){
        if(enJuego){
            g.drawImage(manzana, manzana_x, manzana_y, this);
            
            for(int i =0; i< puntos; i++){
                if(i == 0){
                    g.drawImage(cabeza, x[i], y[i], this);
                }else{
                    g.drawImage(punto, x[i], y[i], this);
                }
            }
        g.setColor(Color.white);
        g.drawString("Puntaje: " + puntaje, ANCHO_TABLERO - 100, 20);
            Toolkit.getDefaultToolkit().sync();
            
        }
    }
    
    private void finJuego(){
        //Mostras el menu de opciones
        timer.stop();
        Object[] options = {"Sí", "No"};
        int option = JOptionPane.showOptionDialog(null, "¿Quieres volver a jugar?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);      
        //Manejando las respuestas
        if (option == JOptionPane.YES_OPTION) {
        inicializarJuego();
        }
        else {
        System.exit(0); // Cierra el programa
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(enJuego){
            verificarManzana();
            verificarColision();
            Mover();
        }
        
        repaint();
         if (!enJuego) {
        timer.stop(); // Detener el temporizador
        finJuego();   // Mostrar fin del juego
    } 
    }
    
    private void verificarManzana(){
        if((x[0] == manzana_x) && (y[0] == manzana_y)){
            puntos++;
            puntaje += 10 ; //incrementa 10 puntos por cada manzana 
            posicionarManzana();
        }
    }
    
    private void verificarColision(){
            // Verificar si la cabeza de la serpiente colisiona con cualquier parte de su cuerpo
    for (int i = 1; i < puntos; i++) {
        if (x[0] == x[i] && y[0] == y[i]) {
            enJuego = false; // Hay colisión
            break;
        }
    }
             // Verificar si la cabeza de la serpiente alcanza los límites del tablero
             if (y[0] >= ALTURA_TABLERO) {
                 y[0] = 0; // La serpiente aparece en la parte superior
             }
             if (y[0] < 0) {
                 y[0] = ALTURA_TABLERO - TAM_PUNTO; // La serpiente aparece en la parte inferior
             }
             if (x[0] >= ANCHO_TABLERO) {
                 x[0] = 0; // La serpiente aparece en el lado izquierdo
             }
             if (x[0] < 0) {
                 x[0] = ANCHO_TABLERO - TAM_PUNTO; // La serpiente aparece en el lado derecho
             }
    }
    
    
    private void Mover(){
        
        //mover los puntos verdes siguiendo la ultima ubicacion del punto rojo
        for(int i = puntos; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        
        if(dirIzquierda){
            x[0] -= TAM_PUNTO;
        }
        
        if(dirDerecha){
            x[0] += TAM_PUNTO;
        }
        
        if(dirArriba){
            y[0] -= TAM_PUNTO;
        }
        
        if(dirAbajo){
            y[0] += TAM_PUNTO;
        }
    }
    
    private class AdaptadorTeclado extends KeyAdapter {
        
        @Override
        public void keyPressed(KeyEvent e){
            int tecla = e.getKeyCode();
            
            if((tecla == KeyEvent.VK_LEFT) && (!dirDerecha)){
                dirIzquierda = true;
                dirArriba = false;
                dirAbajo = false;
            }
            
            if((tecla == KeyEvent.VK_RIGHT) && (!dirIzquierda)){
                dirDerecha = true;
                dirArriba = false;
                dirAbajo = false;
            }
            
            if((tecla == KeyEvent.VK_UP) && (!dirAbajo)){
                dirArriba = true;
                dirDerecha = false;
                dirIzquierda = false;
            }
            
            if((tecla == KeyEvent.VK_DOWN) && (!dirArriba)){
                dirAbajo = true;
                dirDerecha = false;
                dirIzquierda = false;
            }
        }        
    }
}
