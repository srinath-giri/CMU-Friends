package com.example.cmufriends;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LocationCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class HomeActivity extends Activity {

	ListView people;
	String username;
	ParseGeoPoint userLoc;
	List<ParseObject> results;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Parse.initialize(this, "kw8uUVhfdP3VG3tt5wh5eAGcUqB0EdQXNEPNP0Y4",
				"ir2NtarhYtNDV8VwDUCAG0q1dzAOF6zBL0a8gQEx");
		username = getIntent().getExtras().getString("username");
		people = (ListView) findViewById(R.id.peopleList);
		populatePeopleList();
	}

	private void populatePeopleList() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
		query.selectKeys(Arrays.asList("username", "location"));
		try {
			results = query.find();
		} catch (ParseException e) {
			showToast(e.getMessage());
			e.printStackTrace();
		}
		ParseGeoPoint.getCurrentLocationInBackground(1000,
				new LocationCallback() {

					@Override
					public void done(ParseGeoPoint p, ParseException e) {
						if (p != null) {
							userLoc = p;
							sortPeopleList();
						} else {
							showToast("Unable to get User Location because: "
									+ e.getMessage());
						}
					}
				});
	}

	protected void sortPeopleList() {
		ArrayList<ListUser> users = new ArrayList<ListUser>();
		for(ParseObject p : results){
			if(!p.getString("username").equals(username)){
				String u = p.getString("username");
				Double d = userLoc.distanceInMilesTo(p.getParseGeoPoint("location"));
				ListUser user = new ListUser(d, u);
				users.add(user);
			}
		}
		
		Collections.sort(users, new Comparator<ListUser>() {
			@Override
			public int compare(ListUser lhs, ListUser rhs) {
				return lhs.distance.compareTo(rhs.distance);
			}
		});
		addToListView(users);
	}

	private void addToListView(ArrayList<ListUser> users) {
		PeoplesListAdapter ad = new PeoplesListAdapter(this, users);
		people.setAdapter(ad);
		people.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ListUser user = (ListUser) people.getItemAtPosition(arg2);
				goToProfile(user);
			}
		});
	}

	protected void goToProfile(ListUser user) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
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
