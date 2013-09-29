package com.example.hashvoice;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hashvoice.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

public class CameraMainActivity extends Activity {
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    final int TAKE_PICTURE = 1;
    final int GET_BEST_TAG_MATCH = 2;
    static int count = 0;
    private String mFileName = null;
    private String mBestMatch = null;
    private Uri imageUri;
    
    HttpURLConnection connection = null;
    DataOutputStream outputStream = null;
    DataInputStream inputStream = null;

    String pathToOurFile = "/data/file_to_send.mp3";
    String urlServer = "http://50.59.22.188:17021/photo/";
    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary =  "*****";

    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1*1024*1024;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_main);
        takePhoto();
    }



    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        /*imageUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), String.valueOf(count) + ".jpg"));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);*/
        startActivityForResult(intent, TAKE_PICTURE);
    }
    
    public void tag(View view) {
  	  Intent myIntent = new Intent(CameraMainActivity.this, VoiceRecognitionActivity.class);
  	  //myIntent.putExtra("key", value); //Optional parameters
  	  CameraMainActivity.this.startActivityForResult(myIntent, GET_BEST_TAG_MATCH);
    }
    
    
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
    case TAKE_PICTURE:
        if (resultCode == Activity.RESULT_OK) {
            //Uri imageUri;
            Uri selectedImage = imageUri;
            getContentResolver().notifyChange(selectedImage, null);
            ImageView imageView = (ImageView) findViewById(R.id.imageView1);
            ContentResolver cr = getContentResolver();
            Bitmap bitmap;
            try {
                 bitmap = android.provider.MediaStore.Images.Media
                 .getBitmap(cr, selectedImage);

                imageView.setImageBitmap(bitmap);
                Toast.makeText(this, "This file: "+selectedImage.toString(),
                        Toast.LENGTH_LONG).show();
                mFileName = selectedImage.toString();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                        .show();
                Log.e("Camera", e.toString());
            }
        }
        break;
    case GET_BEST_TAG_MATCH:
    	
    	if(resultCode == Activity.RESULT_OK) {
    		ArrayList<String> textMatchList = data
    			    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    		if (null == textMatchList)
    			break;
    		String searchQuery = textMatchList.get(0);
    		//Toast.makeText(this, "GET_BEST_TAG_MATCH.." + searchQuery, Toast.LENGTH_SHORT).show();
    		mBestMatch = searchQuery;
    		
    	} else {
    		ArrayList<String> textMatchList = data
    			    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    		if (null == textMatchList)
    		Toast.makeText(this, "GET_BEST_TAG_MATCH???" + textMatchList.get(0), Toast.LENGTH_SHORT).show();
    		else
    			Toast.makeText(this, "GET_BEST_TAG_MATCH:::::" + textMatchList.get(0), Toast.LENGTH_SHORT).show();
    	}
    	
    	Toast.makeText(this, "FileName: " + mFileName + "  Tag: " + mBestMatch, Toast.LENGTH_SHORT).show();
    	//List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        //nameValuePairs.add(new BasicNameValuePair("image", mFileName));
        Log.d("DEBUGxxxxxxxx",mFileName );
        //nameValuePairs.add(new BasicNameValuePair("tag", mBestMatch));
        //post("http://50.59.22.188:17021/photo/", nameValuePairs);
        Thread thread = new Thread()
    	{
    	    @Override
    	    public void run() {
    	        try {
    	            //while(true) {
    	                //sleep(1000);
    	        	    Log.d("DEBUGxxxxxxxx   1",mFileName );
    	                //post1("");
                  	     //Toast.makeText(getApplicationContext(),"Hurrrah!!! " + started,Toast.LENGTH_LONG).show();
    	            	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
    	                nameValuePairs.add(new BasicNameValuePair("image", mFileName));
    	                Log.d("DEBUGxxxxxxxx",mFileName );
    	                nameValuePairs.add(new BasicNameValuePair("tags", mBestMatch));
    	                post("http://50.59.22.188:17021/photo/", nameValuePairs);
    	            //}
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }
    	    }
    	};

    	thread.start();
        
    	break;
          default:
        	Toast.makeText(this, "Not Supported Message...", Toast.LENGTH_SHORT).show();
    }

}

/*http://50.59.22.188:17021/photo/
POST : tags
anuj sid
image 2nd POST parameter*/

public void post1(String urls) {
	try
	{
		String path = "file:///storage/emulated/0/Pic.jpg";
		Log.d("DEBUGxxxxxxxx   2", "Hurrah" );
		
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        
        Uri imageUri = Uri.fromFile(photo);
	FileInputStream fileInputStream = new FileInputStream(photo);

	Log.d("DEBUGxxxxxxxx   3", "Hurrah  " );
	URL url = new URL(urlServer);
	connection = (HttpURLConnection) url.openConnection();

	// Allow Inputs & Outputs
	connection.setDoInput(true);
	connection.setDoOutput(true);
	connection.setUseCaches(false);

	Log.d("DEBUGxxxxxxxx   3", "Hurrah" );
	// Enable POST method
	connection.setRequestMethod("POST");

	connection.setRequestProperty("Connection", "Keep-Alive");
	connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

	outputStream = new DataOutputStream( connection.getOutputStream() );
	outputStream.writeBytes(twoHyphens + boundary + lineEnd);
	outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + imageUri.getPath() +"\"" + lineEnd);
	outputStream.writeBytes(lineEnd);

	Log.d("DEBUGxxxxxxxx   4", "Hurrah" );
	bytesAvailable = fileInputStream.available();
	bufferSize = Math.min(bytesAvailable, maxBufferSize);
	buffer = new byte[bufferSize];

	// Read file
	Log.d("DEBUGxxxxxxxx   5", "Hurrah  "  + bufferSize);
	bytesRead = fileInputStream.read(buffer, 0, bufferSize);

	while (bytesRead > 0)
	{
	outputStream.write(buffer, 0, bufferSize);
	bytesAvailable = fileInputStream.available();
	bufferSize = Math.min(bytesAvailable, maxBufferSize);
	bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	Log.d("DEBUGxxxxxxxx   5.1", "Hurrah  " + bytesRead );
	}

	outputStream.writeBytes(mBestMatch); 
	
	outputStream.writeBytes(lineEnd);
	outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

	Log.d("DEBUGxxxxxxxx   6", "Hurrah" );
	// Responses from the server (code and message)
	int serverResponseCode = connection.getResponseCode();
	String serverResponseMessage = connection.getResponseMessage();

	Log.d("DEBUGxxxxxxxx   7.......", serverResponseCode + serverResponseMessage );
	fileInputStream.close();
	outputStream.flush();
	outputStream.close();
	}
	catch (Exception ex)
	{
	//Exception handling
		Log.d("DEBUGxxxxxxxx   2", "Ooopsy" );
		ex.printStackTrace();
	}
}

public void post(String url, List<NameValuePair> nameValuePairs) {
    HttpClient httpClient = new DefaultHttpClient();
    HttpContext localContext = new BasicHttpContext();
    HttpPost httpPost = new HttpPost(url);

    try {
    	MultipartEntityBuilder entity = MultipartEntityBuilder.create(); // entity = new MultipartEntityBuilder(); //(HttpMultipartMode.BROWSER_COMPATIBLE);
    	entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        for(int index=0; index < nameValuePairs.size(); index++) {
            if(nameValuePairs.get(index).getName().equalsIgnoreCase("image")) {
                // If the key equals to "image", we use FileBody to transfer the data
                //entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File (nameValuePairs.get(index).getValue())));
                entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File (Environment.getExternalStorageDirectory(), "Pic.jpg")));
            } else {
                // Normal string data
            	entity.addTextBody(nameValuePairs.get(index).getName(), nameValuePairs.get(index).getValue());
                //entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
            }
        }
	
        //httpPost.setEntity(entity);
        httpPost.setEntity(entity.build());

        HttpResponse response = httpClient.execute(httpPost, localContext);

        /*String htmlContent = "";
        try {
            response = httpClient.execute(httpPost);

            Toast.makeText(getApplicationContext(),"Resp " + response.getStatusLine().getStatusCode(),Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),"Resp " + json,Toast.LENGTH_SHORT).show();

            //if (response.getStatusLine().getStatusCode() == 200) {
        } catch (Exception e) {
        	e.printStackTrace();
        }*/
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}