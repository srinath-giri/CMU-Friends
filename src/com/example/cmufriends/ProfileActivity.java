package com.example.cmufriends;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends Activity implements OnClickListener {

	private Button buttonVisitFacebook;
	private Button buttonSendFacebookMessage;
	private Button buttonShowOnMap;
	private TextView textViewProfileName;
	private TextView textViewProfileGender;
	private TextView textViewProfileFacebookID;
	private TextView textViewProfileEmail;
	
	private String andrewID = "";
	private String name = "";
	private String facebookID = "";
	private String facebookNumberID = "";
	private String gender = "";
	private double latitude;
	private double longitude;

	private SocialTask socialTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		textViewProfileName = (TextView) findViewById(R.id.textViewProfileName);
		textViewProfileGender = (TextView) findViewById(R.id.textViewProfileGender);
		textViewProfileFacebookID = (TextView) findViewById(R.id.textViewProfileFacebookID);
		textViewProfileEmail = (TextView) findViewById(R.id.textViewProfileEmail);

		name = getIntent().getExtras().getString("name");
		andrewID = getIntent().getExtras().getString("andrewID");
		facebookID = getIntent().getExtras().getString("facebookID");
		latitude = getIntent().getExtras().getDouble("userLatitude");
		longitude = getIntent().getExtras().getDouble("userLongitude");

		textViewProfileName.setText(name);
		textViewProfileGender.setText(gender);
		textViewProfileFacebookID.setText(facebookID);
		textViewProfileEmail.setText(andrewID+'@'+getString(R.string.andrew_domain));
		
		buttonVisitFacebook = (Button) findViewById(R.id.buttonVisitFacebook);
		buttonSendFacebookMessage = (Button) findViewById(R.id.buttonSendFacebookMessage);
		
		buttonShowOnMap = (Button) findViewById(R.id.buttonShowOnMap);
		buttonShowOnMap.setOnClickListener(this);

		if(facebookNumberID == "") new SocialTask().getFacebookProfile();
	}

	@Override
	public void onClick(View v) {
		if (v == buttonVisitFacebook) {
			visitFacebook();
		}
		else if (v ==  buttonSendFacebookMessage) {
			sendFacebookMessage();
		}
		else if (v ==  buttonShowOnMap) {
			showOnMap();
		}
	}

	private void visitFacebook() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"+facebookNumberID)));
	}

	private void sendFacebookMessage() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://messaging/"+facebookNumberID)));
	}

	private void showOnMap() {
		
	}

	class SocialTask extends AsyncTask<String, Void, String>{

	    @Override
	    protected String doInBackground(String... uri) {
	        HttpClient httpClient = new DefaultHttpClient();
	        HttpResponse httpResponse;
	        String jsonResult = null;
	        try {
	            httpResponse = httpClient.execute(new HttpGet(uri[0]));
	            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	                httpResponse.getEntity().writeTo(buffer);
	                buffer.close();
	                jsonResult = buffer.toString();
	            }
	        } catch (IOException e) {
                Log.d(getString(R.string.title_activity_profile), e.getMessage());
	        }
	        return jsonResult;
	    }

	    @Override
	    protected void onPostExecute(String json) {
	        super.onPostExecute(json);
	        parseJSON(json);
	        textViewProfileGender.setText(gender);
	        buttonSendFacebookMessage.setOnClickListener(ProfileActivity.this);
			buttonVisitFacebook.setOnClickListener(ProfileActivity.this);
	    }

	    private void parseJSON(String jsonResult) {
            try {
				JSONObject user = new JSONObject(jsonResult);
				facebookNumberID = user.getString("id");
				gender = user.getString("gender");
			} catch (JSONException e) {
				Log.d(getString(R.string.title_activity_profile),e.getMessage());
			}
	        Log.d(getString(R.string.title_activity_profile), facebookNumberID);
	    }

		private void getFacebookProfile() {
			execute("http://graph.facebook.com/"+facebookID+"?fields=id,gender");
		}
	}
}
