/**
Handles all the shape methods, has the shape attributes
Physics World
ICS-3UP
@authors Viral Patel, Vanshil Shah, Adit Patel, Kunj Patel
@version May 1, 2014
 */

package com.example.PhysicsWorld;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Shape {
	public float initX, initY, width, height;
	public float currentX, currentY;
	public float centreX, centreY;
	public int color;
	float[][] points;
	public double netForceX, netForceY;
	//mass is in kg
	public float mass = 50, aTime = 0;
	public Vector2D acceleration;
	public Vector2D velocity;
	public List<Vector2D> forces = new ArrayList<Vector2D>();
	
	//Constructors
	public Shape(float x, float y, int c){
		this.initX = this.currentX = x;
		this.initY = this.currentY = y;
		this.color = c;
		this.velocity = new Vector2D(0, 0);
		this.acceleration = new Vector2D(0, 0);
	}
	public Shape(float x, float y, float width, float height, int c){
		this(x,y,c);
		this.width = width;
		this.height = height;	
	}
	public Shape(float initX, float initY, float width, float height,
			float currentX, float currentY, float centreX, float centreY,
			int color, float[][] points, double netForceX, double netForceY,
			float mass, float aTime, Vector2D acceleration, Vector2D velocity,
			List<Vector2D> forces) {
		super();
		this.initX = initX;
		this.initY = initY;
		this.width = width;
		this.height = height;
		this.currentX = currentX;
		this.currentY = currentY;
		this.centreX = centreX;
		this.centreY = centreY;
		this.color = color;
		this.points = points;
		this.netForceX = netForceX;
		this.netForceY = netForceY;
		this.mass = mass;
		this.aTime = aTime;
		this.acceleration = new Vector2D();
		this.velocity = new Vector2D();
		this.forces = forces;
	}

	public void rotatePoints(float x1, float y1, int degrees){//rotates the points of this shape about point (x1, y1)
		for(int i = 0; i < points.length; i++){
			float[] newPoints = rotateXandYPoint(x1, y1, points[i][0], points[i][1], degrees);
			points[i][0] = newPoints[0];
			points[i][1] = newPoints[1];
		}
	}	
		private float[] rotateXandYPoint(float x1, float y1, float x2, float y2, float degrees){/*rotates point (x2, y2)
		 																						about point (x1, y1)*/
			float[] arr = new float[2];
			
			double rx = x2 - x1;
	        double ry = y2 - y1;
	        double angle = (float) Math.atan((y2 - y1)/(x2 - x1));
	        if (rx > 0){
	        	if (ry < 0){
	        		angle = 2*Math.PI - Math.abs(angle);
	        	}
	        	else if(ry == 0){
	        		angle = 0;
	        	}
	        }
	        else if (rx < 0){
	        	if (ry > 0){
	        		angle = Math.PI - Math.abs(angle);
	        	}
	        	else if (ry < 0){
	        		angle = Math.PI + Math.abs(angle);
	        	}
	        	else if (ry == 0){
	        		angle = Math.PI;
	        	}
	        }
	        if (rx == 0){
	        	if (ry > 0){
	        		angle = Math.toRadians(90);
	        	}
	        	else if (ry > 0){
	        		angle = Math.toRadians(270);
	        	}
	        }
	    	float r = (float) Math.sqrt((x1 - x2)*(x1 - x2)  + (y1 - y2)*(y1 - y2));
			
			double a = Math.toRadians(degrees);
			x2 = (float) (Math.cos(angle+a)*r) + x1;
			y2 = (float) (Math.sin(angle+a)*r) + y1;
			
			arr[0] = x2;
			arr[1] = y2;
			
			return arr;
		}
	
	public void calculatePhysics(float time){//calculates the acceleration of the shape based on the netForces applied on it
		time = time-this.aTime;
		netForceX = 0;
		netForceY = 0;
		for(int i = 0; i < forces.size(); i++){
			Vector2D f = forces.get(i); 
			if(f.getStartTime() <= time && f.getEndTime() >= time){
				this.netForceX += f.getXMagnitude();
				this.netForceY += f.getYMagnitude();
			}
			else if (!f.ended){
				f.ended = true;
				this.aTime = time;
			}
		}		
		this.acceleration.setXMagnitude(netForceX/mass);
		this.acceleration.setYMagnitude(netForceY/mass);
	}
	
	public void Draw(Canvas canvas, Paint paint){//draws each individual line of the shape
		for(int i = 0; i < points.length; i++){
			if(i!=points.length-1){
				canvas.drawLine(points[i][0], points[i][1], points[i+1][0], points[i+1][1], paint);	
			}else{
				canvas.drawLine(points[i][0], points[i][1], points[0][0], points[0][1], paint);
			}
		}
	}
	

	
	//Getters and Setters
	public List<Vector2D> getForces() {
		return forces;
	}
	public Vector2D getForce(int i){
		return forces.get(i);
	}
	public void addForce(Vector2D f){
		forces.add(f);
	}
	public void addForce(Vector2D f, int i){
		forces.add(i, f);
	}
	
	public float getX(){
		return this.currentX;
	}
	public float getY(){
		return this.currentY;
	}
	
	public void setX(float x){
		this.currentX = x;
	}
	public void setY(float y){
		this.currentY = y;
	}
	
	
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	
	public float getWidth() {
		return this.width;
	}
	public void setHeight(float height) {
		this.height = height;
	}

	
	public float getMass() {
		return mass;
	}
	public void setMass(float mass) {
		this.mass = mass;
	}

		

	public Vector2D getVelocity() {
		return velocity;
	}
	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}
		public float getxVelocity() {
			return (float)this.velocity.getXMagnitude();
		}
		public void setxVelocity(float xVelocity) {
			this.velocity.setXMagnitude(xVelocity);
		}
		public float getyVelocity() {
			return (float)this.velocity.getYMagnitude();
		}
		public void setyVelocity(float yVelocity) {
			this.velocity.setYMagnitude(yVelocity);
		}
	
	public Vector2D getAcceleration(){
		return acceleration;
	}
	public void setAcceleration(Vector2D acceleration) {
		this.acceleration = acceleration;
	}
		public float getxAcceleration() {
			return (float)this.acceleration.getXMagnitude();
		}
		public void setxAcceleration(float xAcceleration) {
			this.acceleration.setXMagnitude(xAcceleration);
		}
		public float getyAcceleration() {
			return (float)this.acceleration.getYMagnitude();
		}
		public void setyAcceleration(float yAcceleration) {
			this.acceleration.setYMagnitude(yAcceleration);
		}
	
	public float[][] getPoints(){
		return this.points;
	}
		
public void setPoints(float[][] points) {
		this.points = points;
	}
	
	
	public float getInitX() {
		return initX;
	}
	public void setInitX(float initX) {
		this.initX = initX;
	}
	public float getInitY() {
		return initY;
	}
	public void setInitY(float initY) {
		this.initY = initY;
	}

	
	public float getCurrentX() {
		return currentX;
	}
	public void setCurrentX(float currentX) {
		this.currentX = currentX;
	}
	public float getCurrentY() {
		return currentY;
	}
	public void setCurrentY(float currentY) {
		this.currentY = currentY;
	}

	
	public float getCentreX() {
		return centreX;
	}
	public void setCentreX(float centreX) {
		this.centreX = centreX;
	}
	public float getCentreY() {
		return centreY;
	}
	public void setCentreY(float centreY) {
		this.centreY = centreY;
	}

	
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}

	
	public double getNetForceX() {
		return netForceX;
	}
	public void setNetForceX(double netForceX) {
		this.netForceX = netForceX;
	}
	public double getNetForceY() {
		return netForceY;
	}
	public void setNetForceY(double netForceY) {
		this.netForceY = netForceY;
	}
	

	public float getaTime() {
		return aTime;
	}
	public void setaTime(float aTime) {
		this.aTime = aTime;
	}


}
