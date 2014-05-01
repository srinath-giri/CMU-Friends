package com.example.cmufriends;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseGeoPoint;

public class ListUser implements Serializable, Parcelable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -786586799834774767L;
	Double distance;
	String username;
	String facebookId;
	Double longitude;
	Double latitude;
	String name;

	public ListUser(Double d, String u, String n, String f, ParseGeoPoint l) {
		this.distance = Double.valueOf(d);
		this.name = new String(n);
		this.username = new String(u);
		this.facebookId = new String(f);
		if (l != null) {
			this.latitude = Double.valueOf(l.getLatitude());
			this.longitude = Double.valueOf(l.getLongitude());
		} else {
			this.latitude = Double.valueOf(0);
			this.longitude = Double.valueOf(0);
		}
	}

	public ListUser(Parcel source) {
		List<String> strings = new ArrayList<String>();
		source.readStringList(strings);
		double[] doubles = new double[3];
		source.readDoubleArray(doubles);
		this.name = strings.get(0);
		this.username = strings.get(1);
		this.facebookId = strings.get(2);
		this.distance = doubles[0];
		this.longitude = doubles[1];
		this.latitude = doubles[2];
	}

	public static final Parcelable.Creator<ListUser> CREATOR = new Creator<ListUser>() {

		@Override
		public ListUser[] newArray(int size) {
			return new ListUser[size];
		}

		@Override
		public ListUser createFromParcel(Parcel source) {
			return new ListUser(source);
		}
	};

	public void setLocation(ParseGeoPoint location) {
		this.latitude = Double.valueOf(location.getLatitude());
		this.longitude = Double.valueOf(location.getLongitude());
	}

	public ParseGeoPoint getLocation() {
		return new ParseGeoPoint(latitude, longitude);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringList(Arrays.asList(name, username, facebookId));
		double[] arr = new double[3];
		arr[0] = distance;
		arr[1] = longitude;
		arr[2] = latitude;
		dest.writeDoubleArray(arr);
	}
}