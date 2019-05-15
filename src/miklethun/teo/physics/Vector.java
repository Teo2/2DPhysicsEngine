package miklethun.teo.physics;

import java.awt.Color;
import java.awt.Graphics;

public class Vector {
	private double x;
	private double y;
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public double distance(Vector otherVector) {
		return Math.sqrt(Math.pow(x - otherVector.getX(), 2) + Math.pow(y - otherVector.getY(), 2));
	}
	
	public static double distance(Vector v1, Vector v2) {
		return v1.distance(v2);
	}
	
	public double magnitude() {
		return this.distance(new Vector(0, 0));
	}
	
	public static Vector scale(double d, Vector v) {
		return new Vector(d*v.getX(), d*v.getY());
	}
	
	public Vector scale(double d) {
		return Vector.scale(d, this);
	}
	
	public static Vector negative(Vector v) {
		return Vector.scale(-1, v);
	}
	
	public Vector negative() {
		return Vector.negative(this);
	}
	
	public Vector subtract(Vector otherVector) {
		return Vector.add(this, otherVector.negative());
	}
	
	public static Vector average(Vector... vectors) {
		return Vector.add(vectors).scale(1.0 / vectors.length);
	}
	
	public static Vector add(Vector... vectors) {
		double newX = 0;
		double newY = 0;
		for(Vector v : vectors) {
			newX += v.getX();
			newY += v.getY();
		}
		return new Vector(newX, newY);
	}
	
	public Vector add(Vector vector) {
		return Vector.add(this, vector);
	}
	
	public Vector makeUnitLength() {
		return this.scale(1.0 / this.magnitude());
	}
	
	public static Vector closest(Vector location, Vector[] vectors) {
		if(vectors.length == 0) {
			return location;
		}
			
		Vector closestPoint = vectors[0];
		for(int i = 1; i < vectors.length; i++) {
			if(location.distance(vectors[i]) < location.distance(closestPoint)) {
				closestPoint = vectors[i];
			}
		}
		return closestPoint;
	}
	
	public double angle() {
		return Math.atan2(y, x);
	}
	
	public static double crossProd(Vector v1, Vector v2) { //returns the magnitude of the cross product
		return v1.magnitude() * v2.magnitude() * Math.sin(v2.angle() - v1.angle());
	}
	
	public static double dotProd(Vector v1, Vector v2) {
		//return v1.magnitude() * v2.magnitude() * Math.cos(v2.angle() - v1.angle());
		return v1.getX() * v2.getX() + v1.getY() * v2.getY();
	}
	
	public void render(Graphics g, int color) {
		render(g, 1, color);
	}
	
	public void render(Graphics g, int size, int color) {
		g.setColor(new Color(color));
		g.fillOval((int)(x - (size + 1) / 2.0), (int)(y - (size + 1) / 2.0), size + 1, size + 1);
	}
	
	public void renderAsVector(Graphics g, int color, Vector startingLocation) {
		startingLocation.render(g, 12, color);
		g.drawLine((int)startingLocation.getX(), (int)startingLocation.getY(), (int)(startingLocation.getX() + x), (int)(startingLocation.getY() + y));
	}

	/*public int compareTo(Vector otherVector) { //Used to sort vectors into a circlt
		return (int)Math.signum(Math.atan2(y, x) - Math.atan2(otherVector.getY(), otherVector.getX()));
	}*/
}
