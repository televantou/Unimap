package com.eir.unimap;

import java.util.List;

import com.eir.unimap.sparql.SparqlFood;
import com.eir.unimap.R;


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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class Food extends Activity implements OnItemSelectedListener {
	 
	  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 
		setContentView(R.layout.activity_food_directions);
		
		 List<String> results = new SparqlFood().getType();
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
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
	 public void SearchFood(View view) {
	       // Do something in response to button
	   	Intent intent = new Intent(this, FoodDirections.class);
	  
	
		
		Spinner mySpinner = (Spinner)findViewById(R.id.spinner1);
		String feature = mySpinner.getSelectedItem().toString();
		intent.putExtra("food", feature);
	
		
		startActivity(intent);
	 
	 }
}
