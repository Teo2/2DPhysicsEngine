package miklethun.teo.physics;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window extends Canvas implements KeyListener, MouseListener {

	private final String TITLE;
	private final int WIDTH, HEIGHT;
	private final double SCALE;
	private final int FPS;
	
	public JFrame frame;
	private BufferedImage image;
	private boolean running = false;
	
	private boolean[] keys = new boolean[256];
	private boolean[] keysPressed = new boolean[256];
	private boolean[] keysReleased = new boolean[256];
	private boolean[] mouse = new boolean[java.awt.MouseInfo.getNumberOfButtons()];
	private boolean[] mousePressed = new boolean[java.awt.MouseInfo.getNumberOfButtons()];
	private boolean[] mouseReleased = new boolean[java.awt.MouseInfo.getNumberOfButtons()];
	
	private Game game;
	
	public static Window window;
	
	public Window(String title, int width, int height, double scale, int fps) {
		TITLE = title;
		WIDTH = width;
		HEIGHT = height;
		SCALE = scale;
		FPS = fps;
		
		frame = new JFrame();
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize((int)(width*scale), (int)(height*scale));
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		addMouseListener(this);
		addKeyListener(this);
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		window = this;
		
		game = new Game();
		
		
		start();
	}
	
	public void start() {
		if(running) return;
		running = true;
		run();
	}
	
	public void stop() {
		running = false;
	}
	
	
	private void run() {
		long lastTime;
		while(running) {
			lastTime = System.currentTimeMillis();
			update();
			draw();
			long sleep = (long)((1000.0/FPS)-(System.currentTimeMillis()-lastTime));
			try {
				Thread.sleep(sleep < 0 ? 0 : sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		frame.dispose();
	}
	
	private void update() {
		game.update(keys, keysPressed, keysReleased, mouse, mousePressed, mouseReleased);
		
		for(int i = 0; i < 256; i++) {
			keysPressed[i] = false;
			keysReleased[i] = false;
		}
		for(int i = 0; i < java.awt.MouseInfo.getNumberOfButtons(); i++) {
			mousePressed[i] = false;
			mouseReleased[i] = false;
		}
	}
	
	private void draw() {
		Graphics g = image.getGraphics();
		g.setColor(new Color(0xffffff));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		game.draw(g);
		g.dispose();
		g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		mouse[e.getButton()] = true;
		mousePressed[e.getButton()] = true;
	}
	public void mouseReleased(MouseEvent e) {
		mouse[e.getButton()] = false;
		mouseReleased[e.getButton()] = true;
	}
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		keysPressed[e.getKeyCode()] = true;
	}
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		keysReleased[e.getKeyCode()] = true;
	}
	public void keyTyped(KeyEvent e) {}
}