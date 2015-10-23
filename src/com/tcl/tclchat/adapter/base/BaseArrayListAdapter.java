package com.tcl.tclchat.adapter.base;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tcl.tclchat.bean.FaceText;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BaseArrayListAdapter extends BaseAdapter {

	protected Context mContext;
	//用于获取布局文件的对象
	protected LayoutInflater mInflater;
	protected List<FaceText> myDatas = new ArrayList<FaceText>();
	
	public BaseArrayListAdapter(Context context, FaceText... datas) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		if (datas != null && datas.length > 0) {
			myDatas = Arrays.asList(datas);
		}
	}
	
	public BaseArrayListAdapter(Context context, List<FaceText> datas) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		if(datas != null && datas.size() > 0) {
			myDatas = datas;
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return myDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
