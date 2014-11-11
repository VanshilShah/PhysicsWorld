/**
Creates the Triangle and handles all the Triangle methods, has the triangle attributes
Physics World
ICS-3UP
@authors Viral Patel, Vanshil Shah, Adit Patel, Kunj Patel
@version May 1, 2014
 */

package com.example.PhysicsWorld;

import android.graphics.Rect;

public class Triangle extends Shape {	
	float height;
	
	//Constructor
	public Triangle(float x, float y, float length, int c){
		super(x, y, length, length, c);
		width = length;
		this.updatePoints();
		height = y- (float) (Math.sqrt((length*length)-((length/2)*(length/2))));
	}

	public void updatePoints(){//Updates the points of the shape
		points = new float[3][2];
		float height =  (float)(Math.sqrt((width*width)-((width/2)*(width/2))));
		points[0][0] = currentX+width/2;
		points[0][1] = currentY;
		
		points[1][0] = currentX;
		points[1][1] = currentY+height;
		
		points[2][0] = currentX+width;
		points[2][1] = currentY+height;
		
		centreX = currentX + width/2;
		centreY = currentY + height/2;
	}
	public Rect getRect(){//Returns a boundingRect, that is used to check whether or not the user has tapped the box
		return new Rect((int)points[0][0], (int)points[0][1], (int)points[2][0], (int)points[2][1]);
	}
}
