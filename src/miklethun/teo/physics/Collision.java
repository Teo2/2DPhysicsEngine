package miklethun.teo.physics;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Collision {
	private Vector location;
	private Vector unitNormal; //Always towards collider1
	private Solid collider1;
	private Solid collider2;
	private Vector force; //Always towards collider1

	public Collision(Vector intersection1, Vector intersection2, Solid collider1, Solid collider2) {
		this.location = Vector.average(intersection1, intersection2);
		this.collider1 = collider1;
		this.collider2 = collider2;
		Vector parallelToSurface = intersection1.subtract(intersection2);
		Vector perpendicularToSurface1 = new Vector(parallelToSurface.getY(), -parallelToSurface.getX());
		Vector perpendicularToSurface2 = perpendicularToSurface1.negative();
		
		if(Vector.add(location, perpendicularToSurface1).distance(collider1.getPosition()) <= Vector.add(location, perpendicularToSurface2).distance(collider1.getPosition())) {
			this.unitNormal = perpendicularToSurface1.makeUnitLength();
		}
		else {
			this.unitNormal = perpendicularToSurface2.makeUnitLength();
		}
	}
	
	public static List<Collision> getAllCollisions(List<Solid> objects) {
		List<Collision> collisions = new ArrayList<Collision>();
		
		List<ArrayList<Vector>> intersections = new ArrayList<ArrayList<Vector>>();
		for(int i = 0; i < objects.size(); i++) {
			intersections.add(new ArrayList<Vector>());
		}
		for(int i = 0; i < objects.size(); i++) {
			for(int j = i + 1; j < objects.size(); j++) {
				if(objects.get(i).getMass() != Double.POSITIVE_INFINITY || objects.get(j).getMass() != Double.POSITIVE_INFINITY) {
					Vector[] intersectionsWithPolygon = objects.get(i).getPolygon().getIntersections(objects.get(j).getPolygon());
					for(int k = 0; k < intersectionsWithPolygon.length; k += 2) {
						collisions.add(new Collision(intersectionsWithPolygon[k], intersectionsWithPolygon[k + 1], objects.get(i), objects.get(j)));
					}
				}
			}
		}
		List<Collision> collisionsSorted = new ArrayList<Collision>();
		for(int i = 0; i < collisions.size(); i++) {
			if(collisions.get(i).hasPriority()) {
				collisionsSorted.add(collisions.get(i));
			}
			else {
				collisionsSorted.add(0, collisions.get(i));
			}
		}
		return collisionsSorted;
	}
	
	public static void collide(List<Collision> collisions) {
		for(Collision collision : collisions) {
			collision.calculateForce();
			collision.apply();
		}
	}
	
	public void calculateForce() {
		double r1Perp = Vector.crossProd(collider1.getPosition().subtract(location), unitNormal);
		double r2Perp = Vector.crossProd(collider2.getPosition().subtract(location), unitNormal);
		double m1 = collider1.getMass();
		double m2 = collider2.getMass();
		double I1 = collider1.getInertia();
		double I2 = collider2.getInertia();
		double v1 = collider1.getVelocity().magnitude();
		double v2 = collider2.getVelocity().magnitude();
		double omega1 = collider1.getAngularVelocity();
		double omega2 = collider2.getAngularVelocity();
		
		//Collision with any fraction of energy conserved
		double energyFractionConserved = 1.0;
		double a = (1/m1 + 1/m2 + (r1Perp * r1Perp) / I1 + (r2Perp * r2Perp) / I2);
		double b = Vector.dotProd(collider1.getVelocity(), unitNormal) - Vector.dotProd(collider2.getVelocity(), unitNormal) + omega1 * r1Perp - omega2 * r2Perp;
		double previousEnergyDoubled = 2 * (collider1.getKineticEnergy() + collider2.getKineticEnergy());

		double root = b * b - a * previousEnergyDoubled * (1 - energyFractionConserved);
		double collisionForce = (-b + (root < 0 ? 0 : Math.sqrt(root))) / a;
				
		//For energy conservation (redundant)
		//double collisionForce = 2 * (Vector.dotProd(collider2.getVelocity(), unitNormal) - Vector.dotProd(collider1.getVelocity(), unitNormal) + omega2 * r2Perp - omega1 * r1Perp) / (1/m1 + 1/m2 + Math.pow(r1Perp, 2) / I1 + Math.pow(r2Perp, 2) / I2);
		
		
		//For infinite mass (redundant)
		//double collisionForce = -2 * m2 * I2 * (Vector.dotProd(collider2.getVelocity(), unitNormal) + collider2.getAngularVelocity() * r2Perp) / (I2 + m2 * r2Perp * r2Perp);
		
		
		force = unitNormal.scale(collisionForce);
	}
	
	public void apply() {
		collider1.applyForce(force, location);
		collider2.applyForce(force.negative(), location);	
	}
	
	public void uncollide() {//Moves any movable object out of the collision area
		for(int i = 1; i <= 2; i++) {
			Solid collider = i == 1 ? collider1 : collider2;
			if(collider.getMass() != Double.POSITIVE_INFINITY) {
				Line deepnessCheck = new Line(location, Vector.add(location, unitNormal.scale((i == 1 ? -1 : 1) * 0xffffff)));
				List<Vector> intersectionsWithDeepnessCheck = collider.getPolygon().getIntersections(deepnessCheck);
				Vector moveOut = location.subtract(Vector.closest(location, intersectionsWithDeepnessCheck.toArray(new Vector[intersectionsWithDeepnessCheck.size()])));
				collider.setPosition(Vector.add(collider.getPosition(), moveOut));
			}
		}	
	}
	
	public static void uncollide(List<Collision> collisions) {
		for(Collision collision : collisions) {
			collision.uncollide();
		}
	}
	
	
	public Vector getLocation() {
		return location;
	}
	
	public Vector getForce() {
		return force;
	}
	
	public boolean hasPriority() {
		return (collider1.getMass() == Double.POSITIVE_INFINITY) || (collider2.getMass() == Double.POSITIVE_INFINITY);
	}
}
