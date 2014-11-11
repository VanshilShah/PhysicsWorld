/**
Activity that handles all sound
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
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

public class Sound extends ActionBarActivity {
	public static boolean on;
	public static MediaPlayer mp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sound);
		mp = MediaPlayer.create(Sound.this, R.raw.onestop);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	//goes back to settings when back button is pressed
	public void back(View view){
		Intent i = new Intent(getApplicationContext(), Settings.class);     
        startActivity(i);
	}
	
	//toggles sound with toggle button
	public void onToggleClicked(View view) {
	    // Is the toggle on?
	    on = ((ToggleButton) view).isChecked();
	    
	    if (on) {
	    	mp.start();
	    } else {
	    	mp.pause();
	    }
	}
	
	//starts sound when activity is entered
	@Override 
	public void onResume(){
		super.onResume();
		if(on){
        	mp.start();
        }
	}
	
	@Override 
	public void onPause(){
		super.onPause();
		mp.pause();
	}
	    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sound, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_sound,
					container, false);
			return rootView;
		}
	}

}
