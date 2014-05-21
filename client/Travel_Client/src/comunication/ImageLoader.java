package comunication;

import java.io.InputStream;
import java.net.HttpURLConnection;
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

	         InputStream inputStream = connection.getInputStream();

			 bmp = BitmapFactory.decodeStream(inputStream);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

}
