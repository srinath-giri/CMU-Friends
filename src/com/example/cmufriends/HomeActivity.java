package com.example.cmufriends;

import com.parse.Parse;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class HomeActivity extends Activity {
	
	ListView people;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Parse.initialize(this, "kw8uUVhfdP3VG3tt5wh5eAGcUqB0EdQXNEPNP0Y4", "ir2NtarhYtNDV8VwDUCAG0q1dzAOF6zBL0a8gQEx");
		people = (ListView) findViewById(R.id.peopleList);
		populatePeopleList();
	}

	private void populatePeopleList() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
