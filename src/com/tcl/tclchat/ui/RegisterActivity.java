/******************************************************************************/
/*                                                               Date:10/2015 */
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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.im.BmobChat;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;


import com.tcl.tclchat.R;
import com.tcl.tclchat.bean.User;
import com.tcl.tclchat.config.Config;
import com.tcl.tclchat.util.CommonUtils;

public class RegisterActivity  extends BaseActivity{
	Button btn_register;
	EditText et_username, et_password, et_email;
	BmobChatUser currentUser;
	
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_register);
		BmobChat.getInstance(this).init(Config.applicationId);		
		initTopBarForLeft("注册");
		
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_email = (EditText) findViewById(R.id.et_email);
		
		btn_register = (Button) findViewById(R.id.btn_register);		
		btn_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				register();
			}
		});
		
	}
	
	private void register(){
		String name = et_username.getText().toString();
		String password = et_password.getText().toString();
		String pwd_again = et_email.getText().toString();
		
		if(TextUtils.isEmpty(name)){
			showToast(R.string.toast_error_username_null);
			return;
		}
		
		if(TextUtils.isEmpty(password)){
			showToast(R.string.toast_error_password_null);
			return;
		}
		
		if(!pwd_again.equals(password)){
			showToast(R.string.toast_error_comfirm_password);
			return;
		}
		
		boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
		if(!isNetConnected){
			showToast(R.string.network_tips);
			return;
		}
		
		final ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
		progress.setMessage("正在注册");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		//由于每个应用的注册所需的资料都不一样，故IM sdk未提供注册方法，用户可按照bmod SDK的注册方式进行注册。
		//注册的时候需要注意两点：1、User表中绑定设备id和type，2、设备表中绑定username字段
		final User bu = new User();
		bu.setUsername(name);
		bu.setPassword(password);
		//将user和设备id进行绑定
		bu.setDeviceType("android");
		bu.setInstallId(BmobInstallation.getInstallationId(this));
		Log.e("cp3", "zhuce");
		bu.signUp(RegisterActivity.this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				progress.dismiss();
				showToast("注册成功");
				//将设备与username进行绑定
				userManager.bindInstallationForRegister(bu.getUsername());
				//更新地理位置信息
				
				//发广播通知登录页面退出
				
				// 启动主页
				Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
				
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				BmobLog.i(arg1);
				showToast("注册失败" + arg1);
				progress.dismiss();
			}
		});
	}	
}
