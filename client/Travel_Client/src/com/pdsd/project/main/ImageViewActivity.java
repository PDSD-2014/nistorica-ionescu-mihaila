package com.pdsd.project.main;

import login.Session;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		Intent intent = getIntent();
		byte[] byteArray = intent.getByteArrayExtra("image");
		Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		ImageView image = (ImageView) findViewById(R.id.imageView);
		image.setImageBitmap(bmp);
		
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
	    case android.R.id.home:
	        this.finish();
	        return true;
	    case R.id.action_logout:
	    	Session.LogOut(this);
	    	MainActivity.goToFirstActivity(this);
	    	return true;
	    case R.id.add_location:
	    	MainActivity.goToAddLocation(this);
	    	return true;
	    case R.id.search:
			this.recreate();
			return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

}
