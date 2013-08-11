/** Author: Eirini Televantou 
 *  Year: 2013
 *  Organization: University of Southampton
 *  Info: This is the Building activity. Where the user inputs a building number and asks for directions
 *  **/

package com.eir.unimap;

import java.util.List;
import com.eir.unimap.sparql.SparqlDirectionsActivity;
import com.eir.unimap.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Building extends Activity {
	  public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Retrieve the layout when the activity is called  
		setContentView(R.layout.activity_building);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.building, menu);
		return true;
	}
	

	 public void SearchBuilding(View view) {
	       // Do something in response to the button
		
		 //Retrieve the input of the textfield (Building number)
	   	EditText editText = (EditText) findViewById(R.id.bn);
	   	// Create a string and put the input in
	   	String bn= editText.getText().toString();
	   	
	   	//Call the checkBuilding function with the input message as the parameter
	   	boolean result = checkBuilding(bn);
	    //If building exists in dataset start the Directions activity with an intent
	    	if (result== true)
	    	{
			   Intent intent = new Intent(this, Directions.class);
				//Add the building number to the intent
				intent.putExtra(EXTRA_MESSAGE, bn);
				startActivity(intent);
				return ;
		//Else don't start the activity and present a warning
	    	}if (result == false){
	    		Toast.makeText(this, "You did not enter a correct building No", Toast.LENGTH_SHORT).show();
	    	}
		
	      
	 }
	 //Check if building number exists in dataset
	 public boolean checkBuilding(String bn){
		 //Get the results by calling the function which queries the results 
		 List<String> results = new SparqlDirectionsActivity().queryRemoteSparqlEndpointBN();
		 String[]buildArray = new String[results.size()];
		 
		 for(int j = 0;  j< results.size();j++)
		    {
			 
				String buildistring = results.get(j);
				//Crop the URI result to be just the value
	        	int buildindex = buildistring.indexOf("^^");
	        	String buildstring = buildistring.substring(0, buildindex); 
	        	results.set(j, String.valueOf(buildstring));
	        	buildArray[j] = buildstring;
			 	
	        	//if building exists return true
		    	if(buildArray[j].equals(bn)){
		    	
		    		return true;
		    	}
		    }
		    System.out.println(results);
		   return false;
		 
	 }

}
