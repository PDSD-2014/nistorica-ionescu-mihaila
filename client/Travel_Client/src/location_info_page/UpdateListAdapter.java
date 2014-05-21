package location_info_page;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;

import com.pdsd.project.main.ImageViewActivity;
import com.pdsd.project.main.R;
import com.pdsd.project.main.UserProfileActivity;

import comunication.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateListAdapter extends BaseAdapter {

	private Context mContext;
	private JSONArray jsonArray;
	private String urlMedia,userId;
	Bitmap bmp;
	public UpdateListAdapter(Context c,JSONArray array) {
		mContext = c;
		this.jsonArray = array;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return jsonArray.length();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.updates_list_entry , null);
			Log.d("position", String.valueOf(position));
			TextView user = (TextView)rowView.findViewById(R.id.user);
			TextView review = (TextView)rowView.findViewById(R.id.review);
			ImageView image = (ImageView)rowView.findViewById(R.id.imageView);
			int flagVideo = -1;
			try {
				review.setText(jsonArray.getJSONObject(position).getString("text"));
				user.setText(jsonArray.getJSONObject(position).getString("username"));
				flagVideo= jsonArray.getJSONObject(position).getInt("type");
				urlMedia = jsonArray.getJSONObject(position).getString("url");
				userId = jsonArray.getJSONObject(position).getString("user_id");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//update list
			try {
				if (flagVideo == 3){
					image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.video));
					image.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Toast.makeText(mContext, "click video",Toast.LENGTH_SHORT).show();
							mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlMedia)));
						}
					});
				}else if (flagVideo == 2){
					image.setVisibility(View.VISIBLE);
					this.bmp = new ImageLoader().execute(urlMedia).get();
					image.setImageBitmap(bmp);
					image.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Toast.makeText(mContext, "click imagine",Toast.LENGTH_SHORT).show();
							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
							byte[] byteArray = stream.toByteArray();
							Intent intent = new Intent(mContext,ImageViewActivity.class);
							intent.putExtra("image",byteArray);
							mContext.startActivity(intent);
						}
					});
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			user.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,UserProfileActivity.class);
					intent.putExtra("username", userId);
					mContext.startActivity(intent);
				}
			
			});	
		}
		return rowView;
	}

}
