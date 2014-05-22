package comunication;

import android.widget.ImageView;

public class ImageLoaderObject {
	public ImageLoaderObject(String urlMedia, ImageView image) {
		this.url = urlMedia;
		this.view = image;
	}
	String url;
	ImageView view;
}
