package com.eir.unimap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.eir.unimap.sparql.SparqlFood;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.eir.unimap.R;

public class FoodDirections extends FragmentActivity implements LocationListener , OnMarkerClickListener {
    private GoogleMap map;
    private LocationManager locationManager;
    private String provider;
    Marker startPerc;
    double lat1;
 
    double lng1;
    Marker startPerc1;
    public final static String EXTRA_MESSAGE5 = "com.example.myfirstapp.MESSAGE";
    List<String> label = new ArrayList<String>();
    List<String> longi = new ArrayList<String>();
    List<String> lati = new ArrayList<String>();
    List<String> buildingNo = new ArrayList<String>();
    List<String> typel = new ArrayList<String>();
    List<Marker> markers = new ArrayList<Marker>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        Intent intent2 = getIntent();
        String type = intent2.getStringExtra("food");
        List<String> results = new SparqlFood().queryRemoteSparqlEndpoint();
        int size = results.size();
        for (int i = 0; i < size; i=i+5){
        	label.add(results.get(i));
        	longi.add(results.get(i+1));
        	lati.add(results.get(i+2));
        	buildingNo.add(results.get(i+3));
        	typel.add(results.get(i+4));
        
        }
        double[]latArray = new double[lati.size()];
        double[]longArray = new double[longi.size()];
       
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
        	
        	
        }
        System.out.println(label);
        System.out.println(Arrays.toString(longArray));
        System.out.println(Arrays.toString(latArray));
        System.out.println(buildingNo);
        System.out.println(typel);
        System.out.println(type);
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
      //          Toast.LENGTH_LONG).show();
        LatLng coordinate1 = new LatLng(lat1, lng1);
      //  Toast.makeText(this, "Location " + coordinate.latitude+","+coordinate.longitude,
       //         Toast.LENGTH_LONG).show();
        map.setOnMarkerClickListener(this);
         startPerc1 = map.addMarker(new MarkerOptions()
        .position(coordinate1)
        .title("Start")
        .snippet("Search")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate1, 17));
        // Initialize the location fields
       
        
        
        if (location != null) {
            Toast.makeText(this, "Selected Provider " + provider,
                    Toast.LENGTH_SHORT).show();
            onLocationChanged(location);
        } else {

            //do something
        }
        for (int i=0; i < buildingNo.size(); i++)
        {
        	if (typel.get(i).equals(type))
        	{
        	 Toast.makeText(this, "Location " + latArray[i]+","+longArray[i],
                     Toast.LENGTH_LONG).show();
        
             LatLng coordinate = new LatLng(latArray[i], longArray[i]);
              Toast.makeText(this, "Location " + coordinate.latitude+","+coordinate.longitude,
                     Toast.LENGTH_LONG).show();
              startPerc = map.addMarker(new MarkerOptions()
              .position(coordinate)
              .title(label.get(i))
               .snippet(buildingNo.get(i))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.room_pin)));
              markers.add(startPerc);
        	}
         }
        System.out.println(markers.size());
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
        // TODO Auto-generated method stub

    }
    public boolean onMarkerClick(final Marker marker) {
		 for (int i=0; i< markers.size(); i++)
	       {
	        	if (marker.equals(markers.get(i))){
	        		
	        	Intent intent = new Intent(this, SpecificFood.class);
	        	String bn = marker.getSnippet();
	        	System.out.println(bn);
	    		intent.putExtra(EXTRA_MESSAGE5, bn);
	    		startActivity(intent);
	        	}
	       }
			return false;
	    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    

}

        