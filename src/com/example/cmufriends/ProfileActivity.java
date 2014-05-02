package com.example.cmufriends;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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

import com.facebook.widget.ProfilePictureView;

public class ProfileActivity extends Activity implements OnClickListener {

	private Button buttonVisitFacebook;
	private Button buttonSendFacebookMessage;
	private Button buttonShowOnMap;
	private TextView textViewProfileName;
	private TextView textViewProfileGender;
	private TextView textViewProfileFacebookID;
	private TextView textViewProfileEmail;

	private ListUser user;
	private ListUser currentUser;
	private String facebookNumberID = "";
	private String gender = "";
	private ProfilePictureView profPic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		textViewProfileName = (TextView) findViewById(R.id.textViewProfileName);
		textViewProfileGender = (TextView) findViewById(R.id.textViewProfileGender);
		textViewProfileFacebookID = (TextView) findViewById(R.id.textViewProfileFacebookID);
		textViewProfileEmail = (TextView) findViewById(R.id.textViewProfileEmail);
		profPic = (ProfilePictureView) findViewById(R.id.profilePicture);

		user = (ListUser) getIntent().getExtras()
				.getParcelableArrayList("user").get(0);
		currentUser = (ListUser) getIntent().getExtras()
				.getParcelableArrayList("currentUser").get(0);

		textViewProfileName.setText(user.name);
		textViewProfileGender.setText(gender);
		textViewProfileFacebookID.setText(user.facebookId);
		textViewProfileEmail.setText(user.username + '@'
				+ getString(R.string.andrew_domain));

		buttonVisitFacebook = (Button) findViewById(R.id.buttonVisitFacebook);
		buttonSendFacebookMessage = (Button) findViewById(R.id.buttonSendFacebookMessage);

		buttonShowOnMap = (Button) findViewById(R.id.buttonShowOnMap);
		buttonShowOnMap.setOnClickListener(this);

		profPic.setProfileId(user.facebookId);
		if (facebookNumberID.equals("")) {
			new SocialTask().getFacebookProfile();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == buttonVisitFacebook) {
			visitFacebook();
		} else if (v == buttonSendFacebookMessage) {
			sendFacebookMessage();
		} else if (v == buttonShowOnMap) {
			showOnMap();
		}
	}

	private void visitFacebook() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/"
				+ facebookNumberID)));
	}

	private void sendFacebookMessage() {
		startActivity(new Intent(Intent.ACTION_VIEW,
				Uri.parse("fb://messaging/" + facebookNumberID)));
	}

	private void showOnMap() {
		Intent i = new Intent(this, MapActivity.class);
		i.putExtra("mapType", "TwoUsers");
		ArrayList<ListUser> currentUserAsList = new ArrayList<ListUser>(
				Arrays.asList(currentUser));
		i.putParcelableArrayListExtra("currentUser", currentUserAsList);
		ArrayList<ListUser> otherUserAsList = new ArrayList<ListUser>(
				Arrays.asList(user));
		i.putParcelableArrayListExtra("otherUser", otherUserAsList);
		startActivity(i);
	}

	class SocialTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse;
			String jsonResult = null;
			try {
				httpResponse = httpClient.execute(new HttpGet(uri[0]));
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream buffer = new ByteArrayOutputStream();
					httpResponse.getEntity().writeTo(buffer);
					buffer.close();
					jsonResult = buffer.toString();
				}
			} catch (IOException e) {
				Log.d(getString(R.string.title_activity_profile),
						e.getMessage());
			} catch (Exception e) {
				Log.d(getString(R.string.title_activity_profile),
						e.getMessage());
			}
			return jsonResult;
		}

		@Override
		protected void onPostExecute(String json) {
			super.onPostExecute(json);
			if (json != null) {
				parseJSON(json);
				textViewProfileGender.setText(gender);
				buttonSendFacebookMessage
						.setOnClickListener(ProfileActivity.this);
				buttonVisitFacebook.setOnClickListener(ProfileActivity.this);
			}
		}

		private void parseJSON(String jsonResult) {
			try {
				JSONObject user = new JSONObject(jsonResult);
				facebookNumberID = user.getString("id");
				gender = user.getString("gender");
			} catch (JSONException e) {
				Log.d(getString(R.string.title_activity_profile),
						e.getMessage());
			}
			Log.d(getString(R.string.title_activity_profile), facebookNumberID);
		}

		private void getFacebookProfile() {
			execute("http://graph.facebook.com/" + user.facebookId
					+ "?fields=id,gender");
		}
	}

}
