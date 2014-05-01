package com.example.cmufriends;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
					showToast("Successfully registered!");
				} else {
					showToast("Unable to register because " + e.getMessage());
				}
			}
		});
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

}
