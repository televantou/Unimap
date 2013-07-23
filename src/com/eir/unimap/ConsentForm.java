/** Author: Eirini Televantou 
 *  Year: 2013
 *  Organization: University of Southampton
 *  Info: This is the Consent form activity. 
 *  **/

package com.eir.unimap;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.eir.unimap.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ConsentForm extends Activity {

	   String strNewFileName= "";
	  int serverResponseCode = 0;
	  ProgressDialog dialog = null;
	  String upLoadServerUri = null;
	
	  @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consent_form);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.consent_form, menu);
		return true;
	}
	public void submit(View view) {
	    // Do something in response to button
		//Retrieve the name and surname from the textbox
		EditText name = (EditText) findViewById(R.id.namesur);
		//Retrieve the date from the textbox	   	
	   	EditText date = (EditText) findViewById(R.id.date);
	   
	  //Retrieve the input of the textbox. Returns true if they are checked and false if not
	   	CheckBox	q1 = (CheckBox) findViewById(R.id.Box1);
	   	CheckBox	q2 = (CheckBox) findViewById(R.id.Box2);
	   	CheckBox	q3 = (CheckBox) findViewById(R.id.Box3);
	   	CheckBox	q4 = (CheckBox) findViewById(R.id.Box4);
		CheckBox	q5 = (CheckBox) findViewById(R.id.Box5);
		CheckBox	q6 = (CheckBox) findViewById(R.id.Box6);
		
	   //String buffer to add the results
		StringBuffer result = new StringBuffer();
		result.append(" St 1  : ").append(q1.isChecked());
		result.append(", St 2  : ").append(q2.isChecked());
		result.append(", St 3 : ").append(q3.isChecked());
		result.append(", St 4  : ").append(q4.isChecked());
		result.append(", St 5  : ").append(q5.isChecked());
		result.append(", St 6  : ").append(q6.isChecked());
	//Store the results to strings	
	  String res = result.toString();
	   String n=  name.getText().toString();
	   String d= date.getText().toString();
	   
	   //Use telephony manager to retrieve the device's unique ID
	   TelephonyManager  tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
	    String IMEINumber=tm.getDeviceId();
	    //Create the new name for the file. "c" stands for consent form
		  strNewFileName =  ("c"+IMEINumber+".txt");
	   
		  //Gets the file directory of the application on the device
	   File fileDir = getFilesDir();
	    final  String filedir=fileDir.toString();
	    String strFileContents = (" **Start** " + res  + ","+  n +  ","+ d+" **End** ");
	    FileOutputStream outputStream;
	    //String fileName = (filedir + "/" + strNewFileName);
	    try{
	    	//Add the srtings to the file
	    	 outputStream = openFileOutput(strNewFileName, Context.MODE_APPEND);
	    	 outputStream.write(strFileContents.getBytes());
	    	 outputStream.close();
	    	 
	    	//Upload the file
	    	 uploadFile(filedir + "/" + strNewFileName);
	    }catch(IOException e) 
	    {Log.v("TEST","Exception on create ");}
	    
	    Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	    

	 }
	    //This uploads the file to the server
	//This function is the usual one used 
public int uploadFile(String sourceFileUri) {
    
    
    String fileName = sourceFileUri;

    HttpURLConnection conn = null;
    DataOutputStream dos = null;  
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "*****";
    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1 * 1024 * 1024; 
   String sourceFile = (sourceFileUri); 
     
 
         try { 
              
               // open a URL connection to the Servlet
        	 FileInputStream fileInputStream = new FileInputStream(new File(sourceFile));
        	 upLoadServerUri = "http://eirinitelevantou.site11.com/UploadToServer.php";
        	 URL url = new URL(upLoadServerUri);
              
             // Open a HTTP  connection to  the URL
             conn = (HttpURLConnection) url.openConnection(); 
             conn.setDoInput(true); // Allow Inputs
             conn.setDoOutput(true); // Allow Outputs
             conn.setUseCaches(false); // Don't use a Cached Copy
             conn.setRequestMethod("POST");
             conn.setRequestProperty("Connection", "Keep-Alive");
             conn.setRequestProperty("ENCTYPE", "multipart/form-data");
             conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
             conn.setRequestProperty("uploaded_file", fileName); 
              
             dos = new DataOutputStream(conn.getOutputStream());
    
             dos.writeBytes(twoHyphens + boundary + lineEnd); 
             dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);
           
             dos.writeBytes(lineEnd);
    
             // create a buffer of  maximum size
             bytesAvailable = fileInputStream.available(); 
    
             bufferSize = Math.min(bytesAvailable, maxBufferSize);
             buffer = new byte[bufferSize];
    
             // read file and write it into form...
             bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                
             while (bytesRead > 0) {
                  
               dos.write(buffer, 0, bufferSize);
               bytesAvailable = fileInputStream.available();
               bufferSize = Math.min(bytesAvailable, maxBufferSize);
               bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                
              }
    
             // send multipart form data necesssary after file data...
             dos.writeBytes(lineEnd);
             dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
    
             // Responses from the server (code and message)
             serverResponseCode = conn.getResponseCode();
             String serverResponseMessage = conn.getResponseMessage();
               
             Log.i("uploadFile", "HTTP Response is : "
                     + serverResponseMessage + ": " + serverResponseCode);
              
             if(serverResponseCode == 200){
                  
                 runOnUiThread(new Runnable() {
                      public void run() {
                           
                    
                          
                      }
                  });                
             }    
              
             //close the streams //
             fileInputStream.close();
             dos.flush();
             dos.close();
               
        } catch (MalformedURLException ex) {
             
            dialog.dismiss();  
            ex.printStackTrace();
             
            runOnUiThread(new Runnable() {
                public void run() {
                   
                }
            });
             
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
        } catch (Exception e) {
             
         
            e.printStackTrace();
             
            runOnUiThread(new Runnable() {
                public void run() {
                    
                }
            });
            Log.e("Upload file to server Exception", "Exception : "
                                             + e.getMessage(), e);  
        }
          
        return serverResponseCode; 
         
    
   } 
public ArrayList<String> OpenFileDialog(String file){
	   
    //Read file in Internal Storage
    FileInputStream fis;
   
    ArrayList<String> list = new ArrayList<String>();
   
    try {
    fis = openFileInput(file);
     String line = "string";
     BufferedReader buffreader = new BufferedReader(new InputStreamReader(fis));
    while (( line = buffreader.readLine()) != null) {
    
      list.add(line);
      System.out.println(list);
 }
         fis.close();
        
        
    } catch (FileNotFoundException e) {
     e.printStackTrace();
    } catch (IOException e) {
     e.printStackTrace(); 
    }
    return list;
   
  }
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

