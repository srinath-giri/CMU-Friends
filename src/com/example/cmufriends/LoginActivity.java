package com.example.cmufriends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	Button signIn;
	Button signUp;
	EditText andrewId;
	EditText password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Parse.initialize(this, "kw8uUVhfdP3VG3tt5wh5eAGcUqB0EdQXNEPNP0Y4", "ir2NtarhYtNDV8VwDUCAG0q1dzAOF6zBL0a8gQEx");
		signIn = (Button) findViewById(R.id.SignInButton);
		signUp = (Button) findViewById(R.id.SignUpButton);
		andrewId = (EditText) findViewById(R.id.LoginAndrewId);
		andrewId.requestFocus();
		password = (EditText) findViewById(R.id.LoginPassword);

		signIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String andrewIdString = andrewId.getEditableText().toString();
				String passwordString = password.getEditableText().toString();

				// hide the keyboard.
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(andrewId.getWindowToken(), 0);

				ParseUser.logInInBackground(andrewIdString, passwordString,
						new LogInCallback() {
							@Override
							public void done(ParseUser user, ParseException e) {
								if (user != null) {
									// Hooray! The user is logged in.
									loginUser(user);

								} else {
									// Signup failed. Look at the ParseException
									// to see what happened.
									showToast(e.getMessage());
								}
							}
						});
			}
		});
	}

	private void loginUser(ParseUser user) {
		Intent i = new Intent(this, HomeActivity.class);
		i.putExtra("username", user.getUsername());
		startActivity(i);
		finish();
	}
	
	public void showToast(String msg) {
		Toast toast = new Toast(getApplicationContext());
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 0);
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_layout,
				(ViewGroup) findViewById(R.id.toast_layout_root));
		toast.setView(layout);
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(msg);
		toast.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
