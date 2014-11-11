/**
Game setting activity, handles all button, sound and text fields for the game settings
Physics World
ICS-3UP
@authors Viral Patel, Vanshil Shah, Adit Patel, Kunj Patel
@version May 1, 2014
 */
package com.example.PhysicsWorld;

import com.example.msa.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class GameSettings extends ActionBarActivity {
	String square = "10";
	String triangle = "10";
	String circle = "10";
	Boolean drawOut = false;
	Boolean gravity = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    requestWindowFeature(Window.FEATURE_NO_TITLE);	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_settings);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	//starts music when activity is entered
	@Override 
	public void onResume(){
		super.onResume();
		if(Sound.on){
        	Sound.mp.start();
        }
	}
	
	//pauses app on exit of activity
	@Override 
	public void onPause(){
		super.onPause();
		if(Sound.on)
			Sound.mp.pause();
	}
	//back button, goes to settings activity
	public void back(View view){
		Intent i = new Intent(getApplicationContext(), Settings.class);     
        startActivity(i);
	}
	
	//gets value of square edditText, saves it into string
	public void squareSet(View view){
		EditText text = (EditText)findViewById(R.id.squareText);
		String square = text.getText().toString();
		TextView views = (TextView) findViewById(R.id.squareSize);
		views.setText("Size: " + square);
	}
	
	//gets value of circle edditText, saves it into string
	public void circleSet(View view){
		EditText text = (EditText)findViewById(R.id.circleText);
		String circle = text.getText().toString();
		TextView views = (TextView) findViewById(R.id.circleSize);
		views.setText("Size: " + circle);
	}
	
	//gets value of triangle edditText, saves it into string
	public void triangleSet(View view){
		EditText text = (EditText)findViewById(R.id.triangleText);
		String triangle = text.getText().toString();
		TextView views = (TextView) findViewById(R.id.triangleSize);
		views.setText("Size: " + triangle);
	}
	
	public void outBounds(View view){
		final CheckBox checkBox = (CheckBox) findViewById(R.id.drawOutBox);
		drawOut = checkBox.isChecked();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_game_settings,
					container, false);
			return rootView;
		}
	}

}
