package miklethun.teo.physics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Movable extends Solid {
	public static final double DENSITY = 1.0;//0.00004;
	
	public Movable(Vector... vertices) {
		super(Math.abs(new Polygon(vertices).signedArea()) * DENSITY, new Polygon(vertices).inertia() * DENSITY, new Polygon(vertices).centroid(), vertices);
	}
}
