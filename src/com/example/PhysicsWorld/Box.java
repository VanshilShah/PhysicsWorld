/**
Creates the box and handles all the box methods, has the box attributes
Physics World
ICS-3UP
@authors Viral Patel, Vanshil Shah, Adit Patel, Kunj Patel
@version May 1, 2014*/
package com.example.PhysicsWorld;

import android.graphics.Rect;

public class Box extends Shape {
	
	//Constructors
	public Box(float x, float y, int c) {
		super(x, y, c);
		this.updatePoints();
	}
	public Box(float x, float y, float width, float height, int c){
		super(x, y, width, height, c);
		this.updatePoints();
	}
	public Box(Box b){
		super(b.getInitX(), b.getInitY(), b.getWidth(), b.getHeight(),
				b.getCurrentX(), b.getCurrentY(), b.getCentreX(), b.getCentreY(),
				b.getColor(), b.getPoints(), b.getNetForceX(), b.getNetForceY(),
				b.getMass(), b.getaTime(), b.getAcceleration(), b.getVelocity(),
				b.getForces());
		this.updatePoints();
	}
	
	public void updatePoints(){//Updates the points of the shape
		float height = width;
		points = new float[4][2];
		points[0][0] = currentX;
		points[0][1] = currentY;
		
		points[1][0] = currentX+width;
		points[1][1] = currentY;
		
		points[2][0] = currentX+width;
		points[2][1] = currentY+height;
		
		points[3][0] = currentX;
		points[3][1] = currentY+height;
		
		centreX = currentX + width/2;
		centreY = currentY+ height/2;
	}
	public Rect getRect(){//Returns a boundingRect, that is used to check whether or not the user has tapped the box
		return new Rect((int)points[0][0], (int)points[0][1], (int)points[2][0], (int)points[2][1]);
	}
	
}
