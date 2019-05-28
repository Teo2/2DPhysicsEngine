package miklethun.teo.physics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Level {
	public static double gravity = 0.1;
	public List<Solid> objects = new ArrayList<Solid>();
	public boolean paused = false;
	public Level() {
		//objects.add(new Movable(new Vector(120,90), new Vector(210, 110), new Vector(200,200), new Vector(170,240), new Vector(100,200), new Vector(140,140)));
		//objects.add(new Movable(new Vector(307,333), new Vector(377, 300), new Vector(400,400), new Vector(300,500)));
		//objects.add(new Movable(new Vector(633, 311), new Vector(688, 288), new Vector(759, 348), new Vector(691, 388), new Vector(661, 364), new Vector(566, 414), new Vector(554, 340)));
		objects.add(new Movable(new Vector(295, 320), new Vector(281, 305), new Vector(283, 285), new Vector(299, 280), new Vector(312, 285), new Vector(322, 301), new Vector(318, 310), new Vector(425, 481), new Vector(386, 552), new Vector(499, 608), new Vector(545, 519), new Vector(437, 476), new Vector(332, 309), new Vector(332, 284), new Vector(309, 269), new Vector(266, 276), new Vector(268, 303), new Vector(275, 330)));
		
		//objects.add(new Movable(new Vector(616, 320), new Vector(673, 273), new Vector(696, 342), new Vector(785, 333), new Vector(770, 244), new Vector(691, 223), new Vector(743, 159), new Vector(801, 214), new Vector(848, 315), new Vector(776, 411), new Vector(688, 431), new Vector(648, 387), new Vector(586, 478), new Vector(541, 402), new Vector(536, 314), new Vector(508, 232), new Vector(558, 283), new Vector(592, 357), new Vector(596, 323)));
		//objects.add(new Movable(new Vector(570, 261), new Vector(778, 246), new Vector(790, 320), new Vector(689, 321), new Vector(715, 393), new Vector(603, 414), new Vector(568, 342), new Vector(494, 313), new Vector(582, 282)));
		
		
		//objects.add(new Movable(new Vector(,333), new Vector(377, 300), new Vector(400,400), new Vector(300,500)));
		//objects.add(new Movable(new Vector(300,350), new Vector(400, 450), new Vector(500,350), new Vector(400,250)));
		
		//for(int i = 0; i < 1; i++) {
		//	objects.add(new Movable(new Vector(30 + i * 10,30), new Vector(30 + i * 10,40), new Vector(40 + i * 10,30)));
		//}
		
		int points = 40;
		Vector[] circlePoints = new Vector[points];
		double radius = 30;
		for(int i = 0; i < points; i++) {
			circlePoints[i] = new Vector(Math.cos(2 * Math.PI * i / points), Math.sin(2 * Math.PI * i / points)).scale(radius).add(new Vector(300,300));
		}
		LevelGeometry circle = new LevelGeometry(circlePoints);
		objects.add(circle);
		
		
		//slope
		//objects.add(new LevelGeometry(new Vector(-100,0), new Vector(-100, 1000), new Vector(1000,1000)));
		
		//slit
		//objects.add(new LevelGeometry(new Vector(450,-100), new Vector(450, 400), new Vector(470,400), new Vector(470,-100)));
		//objects.add(new LevelGeometry(new Vector(450, 1000), new Vector(450, 500), new Vector(470,500), new Vector(470, 1000)));
		
		//walls
		//objects.add(new LevelGeometry(new Vector(-100,-100), new Vector(-100, 1000), new Vector(10,1000), new Vector(10,-100)));
		//objects.add(new LevelGeometry(new Vector(900,-100), new Vector(900, 1000), new Vector(1000,1000), new Vector(1000,-100)));
		//objects.add(new LevelGeometry(new Vector(10,-100), new Vector(10, 10), new Vector(1000,10), new Vector(1000,-100)));
		//objects.add(new LevelGeometry(new Vector(10,700), new Vector(10, 800), new Vector(1000,800), new Vector(1000,700)));
	}
	
	public void render(Graphics g) {
		for(Solid object : objects) {
			object.render(g);
		}
		
		Solid.centerOfMass(objects.toArray(new Solid[objects.size()])).render(g, 10, 0xff0000);
		//List<Collision> collisions = Collision.getAllCollisions(objects);
		//for(Collision collision : collisions) {
		//	collision.calculateForce();
		//	collision.getForce().scale(100).renderAsVector(g, 0xff0000, collision.getLocation());
		//}
		
		/*for(int i = 0; i < objects.size(); i++) {
			for(int j = i + 1; j < objects.size(); j++) {
				Vector[] intersections = objects.get(i).getPolygon().getIntersections(objects.get(j).getPolygon());
				for(int k = 0; k < intersections.length; k++) {
					intersections[k].render(g, 8, 0xff00ff);
				}
			}
		}*/
		for(int i = 0; i < points.size() - 1; i++) {
			new Line(points.get(i), points.get(i + 1)).render(g);
		}
	}

	private List<Vector> points = new ArrayList<Vector>();
	public void update() {
		if(!paused) {
			for(Solid object : objects) {
				object.applyForce(new Vector(0, gravity).scale(object.getMass()), object.getPosition());
			}
			/*for(int i = 0; i < objects.size(); i++) {
				for(int j = 0; j < objects.size(); j++) {
					if(j != i) {
						Vector r = objects.get(i).getPosition().subtract(objects.get(j).getPosition());
						double M = objects.get(i).getMass();
						double m = objects.get(j).getMass();
						objects.get(j).applyForce(r.makeUnitLength().scale(gravity * M * m / (r.magnitude() * r.magnitude())), objects.get(j).getPosition());
					}
				}
			}*/
			List<Collision> collisions = Collision.getAllCollisions(objects);
			Collision.collide(collisions);
			//Collision.uncollide(collisions, 1);
			for(Solid object : objects) {
				object.update();
			}
			
			//System.out.println(objects.get(0).getMechanicalEnergy() + objects.get(1).getMechanicalEnergy());
		}
		else {
			/*if(Game.mousePressed[1]) {
				int points = 30;
				Vector[] circlePoints = new Vector[points];
				double radius = 55;
				for(int i = 0; i < points; i++) {
					circlePoints[i] = new Vector(Math.cos(2 * Math.PI * i / points), Math.sin(2 * Math.PI * i / points)).scale(radius).add(new Vector(Game.mouseX,Game.mouseY));
				}
				Movable circle = new Movable(circlePoints);
				objects.add(0, circle);
			}*/
			if(Game.mousePressed[1]) {
				System.out.print("new Vector(" + Game.mouseX + ", " + Game.mouseY + "), ");
				points.add(new Vector(Game.mouseX, Game.mouseY));
			}
			if(Game.keysPressed[KeyEvent.VK_ENTER]) {
				objects.add(new Movable(points.toArray(new Vector[points.size()])));
				points.clear();
			}
		}
		
		if(Game.keysPressed[KeyEvent.VK_SPACE]) {
			paused = !paused;
		}
	}
}
