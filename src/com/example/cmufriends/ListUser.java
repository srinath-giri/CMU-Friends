package com.example.cmufriends;

public class ListUser{
	Double distance;
	String username;
	
	public ListUser(Double d, String u){
		this.distance = Double.valueOf(d);
		this.username = new String(u);
	}
}