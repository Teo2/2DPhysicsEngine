package miklethun.teo.physics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Polygon {
	private Vector[] vertices;
	private Vector[] loopVertices;
	public Polygon(Vector[] vertices) {
		this.vertices = vertices;
		
		loopVertices = new Vector[vertices.length + 1];//All the vertices with the first one repeated at the end
		for(int i = 0; i < vertices.length; i++) {
			loopVertices[i] = vertices[i];
		}
		loopVertices[vertices.length] = vertices[0];
	}
	
	public Vector[] getVertices() {
		return vertices;
	}
	
	public double signedArea() {
		double signedArea = 0;
		for(int i = 0; i < loopVertices.length - 1; i++) {
			signedArea += loopVertices[i].getX() * loopVertices[i + 1].getY() - loopVertices[i + 1].getX() * loopVertices[i].getY();
		}
		signedArea *= 0.5;
		return signedArea;
	}
	
	//For more info on the centroid algorithm go to https://en.wikipedia.org/wiki/Centroid#Of_a_polygon
	public Vector centroid() {		
		double signedArea = 0;
		double xCenter = 0;
		double yCenter = 0;
		for(int i = 0; i < loopVertices.length - 1; i++) {
			double value = loopVertices[i].getX() * loopVertices[i + 1].getY() - loopVertices[i + 1].getX() * loopVertices[i].getY();
			xCenter += (loopVertices[i].getX() + loopVertices[i + 1].getX()) * value;
			yCenter += (loopVertices[i].getY() + loopVertices[i + 1].getY()) * value;
			signedArea += value;
		}
		signedArea *= 0.5;
		xCenter *= 1 / (6 * signedArea);
		yCenter *= 1 / (6 * signedArea);
		
		return new Vector(xCenter, yCenter);
	}
	
	public double inertia(Vector aboutPoint) {
		double inertia = 0.0;
		for(int i = 1; i < loopVertices.length - 1; i++) {
			Vector p1 = loopVertices[i].subtract(aboutPoint);
			Vector p2 = loopVertices[i+1].subtract(aboutPoint);
			inertia += Vector.crossProd(p1, p2) * (Vector.dotProd(p1, p1) + Vector.dotProd(p1, p2) + Vector.dotProd(p2, p2));
		}
		inertia /= 12.0;
		return inertia;
	}
	
	public double inertia() {
		return inertia(centroid());
	}
	
	public Polygon transform(double rotation, Vector translation) { //First applies rotation and then the translation
		double[][] rotationMatrix = {{Math.cos(rotation), -Math.sin(rotation)}, {Math.sin(rotation), Math.cos(rotation)}};
		
		Vector[] transformedVertices = new Vector[vertices.length];
		for(int i = 0; i < vertices.length; i++) {
			Vector rotatedVertex = new Vector(
					vertices[i].getX() * rotationMatrix[0][0] + vertices[i].getY() * rotationMatrix[1][0],
					vertices[i].getX() * rotationMatrix[0][1] + vertices[i].getY() * rotationMatrix[1][1]);
			transformedVertices[i] = Vector.add(rotatedVertex, translation);
		}
		
		return new Polygon(transformedVertices);
	}
	
	public List<Vector> getIntersections(Line line) {
		List<Vector> intersections = new ArrayList<Vector>();
		
		for(int i = 0; i < loopVertices.length - 1; i++) {
			Line edge = new Line(loopVertices[i], loopVertices[i + 1]);
			Vector[] intersection = Line.intersections(edge, line);
			for(int k = 0; k < intersection.length; k++) {
				intersections.add(intersection[k]);
			}
		}
		
		return intersections;
	}
	
	public Vector[] getIntersections(Polygon otherShape) {//Always first 2 are a collision pair, then second pair etc.
		List<Vector> intersections = new ArrayList<Vector>();
		
		boolean firstIntersectionEntering = false;
		boolean firstIntersectionFound = false;
		for(int i = 0; i < loopVertices.length - 1; i++) {
			Line edge = new Line(loopVertices[i], loopVertices[i + 1]);
			List<Vector> intersectionsWithEdge = otherShape.getIntersections(edge);
			if(!firstIntersectionFound && intersectionsWithEdge.size() > 0) {
				firstIntersectionFound = true;
				if(otherShape.encloses(loopVertices[i])) {
					firstIntersectionEntering = false;
				}
				else {
					firstIntersectionEntering = true;
				}
			}
			intersections.addAll(otherShape.getIntersections(edge));
		}
		
		if(firstIntersectionFound && !firstIntersectionEntering) {
			Vector firstIntersection = intersections.get(0);
			intersections.remove(0);
			intersections.add(firstIntersection);
		}
		return intersections.toArray(new Vector[intersections.size()]);
	}
	
	public boolean encloses(Vector point) {
		if(getIntersections(new Line(new Vector(-0xffffff, -0xffffff), point)).size() % 2 == 0) return false;
		return true;
	}
	
	public void render(Graphics g, int color, boolean filled) {
		g.setColor(new Color(color));
		
		int[] xPoints = new int[vertices.length];
		int[] yPoints = new int[vertices.length];
		for(int i = 0; i < vertices.length; i++) {
			xPoints[i] = (int)(vertices[i].getX());
			yPoints[i] = (int)(vertices[i].getY());
		}
		
		if(filled) g.fillPolygon(xPoints, yPoints, vertices.length);
		else g.drawPolygon(xPoints, yPoints, vertices.length);
	}
}
