package com.example.orthopuzzlev1;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class SecondOrthoView extends Activity 
{
	static final int REQUEST_IMAGE_CAPTURE = 1;
	ImageView imageView;
	TextView textView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second_ortho_view);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);	
				
	}
		
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    Intent myIntent = new Intent(getApplicationContext(), FirstOrthoView.class);
	    startActivityForResult(myIntent, 0);
	    return true;
	}
	
	
	//Function to go to the next view
	public void showCameraView(View view)
	{
		//Open OpenCV camera to capture live video stream

		Intent cameraViewIntent = new Intent(this, OpenCVCameraActivity.class);
		startActivity(cameraViewIntent);
			
	}
	
}
