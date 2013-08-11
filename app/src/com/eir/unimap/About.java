/** Author: Eirini Televantou 
 *  Year: 2013
 *  Organization: University of Southampton
 *  Info: This is the About activity. Presents information about the application 
 *  **/

package com.eir.unimap;

import com.eir.unimap.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class About extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Retrieve the layout when the activity is called
		setContentView(R.layout.activity_about);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}
	
	
}