package com.example.helloworld;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Display;
import android.view.Menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;


import android.view.View;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.HorizontalScrollView;
import android.view.Gravity;
import android.widget.Toast;

import android.os.StrictMode;
import android.util.Log;

public class MainActivity extends Activity {
	
	final Context context = this;
	public String imageUrl = "https://fbcdn-sphotos-c-a.akamaihd.net/hphotos-ak-ash4/392427_10151467158878197_235430490_n.jpg";
	LinearLayout myGallery;
	HorizontalScrollView myGalleryParent;
	
	public int windowWidth = 0;
	public int windowHeight = 0;
	public String folderPath = "";
	
	MediaPlayer mediaPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//----------check and create folder if not existing
		folderPath = createFolder("helloworld","data.xml") + "/";
		Log.v("IMAGESIZE",folderPath);
    	//----------check and create folder if not existing
		
    	//----------detect device setting and adapt environment
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		windowWidth = size.x;
		windowHeight = size.y;
		
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		//----------detect device setting and adapt environment
		
		myGallery = (LinearLayout)findViewById(R.id.mygallery);
		myGalleryParent = (HorizontalScrollView)findViewById(R.id.myGalleryParent);
		
		myGallery.addView(insertPhoto("https://fbcdn-sphotos-c-a.akamaihd.net/hphotos-ak-ash4/392427_10151467158878197_235430490_n.jpg"));
		myGallery.addView(insertPhoto("https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-ash3/535233_3545266959794_88025146_n.jpg"));
		myGallery.addView(insertPhoto("https://fbcdn-sphotos-a-a.akamaihd.net/hphotos-ak-prn1/934841_154301778075413_1541031409_n.jpg"));
		myGallery.addView(insertPhoto("https://fbcdn-sphotos-c-a.akamaihd.net/hphotos-ak-ash4/428566_10200546157390377_1044035448_n.jpg"));
		
		myGallery.addView(insertPhoto("https://fbcdn-sphotos-a-a.akamaihd.net/hphotos-ak-ash4/421861_10150847049018275_1388003305_n.jpg"));
		myGallery.addView(insertPhoto("https://fbcdn-sphotos-h-a.akamaihd.net/hphotos-ak-snc7/3848_103739568274_4370004_n.jpg"));
	
		//myGalleryParent.scrollTo(200, 0);
		
		//-------load JSON
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("name", "MAMAMA"));
        
		JSONObject json = getJSONfromURL("http://10.0.2.2/jsontry.php?name=HAHAHA", nameValuePairs);
		try {
			JSONArray  jsoncontacts = json.getJSONArray("contacts");
					
				for(int i=0;i < jsoncontacts.length();i++){						
			
			        	JSONObject e = jsoncontacts.getJSONObject(i);
			        	
			        	Toast.makeText(getApplicationContext(), e.getString("name") , Toast.LENGTH_LONG).show();
				}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//-------load JSON
		
		//Toast.makeText(getApplicationContext(), String.valueOf(myGallery.getChildCount()) , Toast.LENGTH_LONG).show();
        
		//-------create and load background song
		mediaPlayer = MediaPlayer.create(
		    this,
		    Uri.parse("http://translate.google.com/translate_tts?tl=en&q=%22hello%20world%20pon%20pon%20pon%20ha%22"));
		mediaPlayer.start();
		mediaPlayer.setLooping(true);
		//-------create and load background song
	}
	
	public static JSONObject getJSONfromURL(String url,List<NameValuePair> postDatas ){

		//initialize
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		//http post
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			
	        httppost.setEntity(new UrlEncodedFormEntity(postDatas));
			
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		}catch(Exception e){
			Log.e("log_tag", "Error in http connection "+e.toString());
		}

		//convert response to string
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
		}catch(Exception e){
			Log.e("log_tag", "Error converting result "+e.toString());
		}

		//try parse the string to a JSON object
		try{
	        	jArray = new JSONObject(result);
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());
		}

		return jArray;
	} 

	
	@Override
	protected void onDestroy() {Log.v("IMAGESIZE","DESTROY");
		mediaPlayer.reset();
		mediaPlayer.release();
		
		super.onDestroy();
	}
	
	@Override
	protected void onStop() {Log.v("IMAGESIZE","STOP");
		if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
		}
		
		super.onStop();
	}
	
	@Override
	protected void onPause() {Log.v("IMAGESIZE","PAUSE");
		if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
		}
		
		super.onPause();
	}
	
	@Override
	protected void onResume() {Log.v("IMAGESIZE","RESUME");
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!mediaPlayer.isPlaying()){
			mediaPlayer.start();
		}
		
		super.onResume();
	}

	View insertPhoto(String imgUrl){
		
		int thisImageHeight = 0;
		int thisImageWidth = 0;
		boolean newImage = true;
		
		LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setLayoutParams(new LayoutParams(windowWidth, windowHeight));
	     layout.setGravity(Gravity.TOP);
	     
	     ImageView imageView = new ImageView(getApplicationContext());
	     imageView.setLayoutParams(new LayoutParams(windowWidth, windowHeight));
	     imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	     
	     String[] imgUrlSeparated = imgUrl.split("/");
	     String imgUrlFilename = imgUrlSeparated[imgUrlSeparated.length - 1];
	     
	     //----------detect if image loaded before?
	     File thisImage = new File(folderPath, imgUrlFilename);
	     if(thisImage.exists()) {
	    	 newImage = false;
	    	 imgUrl = folderPath + imgUrlFilename;
	    	 Log.v("IMAGESZIE","LOCAL : " + imgUrl);
	     }
	     //----------detect if image loaded before?

	     try {
	     
	     Bitmap bitmap;
	    	 
	     if(newImage){
	    	 bitmap = BitmapFactory.decodeStream((InputStream)new URL(imgUrl).getContent());
	     }else{
	    	 bitmap = BitmapFactory.decodeFile(imgUrl);
	     }
	     
	     thisImageHeight = bitmap.getHeight();
	     thisImageWidth = bitmap.getWidth();
	     
	     if(thisImageHeight < thisImageWidth){
	    	 Bitmap resizedbitmap=Bitmap.createScaledBitmap(bitmap, windowHeight, windowWidth, true);
	    	 
	    	 Matrix matrix = new Matrix();
	    	 matrix.postRotate(90);
	    	 Bitmap rotatedbitmap = Bitmap.createBitmap(resizedbitmap, 0, 0, 
	    			 resizedbitmap.getWidth(), resizedbitmap.getHeight(), 
	    	                               matrix, true);
	    	 
	    	 imageView.setImageBitmap(rotatedbitmap);
	    	 //Log.v("IMAGESZIE", windowWidth + "x" + windowHeight + " = " + thisImageHeight + "x" + thisImageWidth + " = " + resizedbitmap.getHeight() + "x" + resizedbitmap.getWidth());
	     }else{
	    	 Bitmap resizedbitmap=Bitmap.createScaledBitmap(bitmap, windowWidth, windowHeight, true);
	    	 imageView.setImageBitmap(resizedbitmap);
	    	 //Log.v("IMAGESZIE", windowWidth + "x" + windowHeight + " = " + thisImageHeight + "x" + thisImageWidth + " = " + resizedbitmap.getHeight() + "x" + resizedbitmap.getWidth());
	     }
	     
	     if(newImage == true){
	    	 try {
	    	       FileOutputStream out = new FileOutputStream(folderPath + imgUrlFilename);
	    	       bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);Log.v("IMAGESIZE","CREATE FILE");
	    	} catch (Exception e) {
	    	       e.printStackTrace();
	    	}
	    	 
	    	 /*OutputStream fOut = null;
	    	 File file = new File(folderPath, imgUrlFilename);
	    	 fOut = new FileOutputStream(file);

	    	 bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
	    	 fOut.flush();
	    	 fOut.close();*/
	    	 
	     }
	     
	     }catch (MalformedURLException e) {
		  e.printStackTrace();
		} catch (IOException e) {
		  e.printStackTrace();
		}

	     
	     layout.addView(imageView);
     return layout;
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public String createFolder(String path, String filename) {

	    File mydir = context.getDir(path, Context.MODE_PRIVATE);
	    File fileWithinMyDir = new File(mydir, filename);
	    try {
			FileOutputStream out = new FileOutputStream(fileWithinMyDir);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //Use the stream as usual to write into the file
	    
	    return mydir.toString();
	}
	
	/*public void clickCheckbox(View v) {
		

    	AlertDialog.Builder alert = new AlertDialog.Builder(context);
    	alert.setTitle("Alert DIalog With EditText"); //Set Alert dialog title here
    	alert.setMessage("Enter your Name Here"); //Message here

    	alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    		MainActivity.this.finish();
    	  }
    	});

    	alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int whichButton) {
    		  dialog.cancel();
    	  }
    	});
    	AlertDialog alertDialog = alert.create();
    	alertDialog.show();

		
    }
	
	public void initially() {
		
		imageUrl = "https://fbcdn-sphotos-c-a.akamaihd.net/hphotos-ak-ash4/392427_10151467158878197_235430490_n.jpg";
    	try {
		  ImageView i = (ImageView)findViewById(R.id.imageView1);
		  Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent());
		  i.setImageBitmap(bitmap); 
		} catch (MalformedURLException e) {
		  e.printStackTrace();
		} catch (IOException e) {
		  e.printStackTrace();
		}
		
		
    }*/
}