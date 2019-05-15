package miklethun.teo.physics;

import java.awt.Color;
import java.awt.Graphics;

public class LevelGeometry extends Solid {
	public LevelGeometry(Vector... vertices) {
		super(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, new Vector(0,0), vertices);
	}
}
