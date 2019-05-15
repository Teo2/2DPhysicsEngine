package miklethun.teo.physics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

public class Game implements MouseListener {

	public static Level level = new Level();
	public static boolean[] keys;
	public static boolean[] keysPressed;
	public static boolean[] keysReleased;
	public static boolean[] mouse;
	public static boolean[] mousePressed;
	public static boolean[] mouseReleased;
	public static int mouseX;
	public static int mouseY;
	
	public Game() {
		Window.window.addMouseListener(this);
	}
	
	public void update(boolean[] keys, boolean[] keysPressed, boolean[] keysReleased, boolean[] mouse, boolean[] mousePressed, boolean[] mouseReleased) {	
		Point p = MouseInfo.getPointerInfo().getLocation();
		Game.mouseX = (int)(p.getX()-Window.window.frame.getX() - Window.window.frame.getInsets().left);
		Game.mouseY = (int)(p.getY()-Window.window.frame.getY() - Window.window.frame.getInsets().top);
		Game.keys = keys;
		Game.keysPressed = keysPressed;
		Game.keysReleased = keysReleased;
		Game.mouse = mouse;
		Game.mousePressed = mousePressed;
		Game.mouseReleased = mouseReleased;
		
		level.update();
	}

	public void draw(Graphics g) {
		level.render(g);
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
	public static void main(String[] args) {
		new Window("Physics Engine", 960, 720, 1, 60);
	}
}