package com.eir.unimap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.eir.unimap.sparql.SparqlSpecificFood;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.eir.unimap.R;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class SpecificFood extends FragmentActivity implements LocationListener {
	   private GoogleMap map;
	   double lat1;
	    double lat;
	    double lng;
	    double lng1;
	    Marker startPerc1;
	    private LocationManager locationManager;
	    private String provider;
	    String message="";
	   
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
	
	Intent intent1 = getIntent();
	 message = intent1.getStringExtra(FoodDirections.EXTRA_MESSAGE5);
	List<String> results = new SparqlSpecificFood().queryRemoteSparqlEndpoint();
	setContentView(R.layout.activity_specific_food);
	   

	    List<String> longi = new ArrayList<String>();
       List<String> lati = new ArrayList<String>();
       List<String> buildingNo = new ArrayList<String>();
       
       int size = results.size();
       for (int i = 0; i < size; i=i+3){
       	longi.add(results.get(i));
       	lati.add(results.get(i+1));
       	buildingNo.add(results.get(i+2));
       	
         }
       double[]latArray = new double[lati.size()];
       double[]longArray = new double[longi.size()];
       String[]buildArray = new String[buildingNo.size()];
       for(int i=0; i<longi.size();i++)
       {
   	  
   	 
   	   
       	String longistring = longi.get(i);
       	 int longindex = longistring.indexOf("^^");
       	String longstring = longistring.substring(0, longindex); 
       	longi.set(i, String.valueOf(longstring));
       	longArray[i] = Double.parseDouble(longstring);
       	
       	String latistring = lati.get(i);
       	int latindex = latistring.indexOf("^^");
       	String latstring = latistring.substring(0, latindex); 
       	lati.set(i, String.valueOf(latstring));
       	latArray[i] = Double.parseDouble(latstring);
       	
       	String buildistring = buildingNo.get(i);
       	int buildindex = buildistring.indexOf("^^");
       	String buildstring = buildistring.substring(0, buildindex); 
       	buildingNo.set(i, String.valueOf(buildstring));
       	buildArray[i] = buildstring;
 
       }
       System.out.println(Arrays.toString(longArray));
       System.out.println(Arrays.toString(latArray));
       System.out.println(buildingNo);
       
      
       map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
      
       

       LocationManager service = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       boolean enabledGPS = service
               .isProviderEnabled(LocationManager.GPS_PROVIDER);
       boolean enabledWiFi = service
               .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

       // Check if enabled and if not send user to the GSP settings
       // Better solution would be to display a dialog and suggesting to 
       // go to the settings
       if (!enabledGPS) {
           Toast.makeText(this, "GPS signal not found", Toast.LENGTH_LONG).show();
           Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
           startActivity(intent);
       }

       locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       // Define the criteria how to select the locatioin provider -> use
       // default
       Criteria criteria = new Criteria();
       provider = locationManager.getBestProvider(criteria, false);
       locationManager.requestLocationUpdates( provider, 0, 0, this);
  	 Location location = locationManager.getLastKnownLocation(provider);
     if (location == null) {
         lat1 = 50.936291;
       		  lng1=-1.397173;
       } else{
   
    lat1 =  location.getLatitude();
   lng1 = location.getLongitude();}
       //  Toast.makeText(this, "Location " + lat+","+lng,
//                  Toast.LENGTH_LONG).show();
       LatLng coordinate1 = new LatLng(lat1, lng1);
       //   Toast.makeText(this, "Location " + coordinate.latitude+","+coordinate.longitude,
//                  Toast.LENGTH_LONG).show();
        startPerc1 = map.addMarker(new MarkerOptions()
       .position(coordinate1)
       //.title("Start")
       //.snippet("Search")
       .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
       map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate1, 17));
       // Initialize the location fields
       if (location != null) {
          Toast.makeText(this, "Selected Provider " + provider,
                  Toast.LENGTH_SHORT).show();
           onLocationChanged(location);
       } else {
       	Toast.makeText(this, "We had a problem locating you",
                Toast.LENGTH_SHORT).show();
 }
       for (int i=0; i< lati.size(); i++)
       {
       	if (buildArray[i].equals(message))
       	{
      //  	 Toast.makeText(this, "Location " + latArray[i]+","+longArray[i],
        //             Toast.LENGTH_LONG).show();
     
        	
         //  Toast.makeText(this, "Location " + lat+","+lng,
         //           Toast.LENGTH_LONG).show();
            LatLng current = coordinate1;
            LatLng coordinate = new LatLng(latArray[i], longArray[i]);
            // Toast.makeText(this, "Location " + coordinate.latitude+","+coordinate.longitude,
            //        Toast.LENGTH_LONG).show();
              Marker startPerc = map.addMarker(new MarkerOptions()
             .position(coordinate)
             .title(buildingNo.get(i))
             //.snippet(label.get(i))
             
             .icon(BitmapDescriptorFactory.fromResource(R.drawable.room_pin)));
              
              MapDirections  md = new MapDirections();
  	        org.w3c.dom.Document doc = md.getDocument(current, coordinate,
  	        		MapDirections.MODE_WALKING);

  	        	ArrayList<LatLng> directionPoint = md.getDirection(doc);
              PolylineOptions rectLine = new PolylineOptions().width(3).color(
                      Color.BLACK);

              for (int j = 0; j < directionPoint.size(); j++) {
                  rectLine.add(directionPoint.get(j));
              }
              Polyline polylin = map.addPolyline(rectLine);
       	}
        }
     
}
   

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.directions, menu);
	return true;
}




/* Request updates at startup */
@Override
protected void onResume() {
super.onResume();
locationManager.requestLocationUpdates(provider, 400, 1, this);
}

/* Remove the locationlistener updates when Activity is paused */
@Override
protected void onPause() {
super.onPause();
locationManager.removeUpdates(this);
}

@Override
public void onLocationChanged(Location location) {
    double lat =  location.getLatitude();
    double lng = location.getLongitude();
 //  Toast.makeText(this, "Location " + lat+","+lng,
 //           Toast.LENGTH_LONG).show();
    LatLng coordinate = new LatLng(lat, lng);
 //   Toast.makeText(this, "Location " + coordinate.latitude+","+coordinate.longitude,
 //           Toast.LENGTH_LONG).show();
   startPerc1.setPosition(coordinate);


}


@Override
public void onProviderDisabled(String provider) {
Toast.makeText(this, "Enabled new provider " + provider,
       Toast.LENGTH_SHORT).show();

}


@Override
public void onProviderEnabled(String provider) {
Toast.makeText(this, "Disabled provider " + provider,
       Toast.LENGTH_SHORT).show();

}


@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
//TODO Auto-generated method stub

}
public void saveLocationOnClick1(View VIEW) {
	 locationManager.requestLocationUpdates( provider, 0, 0, this);
	 Location location = locationManager.getLastKnownLocation(provider);
	 if (location == null) {
         lat = 50.936291;
       		  lng=-1.397173;
       } else{
 
  lat =  location.getLatitude();
 lng = location.getLongitude();}
String lat1 = String.valueOf(lat);
String lng1 = String.valueOf(lng);
 	Intent intent = new Intent(this, FileWrite.class);
 	 
	intent.putExtra("lat", lat1);
	intent.putExtra("lng", lng1);
	intent.putExtra("msg", message);
	startActivity(intent);
}
@Override
protected void onSaveInstanceState(Bundle outState) {
    //No call for super(). Bug on API Level > 11.
}


}

