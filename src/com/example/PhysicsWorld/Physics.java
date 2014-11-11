/**
Handles physics for all objects
Physics World
ICS-3UP
@authors Viral Patel, Vanshil Shah, Adit Patel, Kunj Patel
@version May 1, 2014
 */

package com.example.PhysicsWorld;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Physics {
	
	public boolean timeStart = false, gravityEnabled = true;
	
	public int selectedBox = -1, selectedTriangle = -1, selectedCircle =-1;
	
	public float timeElapsed, timeOffset,elasticEfficency, uS = 0.5f, uK = 0.4f, g = 9.81f;
	public float [][] intersectionPoints = {{0,0},{0,0}};
	public float[][] intersectedLine;

	public double hScale, vScale;
	
	public List<Box> boxes = new ArrayList<Box>(), oldBoxes = new ArrayList<Box>();
	public List<Circle> circles = new ArrayList<Circle>(), oldCircles = new ArrayList<Circle>();
	public List<Triangle> triangles = new ArrayList<Triangle>(), oldTriangles = new ArrayList<Triangle>();
	


	//Constructor
	public Physics(){
		elasticEfficency = 0.99f;
		hScale = .01; // pixels/meter
		vScale = .01;
		timeElapsed = 0;
		timeOffset = 0;
	}
	
	public void start(){//Resets time and other variables related to simulation
		if(gravityEnabled){
			addGravity();
		}
		timeOffset = System.nanoTime();
		timeElapsed = 0;
		timeStart = true;
		saveCircles();
	}
		private void saveCircles(){
			oldCircles.clear();
			for(Circle b: this.circles){
				oldCircles.add(new Circle(b));
			}
		}
		private void addGravity(){
			for (int i = 0; i < boxes.size(); i++){
				Box b = boxes.get(i);
				b.addForce(new Vector2D(0, b.mass*this.g, 0, 999999999.0));
				boxes.remove(i);
				boxes.add(i, b);
			}
			 
			for (int i = 0; i < triangles.size(); i++){
				Triangle b = triangles.get(i);
				b.addForce(new Vector2D(0, b.mass*this.g, 0, 999999999.0));
				triangles.remove(i);
				triangles.add(i, b);
			}
			
			for (int i = 0; i < circles.size(); i++){
				Circle b = circles.get(i);
				b.addForce(new Vector2D(0, b.mass*this.g, 0, 999999999.0));
				circles.remove(i);
				circles.add(i, b);
			}
		}
		
		
	public void resetShapes(){//Resets the moving objects to their position before the simulation
		loadCircles();
		timeStart = false;
	}
		private void loadCircles(){
			circles.clear();
			for(Circle b: this.oldCircles){
				b.forces.remove(b.forces.size()-1);//removes the gracity added at the start of simulation
				circles.add(new Circle(b));
			}
		}
	
	public void update(){//runs kinematics if the mode has been enabled	
		if (timeStart){
			this.timeElapsed = (System.nanoTime() - this.timeOffset)/1000000000;
			runKinematics();	
		}
		regenPoints();
	}	
		private void runKinematics(){
			for (int i = 0; i < circles.size(); i++){
				Circle b = circles.get(i);
				b.calculatePhysics(timeElapsed);
				b.setxVelocity(b.getxVelocity() + b.getxAcceleration());
				b.setyVelocity(b.getyVelocity() + b.getyAcceleration());
				if(this.checkAllIntersectionsWith(b)){
					bounceCollision(b, intersectedLine);
				}
				b.setX((float)(b.getX() + this.hScale * b.getxVelocity()));
				b.setY((float)(b.getY() + this.vScale * b.getyVelocity()));	
				b.updatePoints();
				circles.remove(i);
				circles.add(i, b);
			}
		}
			private void bounceCollision(Circle c, float[][] line){//bounces objects in the case of a collision
				
				double slope = -(line[1][0] - line[0][0])/(line[1][1] - line[0][1]);
				double lineSlope = (line[1][1] - line[0][1])/(line[1][0] - line[0][0]);
				double angle = Math.tanh(lineSlope);
				angle = (c.getVelocity().getDirection() - angle)%(Math.PI/2);
				
				double Ap = c.getVelocity().getMagnitude() * Math.sin(angle);
				
				float newXVelocity = (float) (c.getVelocity().getXMagnitude()+ 2*Ap * Math.cos(Math.tanh(slope)));
				float newYVelocity = (float) (c.getVelocity().getYMagnitude()+ 2*Ap * Math.sin(Math.tanh(slope)));
				c.setxVelocity(newXVelocity);
				c.setyVelocity(newYVelocity);
			}	
			private void rollCollision(Circle c, float[][] line){//rolls objects in the case of a collision
				double slope = -(line[1][0] - line[0][0])/(line[1][1] - line[0][1]);
				double lineSlope = (line[1][1] - line[0][1])/(line[1][0] - line[0][0]);
				double angle = Math.tanh(lineSlope);
				angle = (c.getVelocity().getDirection() - angle)%(Math.PI/2);
				double Ap = c.getVelocity().getMagnitude() * Math.sin(angle);
				
				float newXVelocity = (float) (c.getVelocity().getXMagnitude()+ Ap * Math.cos(Math.tanh(slope)));
				float newYVelocity = (float) (c.getVelocity().getYMagnitude()+ Ap * Math.sin(Math.tanh(slope)));
				c.setxVelocity(newXVelocity);
				c.setyVelocity(newYVelocity);
			}
			private void regenPoints(){
				for(int i = 0; i < boxes.size(); i++){
					Box b = boxes.get(i);
					b.updatePoints();
					boxes.remove(i);
					boxes.add(i, b);
					
				}
				for(int i = 0; i < triangles.size(); i++){
					Triangle t = triangles.get(i);
					t.updatePoints();
					triangles.remove(i);
					triangles.add(i, t);
					
				}
				for(int i = 0; i < circles.size(); i++){
					Circle c = circles.get(i);
					c.updatePoints();
					circles.remove(i);
					circles.add(i, c);	
				}
			}
	
	public int[] checkPoints(float x, float y){/*checks whether or not the selected points are inside a shape
												and returns an array containing what type of shape is selected
												and its position*/
		for(int i = 0; i < boxes.size(); i++){
			if(boxes.get(i).getRect().contains((int)x, (int)y)){
				selectedBox = i;
				selectedCircle = -1;
				selectedTriangle = -1;
				int[] arr = {0, i};
				return arr;
			}
		}
		for(int i = 0; i < circles.size(); i++){
			if(circles.get(i).getRect().contains((int)x, (int)y)){
				selectedCircle = i;
				selectedBox = -1;
				selectedTriangle = -1;
				int[] arr = {1, i};
				return arr;
			}
		}
		for(int i = 0; i < triangles.size(); i++){
			if(triangles.get(i).getRect().contains((int)x, (int)y)){
				selectedTriangle = i;
				selectedBox = -1;
				selectedCircle = -1;
				int[] arr = {2, i};
				return arr;
			}
		}
		int[] arr = {-1, -1};
		return arr;
	}

	public boolean checkAllIntersectionsWith(Shape s){/*checks whether or not Shape s intersects with any of 
														the other shapes*/
		for(int i = 0; i < boxes.size(); i++){
			if(checkIntersections(boxes.get(i).getPoints(), s)){
				return true;
			}
		}
		for(int i = 0; i < circles.size(); i++){
			if(s!=circles.get(i)){
				if(checkIntersections(circles.get(i).getPoints(), s)){
					return true;
				}
			}
		}
		for(int i = 0; i < triangles.size(); i++){
			if(checkIntersections(triangles.get(i).getPoints(), s)){
				return true;
			}
		}
		return false;
	}
		boolean checkIntersections(float[][] points1, Shape b) {//checks intersection between a point array and a shape
			float[][] points2 = b.getPoints();
			for(int i = 0; i < points1.length; i++){
				float x1, y1, x2, y2, x3, y3, x4, y4;
				if(i!=points1.length-1){
					x1 = points1[i][0];
					y1 = points1[i][1];
					x2 = points1[i+1][0];
					y2 = points1[i+1][1];
				}			
				else{
					x1 = points1[i][0];
					y1 = points1[i][1];
					x2 = points1[0][0];
					y2 = points1[0][1];
				}
				for(int j = 0 ; j < points2.length; j++){
					if(j!=points2.length-1){
						x3 = points2[j][0];
						y3 = points2[j][1];
						x4 = points2[j+1][0];
						y4 = points2[j+1][1];
					}		
					else{
						x3 = points2[j][0];
						y3 = points2[j][1];
						x4 = points2[0][0];
						y4 = points2[0][1];
					}
					if(checkLineIntersection(x1, y1, x2, y2, x3, y3, x4, y4)){
						float[][] in = {{x1, y1}, {x2, y2}};
						intersectedLine = in;
						intersectionPoints = getIntersectionPoints(x1, y1, x2, y2, x3, y3, x4, y4);
						return true;
					}
				}
			}
			return false;
		}	
			public boolean checkLineIntersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){ // Checks Line to Line intersection, returns false if either of the lines have zero length
		      if (x1 == x2 && y1 == y2 ||
		              x3 == x4 && y3 == y4){
		           return false;
		        }
		        // Fastest method, based on Franklin Antonio's "Faster Line Segment Intersection" topic "in Graphics Gems III" book (http://www.graphicsgems.org/)
		        double ax = x2-x1;
		        double ay = y2-y1;
		        double bx = x3-x4;
		        double by = y3-y4;
		        double cx = x1-x3;
		        double cy = y1-y3;
	
		        double alphaNumerator = by*cx - bx*cy;
		        double commonDenominator = ay*bx - ax*by;
		        if (commonDenominator > 0){
		           if (alphaNumerator < 0 || alphaNumerator > commonDenominator){
		              return false;
		           }
		        }else if (commonDenominator < 0){
		           if (alphaNumerator > 0 || alphaNumerator < commonDenominator){
		              return false;
		           }
		        }
		        double betaNumerator = ax*cy - ay*cx;
		        if (commonDenominator > 0){
		           if (betaNumerator < 0 || betaNumerator > commonDenominator){
		              return false;
		           }
		        }else if (commonDenominator < 0){
		           if (betaNumerator > 0 || betaNumerator < commonDenominator){
		              return false;
		           }
		        }
		        if (commonDenominator == 0){
		           // This code wasn't in Franklin Antonio's method. It was added by Keith Woodward.
		           // The lines are parallel.
		           // Check if they're collinear.
		           double y3LessY1 = y3-y1;
		           double collinearityTestForP3 = x1*(y2-y3) + x2*(y3LessY1) + x3*(y1-y2);   // see http://mathworld.wolfram.com/Collinear.html
		           // If p3 is collinear with p1 and p2 then p4 will also be collinear, since p1-p2 is parallel with p3-p4
		           if (collinearityTestForP3 == 0){
		              // The lines are collinear. Now check if they overlap.
		              if (x1 >= x3 && x1 <= x4 || x1 <= x3 && x1 >= x4 ||
		                    x2 >= x3 && x2 <= x4 || x2 <= x3 && x2 >= x4 ||
		                    x3 >= x1 && x3 <= x2 || x3 <= x1 && x3 >= x2){
		                 if (y1 >= y3 && y1 <= y4 || y1 <= y3 && y1 >= y4 ||
		                       y2 >= y3 && y2 <= y4 || y2 <= y3 && y2 >= y4 ||
		                       y3 >= y1 && y3 <= y2 || y3 <= y1 && y3 >= y2){
		                    return true;
		                 }
		              }
		           }
		           return false;
		        }
		        return true;
		}
				public float[][] getIntersectionPoints(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){//returns the intersection point of two shapes
					float [][] coordinates = new float [2][2];
					double denominator = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
					if (denominator == 0.0) { // Lines are parallel.	
						return coordinates = twoPointCollision(x1, y1, x2, y2, x3, y3, x4, y4);
					}		
					//core math for getting the intersection points
					double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3))/denominator;
					double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3))/denominator;
					if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
					      // Get the intersection point.
						coordinates[0][0] = (int) (x1 + ua*(x2 - x1));
						coordinates[0][1] = (int) (y1 + ua*(y2 - y1));
					}
					
					return coordinates;
				}
				public float [][] twoPointCollision (float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){//returns the intersection point of two shapes
				
				float [][] coordinates = new float[2][2];
						
				if(checkLineIntersection(x1, y1, x2, y2, x3, y3, x3+1, y3+1)){
					coordinates[0][0] = x3;
					coordinates[0][1] = y3;
				}
				else if (checkLineIntersection(x1, y1, x2, y2, x4, y4, x4+1, y4+1)){
					coordinates[0][0] = x4;
					coordinates[0][1] = y4;
				}
				if(checkLineIntersection(x3, y3, x4, y4, x1, y1, x1+1, y1+1)){
					coordinates[1][0] = x1;
					coordinates[1][1] = y1;
				}
				else if (checkLineIntersection(x3, y3, x4, y4, x2, y2, x2+1, y2+1)){
					coordinates[1][0] = x2;
					coordinates[1][1] = y2;
				
				}
				return coordinates;
			}
	
	///draws all of the shapes in this class
	public void drawShapes(Canvas canvas, Paint paint){
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(4);
		for(int i = 0; i < boxes.size(); i++){
			if(i == selectedBox){
				paint.setColor(Color.BLUE);
			}
			else{
				paint.setColor(Color.BLACK);
			}
			boxes.get(i).Draw(canvas, paint);
		}
		for(int i = 0; i < circles.size(); i++){
			if(i == selectedCircle){
				paint.setColor(Color.BLUE);
			}
			else{
				paint.setColor(Color.BLACK);
			}
			circles.get(i).Draw(canvas, paint);
		}
		for(int i = 0; i < triangles.size(); i++){
			if(i == selectedTriangle){
				paint.setColor(Color.BLUE);
			}
			else{
				paint.setColor(Color.BLACK);
			}
			triangles.get(i).Draw(canvas, paint);
		}
	}

	
	//Getters and Setters
	public void addBox(Box b){
		boxes.add(b);
	}
	public void addTriangle (Triangle t){
		triangles.add(t);
	}
	public void addCircle(Circle c){
		circles.add(c);
	}
	
	public void addBox(int i, Box b){
		boxes.add(i, b);
	}
	public void addCircle(int i, Circle c){
		circles.add(i, c);
	}
	public void addTriangle(int i, Triangle t){
		triangles.add(i, t);
	}
	
	public Box getBox(int i){
		return boxes.get(i);
	}
	public Circle getCircle(int i){
		return circles.get(i);
	}
	public Triangle getTriangle(int i){
		return triangles.get(i);
	}
	
	
	public float getuS() {
		return uS;
	}
	public void setuS(float uS) {
		this.uS = uS;
	}

	public float getuK() {
		return uK;
	}
	public void setuK(float uK) {
		this.uK = uK;
	}

	public float getG() {
		return g;
	}
	public void setG(float g) {
		this.g = g;
	}
}