/**
Handles all the physics, creates all the objects, updates ll states, main simulation base
Physics World
ICS-3UP
@authors Viral Patel, Vanshil Shah, Adit Patel, Kunj Patel
@version May 1, 2014
 */
package com.example.PhysicsWorld;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.msa.R;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback  {
	 PanelThread _thread;	 
	 
	 public boolean pressed, boxPressed, trianglePressed, circlePressed, kinematics;
	 
	 public int boxHeight, boxWidth = 150;
	 public int selectedShapeLoc = -1; 
	 public int sEState = 0, sEHomescreen = 0, sEShapeSelected = 1, sEBoxSelected = 2, sETriangleSelected = 3, sECircleSelected = 4;
	 public int hState = -1, fSSelected = 0, fKSelected = 1, gSelected = 2;
	 public int eState = -1, sizeSelected = 0, massSelected = 1, forcesSelected = 2;
	 
	 public float fingerOffset = 125, touchX, touchY;
	 public float forceX, forceY;
	 
	 public Paint paint = new Paint();
	 public Shape selectedShape;
	 public Rect boxRect, triangleRect, circleRect, worldRect;
	 public Box tempBox, selectedBox;
	 public Circle tempCircle, selectedCircle;
	 public Triangle tempTriangle, selectedTriangle;
	 public Vector2D tempForce = new Vector2D(0, 0);
	
	 public Button backButton, fSButton, fKButton, gButton, playButton;
	 public Button sizeButton, massButton, forcesButton;
	 public Button but;
	 
	 public DragBar db;
	
	 public Physics physics = new Physics();
	 
	 public Bitmap backgroundImg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
	 
	 //Constructors
	 public DrawingPanel(Context context) { 
	        super(context); 
	    	this.setBackgroundColor(Color.WHITE);
	        paint.setAntiAlias(true);
	        paint.setStrokeWidth(6f);
	        paint.setStyle(Paint.Style.STROKE);
	        paint.setStrokeJoin(Paint.Join.ROUND);
	        getHolder().addCallback(this);
	    }
	 
	 //Essentially the main method, runs multiple times and is where updating and drawing is done.
	 	@Override 
	 public void onDraw(Canvas canvas) {
	        //do drawing stuff here.
	 		update();
	 		draw(canvas, paint);	 		
	    }
	
	 	
	 	@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	    }
	    @Override
		public void surfaceCreated(SurfaceHolder holder) {


	     setWillNotDraw(false); //Allows us to use invalidate() to call onDraw()


	     _thread = new PanelThread(getHolder(), this); //Start the thread that
	        _thread.setRunning(true);                     //will make calls to 
	        _thread.start();                              //onDraw()
	        init();
	    }
	    @Override
		public void surfaceDestroyed(SurfaceHolder holder) {
	     try {
	            _thread.setRunning(false);                //Tells thread to stop
	     _thread.join();                           //Removes thread from mem.
	 } catch (InterruptedException e) {}
	    }
//------------------------------------------The-Initialization-of-Objects-----------------------------------------------------------------------	   
	    private void init(){	    	
			initCreatorRects();
			initTempShapes();
			initHButtons();
			db = new DragBar(boxWidth*2, boxHeight*3, boxWidth*6, boxHeight, 1, 1, 1);	
	    }
	    private void initCreatorRects(){
	    	boxHeight = getHeight()/4;
	    	boxWidth = getWidth()/10;
	    	boxRect = new Rect(0, 0, boxWidth, boxHeight);
			triangleRect = new Rect(0, boxHeight, boxWidth, boxHeight*2); 
			circleRect = new Rect(0, boxHeight*2, boxWidth, boxHeight*3);
			worldRect = new Rect(boxWidth, 0, getWidth(), boxHeight*3);
	    }
	    private void initTempShapes(){
	    	int z = 500;
	    	tempBox = new Box(z, z, 0, 0, 0);
			tempCircle = new Circle(z, z, 0, 0);
			tempTriangle = new Triangle(z, z, 0, 0);
	    }
	    private void initHButtons(){
	    	float boxWidth2 = boxWidth*2;
	    	backButton = new Button(0, boxHeight*3, boxWidth2, boxHeight, "Back to Home");
	    	fSButton = new Button(boxWidth2, boxHeight*3, boxWidth2, boxHeight, "Change Fs");
	    	fKButton = new Button(boxWidth2*2, boxHeight*3, boxWidth2, boxHeight, "Change Fk");
	    	gButton = new Button(boxWidth2*3, boxHeight*3, boxWidth2, boxHeight, "Change Gravity");
	    	playButton = new Button(boxWidth2*4, boxHeight*3, boxWidth2, boxHeight, "Change Play"); 	
	    }
	    private void initDB(float startVal, float endVal, float currentVal){
	    	backButton = new Button(0, boxHeight*3, boxWidth*2, boxHeight, "Return and Save");
	    	db = new DragBar(boxWidth*2, boxHeight*3, boxWidth*6, boxHeight, startVal, endVal, currentVal);
	    }	    
	    private void initEButtons(){
	    	float boxWidth2 = boxWidth*2;
	    	backButton = new Button(0, boxHeight*3, boxWidth2, boxHeight, "Return and Save");
	    	sizeButton = new Button(boxWidth2, boxHeight*3, boxWidth2, boxHeight, "Resize");
	    	massButton = new Button(boxWidth2*2, boxHeight*3, boxWidth2, boxHeight, "Mass");
	    	forcesButton = new Button(boxWidth2*3, boxHeight*3, boxWidth2, boxHeight, "Edit Forces");
	    	playButton = new Button(boxWidth2*4, boxHeight*3, boxWidth2, boxHeight, "Change Play");
	    }
 //------------------------------------------The-Drawing-of-Objects----------------------------------------------------------------------------- 	    
	    private void draw(Canvas canvas, Paint paint){
            
	    	canvas.drawBitmap(backgroundImg, 0, 0, null);
            
	    	drawShapeCreator(canvas, paint);
	    	
	    	drawTempShapes(canvas, paint);
	    	
	    	drawShapeEditor(canvas, paint);
	    	
	    	setPaint(4, Color.BLACK);
	    	physics.drawShapes(canvas, paint);
	    }
	    private void drawShapeCreator(Canvas canvas, Paint paint){
    		setPaint(3, Color.BLACK);
	    	canvas.drawRect(boxRect, paint);
	    	canvas.drawRect(triangleRect, paint);
	    	canvas.drawRect(circleRect, paint);
	    	
	    	//Rectangle Button
	    	setPaint(2, Color.GRAY);
	    	paint.setTextSize(20);
	    	canvas.drawText("Make Box", 10, 25, paint);
	    	setPaint(3, paint.getColor());
	    	canvas.drawRect(25, 50, 125, 150, paint);
	    	
	    	//Triangle Button
	    	setPaint(2, Color.GRAY);
	    	paint.setTextSize(20);
	    	canvas.drawText("Make Triangle", 10, boxHeight + 40, paint);
	    	setPaint(3, paint.getColor());
	    	Triangle t = new Triangle(25/2, boxHeight + 75, 125, Color.BLACK);
	    	t.Draw(canvas, paint);
	    	
	    	//Circle Button
	    	setPaint(2,Color.GRAY);
	    	paint.setTextSize(20);
	    	canvas.drawText("Make Circle", 10, 530,paint);		    	
	    	setPaint(3, paint.getColor());
	    	canvas.drawCircle(75, 640, 62, paint);
	    	
	    	setPaint(5, Color.BLACK);
	    	canvas.drawRect(worldRect, paint);
    	}	    
    	private void drawTempShapes(Canvas canvas, Paint paint){
    		if(physics.checkAllIntersectionsWith(tempBox)||physics.checkAllIntersectionsWith(tempCircle)||physics.checkAllIntersectionsWith(tempTriangle)){
    			setPaint(3, Color.RED);
    		}

    		else{
    			setPaint(3, Color.BLACK);
    		}		    	
	    	tempBox.Draw(canvas, paint);
	    	tempCircle.Draw(canvas,paint);
    		tempTriangle.Draw(canvas, paint);

    	}    	
    	private void drawShapeEditor(Canvas canvas, Paint paint){	
    		if(sEState == sEHomescreen){
	    		if(hState == -1){
	    			if(!kinematics){
		    			drawHButtons(canvas, paint);	
	    			}	    			
	    			else if(selectedShape != null){
	    				drawDataPanel(selectedShape, canvas, paint);
	    			}
	    		}else{
	    			drawDB(canvas, paint);	    			
	    		}
    		}
    		else if(sEState == sEBoxSelected || sEState == sETriangleSelected ||sEState == sECircleSelected){
    			if(eState == -1){
	    			drawEButtons(canvas, paint);		    			
	    		}else if(eState == forcesSelected){
	    			drawForceCreator(canvas, paint);
	    		}
	    		else{
	    			drawDB(canvas, paint);	
	    		}
    		}	
    	}   	
    	private void drawHButtons(Canvas canvas, Paint paint){
    		backButton.draw(canvas, paint);
	    	fSButton.draw(canvas, paint);
	    	fKButton.draw(canvas, paint);
	    	gButton.draw(canvas, paint);
	    	playButton.draw(canvas, paint);
    	}    	
    	private void drawDB(Canvas canvas, Paint paint){
    		backButton.draw(canvas, paint);
    		db.draw(canvas, paint);
    		
    	}    	
    	private void drawEButtons(Canvas canvas, Paint paint){
    		backButton.draw(canvas, paint);
	    	sizeButton.draw(canvas, paint);
	    	massButton.draw(canvas, paint);
	    	forcesButton.draw(canvas, paint);
	    	playButton.draw(canvas, paint);
    	}    	
    	private void drawForceCreator(Canvas canvas, Paint paint){
    		backButton.draw(canvas, paint);    		
    		canvas.drawLine(selectedShape.centreX, selectedShape.centreY, forceX, forceY, paint);
    	}   	
    	private void drawDataPanel(Shape s, Canvas canvas, Paint paint){
    		String accelerationX= "AccelerationX: " + s.acceleration.getXMagnitude();
    		String accelerationY = "AccelerationY: " + s.acceleration.getYMagnitude();
    		String velocityX = "VelocityX: " + s.velocity.getXMagnitude();
    		String velocityY = "VelocityY: " + s.velocity.getYMagnitude();
	    	String currentX = "CurrentX: " + s.getX();
	    	String currentY = "CurrentY: " + s.getY();
	    	String xDisplacement = "DisplacementX: " + (s.getX() - s.initX);
	    	String yDisplacement = "DisplacementY: " + (s.getY() - s.initY);
	    	String timeElapsed = "Time Elapsed: " + physics.timeElapsed; 
	    			
	    	paint.setTextSize(30);
	    	paint.setColor(Color.BLACK);
    		canvas.drawText(accelerationX , boxWidth*2, boxHeight*3 + 40, paint);
    		canvas.drawText(accelerationY , boxWidth*5, boxHeight*3 + 40, paint);	
    		canvas.drawText(velocityX, boxWidth*2, boxHeight*3 + 80, paint);		
    		canvas.drawText(velocityY, boxWidth*5, boxHeight*3 + 80, paint);	
    		canvas.drawText(currentX , boxWidth*2, boxHeight*3 + 120, paint);	
    		canvas.drawText(currentY ,  boxWidth*5, boxHeight*3 + 120,paint);	
    		canvas.drawText(xDisplacement , boxWidth*2, boxHeight*3 + 160,paint);	
    		canvas.drawText(yDisplacement ,  boxWidth*5, boxHeight*3 + 160,paint);
    		canvas.drawText(timeElapsed ,  boxWidth*2, boxHeight*3 + 200,paint);
    		playButton.draw(canvas, paint);
    	}
    	
    	private void setPaint(int strokeWidth, int color){
    		paint.setStrokeWidth(strokeWidth);
    		paint.setColor(color);
    	}
//------------------------------------------Updating-of-Objects--------------------------------------------------------------------------------      
    	
    	private void update(){
    		 	physics.update();
    	    }
    	
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	      float eventX = event.getX();
	      float eventY = event.getY();
	      switch (event.getAction()) {
		      case MotionEvent.ACTION_DOWN:
		        screenTouched(eventX, eventY);
		        return true;
		      case MotionEvent.ACTION_MOVE:
		        screenMoved(eventX, eventY);
		        break;
		      case MotionEvent.ACTION_UP:
		    	screenReleased(eventX, eventY);
		        break;
		      default:
		        return false;
	      }
	      return true;
	    }
	    
	   
		    private void screenTouched(float x, float y){
		    	checkIfShapePressed();
		    	checkEditorButtons(x, y);
		    	if(sEState == sEHomescreen){
			    	if(boxRect.contains((int)x, (int)(y))){
			    		boxPressed = true;
			    	}
			    	if(circleRect.contains((int)x, (int)(y))){
			    		circlePressed = true;
			    	}
			    	if(triangleRect.contains((int)x, (int)(y))){
			    		trianglePressed = true;
			    	}
		    	}	    	
		    }    
		    	private void checkIfShapePressed(){
		    	if(sEState == sEHomescreen){
		    		if(hState == -1){
		    			int[] arr = physics.checkPoints(touchX, touchY);
		    			switch(arr[0]){
			    			case 0:{
			    				sEState = sEBoxSelected;
			    				selectedShape = physics.boxes.get(arr[1]);
			    				selectedShapeLoc = arr[1];
			    				initEButtons();
			    				break;
			    			}
			    			case 1:{
			    				sEState = sECircleSelected;
			    				selectedShape = physics.circles.get(arr[1]);
			    				selectedShapeLoc = arr[1];
			    				initEButtons();
			    				break;
			    			}
			    			case 2:{
			    				sEState = sETriangleSelected;
			    				selectedShape = physics.triangles.get(arr[1]);
			    				selectedShapeLoc = arr[1];
			    				initEButtons();
			    				break;
			    			}
		    			}
		    		}
	    		}
		    }
		    	private void checkEditorButtons(float x, float y){
		  	    	if(sEState == sEHomescreen){
		  	    		if(hState == -1){	
		  	    			checkHomeButtons(x, y);
		  	    		}else{
		  	    			checkDragBarButtons(x, y);
		  	    		}
		      		}else if(sEState == sEBoxSelected || sEState == sETriangleSelected ||sEState == sECircleSelected){
		      			if(eState == -1){	
		  	    			checkShapeEditorButtons(x, y);
		  	    		}else{
		  	    			checkDragBarButtons(x, y);
		  	    		}
		      		}
		  	    	if(worldRect.contains((int)x,(int)y)){
		  	    		if(sEState == sEBoxSelected || sEState == sETriangleSelected ||sEState == sECircleSelected){
		  	    			if(eState == forcesSelected){	
		  		    			updateTempForce(x, y);
		  		    		}
		  		    	}
		  		    	touchX = x;
		  		    	touchY = y;
		  	    	}
		  	    }   
		  	    private void checkHomeButtons(float x, float y){
		  	    	if(backButton.contains(x, y)){
		  	    		
		  	    	}else if(fSButton.contains(x, y)){
		  	    		hState = fSSelected;
		  	    		initDB(0, 1, physics.getuS());
		  	    	}else if(fKButton.contains(x, y)){
		  	    		hState = fKSelected;
		  	    		initDB(0, 1, physics.getuK());
		  	    	}else if(gButton.contains(x, y)){
		  	    		hState = gSelected;
		  	    		initDB(0, 20, physics.getG());
		  	    	}else if(playButton.contains(x, y)){
		  	    		if(!kinematics){
		  	    			physics.start();
		  		    		kinematics = true;
		  	    		}
		  	    		else{
		  	    			physics.resetShapes();
		  	    			kinematics = false;
		  	    		}
		  	    	}
		  	    } 
		  	    private void checkDragBarButtons(float x, float y){
		  	    	if(backButton.contains(x, y)){//on the press of the back button
		  	    		if(sEState == sEHomescreen){
		  	    			switch(hState){
		  			    		case 0:{//fSSelected
		  			    			physics.setuS(db.currentVal);
		  			    			break;
		  			    		}
		  			    		case 1:{//fKSelected
		  			    			physics.setuK(db.currentVal);
		  			    			break;
		  			    		}
		  			    		case 2:{//gSelected
		  			    			physics.setG(db.currentVal);
		  			    			break;
		  			    		}
		  	    			}	    
		  		    		initHButtons();
		  		    		hState = -1;
		  	    		}
		  	    		else if(sEState == sEBoxSelected || sEState == sETriangleSelected ||sEState == sECircleSelected){
		  	    			switch(eState){
		  		    			case 0:{//size selected
		  		    				selectedShape.setWidth(db.currentVal);
		  		    				break;
		  		    			}case 1:{//mass selected
		  		    				selectedShape.setMass(db.currentVal);
		  		    				break;
		  		    			}case 2:{//forces selected
		  		    				selectedShape.addForce(new Vector2D(selectedShape.centreX-forceX, selectedShape.centreY-forceY));
		  		    				break;
		  		    			}
		  	    			}
		  	    			switch(sEState){
		  			    		case 2:{//boxSelected
		  			    			physics.boxes.remove(selectedShapeLoc);
		  			    			physics.addBox(selectedShapeLoc, (Box)selectedShape);
		  			    			break;
		  			    		}
		  			    		case 3:{//triangleSelected
		  			    			physics.triangles.remove(selectedShapeLoc);
		  			    			physics.addTriangle(selectedShapeLoc, (Triangle)selectedShape);
		  			    			break;//circeSelected
		  			    		}
		  			    		case 4:{
		  			    			physics.circles.remove(selectedShapeLoc);
		  			    			physics.addCircle(selectedShapeLoc, (Circle)selectedShape);
		  			    			break;
		  			    		}
		  	    			}	
		  	    			initEButtons();
		  	    			eState = -1;	    			
		  	    		}
		  	    	}
		  	    }
		  	    private void checkShapeEditorButtons(float x, float y){
		  			if(backButton.contains(x, y)){
		  				sEState = sEHomescreen;
		  				hState = -1;
		  				eState = -1;
		  				selectedShapeLoc = -1;
		  				physics.selectedBox = -1;
		  				physics.selectedCircle = -1;
		  				physics.selectedTriangle = -1;
		  				initHButtons();
		  	    	}else if(sizeButton.contains(x, y)){
		  	    		eState = sizeSelected;
		  	    		initDB(0, 400, selectedShape.getWidth());
		  	    	}else if(massButton.contains(x, y)){
		  	    		eState = massSelected;
		  	    		initDB(1, 100, selectedShape.getMass());
		  	    	}else if(forcesButton.contains(x, y)){
		  	    		eState = forcesSelected;
		  	    	}else if(playButton.contains(x, y)){
		  	    		if(!kinematics){
		  	    			physics.start();
		  		    		kinematics = true;
		  		    		sEState = sEHomescreen;
		  					hState = -1;
		  					eState = -1;
		  					initHButtons();
		  	    		}
		  	    		else{
		  	    			physics.resetShapes();
		  	    			kinematics = false;
		  	    		}
		  	    	}
		  		}
		  		private void updateTempForce(float x, float y){
					forceX = x;
					forceY = y;
				}
	
		  	private void screenMoved(float x, float y){
	    		db.updateValue(x, y);//updates the value of the dragbar	
	    		checkTempShapes(x, y);//repositions the temporary shape based on screen touches
		    }
		  		private void checkTempShapes(float x, float y){
		    	if(boxPressed){
		    		if(worldRect.contains((int)x, (int)(y-fingerOffset))&&(y-fingerOffset+50)<worldRect.bottom){
			    		tempBox = new Box(x, (y-fingerOffset), 50, 50, Color.BLACK);
		    		}else if(x>boxWidth){
		    			tempBox = new Box(x, tempBox.getY(), 50, 50, Color.BLACK);
		    		}else if(x<boxWidth&&y-fingerOffset<worldRect.bottom){
		    			tempBox = new Box(tempBox.getX(), y, 50, 50, Color.BLACK);
		    		}
		    	}
		    	if(circlePressed){
		    		if(worldRect.contains((int)x, (int)(y-fingerOffset))&&(y-fingerOffset+50)<worldRect.bottom){
			    		tempCircle = new Circle((int)x, (int)(y-fingerOffset), 25, Color.BLACK);
		    		}
		    		else if(x>boxWidth){
		    			tempCircle = new Circle((int)x, tempCircle.getY(), 25, Color.BLACK);
		    		}
		    		else if(x<boxWidth&&y-fingerOffset<worldRect.bottom){
		    			tempCircle = new Circle(tempCircle.getX(), (int)(y-fingerOffset),25, Color.BLACK);
		    		}
		    	}
		    	
		    	
		    	if(trianglePressed){
		    		if(worldRect.contains((int)x, (int)(y-fingerOffset))&&(y-fingerOffset+50)<worldRect.bottom){
			    		tempTriangle = new Triangle((int)x, (int)(y-fingerOffset), 50, Color.BLACK);
		    		}
		    		else if(x>boxWidth){
		    			tempTriangle = new Triangle((int)x, tempTriangle.getY(), 50, Color.BLACK);
		    		}
		    		else if(x<boxWidth&&y-fingerOffset<worldRect.bottom){
		    			tempTriangle = new Triangle(tempTriangle.getX(), (int)(y-fingerOffset), 50, Color.BLACK);
		    		}
		    	}
		    	if(worldRect.contains((int)x,(int)y)){
		    		if(sEState == sEBoxSelected || sEState == sETriangleSelected ||sEState == sECircleSelected){
		    			if(eState == forcesSelected){	
			    			updateTempForce(x, y);
			    		}
			    	}
		    	}
		  	}
		   
		  	
		  	private void screenReleased(float x, float y){
		    	saveTempShapes(x, y);//saves the temporary shape if its position is valid
		    }
		   		private void saveTempShapes(float x, float y){
		    	if(boxPressed){	    		
					if(worldRect.contains(tempBox.getRect())){
						if(!physics.checkAllIntersectionsWith(tempBox)){
							Box box;
							if((y-fingerOffset)>worldRect.bottom){
								box = new Box(x, worldRect.bottom-50, 50, 50, Color.BLACK);
							}
							else if(x<boxWidth){
								box = new Box(boxWidth, y-fingerOffset, 50, 50, Color.BLACK);
							}
							else if(y-fingerOffset<0){
								box = new Box(x, 0, 50, 50, Color.BLACK);
							}
							else{
								box = new Box(x, y-fingerOffset, 50, 50, Color.BLACK);
							}
							if(!physics.checkAllIntersectionsWith(box)){
								physics.addBox(box);
							}
		    			}
		    		}
		    		boxPressed = false;
	    			tempBox = new Box(0, 0 , 0, 0, 0);
		    	}
		    	if (circlePressed){
		    		if(worldRect.contains(tempCircle.getRect())){
						if(!physics.checkAllIntersectionsWith(tempCircle)){
							Circle circle;
							if((y-fingerOffset)>worldRect.bottom){
								circle = new Circle(x, worldRect.bottom-50, 25, Color.BLACK);
							}
							else if(x<boxWidth){
								circle = new Circle(boxWidth, y-fingerOffset, 25, Color.BLACK);
							}
							else if(y-fingerOffset<0){
								circle = new Circle(x, 0, 25, Color.BLACK);
							}
							else{
								circle = new Circle(x, y-fingerOffset, 25, Color.BLACK);
							}
							if(!physics.checkAllIntersectionsWith(circle)){
								physics.addCircle(circle);
							}
						}
		    		}
				circlePressed = false;
				tempCircle = new Circle(0, 0 , 0, 0);
		    	}
		    	
		    	
		    	if (trianglePressed){
		    		if(worldRect.contains(tempTriangle.getRect())){
						if(!physics.checkAllIntersectionsWith(tempTriangle)){
							Triangle triangle;
							if((y-fingerOffset)>worldRect.bottom){
								triangle = new Triangle(x, worldRect.bottom-50, 50, Color.BLACK);
							}
							else if(x<boxWidth){
								triangle = new Triangle(boxWidth, y-fingerOffset, 50, Color.BLACK);
							}
							else if(y-fingerOffset<0){
								triangle = new Triangle(x, 0, 50, Color.BLACK);
							}
							else{
								triangle = new Triangle(x, y-fingerOffset, 50, Color.BLACK);
							}
							if(!physics.checkAllIntersectionsWith(triangle)){
								physics.addTriangle(triangle);
							}
						}
		    		}
				trianglePressed = false;
				tempTriangle = new Triangle(0, 0 , 0, 0);
		    	}
		    }
	
		  
		   	
}