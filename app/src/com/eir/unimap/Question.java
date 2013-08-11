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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Question extends Activity {
	String strNewFileName="";
	  int serverResponseCode = 0;
	  ProgressDialog dialog = null;
	  String upLoadServerUri = null;
	   
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
		
		
	}
	public void submit(View view) {
	    // Do something in response to button
		EditText Q2A = (EditText) findViewById(R.id.q2a);
	   
	   	
	   	EditText Q6A = (EditText) findViewById(R.id.q6a);
	   
	   	
	   	RadioGroup	q1group = (RadioGroup) findViewById(R.id.q1g);
	   	RadioGroup	q3group = (RadioGroup) findViewById(R.id.q3g);
	   	RadioGroup	q4group = (RadioGroup) findViewById(R.id.q4g);
	   	RadioGroup	q5group = (RadioGroup) findViewById(R.id.q5g);
	   	int Id1 = q1group.getCheckedRadioButtonId();
	   	int Id3 = q3group.getCheckedRadioButtonId();
	   	int Id4 = q4group.getCheckedRadioButtonId();
	   	int Id5 = q5group.getCheckedRadioButtonId();
		// find the radiobutton by returned id
	     
	   RadioButton Q1YN =(RadioButton) findViewById(Id1);
	   RadioButton Q3YN=(RadioButton) findViewById(Id3);
	   RadioButton Q4YN=(RadioButton) findViewById(Id4);
	   RadioButton Q5YN=(RadioButton) findViewById(Id5);
	   
	   String Q1A = Q1YN.getText().toString();
	   String Q3A = Q3YN.getText().toString();
	   String Q4A = Q4YN.getText().toString();
	   String Q5A = Q5YN.getText().toString();
	   String q6a= Q6A.getText().toString();
	   String q2a= Q2A.getText().toString();
	 
	   TelephonyManager  tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
	    String IMEINumber=tm.getDeviceId();
		  strNewFileName =  ("q"+IMEINumber+".txt");
	   File fileDir = getFilesDir();
	    final  String filedir=fileDir.toString();
	    String strFileContents = (" **Start** "+Q1A + "," + q2a+ "," + Q3A  + "," +Q4A  + ","+  Q5A +  ","+q6a +" **End** ");
	    FileOutputStream outputStream;
	    //String fileName = (filedir + "/" + strNewFileName);
	    try{
	    	 outputStream = openFileOutput(strNewFileName, Context.MODE_APPEND);
	    	 outputStream.write(strFileContents.getBytes());
	    	 outputStream.close();
	    	 
	    	
	    	 uploadFile(filedir + "/" + strNewFileName);
	    }catch(IOException e) 
	    {Log.v("TEST","Exception on create ");}
	    
	    Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	    

	 }
	    
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
                           
                          String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                        +" http://www.androidexample.com/media/uploads/"
                                        +strNewFileName;
                           
                          
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



