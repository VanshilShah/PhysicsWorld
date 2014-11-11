/**
Activity that handles the drawing panel
Physics World
ICS-3UP
@authors Viral Patel, Vanshil Shah, Adit Patel, Kunj Patel
@version May 1, 2014
 */

package com.example.PhysicsWorld;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.LinearLayout;

public class Simulator extends ActionBarActivity{
	DrawingPanel dp;
	LinearLayout ll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    requestWindowFeature(Window.FEATURE_NO_TITLE);			
		super.onCreate(savedInstanceState);
		
		//opens drawingPanel, where simulation runs
		ll = new LinearLayout(this);
		dp = new DrawingPanel(this);
		ll.addView(dp);
		setContentView(ll);

	}
	
}