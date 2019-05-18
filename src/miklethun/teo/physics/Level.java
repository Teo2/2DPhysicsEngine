package miklethun.teo.physics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Level {
	public static double gravity = 0.04;
	public List<Solid> objects = new ArrayList<Solid>();
	public boolean paused = false;
	public Level() {
		//objects.add(new Movable(new Vector(120,90), new Vector(210, 110), new Vector(200,200), new Vector(170,240), new Vector(100,200), new Vector(140,140)));
		//objects.add(new Movable(new Vector(307,333), new Vector(377, 300), new Vector(400,400), new Vector(300,500)));
		//objects.add(new Movable(new Vector(300,350), new Vector(400, 450), new Vector(500,350), new Vector(400,250)));
		
		//for(int i = 0; i < 1; i++) {
		//	objects.add(new Movable(new Vector(30 + i * 10,30), new Vector(30 + i * 10,40), new Vector(40 + i * 10,30)));
		//}
		
		
		for(int points = 3; points < 250; points++) {
			Vector[] circlePoints = new Vector[points];
			double radius = 1;
			for(int i = 0; i < points; i++) {
				circlePoints[i] = new Vector(Math.cos(2 * Math.PI * i / points), Math.sin(2 * Math.PI * i / points)).scale(radius).add(new Vector(550,400));
			}
			Movable circle = new Movable(circlePoints);
			objects.add(circle);
			
			System.out.println(circle.getInertia());
			//System.out.println(circle.getMass());
		}
		
		
		//slope
		//objects.add(new LevelGeometry(new Vector(-100,0), new Vector(-100, 1000), new Vector(1000,1000)));
		
		//slit
		objects.add(new LevelGeometry(new Vector(450,-100), new Vector(450, 400), new Vector(470,400), new Vector(470,-100)));
		objects.add(new LevelGeometry(new Vector(450, 1000), new Vector(450, 500), new Vector(470,500), new Vector(470, 1000)));
		
		//walls
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
		if(!paused) {
			for(Solid object : objects) {
				object.applyForce(new Vector(0, gravity).scale(object.getMass()), object.getPosition());
			}
			List<Collision> collisions = Collision.getAllCollisions(objects);
			Collision.collide(collisions);
			for(Solid object : objects) {
				object.update();
			}
			Collision.uncollide(collisions, 1);
			
			double maxIterations = 10.0;
			//Collision.uncollide(collisions, 1/maxIterations);
			//for(int i = 2; i <= maxIterations; i++) {
			//	Collision.uncollide(Collision.getAllCollisions(objects), i / maxIterations);
			//}
			
			System.out.println(objects.get(0).getMechanicalEnergy() + objects.get(1).getMechanicalEnergy());
		}
		else {
			if(Game.mousePressed[1]) {
				int points = 3;
				Vector[] circlePoints = new Vector[points];
				double radius = 80;
				for(int i = 0; i < points; i++) {
					circlePoints[i] = new Vector(Math.cos(2 * Math.PI * i / points), Math.sin(2 * Math.PI * i / points)).scale(radius).add(new Vector(Game.mouseX,Game.mouseY));
				}
				Movable circle = new Movable(circlePoints);
				objects.add(0, circle);
			}
		}
		if(Game.keysPressed[KeyEvent.VK_SPACE]) {
			paused = !paused;
		}
	}
}
