package com.eir.unimap;

import com.eir.unimap.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      

    }

 
   /** Called when the user clicks the Send button */
   public void goToHelp(View view) {
       // Do something in response to button
   	Intent intent = new Intent(this, About.class);
	startActivity(intent);
   }
   public void goToBuilding(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, Building.class);
		startActivity(intent);
	}
	public void goToRooms(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, Rooms.class);
		startActivity(intent);
	}
	public void goToFood(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, Food.class);
		startActivity(intent);
		
	}
	
	public void goToQuestionnaire(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, Question.class);
		startActivity(intent);
		
	}
	public void goToInfoSheet(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, Sheet.class);
		startActivity(intent);
		
	}
	public void goToConsent(View view) {
	    // Do something in response to button
		Intent intent = new Intent(this, ConsentForm.class);
		startActivity(intent);
		
	}
}


