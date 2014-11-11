/**
Creates button and handles button attributes
Physics World
ICS-3UP
@authors Viral Patel, Vanshil Shah, Adit Patel, Kunj Patel
@version May 1, 2014*/
package com.example.PhysicsWorld;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Button {
	public float x, y, width, height;
	public Rect rect;
	public String text = "";

	//Constructors
	public Button(float x, float y, float width, float height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		rect = new Rect((int)x, (int)y, (int)(x+width), (int)(y+height));
	}
	public Button(float x, float y, float width, float height, String text){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		rect = new Rect((int)x, (int)y, (int)(x+width), (int)(y+height));
		this.text = text;
	}

	public boolean contains(float x, float y){//Checks if the selected x and y points are contained by the button
		if(rect.contains((int)x, (int)y)){
			return true;
		}
		return false;
	}
	
	public void setText(String text){//Changes the text of the Button
		this.text = text;
	}
	
	public void draw(Canvas canvas, Paint paint){//Draws the button at the x and y location of the Button
		paint.setStrokeWidth(4);
		paint.setTextSize(15*(width/text.length())/10);//Ensures that the text always fits in the button
		paint.setColor(Color.BLACK);
		canvas.drawRect(rect, paint);
		paint.setColor(Color.BLACK);
		canvas.drawText(text, x, y+paint.getTextSize(), paint);
	}

	
}
