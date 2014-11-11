/**
Creates the circle and handles all the circle methods, has the circle attributes
Physics World
ICS-3UP
@authors Viral Patel, Vanshil Shah, Adit Patel, Kunj Patel
@version May 1, 2014*/

package com.example.PhysicsWorld;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Circle extends Shape{
	public float radius;
	
	//Constructors
	public Circle( Circle b){
		super(b.getInitX(), b.getInitY(), b.getWidth(), b.getHeight(),
				b.getCurrentX(), b.getCurrentY(), b.getCentreX(), b.getCentreY(),
				b.getColor(), b.getPoints(), b.getNetForceX(), b.getNetForceY(),
				b.getMass(), b.getaTime(), b.getAcceleration(), b.getVelocity(),
				b.getForces());
		this.radius = b.radius;
		this.updatePoints();
	}
	public Circle(float x, float y, float r, int c) {
		super(x, y, r*2, r*2, c);
		width = r;
		this.radius = r;
		this.updatePoints();
	}
	
	public void updatePoints (){//Updates the points of the shape
		points = new float[8][2];
		
		float x1 = currentX, y1 = currentY;
		radius = width;
		points[0][0] = x1;
		points[0][1] = (float) (y1+(radius*2)*0.3);

		points[1][0] = (float) (x1+(radius*2)*0.3);
		points[1][1] = y1;

		points[2][0] = (float) (x1+(radius*2)*0.7);
		points[2][1] = y1;

		points[3][0] = (x1+(radius*2));
		points[3][1] = (float) (y1+(radius*2)*0.3);
	
		points[4][0] = (x1+(radius*2));
		points[4][1] = (float) (y1+(radius*2)*0.7);
		
		points[5][0] = (float) (x1+(radius*2)*0.7);
		points[5][1] = (y1+(radius*2));

		points[6][0] = (float) (x1+(radius*2)*0.3);
		points[6][1] = (y1+(radius*2));
		
		points[7][0] = x1;
		points[7][1] = (float) (y1+(radius*2)*0.7);
		
		centreX = currentX + width;
		centreY = currentY + height/2;
		
	}
	
	public Rect getRect(){//Returns a bounding rect around the circle
		return new Rect((int)(currentX) ,(int)(currentY) ,(int)(currentX+(radius*2)) ,(int)(currentY+(radius*2))) ;
	}	
	
	@Override
	public void Draw(Canvas canvas, Paint paint){//Overrides the standard draw method because circles do not have edges
		canvas.drawCircle(currentX+radius, currentY+radius, radius, paint);
	}
	
	public void drawPoints(Canvas canvas, Paint paint){//The original draw method to show where the circle collision occurs
		for(int i = 0; i < points.length; i++){
			if(i!=points.length-1){
				canvas.drawLine(points[i][0], points[i][1], points[i+1][0], points[i+1][1], paint);	
			}else{
				canvas.drawLine(points[i][0], points[i][1], points[0][0], points[0][1], paint);
			}
		}
	}
	
	
}
