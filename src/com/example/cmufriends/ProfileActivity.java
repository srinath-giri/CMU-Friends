package com.example.cmufriends;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class ProfileActivity extends Activity implements OnClickListener {

	private Button buttonVisitFacebook;
	private Button buttonSendFacebookMessage;
	private Button buttonShowOnMap;
	private TextView textViewProfileName;
	private TextView textViewProfileGender;
	private TextView textViewProfileFacebookID;
	private TextView textViewProfileEmail;
	
	private String andrewID = "yuzhang1";
	private String gender = "male";
	private String facebookID = "cheng.lap.9";
	private String email = "yuzhang@andrew.cmu.edu";
	private String latitude = "49.00";
	private String longditude = "49.00";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		textViewProfileName = (TextView) findViewById(R.id.textViewProfileName);
		textViewProfileGender = (TextView) findViewById(R.id.textViewProfileGender);
		textViewProfileFacebookID = (TextView) findViewById(R.id.textViewProfileFacebookID);
		textViewProfileEmail = (TextView) findViewById(R.id.textViewProfileEmail);
		textViewProfileName.setText(andrewID);
		textViewProfileGender.setText(gender);
		textViewProfileFacebookID.setText(facebookID);
		textViewProfileEmail.setText(email);
		
		buttonVisitFacebook = (Button) findViewById(R.id.buttonVisitFacebook);
		buttonVisitFacebook.setOnClickListener(this);
		
		buttonSendFacebookMessage = (Button) findViewById(R.id.buttonSendFacebookMessage);
		buttonSendFacebookMessage.setOnClickListener(this);
		
		buttonShowOnMap = (Button) findViewById(R.id.buttonShowOnMap);
		buttonShowOnMap.setOnClickListener(this);
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

	}

	private void sendFacebookMessage() {
		
	}

	private void showOnMap() {
		
	}

}
