package miklethun.teo.physics;

import java.awt.Color;
import java.awt.Graphics;

public class Line {
	private final static double EPSILON = 0.1; //Used to help eliminate floating point errors in calculations
	
	private Vector point1;
	private Vector point2;
	public Line(Vector point1, Vector point2) {
		this.point1 = point1;
		this.point2 = point2;
	}
	
	public Line(int x1, int y1, int x2, int y2) {
		this(new Vector(x1, y1), new Vector(x2, y2));
	}
	
	public Vector getPoint1() {
		return point1;
	}
	
	public Vector getPoint2() {
		return point2;
	}
	
	//See https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection for more information on the algorithm
	public static Vector[] intersections(Line line1, Line line2) { //If there are infinitely many intersections, only returns the 2 centermost endpoints of both lines (TO DO)
		double x1 = line1.getPoint1().getX();
		double y1 = line1.getPoint1().getY();
		double x2 = line1.getPoint2().getX();
		double y2 = line1.getPoint2().getY();
		double x3 = line2.getPoint1().getX();
		double y3 = line2.getPoint1().getY();
		double x4 = line2.getPoint2().getX();
		double y4 = line2.getPoint2().getY();
		
		double detDenom = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
		
		
		//TO DO LATER!!!
		if(Math.abs(detDenom) < EPSILON) { //If the two lines are (close to) parallel.
			if(true) {
			//if(Math.abs(Point.distance(line1.getPoint1(), line2.getPoint1()) + Point.distance(line1.getPoint1(), line2.getPoint1()) - Point.distance((line1.getPoint1(), (line1.getPoint2())) > EPSILON) {
				//If point1 of the line2 is not (close to being) on line1 the the two lines are not (very close to) touching
				return new Vector[0];
			}	
		}
		
		double det1 = (x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4);
		double det2 = (x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3);
		
		//1st degree Bezier parameters for the lines intersection point
		double param1 = det1 / detDenom;
		double param2 = -det2 / detDenom; 
		
		if(!(0 <= param1 && param1 <= 1) || !(0 <= param2 && param2 <= 1)) {
			return new Vector[0];
		}
		
		Vector instersection = new Vector(x1 + param1 * (x2 - x1), y1 + param1 * (y2 - y1));
		Vector[] instersections = {instersection};
		return instersections;
	}
	
	public Vector[] intersections(Line otherLine) {
		return intersections(this, otherLine);
	}
	
	public void render(Graphics g, int color) {
		g.setColor(new Color(color));
		g.drawLine((int)point1.getX(), (int)point1.getY(), (int)point2.getX(), (int)point2.getY());
	}

	public void render(Graphics g) {
		render(g, 0x000000);
	}
}
