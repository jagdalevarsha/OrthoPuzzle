package com.example.orthopuzzlev1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstOrthoView extends Activity 
{
	Button btnNextView;  
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_ortho_view);
                
    }
    
    //Function to go to the next view
    public void showNextView(View view)
    {
    	Intent secondOrthoViewIntent = new Intent(this, SecondOrthoView.class);
    	startActivity(secondOrthoViewIntent);    	    	
    }
}
