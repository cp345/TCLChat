/******************************************************************************/
/*                                                               Oct 20, 2015 */
/*                                PRESENTATION                                */
/*                                                                            */
/*       Copyright 2015 TCL Communication Technology Holdings Limited.        */
/*                                                                            */
/* This material is company confidential, cannot be reproduced in any form    */
/* without the written permission of TCL Communication Technology Holdings    */
/* Limited.                                                                   */
/*                                                                            */
/* -------------------------------------------------------------------------- */
/*  Author :  Yichao.Gu                                                       */
/*  Email  :  yichao.gu@tcl.com                                       		  */
/*  Role   :                                                                  */
/*  Reference documents :                                         			  */
/* -------------------------------------------------------------------------- */
/*  Comments :                                                                */
/*  File     :             													  */
/*  Labels   :                                                                */
/* -------------------------------------------------------------------------- */
/* ========================================================================== */
/*     Modifications on Features list / Changes Request / Problems Report     */
/* -------------------------------------------------------------------------- */
/*    date   |        author        |         Key          |     comment      */
/* ----------|----------------------|----------------------|----------------- */
/*           |  		            |                      |				  */
/*           |                      |                      |				  */
/*           |                      |                      | 			      */
/* ----------|----------------------|----------------------|----------------- */
/* 			 |			            |      			       |				  */
/*           |                      |                      |				  */
/* ----------|----------------------|----------------------|----------------- */
/******************************************************************************/

package com.tcl.tclchat.ui;

import com.tcl.tclchat.view.dialog.DialogTips;
import com.tcl.tclchat.config.BmobConstants;
import com.tcl.tclchat.config.Config;
import com.tcl.tclchat.util.CommonUtils;
import com.tcl.tclchat.R;

import cn.bmob.im.BmobChat;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.SaveListener;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends BaseActivity implements OnClickListener{
	
	EditText et_username, et_password;
	Button btn_login;
	TextView btn_register;
	BmobChatUser currentUser;
	
	private MyBroadcastReceiver receiver = new MyBroadcastReceiver();
	
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_login);
		BmobChat.getInstance(this).init(Config.applicationId);
		init();
		//注册推出广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(BmobConstants.ACTION_REGISTER_SUCCESS_FINISH);
		registerReceiver(receiver, filter);
		
//		showNotice();		
	}
	
	public void showNotice() {
		DialogTips dialog = new DialogTips(this,"提示",getResources().getString(R.string.show_notice), "确定",true,true);
		// 设置成功事件
		dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int userId) {
				
			}
		});
		// 显示确认对话框
		dialog.show();
		dialog = null;
	}
	
	private void init() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_register = (TextView) findViewById(R.id.btn_register);
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
				
	}
	
	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent != null && BmobConstants.ACTION_REGISTER_SUCCESS_FINISH.equals(intent.getAction())) {
				finish();				
			}
		}		
	}			
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btn_register) {
			Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
		}else {
			boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
			if (!isNetConnected) {
				showToast(R.string.network_tips);
				return;
			}
			login();
		}
	}
	
	private void login(){
		String name = et_username.getText().toString();
		String password = et_password.getText().toString();
		
		if(TextUtils.isEmpty(name)){
			showToast(R.string.toast_error_username_null);
			return;
		}
		
		if(TextUtils.isEmpty(password)){
			showToast(R.string.toast_error_password_null);
			return;
		}
		
		final ProgressDialog progress = new ProgressDialog(
				LoginActivity.this);		
		progress.setMessage("正在登录中");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		userManager.login(name, password, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						progress.setMessage("正在获取好友列表");
					}
				});
				//更新用户的地理位置以及好友的资料
				
				progress.dismiss();
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				progress.dismiss();
				BmobLog.i(arg1);
				showToast(arg1);				
			}
		});				
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}	
}
