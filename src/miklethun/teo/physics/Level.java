package miklethun.teo.physics;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Level {
	public static double gravity = 0.04;
	public List<Solid> objects = new ArrayList<Solid>();
	
	public Level() {
		//objects.add(new Movable(new Vector(120,90), new Vector(210, 110), new Vector(200,200), new Vector(170,240), new Vector(100,200), new Vector(140,140)));
		//objects.add(new Movable(new Vector(307,333), new Vector(377, 300), new Vector(400,400), new Vector(300,500)));
		//objects.add(new Movable(new Vector(300,350), new Vector(400, 450), new Vector(500,350), new Vector(400,250)));
		
		int points = 6;
		Vector[] circlePoints = new Vector[points];
		double radius = 100;
		for(int i = 0; i < points; i++) {
			circlePoints[i] = new Vector(Math.cos(2 * Math.PI * i / points), Math.sin(2 * Math.PI * i / points)).scale(radius).add(new Vector(550,400));
		}
		Movable circle = new Movable(circlePoints);
		objects.add(circle);
		
		System.out.println(circle.getInertia());
		System.out.println(circle.getMass());
		
		
		
		objects.add(new LevelGeometry(new Vector(-100,-100), new Vector(-100, 1000), new Vector(10,1000), new Vector(10,-100)));
		objects.add(new LevelGeometry(new Vector(900,-100), new Vector(900, 1000), new Vector(1000,1000), new Vector(1000,-100)));
		objects.add(new LevelGeometry(new Vector(10,-100), new Vector(10, 10), new Vector(1000,10), new Vector(1000,-100)));
		objects.add(new LevelGeometry(new Vector(10,700), new Vector(10, 800), new Vector(1000,800), new Vector(1000,700)));
	}
	
	public void render(Graphics g) {
		for(Solid object : objects) {
			object.render(g);
		}
		//List<Collision> collisions = Collision.getAllCollisions(objects);
		//for(Collision collision : collisions) {
		//	collision.calculateForce();
		//	collision.getForce().scale(100).renderAsVector(g, 0xff0000, collision.getLocation());
		//}
		
		for(int i = 0; i < objects.size(); i++) {
			for(int j = i + 1; j < objects.size(); j++) {
				Vector[] intersections = objects.get(i).getPolygon().getIntersections(objects.get(j).getPolygon());
				for(int k = 0; k < intersections.length; k++) {
					intersections[k].render(g, 8, 0xff00ff);
				}
			}
		}
	}

	public void update() {
		for(Solid object : objects) {
			object.applyForce(new Vector(0, gravity).scale(object.getMass()), object.getPosition());
		}
		List<Collision> collisions = Collision.getAllCollisions(objects);
		Collision.collide(collisions);
		for(Solid object : objects) {
			object.update();
		}
		Collision.uncollide(collisions);
		
		//System.out.println(objects.get(0).toString());
	}
}
