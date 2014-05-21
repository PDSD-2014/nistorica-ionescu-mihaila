package location_info_page;

import org.json.JSONArray;
import org.json.JSONException;

import com.pdsd.project.main.R;
import com.pdsd.project.main.UserProfileActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RatingListAdapter extends BaseAdapter {

	private Context mContext;
	JSONArray jsonArray;
	private String userId;
	
	public RatingListAdapter(Context c,JSONArray array) {
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
		TextView user,rating,descr,date;
		Log.d("position", String.valueOf(position));
		if (rowView == null){
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.rating_list_entry , null);
				
			user = (TextView)rowView.findViewById(R.id.user);
			rating = (TextView)rowView.findViewById(R.id.rating);
			descr = (TextView)rowView.findViewById(R.id.description);
			date = (TextView)rowView.findViewById(R.id.rating_date);
			
		
			try{
	
				user.setText(jsonArray.getJSONObject(position).getString("username"));
				
				rating.setText(jsonArray.getJSONObject(position).getString("value"));
				
				int rateVal = Integer.parseInt(jsonArray.getJSONObject(position).getString("value"));
				
				if(rateVal >=8){
					rating.setTextColor(Color.rgb(0,255,0));
				}else{
					if(rateVal<=4){
						rating.setTextColor(Color.rgb(255, 0,0));
					}else{
						rating.setTextColor(Color.rgb(255, 128,0));
					}
				}
				
				descr.setText(jsonArray.getJSONObject(position).getString("body"));
				date.setText(jsonArray.getJSONObject(position).getString("rating_date"));
			}catch(JSONException je){
				je.printStackTrace();
			}
			
			try {
				userId = jsonArray.getJSONObject(position).getString("user_id");
			} catch (JSONException e) {
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
