package location;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import login.Session;


import org.json.JSONArray;
import org.json.JSONObject;

import com.pdsd.project.main.Common;
import com.pdsd.project.main.R;

import comunication.ServerTask;
import comunication.ServerTaskObject;
import comunication.UploadServerTask;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class UpdateFragment extends Fragment {

	public static final int SELECT_FILE_CODE=1;
	public static final int RESULT_ERROR = 3;
	
	static LinearLayout updateLayout;
	RadioGroup typeRG;
	String updateType="1";
	EditText newReview;
	EditText videoLink;
	
	static String fileName;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
	        updateLayout = (LinearLayout) inflater.inflate(R.layout.updates_loc_list, container, false);
			String locationId = LocationActivity.locationId;
			
			Map<String,String> arguments = new HashMap<String,String>();
			arguments.put("type", "3");
			arguments.put("location_id", locationId);
			ServerTask serverTask = new ServerTask("get_update.php", arguments, getActivity());
			serverTask.execute();
			typeRG=	(RadioGroup)updateLayout.findViewById(R.id.radioType);
			newReview = (EditText)updateLayout.findViewById(R.id.review);
			videoLink = (EditText)updateLayout.findViewById(R.id.video_link);
			
			Button add = (Button) updateLayout.findViewById(R.id.add);
			add.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					String review = newReview.getText().toString();
					try {
						review=URLEncoder.encode(review, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					if (review == null || review.isEmpty()){
						
						Toast.makeText(getActivity(), "Review empty!",Toast.LENGTH_SHORT).show();
					}
					else{
						Map<String,String> arguments = new HashMap<String,String>();
						arguments.put("type","1");
						if(updateType.equals("1")){
							
						}else {
							if(updateType.equals("2")){
								String photoUrl = "http://happy-box.ro/travelhelper/img/"+fileName;
								try{
									photoUrl=URLEncoder.encode(photoUrl,"UTF-8");
								}catch(UnsupportedEncodingException e){
									e.printStackTrace();
								}
								arguments.put("url",photoUrl);
							}else{
								String videoUrl = videoLink.getText().toString();
								try{
									videoUrl=URLEncoder.encode(videoUrl,"UTF-8");
								}catch(UnsupportedEncodingException e){
									e.printStackTrace();
								}
								arguments.put("url",videoUrl);
							}
						}
						
						arguments.put("user_id", Session.getUserId(getActivity()));
						arguments.put("location_id", LocationActivity.locationId);
						arguments.put("update_type", updateType);
						arguments.put("update_text", review);
						ServerTaskObject serverTask2 = new ServerTaskObject("post_new.php", arguments, getActivity());
						serverTask2.execute();
					}
					
				}
			});
			
			typeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					switch(checkedId){
						case R.id.radioVideo :{
							updateLayout.findViewById(R.id.select_photo)
										.setVisibility(View.GONE);
							updateLayout.findViewById(R.id.video_link)
										.setVisibility(View.VISIBLE);
							updateType="3";
						}break;
						case R.id.radioImage :{
							updateLayout.findViewById(R.id.select_photo)
										.setVisibility(View.VISIBLE);
							updateLayout.findViewById(R.id.video_link)
										.setVisibility(View.GONE);	
							updateType="2";
						}break;
						default :{
							updateLayout.findViewById(R.id.select_photo)
										.setVisibility(View.GONE);
							updateLayout.findViewById(R.id.video_link)
										.setVisibility(View.GONE);
							updateType="1";
						}break;
					
					}
					
				}
			});
			
			Button selectPhoto = (Button) updateLayout.findViewById(R.id.select_photo);
			selectPhoto.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					openGallery(SELECT_FILE_CODE);
					
				}
			});
			
		return updateLayout;
		
	}
	public void openGallery(int req_code){

        Intent intent = new Intent();

        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent,"Select file to upload "), req_code);

   }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		
			Uri selectedImageUri = data.getData();
			switch(requestCode ){
				case SELECT_FILE_CODE : {
					String path = getPath(selectedImageUri);
					UploadServerTask ust = new UploadServerTask(path,getActivity());
					ust.execute();
				}break;
			}
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public static void onPostExecute(Activity act, JSONArray result) {
		ListView updateList = (ListView) act.findViewById(R.id.updates_list);
		updateList.setAdapter(new UpdateListAdapter(act,result));
	}
	
	public static void onPostExecute(Activity act, JSONObject result) {
		try {
			Common.printError(act, result.getString("message"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void onPostExecute(Activity act , String result){
		fileName = result;
		((Button)updateLayout.findViewById(R.id.select_photo)).setText(fileName);
		((Button)updateLayout.findViewById(R.id.select_photo)).setEnabled(false);
	}
		
	 public String getPath(Uri uri) {

	        String[] projection = { MediaStore.Images.Media.DATA };

	        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

	        cursor.moveToFirst();

	        return cursor.getString(column_index);

	    }
	
}