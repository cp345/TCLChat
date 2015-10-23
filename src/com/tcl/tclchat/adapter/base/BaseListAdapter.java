package com.tcl.tclchat.adapter.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.im.util.BmobLog;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

/**
 * @Decription 基础的适配器
 * @author song.chen
 *
 * @param <E>
 */

public abstract class BaseListAdapter<E> extends BaseAdapter {

	public List<E> list;
	
	public Context mContext;
	
	public LayoutInflater mInflater;
	
	/**构造函数*/
	public BaseListAdapter(Context context, List<E> list) {
		// TODO Auto-generated constructor stub
		super();
		this.mContext = context;
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public void add(E e) {
		this.list.add(e);
		notifyDataSetChanged();
	}
	
	public void addAll(List<E> e) {
		this.list.addAll(list);
		notifyDataSetChanged();       
	}
	
	public void remove(int position) {
		this.list.remove(position);
		notifyDataSetChanged();
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**绘制listView的一行*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = bindView(position, convertView, parent);
		//绑定内部点击
		addOnInternalClickListener(convertView, position,  list.get(position));
		return convertView;
	}
	
	public abstract View bindView(int position, View convertView, ViewGroup parent);
	
	//adapter中的内部点击事件
	public Map<Integer, onInternalClickListener> canClickItem; 

	private void addOnInternalClickListener(final View itemView, final Integer position, 
			final Object valuesMap) {
		if(canClickItem != null) {
			for (Integer key : canClickItem.keySet()) {
				View inView = itemView.findViewById(key);
				final onInternalClickListener inViewListener = canClickItem.get(key);
				
				if(inView != null && inViewListener != null) {
					inView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							inViewListener.OnClickListener(itemView, v, position, valuesMap);
						}
					});
				}
			}
		}
	}
	
	public void setOnInViewClickListener(int key, onInternalClickListener onClickListener) {
		if (canClickItem == null)
			canClickItem = new HashMap<Integer, onInternalClickListener>();
		canClickItem.put(key, onClickListener);
	}
	
	public interface onInternalClickListener {
		public void OnClickListener(View parentV, View v, Integer position,
				Object values);
	}
	
	Toast mToast;

	/**输出提示*/
	public void showToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			((Activity) mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mToast == null) {
						mToast = Toast.makeText(mContext, text,
								Toast.LENGTH_SHORT);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});
		}
	}
	
	public void showLog(String msg) {
		BmobLog.i(msg);
	}
	
	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

}