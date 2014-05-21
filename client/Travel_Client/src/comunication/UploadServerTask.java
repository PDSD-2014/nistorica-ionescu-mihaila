package comunication;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import location_info_page.UpdateFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

public class UploadServerTask extends AsyncTask<Void, Void, String>  {

	private String filePath;
	private ProgressDialog progress = null;
	private String dialogTitle = null;
	private Activity act = null;
	private UploadServerTask task = null;
	String errorMessage = null;
	
	public UploadServerTask (String path,Activity act){
		
		this.filePath = path;
		this.act = act;
		this.task = this;
	}
	
	@Override
	protected void onPreExecute () {

		/* Initialize Progress Dialog */
		progress = new ProgressDialog(act);
		
		if (this.dialogTitle != null) {
			progress.setTitle(this.dialogTitle);
		}
		
		progress.setMessage("Uploading photo to server");
		
		progress.setIndeterminate(true);
		progress.setCancelable(false);
		
		/* Button Cancel */
		progress.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {

		        /* Cancel task */
		    	if (task != null) {
		    		task.cancel(true);
		    	}
		    	
		        errorMessage = "Action canceled.";
		    }
		});
		
		progress.show();
		
		if (!connectionTest.isConnected(act)) {
			this.cancel(true);
		}
		
	}
	@Override
	protected String doInBackground(Void... arg0) {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		String pathToOurFile = this.filePath;
		String urlServer = "http://54.72.182.209/server/upload_photo.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary =  "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1*1024*1024;

		try
		{
			FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );
	
			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();
	
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
	
			// Enable POST method
			connection.setRequestMethod("POST");
	
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
	
			outputStream = new DataOutputStream( connection.getOutputStream() );
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
			outputStream.writeBytes(lineEnd);
	
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
	
			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	
			while (bytesRead > 0)
			{
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}
		
				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		
				// Responses from the server (code and message)
				 connection.getResponseCode();
				 connection.getResponseMessage();
		
				fileInputStream.close();
				outputStream.flush();
				outputStream.close();
		}
		catch (Exception ex){
		//Exception handling
		}
		return new File(pathToOurFile).getName();
	}

	@Override
	protected void onPostExecute(String result) {

		UpdateFragment.onPostExecute(act ,result);
		progress.dismiss();
	}

}
