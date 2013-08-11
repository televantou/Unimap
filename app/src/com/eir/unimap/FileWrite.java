package com.eir.unimap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.eir.unimap.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

public class FileWrite extends Activity {
	   int serverResponseCode = 0;
	    ProgressDialog dialog = null;
	    String strNewFileName ="";
	    String upLoadServerUri = null;
	   
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    TelephonyManager  tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    String IMEINumber=tm.getDeviceId();

     strNewFileName =  (IMEINumber+".txt");
    File fileDir = getFilesDir();
    final  String filedir=fileDir.toString();
    Intent intent1 = getIntent();
    String mes = intent1.getStringExtra("msg");
    String lat = intent1.getStringExtra("lat");
    String lng = intent1.getStringExtra("lng");
    
    
    
   
  
    String strFileContents = (mes + "," + lat + "," + lng+ ",");
    FileOutputStream outputStream;
    try{
    	 outputStream = openFileOutput(strNewFileName, Context.MODE_APPEND);
    	 outputStream.write(strFileContents.getBytes());
    	 outputStream.close();
    	 
    	
    	 uploadFile(filedir + "/" + strNewFileName);
    }
    catch(IOException e) 
    {Log.v("TEST","Exception on create ");}
    
   System.out.print(OpenFileDialog(strNewFileName));
   

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
}  


