package com.example.cmufriends;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PeoplesListAdapter extends BaseAdapter{
	
	private ArrayList<ListUser> listUsers;
	private LayoutInflater layout;
	
	public PeoplesListAdapter(Context c, ArrayList<ListUser> u){
		this.layout = LayoutInflater.from(c);
		this.listUsers = u;
	}

	@Override
	public int getCount() {
		if(listUsers != null){
			return listUsers.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return listUsers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	static class ViewHolder {
		TextView andrewId;
		TextView distance;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			view = layout.inflate(R.layout.tab_view, null);
			holder = new ViewHolder();
			holder.andrewId = (TextView) view.findViewById(R.id.tabAndrewId);
			holder.distance = (TextView) view.findViewById(R.id.tabDistance);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.andrewId.setText(listUsers.get(pos).name);
		String dis = new DecimalFormat("#.##").format(listUsers.get(pos).distance);
		holder.distance.setText( dis +" miles");
		return view;
	}

}
