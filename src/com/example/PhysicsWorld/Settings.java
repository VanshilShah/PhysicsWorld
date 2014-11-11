/**
Setting activity, handles all button, sound and text fields for the settings
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

public class Settings extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    requestWindowFeature(Window.FEATURE_NO_TITLE);	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		 
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
	
	//stops music when activity is exited
	@Override 
	public void onPause(){
		super.onPause();
		if(Sound.on)
			Sound.mp.pause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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
	
	//goes to mainActivity when back button is pressed 
	public void back(View view){
		Intent i = new Intent(getApplicationContext(), MainActivity.class);     
        startActivity(i);
	}
	
	//goes to sound when sound button is pressed
	public void toSound(View view){
    	Intent i = new Intent(getApplicationContext(), Sound.class);     
        startActivity(i);
    }
	
	//goes to gameSettings when the button is pressed
	public void toGameSettings(View view){
    	Intent i = new Intent(getApplicationContext(), GameSettings.class);     
        startActivity(i);
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
			View rootView = inflater.inflate(R.layout.fragment_settings,
					container, false);
			return rootView;
		}
	}

}
