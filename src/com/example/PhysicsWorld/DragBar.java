/**
Creates the dragbar and handles all the dragbar methods, has the dragbar attributes
Physics World
ICS-3UP
@authors Viral Patel, Vanshil Shah, Adit Patel, Kunj Patel
@version May 1, 2014
 */
package com.example.PhysicsWorld;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class DragBar {
	float x, y, width, height, startVal, endVal, currentVal, margin = 25, startX, middleY, endX, currentX;
	
	//Constructor
	public DragBar(float x, float y, float width, float height, float startVal, float endVal, float currentVal){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.startVal = startVal;
		this.endVal = endVal;
		this.currentVal = currentVal;
		startX = x+margin;
		middleY = y+height/2;
		endX = (x+width)-margin;
		currentX = startX + (endX-startX)/((endVal - startVal)/currentVal);
	}
	
	public void draw(Canvas canvas, Paint paint){//Draws the dragbar
		
		paint.setStrokeWidth(2);
		canvas.drawLine(startX, middleY, endX, middleY, paint);
		
		canvas.drawRect((int)(currentX-12.5), (int)(middleY-25), (int)(currentX+12.5), (int)(middleY+25), paint);
		
		paint.setStrokeWidth(4);
		paint.setTextSize(30);
		canvas.drawText(startVal + "", x, middleY-paint.getTextSize()/2, paint);
		canvas.drawText(endVal + "",  endX, middleY-paint.getTextSize()/2, paint);
		
		String cv = String.format("%.2f", currentVal);//Shows only		
		canvas.drawText(cv, currentX-25, middleY-50, paint);
		
		paint.setStrokeWidth(2);
	}
	
	public boolean contains(float x, float y){//Checks if the selected x and y points are contained by the dragbar
		Rect r = new Rect((int)(this.x), (int)(this.y), (int)(this.x + this.width), (int)(this.y + this.height));
		if (r.contains((int)x, (int)y)){
			return true;
		}
		return false;
	}
	
	public void updateValue(float x, float y){//updates the value written by the dragbar based on where the user taps along the line
		if(this.contains(x, y)){
			if(x>startX && x<endX){
				currentX = x;	
			}else if(x>endX){
				currentX = endX;	
			}else if(x<startX){
				currentX = startX;
			}
			currentVal = (startVal + (endVal-startVal)/((endX - startX)/(currentX-startX)));
		}
	}

}
