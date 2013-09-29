package com.example.hashvoice;

import android.os.Bundle;
import android.net.Uri;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class HashVoice extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hash_voice);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.hash_voice, menu);
		Uri uri = Uri.parse("http://50.59.22.188:17021/index");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		return true;
	}

}
