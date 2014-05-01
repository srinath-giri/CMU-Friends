package com.example.cmufriends;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends Activity implements OnClickListener {

	private Button buttonRegister;
	private EditText editTextAndrewID;
	private EditText editTextPassword;
	private EditText editTextName;
	private EditText editTextDepartment;
	private EditText editTextEmail;
	private EditText editTextFacebookID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		buttonRegister = (Button) findViewById(R.id.buttonRegister);
		buttonRegister.setOnClickListener(this);
		editTextAndrewID = (EditText) findViewById(R.id.editTextRegisterAndrewID);
		editTextPassword = (EditText) findViewById(R.id.editTextRegisterPassword);
		editTextName = (EditText) findViewById(R.id.editTextRegisterName);
		editTextDepartment = (EditText) findViewById(R.id.editTextRegisterDepartment);
		editTextEmail = (EditText) findViewById(R.id.editTextRegisterEmail);
		editTextFacebookID = (EditText) findViewById(R.id.editTextRegisterFacebookID);
	}

	@Override
	public void onClick(View v) {
		if (v == buttonRegister) {
			register();
		}
	}

	private void register() {
		String andrewID = editTextAndrewID.getText().toString();
		String password = editTextPassword.getText().toString();
		String name = editTextName.getText().toString();
		String department = editTextDepartment.getText().toString();
		String email = editTextEmail.getText().toString();
		String facebookID = editTextFacebookID.getText().toString();

		ParseUser user = new ParseUser();
		user.setUsername(andrewID);
		user.setPassword(password);
		user.put("name", name);
		user.put("department", department);
		user.setEmail(email);
		user.put("facebookID", facebookID);

		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
