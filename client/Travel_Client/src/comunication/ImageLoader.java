package comunication;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {


	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap bmp = null;
		URL url;
		
		try {
			 Log.d("image", params[0]);
			 url = new URL(params[0]);
			 HttpURLConnection connection= (HttpURLConnection)url.openConnection();
//	         connection.setDoInput(true);
//	         connection.connect();
	         InputStream inputStream = connection.getInputStream();
	         Log.d("asd", "inputstream");
			 bmp = BitmapFactory.decodeStream(inputStream);
			 Log.d("bmp", bmp.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bmp;
	}
	
	
}
