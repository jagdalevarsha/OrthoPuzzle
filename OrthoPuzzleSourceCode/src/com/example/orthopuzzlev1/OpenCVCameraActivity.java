package com.example.orthopuzzlev1;

import java.util.ArrayList;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;

public class OpenCVCameraActivity extends Activity implements CvCameraViewListener2
{
	String openCVCameraActivityTag = "Open CV Camera Activity";
	private CameraBridgeViewBase mOpenCvCameraView;
	Mat rgba, hsv, hierarchy, red;
	static ArrayList<MatOfPoint2f> approxContour2fArray; 
	static double sideSize;
	static double sideSizes[][];
	static int k = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		Log.d(openCVCameraActivityTag,"Open CV Camera on create function");
		super.onCreate(savedInstanceState);
		
		//Set the window to be always turned on during this activity
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.activity_open_cvcamera);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);		
		
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv_camera);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		mOpenCvCameraView.setCvCameraViewListener(this);
	}
	
	//Setting the back option
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    Intent myIntent = new Intent(getApplicationContext(), SecondOrthoView.class);
	    startActivityForResult(myIntent, 0);
	    return true;
	}
	
	//Initialize openCV
		private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback (this)
		{
			@Override
			public void onManagerConnected(int status)
			{
				switch(status)
				{
					case LoaderCallbackInterface.SUCCESS:
					{
						//Open CV initialization successful
						Log.i(openCVCameraActivityTag, "Initialisation successful");
						mOpenCvCameraView.enableView();				
					}
					break;
					
					default:
					{
						super.onManagerConnected(status);					
					}
					break;
				
				}
				
			}
			
		};
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);		
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCameraViewStopped() 
	{
		// TODO Auto-generated method stub
		
	}
		
	public Mat onCameraFrame(CvCameraViewFrame inputFrame)
	{	
		//For every iteration, the size of the side will reinitialized 
		
		sideSize = 0;
		
		Mat colorMat = inputFrame.rgba();
		
		Mat grayMat = inputFrame.gray();
		
		Imgproc.threshold(grayMat, grayMat, 128, 255, Imgproc.THRESH_BINARY);
		
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>(); 
		hierarchy = new Mat();

		Imgproc.findContours(grayMat, contours, hierarchy,Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		
		sideSizes = new double[12][4];
		
		List<MatOfPoint> squareContours = getSquareContours(contours);
				
		Imgproc.drawContours(colorMat, squareContours, -1 , new Scalar(0,0,255),10);
		
		//Show the user that he has won the game
		
//		if(squareContours != null)
//			
//			this.runOnUiThread(new Runnable() {
//			    public void run() {
//					Toast.makeText(getApplicationContext(), "Yay!!You got it right!", Toast.LENGTH_LONG).show();
//			    }
//			});
				
		hierarchy.release();
		grayMat.release();
		
		return colorMat;
			
	}

	public static boolean isContourSquare(MatOfPoint thisContour,int m) 
	{
	    Rect ret = null;
	    
	    MatOfPoint2f thisContour2f = new MatOfPoint2f();
	    
	    MatOfPoint approxContour = new MatOfPoint();
	    MatOfPoint2f approxContour2f = new MatOfPoint2f();

	    thisContour.convertTo(thisContour2f, CvType.CV_32FC2);

	    int contourSize = (int)thisContour.total();

	    Imgproc.approxPolyDP(thisContour2f, approxContour2f, contourSize * 0.05	, true);

	    approxContour2f.convertTo(approxContour, CvType.CV_32S);
	    	   
	    if (approxContour2f.total() == 4 ) 
	    {
	    	
	    	//Additional logic
	    	
	    	double temp_double1[] = approxContour2f.get(0, 0);
	    	Point p1 = new Point(temp_double1[0], temp_double1[1]);	    	
	    	Log.i("P1 x"+p1.x,"P1 y"+p1.y);
	    	
	    	double temp_double2[] = approxContour2f.get(1, 0);
	    	Point p2 = new Point(temp_double2[0], temp_double2[1]);	    	
	    	Log.i("P2 x"+p2.x,"P2 y"+p2.y);
	    	
	    	double temp_double3[] = approxContour2f.get(2, 0);
	    	Point p3 = new Point(temp_double3[0], temp_double3[1]);	    	
	    	Log.i("P3 x"+p3.x,"P3 y"+p3.y);
	    	
	    	double temp_double4[] = approxContour2f.get(3, 0);
	    	Point p4 = new Point(temp_double4[0], temp_double4[1]);		    	
	    	
         	int dist1, dist2, dist3, dist4;
         	
         	dist1 = (int) Math.sqrt(Math.pow((p1.x - p2.x),2) + Math.pow((p1.y-p2.y),2));
         	dist2 = (int) Math.sqrt(Math.pow((p3.x - p2.x),2) + Math.pow((p2.y-p3.y),2));
         	dist3 = (int) Math.sqrt(Math.pow((p3.x - p4.x),2) + Math.pow((p3.y-p4.y),2));
         	dist4 = (int) Math.sqrt(Math.pow((p1.x - p4.x),2) + Math.pow((p1.y-p4.y),2));
         	         		    	
	    	//Remove smaller squares
	    	if(dist1 > 100 && dist2 >100 && dist3 >100 && dist4 >100)
	    	{
	    		
	    		//Check for the length to be the same
	    		if(Math.abs(dist1 - dist2) < 150 && Math.abs(dist2 - dist3) < 150 && Math.abs(dist3 - dist4) < 150 && Math.abs(dist4 - dist1) < 150 && Math.abs(dist1 - dist3) <150 && (dist2-dist4)<150)
	    		{
	    			
	    	    	Log.i("Co-ordinates for Shape"+m, p1.x+","+p1.y+","+p2.x+","+p2.y+p3.x+","+p3.y+p4.x+","+p4.y);
	    	    	
	    	    	Log.i("Length of the Shape size"+m, dist1+","+ dist2 +","+dist3+","+dist4);
	    	    	
	    	    	sideSizes[k][0] = dist1;
	             	sideSizes[k][1] = dist2;
	             	sideSizes[k][2] = dist3;
	             	sideSizes[k][3] = dist4;
	             	             	
	             	k++;
	    			
	    			ret = Imgproc.boundingRect(approxContour);
			        Log.i("Square", "Square");
	    			
	    		}
	    			    		
	    	} 
	        
	    }
	    else 
	    {
	    	Log.i("Not a square", approxContour2f.total()+"");
	    }
	    
	    return (ret != null);
	}
	
	public static List<MatOfPoint> getSquareContours(List<MatOfPoint> contours) 
	{

	    List<MatOfPoint> squares = null;
	    List<MatOfPoint> puzzleSquares = null;
	       
	    int i = 1;
	    
	    k =0;
	    
	    for (MatOfPoint c : contours)
	    {
	        if (isContourSquare(c,i)) 
	        {
	        	//Additional logic
	        	      	
	            if (squares == null)
	                squares = new ArrayList<MatOfPoint>();
	            
	            squares.add(c);	          
	        }
	        i++;
	    }
	    
	    boolean isPuzzleSolved = false;
	    
	    //Check condition for puzzle square
        
        //Condition 1 - Three squares should be detected\  && squares.size()==3
    	if(squares!=null)
        	{
                puzzleSquares = new ArrayList<MatOfPoint>();
        		
        	    double [][] centroidArray = new double[3][2];
        	    
        	    int j = 0;
        	    
        	    
        	    	for(MatOfPoint square : squares)
        	    	{
               			//Find Centroid
            			
            			Moments moments  = Imgproc.moments(square);
            	    	
            			Point centroid = new Point();
            			
            			centroid.x = moments.get_m10() / moments.get_m00();
            			centroid.y = moments.get_m01() / moments.get_m00();
            			
            			centroidArray[j][0] = centroid.x;
            			centroidArray[j][1] = centroid.y;
            			
            			Log.e("Centroid for Shape"+(j+1), centroid.x+","+centroid.y);
            	    	
                   	    puzzleSquares.add(square);
                   	    
                   	    j++;
            		} 
        	    	
        	    	//Traverse the centroid array
        	    	
        	    	//Calculate average square size
        	    	
        	    	double total = 0;
        	    	
        	    	for(int x=0;x<4;x++)
        	    	{
        	    		for(int y=0;y<squares.size();y++)
        	    		{
        	    			total += sideSizes[y][x];
        	    		}
        	    	}
        	    	
        	    	sideSize = total / (sideSizes.length);
        	    	
        	    	Log.e("Side Size",sideSize+":"+total+":"+sideSizes.length+":"+squares.size());
        	    	
        	    	//Check if the distance between the centroid is less than the side length
        	    	
        	    	double distBetweenCentroid12;
        	    	double distBetweenCentroid13;
        	    	double distBetweenCentroid23;
       	    		
        	    	distBetweenCentroid12 = (int) Math.sqrt(Math.pow((centroidArray[0][0] - centroidArray[1][0]),2) + Math.pow((centroidArray[0][1] - centroidArray[1][1]),2));
        	    	distBetweenCentroid13 = (int) Math.sqrt(Math.pow((centroidArray[0][0] - centroidArray[2][0]),2) + Math.pow((centroidArray[0][1] - centroidArray[2][1]),2));
        	    	distBetweenCentroid23 = (int) Math.sqrt(Math.pow((centroidArray[1][0] - centroidArray[2][0]),2) + Math.pow((centroidArray[1][1] - centroidArray[2][1]),2));
        	    	
        	    	Log.e("Distance between centroids",distBetweenCentroid12+" "+distBetweenCentroid13+" "+distBetweenCentroid23);
        	  
        	    	int largest;
        	    	
					if((Math.abs(distBetweenCentroid12-sideSize)<50) && (Math.abs(distBetweenCentroid13-sideSize)<50) ||  (Math.abs(distBetweenCentroid12-sideSize)<50) && (Math.abs(distBetweenCentroid23-sideSize)<50) || (Math.abs(distBetweenCentroid13-sideSize)<50 && Math.abs(distBetweenCentroid23-sideSize)<50)) 
					{
        	    		//Check for right angle
        	    		//Find the largest side and then apply Pythagoras theorem
        	    		
        	    		largest = 1;
        	    	    
        	    	    if((distBetweenCentroid13 > distBetweenCentroid12) && (distBetweenCentroid13>distBetweenCentroid23))
        	    	    	largest = 2;
        	    	
        	    	    else if((distBetweenCentroid23 > distBetweenCentroid12) && (distBetweenCentroid23>distBetweenCentroid13))
        	    	    largest = 3;
        	    	            	    	    
        	    	    switch(largest) 
        	    	    {
        	    	    case 1: if(Math.sqrt((Math.pow(distBetweenCentroid13,2) + Math.pow(distBetweenCentroid23,2))) - distBetweenCentroid12 < 30)
        	    	    		isPuzzleSolved = true;
        	    	    		break;
        	    	    
        	    	    case 2:	if(Math.sqrt((Math.pow(distBetweenCentroid12,2) + Math.pow(distBetweenCentroid23,2))) - distBetweenCentroid13 < 30)
    	    	    			isPuzzleSolved = true;
    	    	    			break;
        	    	    	
        	    	    case 3:	if(Math.sqrt((Math.pow(distBetweenCentroid12,2) + Math.pow(distBetweenCentroid13,2))) - distBetweenCentroid23 < 30)
    	    	    			isPuzzleSolved = true;
    	    	    			break;
        	    	    	
        	    	    default: break;
        	    	    
        	    	    }
        	    	    	        	    		
					}
        	    	else //The squares are not close enough
        	    		
        	    		isPuzzleSolved = false;
        	    			        	    	
        	    }
        	
        	else //Three squares are not detected 
        		isPuzzleSolved = false;
        		          	    
    if(isPuzzleSolved)	
    	return puzzleSquares;
        else
        	return null;
	}
	

	public void onPause()
	{
		super.onPause();
		if(mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();		
	}
	
	public void onDestroy()
	{
		super.onDestroy();
		if(mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();		
	}
	
}
