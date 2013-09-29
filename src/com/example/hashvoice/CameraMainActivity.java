package com.example.hashvoice;

import java.io.File;
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


public class CameraMainActivity extends Activity {
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
    final int TAKE_PICTURE = 1;
    private Uri imageUri;

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
        startActivityForResult(intent, TAKE_PICTURE);
    }
    
    public void tag(View view) {
  	  Intent myIntent = new Intent(CameraMainActivity.this, VoiceRecognitionActivity.class);
  	  //myIntent.putExtra("key", value); //Optional parameters
  	  CameraMainActivity.this.startActivity(myIntent);
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
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                        .show();
                Log.e("Camera", e.toString());
            }
        }
    }
}
}