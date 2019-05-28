package miklethun.teo.physics;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public abstract class Solid implements LevelObject {
	private Polygon polygon; //With vertices relative to COM and no rotation
	private Polygon transformedPolygon; //With vertices relative to (0,0)
	private Vector position; //Position of the COM
	private Vector velocity = new Vector(0, 0);
	private double angle = 0;
	private double angularVelocity = 0;
	private double mass;
	private double inertia;
	private double angularAcceleration = 0;
	private Vector acceleration = new Vector(0,0);
	
	public Solid(double mass, double inertia, Vector centerOfMass, Vector... vertices) {
		this.mass = mass;
		this.inertia = inertia;
		transformedPolygon = new Polygon(vertices);
		position = centerOfMass;
		polygon = transformedPolygon.transform(0, centerOfMass.negative());
	}

	public void applyForce(Vector force, Vector fromPoint) {
		if(inertia != Double.POSITIVE_INFINITY) {
			Vector r = position.subtract(fromPoint);
			double torque = Vector.crossProd(r, force);
			angularAcceleration += torque / inertia;
		}
		if(mass != Double.POSITIVE_INFINITY) {
			acceleration = Vector.add(acceleration, force.scale(1 / mass));
		}
	}
	
	public void applyAcceleration() {
		angularVelocity += angularAcceleration;
		velocity = Vector.add(velocity, acceleration);
		
		angularAcceleration = 0;
		acceleration = new Vector(0,0);
	}
	
	public void physicsStep() {
		position = Vector.add(position, velocity);
		angle += angularVelocity;
		
		transformedPolygon = polygon.transform(angle, position);
	}
	
	private Vector firstPoint;
	public void update() {
		applyAcceleration();
		physicsStep();
		
		if(Game.mousePressed[1] && transformedPolygon.encloses(new Vector(Game.mouseX, Game.mouseY))) {
			firstPoint = new Vector(Game.mouseX, Game.mouseY);
		}
		else if(Game.mouseReleased[1] && firstPoint != null) {
			Vector newPoint = new Vector(Game.mouseX, Game.mouseY);
			applyForce(newPoint.subtract(firstPoint).scale(mass / 25), firstPoint);
			firstPoint = null;
		}
	}
	
	public Vector getVelocityAt(Vector location) {
		Vector fromCenter = location.subtract(position);
		double angleToCenter = fromCenter.angle();
		Vector rotational = new Vector(Math.sin(angleToCenter), -Math.cos(angleToCenter)).scale(fromCenter.magnitude() * angularVelocity);
		return Vector.add(velocity, rotational);
	}
	
	public double getKineticEnergy() {
		double v = velocity.magnitude();
		double linear = v == 0 ? 0 : 0.5 * mass * v * v; //Ternary operator needed to avoid 0 * infinity
		double rotational = angularVelocity == 0 ? 0 : 0.5 * inertia * angularVelocity * angularVelocity;
		return linear + rotational;
	}
	
	public double getMechanicalEnergy() {
		return getKineticEnergy() - mass * Level.gravity * position.getY();
	}
	
	public void render(Graphics g) {
		render(g, 0x000000);
	}
	
	public void render(Graphics g, int color) {
		position.render(g, 10, 0x000000);
		transformedPolygon.render(g, color, false);
	}

	public Polygon getPolygon() {
		return transformedPolygon;
	}
	
	public Vector getPosition() {
		return position;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public double getAngularVelocity() {
		return angularVelocity;
	}

	public double getMass() {
		return mass;
	}

	public double getInertia() {
		return inertia;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}
	
	public String toString() {
		return "Energy: " + getMechanicalEnergy();
	}
	
	public static Vector centerOfMass(Solid...solids) {
		Vector centerOfMass = new Vector(0,0);
		double totalMass = 0;
		for(Solid solid : solids) {
			centerOfMass = centerOfMass.add(solid.getPosition().scale(solid.getMass()));
			totalMass += solid.getMass();
		}
		return centerOfMass.scale(1 / totalMass);
	}
}
