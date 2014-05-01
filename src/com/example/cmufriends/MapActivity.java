package com.example.cmufriends;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;

public class MapActivity extends Activity {

	private GoogleMap map;
	private String mapType;
	private ArrayList<ListUser> users;
	private ListUser currentUser;
	private ListUser otherUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		Bundle b = getIntent().getExtras();
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mapType = b.getString("mapType");
		currentUser = (ListUser) b.getParcelableArrayList("currentUser").get(0);
		addUserMarker(currentUser);
		if (mapType.equals("AllUsers")) {
			users = b.getParcelableArrayList("users");
			addAllMarkers();
			moveMapToUser(8);
		} else if (mapType.equals("TwoUsers")) {
			otherUser = (ListUser) b.getParcelableArrayList("otherUser").get(0);
			addUserMarker(otherUser);
			showRoute();
			moveMapToUser(12);
		}
	}

	private void showRoute() {
	}

	private void moveMapToUser(int zoom) {
		ParseGeoPoint userLoc = currentUser.getLocation();
		LatLng userL = new LatLng(userLoc.getLatitude(), userLoc.getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(userL, zoom));
	}

	private void addAllMarkers() {
		for (ListUser user : users) {
			addUserMarker(user);
		}
	}

	private void addUserMarker(ListUser user) {
		ParseGeoPoint userLoc = user.getLocation();
		LatLng userPosition = new LatLng(userLoc.getLatitude(),
				userLoc.getLongitude());
		MarkerOptions userOptions = new MarkerOptions().position(userPosition);
		userOptions.title(user.name);
		map.addMarker(userOptions);
	}

}
