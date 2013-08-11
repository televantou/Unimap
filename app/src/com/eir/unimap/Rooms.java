package com.eir.unimap;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.eir.unimap.sparql.SparqlDirectionsActivity;
import com.eir.unimap.sparql.SparqlRoom;
import com.eir.unimap.R;
public class Rooms extends Activity implements OnItemSelectedListener {
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_rooms);
		
		//This is for the spinner
		List<String> results = new SparqlRoom().getFeatures();
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(this);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item,results);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}
	
	 public void onItemSelected(AdapterView<?> parent, View view, 
	            int pos, long id) {
	        // An item was selected. You can retrieve the selected item using
	        // parent.getItemAtPosition(pos)
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	        // Another interface callback
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rooms, menu);
		return true;
	}
	 public void SearchRoom(View view) {
	       // Do something in response to button
	   EditText editText = (EditText) findViewById(R.id.room);
		String capacity = editText.getText().toString();
		   	
	    if (capacity.equals("")) {
		    Toast.makeText(this, "You did not enter required room capacity.", Toast.LENGTH_SHORT).show();
		    return;
		}else{
			
		
		Spinner mySpinner = (Spinner)findViewById(R.id.spinner);
		String feature = mySpinner.getSelectedItem().toString();
		boolean result = checkFeature(feature, capacity);
	
    	if (result== true)
    	{
		Intent intent = new Intent(this, RoomDirections.class);
		intent.putExtra("capa", capacity);
		intent.putExtra("feat", feature);
		startActivity(intent);
		}
		if (result == false){
    		Toast.makeText(this, "No room compination found", Toast.LENGTH_SHORT).show();
    	}
	 }
	 
	 }
	 public boolean checkFeature(String bn, String bnn){
		 String feat = bn;
		 int bn1 = Integer.parseInt(bnn);
		 List<String> results = new SparqlRoom().getCapacity(bn1);
			
		  for(int j = 0;  j< results.size();j++)
		 {
		 				
		    	if(results.get(j).equals(feat)){
		    	
		    		return true;
		    		
		    	}
		  }
		   
		   return false;
		 
	 }



	 
	 
	 
	
}
