/**
2D Vector Object that stores magnitudes on x an y axis, also gives the direction of the vector
Physics World
ICS-3UP
@authors Viral Patel, Vanshil Shah, Adit Patel, Kunj Patel
@version May 1, 2014
 */

package com.example.PhysicsWorld;

public class Vector2D {
	public boolean ended = false;
	private double xMagnitude, yMagnitude, magnitude, direction;
	private double startTime = 0, endTime = 2;

	//Constructors
	public Vector2D(){
		
	}
	public Vector2D(double xMagnitude, double yMagnitude){
		this.xMagnitude = xMagnitude;
		this.yMagnitude = yMagnitude;
		this.direction = 0;
	}
	public Vector2D(double xMagnitude, double yMagnitude, double startTime, double endTime){
		this.xMagnitude = xMagnitude;
		this.yMagnitude = yMagnitude;
		this.startTime = startTime;
		this.endTime = endTime;
		this.direction = 0;
	}
	
		
//Getters and Setters
	public double getXMagnitude(){
		return this.xMagnitude;
	}
	public double getYMagnitude(){
		return this.yMagnitude;
	}
	public void setXMagnitude(double xMagnitude) {
		this.xMagnitude = xMagnitude;
	}
	public void setYMagnitude(double yMagnitude) {
		this.yMagnitude = yMagnitude;
	}
	
	public double getMagnitude() {
		this.magnitude = Math.sqrt(xMagnitude*xMagnitude+yMagnitude*yMagnitude);
		return this.magnitude;
	}
	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}
	
	public double getDirection() {
		this.direction = Math.tanh(this.getXMagnitude()/this.getYMagnitude());
		return this.direction;
	}
	public void setDirection(double direction) {
		this.direction = direction;
	}
	
	
	public double getStartTime() {
		return startTime;
	}
	public void setStartTime(float startTime) {
		this.startTime = startTime;
	}

	public double getEndTime() {
		return endTime;
	}
	public void setEndTime(float endTime) {
		this.endTime = endTime;
	}
}
