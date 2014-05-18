package com.pdsd.project.main;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_view, menu);
		return true;
	}

}
