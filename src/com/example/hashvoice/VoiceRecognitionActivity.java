package com.example.hashvoice;

//import com.example.voicerecognition.R;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

//package com.rakesh.voicerecognitionexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
 
public class VoiceRecognitionActivity extends Activity {
 private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
 int SearchMode = 0;
 private EditText metTextHint;
 private ListView mlvTextMatches;
 private Spinner msTextMatches;
 private Button mbtSpeak;
 
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_voice_recognition);
  metTextHint = (EditText) findViewById(R.id.etTextHint);
  mlvTextMatches = (ListView) findViewById(R.id.lvTextMatches);
  msTextMatches = (Spinner) findViewById(R.id.sNoOfMatches);
  mbtSpeak = (Button) findViewById(R.id.btSpeak);
  //mbtPick = (Button) findViewById(R.id.Pick);
  checkVoiceRecognition();
  SearchMode = 0;
  privSpeak(0);
 }
 
 public void checkVoiceRecognition() {
  // Check if voice recognition is present
  PackageManager pm = getPackageManager();
  List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
    RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
  if (activities.size() == 0) {
   mbtSpeak.setEnabled(false);
   mbtSpeak.setText("Voice recognizer not present");
   Toast.makeText(this, "Voice recognizer not present",
     Toast.LENGTH_SHORT).show();
  }
 }
 
 public void pick(View view) {
	 Intent myIntent = new Intent(VoiceRecognitionActivity.this, CameraMainActivity.class);
	 VoiceRecognitionActivity.this.startActivity(myIntent);
	 SearchMode = 0;
	 
 }
 
 public void search(View view) {
	 Log.d("Search xxxxxxxx ", "OK:");
	 SearchMode = 1;
	 privSpeak(1);
	 
 }
 
 private void privSpeak(int state) {
	  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	 
	  // Specify the calling package to identify your application
	  intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
	    .getPackage().getName());
	 
	  // Display an hint to the user about what he should say.
	  intent.putExtra(RecognizerIntent.EXTRA_PROMPT, metTextHint.getText()
	    .toString());
	 
	  // Given an hint to the recognizer about what the user is going to say
	  //There are two form of language model available
	  //1.LANGUAGE_MODEL_WEB_SEARCH : For short phrases
	  //2.LANGUAGE_MODEL_FREE_FORM  : If not sure about the words or phrases and its domain.
	intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	    RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
	 
	  // If number of Matches is not selected then return show toast message
	  if (msTextMatches.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
	   Toast.makeText(this, "Please select No. of Matches from spinner",
	     Toast.LENGTH_SHORT).show();
	   return;
	  }
	 
	  int noOfMatches = Integer.parseInt(msTextMatches.getSelectedItem()
	    .toString());
	  // Specify how many results you want to receive. The results will be
	  // sorted where the first result is the one with higher confidence.
	  intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);
	  intent.putExtra("SearchState", state);
	  //Start the Voice recognizer activity for the result.
	  startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
 }
 
 public void speak(View view) {
  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
 
  // Specify the calling package to identify your application
  intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
    .getPackage().getName());
 
  // Display an hint to the user about what he should say.
  intent.putExtra(RecognizerIntent.EXTRA_PROMPT, metTextHint.getText()
    .toString());
 
  // Given an hint to the recognizer about what the user is going to say
  //There are two form of language model available
  //1.LANGUAGE_MODEL_WEB_SEARCH : For short phrases
  //2.LANGUAGE_MODEL_FREE_FORM  : If not sure about the words or phrases and its domain.
intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
    RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
 
  // If number of Matches is not selected then return show toast message
  if (msTextMatches.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
   Toast.makeText(this, "Please select No. of Matches from spinner",
     Toast.LENGTH_SHORT).show();
   return;
  }
 
  int noOfMatches = Integer.parseInt(msTextMatches.getSelectedItem()
    .toString());
  // Specify how many results you want to receive. The results will be
  // sorted where the first result is the one with higher confidence.
  intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);
  intent.putExtra("SearchState", 0);
  //Start the Voice recognizer activity for the result.
  startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
 }
 
 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	 ArrayList<String> textMatchList = null;
  if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)
 
   //If Voice recognition is successful then it returns RESULT_OK
   if(resultCode == RESULT_OK) {
 
    textMatchList = data
    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    
    //Bundle extras = data.getExtras();
    int SearchState = data.getIntExtra("SearchState", 0);
    SearchState = SearchMode;
    
    showToastMessage("SearchState " + SearchState);
 
    if (!textMatchList.isEmpty()) {
     // If first Match contains the 'search' word
     // Then start web search.
     if (textMatchList.get(0).contains("search")) {
 
        String searchQuery = textMatchList.get(0);
                                           searchQuery = searchQuery.replace("search","");
        Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
        search.putExtra(SearchManager.QUERY, searchQuery);
        startActivity(search);
     } else if (SearchState == 1){
    	 String searchQuery = textMatchList.get(0);
    	 showToastMessage("searchQuery:  " + searchQuery);
    	 searchText(searchQuery);
     } else {
         // populate the Matches
         mlvTextMatches
      .setAdapter(new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1,
        textMatchList));
     }
 
    }
   //Result code for various error.
   }else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
    showToastMessage("Audio Error");
   }else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
    showToastMessage("Client Error");
   }else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
    showToastMessage("Network Error");
   }else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
    showToastMessage("No Match");
   }else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
    showToastMessage("Server Error");
   }
  super.onActivityResult(requestCode, resultCode, data);

  
  Intent resultIntent = new Intent();
  resultIntent.putExtra(RecognizerIntent.EXTRA_RESULTS, textMatchList);
  //TODO Add extras or a data URI to this intent as appropriate.
  setResult(Activity.RESULT_OK, resultIntent);
  //CameraMainActivity.this.onActivityResult(requestCode, resultCode, data);
 }
 
 private static String convertStreamToString(InputStream is) {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
 }
 private void searchText(String text) {
	 Log.d("searchText 1"," searchText");
	 if(null == text) return;
	 
	 Log.d("searchText 1.1",text);
	 
	 String url = "http://50.59.22.188:17021/photosearch/" + text;
	 HttpClient httpclient = new DefaultHttpClient();
	 
	 Log.d("searchText 1.1",url);
	 // Prepare a request object
	 HttpGet httpget = new HttpGet(url); 
	// Execute the request
	    HttpResponse response;
	    try {
	    	 Log.d("searchText 1.1","Response...");
	        response = httpclient.execute(httpget);
	        // Examine the response status
	        Log.d("searchText 2",response.getStatusLine().toString());

	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();
	        // If the response does not enclose an entity, there is no need
	        // to worry about connection release

	        if (entity != null) {

	            // A Simple JSON Response Read
	            InputStream instream = entity.getContent();
	            final String result= convertStreamToString(instream);
	            
	            Log.d("searchText 3",result);
	        	/*JSONObject mainObject = new JSONObject(result);
	        	//String menuObject = mainObject.getString("photo");
	        	Log.d("searchText 3.1",result);
	        	Log.d("searchText 3.1",result);
	        	 final JSONArray a;
     
	        	  a = mainObject.getJSONArray("photo");
                
                
	        	
	        	
	        	Log.d("searchText 3.2", "Length :" + a.length());
	        	
	        	for (int i = 0; i < a.length(); i++) {
	        	    Log.d("XXXXXX Values...", a.getString(i));
	        	}*/
	        	
	            Thread thread = new Thread()
	        	{
	        	    @Override
	        	    public void run() {
	        	        try {
	        	            //while(true) {
	        	                //sleep(1000);
	        	        	Log.d("searchText 3.4: xxxxx ","getImage");
	        	        	//getImage(a.getString(0));
	        	        	//int count =0;
							getImage(CameraMainActivity.count -1  + "Pic.jpg");

	        	            //}
	        	        } catch (Exception e) {
	        	            e.printStackTrace();
	        	        }
	        	    }
	        	};

	        	thread.start();
	            
	            Log.d("searchText 3.5: xxxxx ",result);
	            // now you have the string representation of the HTML request
	            instream.close();
	        }


	    } catch (Exception e) {}
	 
 }
 
 private static Bitmap convertStreamToBitmap(InputStream is) {
	 Bitmap bitmap = null;
	 Log.d("Search convertStreamToBitmap  1"," STEP 1");
     try
     {
    	 Log.d("Search convertStreamToBitmap  2"," STEP 2");
         bitmap = BitmapFactory.decodeStream(is);
         Log.d("Search convertStreamToBitmap  2.1",""+bitmap.getWidth());
         is.close();
         is = null;
     }
     catch(IOException exception)
     {
             bitmap = null;
             exception.printStackTrace();
     }
     catch (OutOfMemoryError e) 
     {
    	 bitmap = null;
    	 e.printStackTrace();
     }

     return bitmap;
 }

 
 private void getImage(String name) {
	 Log.d("Search Q 1 "," getImage");
	 if(null == name) return;
	 
	 String url = "http://50.59.22.188:17021/media/" + name;
	 HttpClient httpclient = new DefaultHttpClient();
	 // Prepare a request object
	 HttpGet httpget = new HttpGet(url); 
	// Execute the request
	    HttpResponse response;
	    try {
	    	Log.d("Search Q 1.2", "URL :  " + url);
	        response = httpclient.execute(httpget);
	        // Examine the response status
	        Log.d("Search Q 1.2",response.getStatusLine().toString());

	        // Get hold of the response entity
	        HttpEntity entity = response.getEntity();
	        // If the response does not enclose an entity, there is no need
	        // to worry about connection release

	        if (entity != null) {

	            // A Simple JSON Response Read

	        	ImageView imageView = (ImageView) findViewById(R.id.imageView2);
	        	Log.d("Search Q 2"," getImage");
	        	
	            InputStream instream = entity.getContent();
	            Log.d("Search Q 2"," getImage");
	            Bitmap result= convertStreamToBitmap(instream);
	            imageView.setImageBitmap(result);
	            Log.d("Search Q 3"," getImage");
	            //Log.d("getImaget : xxxxx ",result);
	            // now you have the string representation of the HTML request
	            instream.close();
	        }


	    } catch (Exception e) {}
	 
 }
 

 /**
 * Helper method to show the toast message
 **/
 void showToastMessage(String message){
  Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
 }
}

/*public class VoiceRecognitionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_recognition);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.voice_recognition, menu);
		return true;
	}

}*/
