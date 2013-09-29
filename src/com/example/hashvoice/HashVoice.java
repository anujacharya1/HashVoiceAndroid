package com.example.hashvoice;

import android.os.Bundle;
import android.net.Uri;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.example.hashvoice.R;

public class HashVoice extends Activity {
	private Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final Context context = this;
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hash_voice);
 
		button = (Button) findViewById(R.id.buttonUrl);
 
		button.setOnClickListener(new OnClickListener() {
 
		  public void onClick(View arg0) {
		    Intent intent = new Intent(context, WebViewActivity.class);
		    startActivity(intent);
		  }
 
		});
 
	}
	
 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.hash_voice, menu);
		return true;
	}

}
