package com.example.hashvoice;
 
import com.example.hashvoice.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
 
public class WebViewActivity extends Activity {
 
	private WebView webView;
	int started = 0;
	private boolean state = false;
    private  static final String localhost_url = "http://127.0.0.1:8000/";
	
	private static final String TAG = "WebViewActivity";
 
    public String convertToString(InputStream inputStream){
        StringBuffer string = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                string.append(line + "\n");
            }
        } catch (IOException e) {}
        return string.toString();
    }


    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		started = 0;
 
		
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(localhost_url+"index");
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		// Must be after loadUrl!
		webView.setBackgroundColor(0x00000000); // transparent RGB
		webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        // Put background image in res/drawable/splash.jpg
        Drawable theSplash = getResources().getDrawable(R.drawable.splash);
        webView.setBackground(theSplash);
		
		webView.setWebViewClient(new WebViewClient() {
			@Override
		    public boolean shouldOverrideUrlLoading(WebView webview, String url){
				
		        webview.loadUrl(url);
		        if(state == true) return true;
		        HttpGet httpGet = new HttpGet(url);
		        // this receives the response
		        final HttpClient httpClient   = new DefaultHttpClient();
		        HttpResponse response = null;
		        String htmlContent = "";
		        try {
		            response = httpClient.execute(httpGet);

		            //Toast.makeText(getApplicationContext(),"Resp " + response.getStatusLine().getStatusCode(),Toast.LENGTH_SHORT).show();
		            //Toast.makeText(getApplicationContext(),"Resp " + json,Toast.LENGTH_SHORT).show();

		            //if (response.getStatusLine().getStatusCode() == 200) {
		                // la conexion fue establecida, obtener el contenido
		                HttpEntity entity = response.getEntity();
		                if (entity != null) {
		                    InputStream inputStream = entity.getContent();
		                    htmlContent = convertToString(inputStream);
		                    if(null != htmlContent) {
		                    	//Toast.makeText(getApplicationContext(),"body " + htmlContent,Toast.LENGTH_LONG).show();
		                    }
		                    
		                    //Log.d(TAG, htmlContent);
		                    if (htmlContent.contains("Welcome to Flickr!") && null != htmlContent) { 
		                    	//webView.loadData(htmlContent, "text/html", "utf-8");
                                Thread th = new Thread(){
                                    @Override
                                    public void run(){
                                        try {

                                            //while(true) {
                                            sleep(5000);
                                            Intent myIntent = new Intent(WebViewActivity.this, CameraMainActivity.class);
                                            //myIntent.putExtra("key", value); //Optional parameters
                                            WebViewActivity.this.startActivity(myIntent);
                                            state = true;
                                            //Toast.makeText(getApplicationContext(),"Hurrrah!!! " + started,Toast.LENGTH_LONG).show();
                                            //}
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
//		                    	Thread thread = new Thread()
//		                    	{
//		                    	    @Override
//		                    	    public void run() {
//		                    	        try {
//
//		                    	            //while(true) {
//		                    	                sleep(5000);
//		                    	                 Intent myIntent = new Intent(WebViewActivity.this, CameraMainActivity.class);
//		      		                    	     //myIntent.putExtra("key", value); //Optional parameters
//		      		                    	     WebViewActivity.this.startActivity(myIntent);
//		      		                    	     state = true;
//		      		                    	     //Toast.makeText(getApplicationContext(),"Hurrrah!!! " + started,Toast.LENGTH_LONG).show();
//		                    	            //}
//		                    	        } catch (InterruptedException e) {
//		                    	            e.printStackTrace();
//		                    	        }
//		                    	    }
//		                    	};

//		                    	thread.start();
                                th.start();
		                    	  
		                    }
		                    started++;
		                    //Toast.makeText(getApplicationContext(),"Loading... Please Wait! ",Toast.LENGTH_LONG).show();
		                }
		            //}
		         } catch (IOException e) {
		        	 Toast.makeText(getApplicationContext(),"----" + e.getMessage() ,Toast.LENGTH_SHORT).show();
		         }
		        
		        //Toast.makeText(getApplicationContext(), "In shouldOverrideUrlLoading " + url, Toast.LENGTH_SHORT).show();
		        return true;
		    }
		   
		   /*@Override
		   public void onPageStarted(WebView view, String url, Bitmap favicon) {

			   Toast.makeText(getApplicationContext(), "Page Started", Toast.LENGTH_SHORT).show();

		   }*/

		   @Override
		   public void onPageFinished(WebView view, String url) {

			   //Toast.makeText(getApplicationContext(), "Page Finished " + url, Toast.LENGTH_SHORT).show();

		   }
		   
		   /*@Override
		   public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
			   Toast.makeText(getApplicationContext(), "onReceivedHttpAuthRequest ", Toast.LENGTH_SHORT).show();
		   }*/
			   
			   
		});
 
	}
 
}