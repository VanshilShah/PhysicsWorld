/**
Main activity, handles all the button, sound and text fields for the main activity 
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

public class MainActivity extends ActionBarActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	    requestWindowFeature(Window.FEATURE_NO_TITLE);	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
	
	@Override 
	public void onResume(){
		super.onResume();
		if(Sound.on){
        	Sound.mp.start();
        }
	}
	@Override 
	public void onPause(){
		super.onPause();
		if(Sound.on)
			Sound.mp.pause();
	}	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    public void toPlay(View view){
    	//change to Simulator once code gets combined
    	Intent i = new Intent(getApplicationContext(), Simulator.class);     
        startActivity(i);
    }
    public void toSettings(View view){
    	Intent i = new Intent(getApplicationContext(), Settings.class);     
        startActivity(i);
    }
    public void toHelp(View view){
    	Intent i = new Intent(getApplicationContext(), Help.class);     
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
