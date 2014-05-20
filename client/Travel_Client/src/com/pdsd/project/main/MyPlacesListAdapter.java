package com.pdsd.project.main;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import location.LocationActivity;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class MyPlacesListAdapter extends BaseAdapter {

	private Context mContext;
	JSONArray jsonArray;
	Bitmap bmp;
	private String urlMedia;
	public MyPlacesListAdapter(Context c,JSONArray array) {
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

	public static Drawable LoadImageFromWebOperations(String url) {
	    try {
	        InputStream is = (InputStream) new URL(url).getContent();
	        Drawable d = Drawable.createFromStream(is, "src name");
	        return d;
	    } catch (Exception e) {
	        return null;
	    }
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		
		if (rowView == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_entry, null);
			
			ImageView image = (ImageView)rowView.findViewById(R.id.imageView);
			TextView location = (TextView)rowView.findViewById(R.id.location);
			TextView review = (TextView)rowView.findViewById(R.id.review);
			TextView date = (TextView)rowView.findViewById(R.id.date);
			TextView rating = (TextView)rowView.findViewById(R.id.location_rating);
			review.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				}
			});
			
			TextView user = (TextView)rowView.findViewById(R.id.user);
			
			
			ViewHolder holder = new ViewHolder();
			holder.image = image;
			holder.location = location;
			holder.review = review;
			holder.user = user;
			holder.date = date;
			holder.rating = rating;
			
			rowView.setTag(holder);
			
		}
		
		
		ViewHolder holder = (ViewHolder) rowView.getTag();
		int flagVideo = -1;
		try {
			holder.image.setVisibility(View.GONE);
			holder.location.setText(jsonArray.getJSONObject(position).getString("location_name"));
			String text = jsonArray.getJSONObject(position).getString("text");
			if (text.equals("null")) {
				text = "";
			}
			holder.review.setText(text);
			holder.user.setText(jsonArray.getJSONObject(position).getString("username"));
			flagVideo= jsonArray.getJSONObject(position).getInt("type");
			urlMedia = jsonArray.getJSONObject(position).getString("url");
			holder.date.setText( jsonArray.getJSONObject(position).getString("date"));
			holder.rating.setText(jsonArray.getJSONObject(position).getString("location_rating"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//set location click
		try {
			holder.loc_id = jsonArray.getJSONObject(position).getString("location_id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.location.setOnClickListener(new MyLocationOnClickListener(holder.loc_id));
		
		if (flagVideo == 3){
			holder.image.setVisibility(View.VISIBLE);
			holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.video));
			holder.image.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlMedia)));
				}
			});
		}else if (flagVideo == 2){
			holder.image.setVisibility(View.VISIBLE);
			//this.bmp = new ImageLoader().execute(urlMedia).get();
			holder.image.setImageBitmap(bmp);
			holder.image.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] byteArray = stream.toByteArray();
					Intent intent = new Intent(mContext,ImageViewActivity.class);
					intent.putExtra("image",byteArray);
					mContext.startActivity(intent);
				}
			});
		}
		
		//set user click
		try {
			holder.user_id = jsonArray.getJSONObject(position).getString("user_id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.user.setOnClickListener(new MyUserOnClickListener(holder.user_id));
		
		return rowView;
	}
	
	public static class ViewHolder{
		public TextView location;
		public ImageView image;
		public TextView user;
		public TextView review;
		public String loc_id;
		public String user_id;
		public TextView date;
		public TextView rating;
	}
	
	public class MyLocationOnClickListener implements OnClickListener
	   {

		 String myVariable;
	     public MyLocationOnClickListener(String myLovelyVariable) {
	          this.myVariable = myLovelyVariable;
	     }

	     @Override
	     public void onClick(View v)
	     {
	    	 Intent intent = new Intent(mContext, LocationActivity.class);
			 intent.putExtra("location_id", myVariable);
			 mContext.startActivity(intent);
	     }

	  };
	  
	  public class MyUserOnClickListener implements OnClickListener
	   {

		 String myVariable;
	     public MyUserOnClickListener(String myLovelyVariable) {
	          this.myVariable = myLovelyVariable;
	     }

	     @Override
	     public void onClick(View v)
	     {
	    		Intent intent = new Intent(mContext,UserProfileActivity.class);
				intent.putExtra("username", myVariable);
				mContext.startActivity(intent);
	     }

	  };
}
