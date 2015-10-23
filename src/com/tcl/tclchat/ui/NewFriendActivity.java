/**************************************************************************/
/**                                                  @Date: Oct 16, 2015 */
/*   Copyright 2015 TCL Communication Technology Holdings Limited         */
/*                                                                        */
/* This material is company confidential,cannot be reproduced in any from */
/* without the written permission of TCL Communication Technology Holdings*/
/* Limited                                                                */
/*                                                                        */
/*----------------------------------------------------------------------- */
/* Auther : Song.Chen                                                     */
/* E-mail : song.chen@tcl.com                                             */
/* Role   :                                                               */
/* Reference documents:                                                   */
/* ---------------------------------------------------------------------- */
/* Comments:                                                              */
/* File   :                                                               */
/* Labels :                                                               */
/**************************************************************************/


/* ---------------------------------------------------------------------- */
/* date       |auther       |key           |comment(what, where,why)      */
/* ---------------------------------------------------------------------- */
/*                                                                        */
/* ---------------------------------------------------------------------- */
package com.tcl.tclchat.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.db.BmobDB;

import com.tcl.tclchat.R;
import com.tcl.tclchat.adapter.NewFriendAdapter;
import com.tcl.tclchat.view.dialog.DialogTips;

/**
 * 新朋友
 */
public class NewFriendActivity extends ActivityBase implements OnItemLongClickListener {

	ListView mListView;
	NewFriendAdapter adapter;
	
	String from="";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_friend);
		from = getIntent().getStringExtra("from");
		
		initView();
	}
	
	private void initView(){
		initTopBarForLeft("新朋友");
		mListView = (ListView)findViewById(R.id.list_newfriend);
		//为每个子项增加长按点击事件
		mListView.setOnItemLongClickListener(this);
		//获取所有的好友请求数据-默认按照时间的先后顺序排列
		adapter = new NewFriendAdapter(this,BmobDB.create(this).queryBmobInviteList());
		mListView.setAdapter(adapter);
		if(from == null){//若来自通知栏的点击，则定位到最后一条
			mListView.setSelection(adapter.getCount());
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, 
			int position, long arg3) {
		//得到指定位置的好友请求，更新数据库
		BmobInvitation invite = (BmobInvitation) adapter.getItem(position);
		showDeleteDialog(position, invite);
		return true;
	}
	
	/**删除请求信息*/
	public void showDeleteDialog(final int position,final BmobInvitation invite) {
		DialogTips dialog = new DialogTips(this,invite.getFromname(),
				"删除好友请求", "确定", true, true);
		// 设置成功事件
		dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int userId) {
				deleteInvite(position,invite);
			}
		});
		// 显示对话框
		dialog.show();
		dialog = null;
	}
	
	/** 
	 * 删除请求*/
	private void deleteInvite(int position, BmobInvitation invite){
		adapter.remove(position);
		BmobDB.create(this).deleteInviteMsg(invite.getFromid(), 
				Long.toString(invite.getTime()));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(from == null){
			startAnimActivity(MainActivity.class);
		}
	}
	
	
}
