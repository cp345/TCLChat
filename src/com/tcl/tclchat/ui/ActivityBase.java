/**************************************************************************/
/**                                                     @date 19/10/2015 */
/*   Copyright 2015 TCL Communication Technology Holdings Limited         */
/*                                                                        */
/* This material is company confidential,cannot be reproduced in any from */
/* without the written permission of TCL Communication Technology Holdings*/
/* Limited                                                                */
/*                                                                        */
/*----------------------------------------------------------------------- */
/* Auther : Song.Chen                                                     */
/* E-mail : song.chen@tcl.com                                             */
/* Role   : GamePanel                                                     */
/* Reference documents:                                                   */
/* ---------------------------------------------------------------------- */
/* Comments:                                                              */
/* File   :                                                               */
/* Labels :                                                               */
/**************************************************************************/


/* ---------------------------------------------------------------------- */
/* date         |auther      |key           |comment(what, where,why)     */
/* ---------------------------------------------------------------------- */
/* 19/10/2015   |song.chen   |              |Add drawSun                  */
/* ---------------------------------------------------------------------- */
package com.tcl.tclchat.ui;

import cn.bmob.im.BmobUserManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 用于检测账号是否在被登录的类
 * @author song.chen
 *
 */
public class ActivityBase extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//自动登录状态下的检测是否在其他设备登录
		checkLogin();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkLogin();
	}

	private void checkLogin() {
		BmobUserManager userManager = BmobUserManager.getInstance(this);
		
		if(userManager.getCurrentUser() == null) {
			showToast("您的帐号已在其他设备登录");
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		}
	}
	
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) this.getSystemService
				(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager
				.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
}
