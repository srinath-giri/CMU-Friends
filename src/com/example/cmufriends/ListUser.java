package com.example.cmufriends;

import java.io.Serializable;

import com.parse.ParseGeoPoint;

public class ListUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -786586799834774767L;
	Double distance;
	String username;
	String facebookId;
	ParseGeoPoint location;
	String name;

	public ListUser(Double d, String u, String n, String f, ParseGeoPoint l) {
		this.distance = Double.valueOf(d);
		this.name = new String(n);
		this.username = new String(u);
		this.facebookId = new String(f);
		this.location = new ParseGeoPoint(l.getLatitude(), l.getLongitude());
	}
}