package comunication;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;


public class ImageLoader extends AsyncTask<ImageLoaderObject, Void, Bitmap> {
	ImageView img_view = null;
	
	@Override
	protected Bitmap doInBackground(ImageLoaderObject... params) {
		URL url;
		Bitmap bmp = null;
		this.img_view = params[0].view;
		
		try {
			 url = new URL(params[0].url);
			 HttpURLConnection connection= (HttpURLConnection)url.openConnection();

	         InputStream inputStream = connection.getInputStream();

			 bmp = BitmapFactory.decodeStream(inputStream);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
       if (img_view != null) {
    	   img_view.setImageBitmap(result);
       }
    }

}
