package com.example.cmufriends;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.parse.LocationCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class HomeActivity extends Activity {

	PullToRefreshListView people;
	String username;
	ListUser currentUser;
	List<ParseUser> results;
	ArrayList<ListUser> users;
	Button showMapButton;
	ProgressBar locationSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		username = getIntent().getExtras().getString("username");
		people = (PullToRefreshListView) findViewById(R.id.peopleList);
		showMapButton = (Button) findViewById(R.id.homeShowMapButton);
		locationSpinner = (ProgressBar) findViewById(R.id.locationSpinner);
		showMapButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMap();
			}

		});
		populatePeopleList();
	}

	private void showMap() {
		Intent i = new Intent(this, MapActivity.class);
		i.putParcelableArrayListExtra("users", users);
		i.putExtra("mapType", "AllUsers");
		ArrayList<ListUser> currentUserAsList = new ArrayList<ListUser>(
				Arrays.asList(currentUser));
		i.putParcelableArrayListExtra("currentUser", currentUserAsList);
		startActivity(i);
	}

	private void populatePeopleList() {
		showSpinner();
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.selectKeys(Arrays.asList("username", "location", "facebookID",
				"name"));
		try {
			results = query.find();
			initPeopleList(results);
		} catch (ParseException e) {
			showToast("Unable to get CMU friends because: " + e.getMessage());
			e.printStackTrace();
		}
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		ParseGeoPoint.getCurrentLocationInBackground(60000, criteria,
				new LocationCallback() {

					@Override
					public void done(ParseGeoPoint p, ParseException e) {
						if (p != null) {
							currentUser.setLocation(p);
							updateUserLocation(p);
							sortPeopleList();
						} else {
							showToast("Unable to get User Location because: "
									+ e.getMessage());
						}
					}
				});
	}

	private void showSpinner() {
		showMapButton.setEnabled(false);
		showMapButton.setVisibility(View.INVISIBLE);
		locationSpinner.setEnabled(true);
		locationSpinner.setVisibility(View.VISIBLE);
	}

	private void hideSpinner() {
		showMapButton.setEnabled(true);
		showMapButton.setVisibility(View.VISIBLE);
		locationSpinner.setEnabled(false);
		locationSpinner.setVisibility(View.INVISIBLE);

	}

	protected void updateUserLocation(ParseGeoPoint p) {
		ParseUser user = ParseUser.getCurrentUser();
		user.put("location", p);
		user.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e != null) {
					showToast("Unable to update User Location because: "
							+ e.getMessage());
				}
			}
		});
	}

	private void initPeopleList(List<ParseUser> results) {
		users = new ArrayList<ListUser>();
		for (ParseObject p : results) {
			if (!p.getString("username").equals(username)) {
				String u = p.getString("username");
				ParseGeoPoint loc = new ParseGeoPoint(0, 0);
				if (p.containsKey("location")) {
					loc = p.getParseGeoPoint("location");
				}
				Double d = Double.valueOf(0);
				String n = p.getString("name");
				String f = p.getString("facebookID");
				ListUser user = new ListUser(d, u, n, f, loc);
				users.add(user);
			} else {
				String u = p.getString("username");
				Double d = Double.valueOf(0);
				String n = p.getString("name");
				String f = p.getString("facebookID");
				currentUser = new ListUser(d, u, n, f, null);
			}
		}
		addToListView(users);
	}

	protected void sortPeopleList() {
		for (ListUser u : users) {
			u.distance = currentUser.getLocation().distanceInMilesTo(
					u.getLocation());
		}
		Collections.sort(users, new Comparator<ListUser>() {
			@Override
			public int compare(ListUser lhs, ListUser rhs) {
				return lhs.distance.compareTo(rhs.distance);
			}
		});
		addToListView(users);
		hideSpinner();
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
		people.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				populatePeopleList();
			}
		});
	}

	protected void goToProfile(ListUser user) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putParcelableArrayListExtra("users", users);
		ArrayList<ListUser> currentUserAsList = new ArrayList<ListUser>(
				Arrays.asList(currentUser));
		i.putParcelableArrayListExtra("currentUser", currentUserAsList);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
