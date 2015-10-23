/**************************************************************************/
/**                                                  @Date:  Oct 20, 2015*/
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
package com.tcl.tclchat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;

import com.tcl.tclchat.CustomApplication;
import com.tcl.tclchat.R;
import com.tcl.tclchat.ui.BlackListActivity;
import com.tcl.tclchat.ui.FragmentBase;
import com.tcl.tclchat.ui.LoginActivity;
import com.tcl.tclchat.ui.SetMyInfoActivity;
import com.tcl.tclchat.util.SharePreferenceUtil;

/**设置界面*/
public class SettingsFragment extends FragmentBase implements OnClickListener {

	/**退出登录*/
	Button btn_logout;
	/**用户账号*/
	TextView tv_set_name;
	
	/**账户信息的布局*/
	RelativeLayout layout_info;
	/**黑名单的布局*/
	RelativeLayout layout_blacklist;
	/**新消息通知切换的布局*/
	RelativeLayout rl_switch_notification;
	/**声音切换的布局*/
	RelativeLayout rl_switch_voice;
	/**震动切换的布局*/
	RelativeLayout rl_switch_vibrate;
	
	/**新消息通知打开*/
	ImageView iv_open_notification;
	/**新消息通知关闭*/
	ImageView iv_close_notification;
	/**声音打开*/
	ImageView iv_open_voice;
	/**声音关闭*/
	ImageView iv_close_voice;
	/**震动打开*/
	ImageView iv_open_vibrate;
	/**震动关闭*/
	ImageView iv_close_vibrate;
	
	View view1,view2;
	SharePreferenceUtil mSharedUtil;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//mSharedUtil = mApplication.getSpUtil();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_set,  container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void initView() {
		initTopBarForOnlyTitle("设置");
		
		layout_blacklist = (RelativeLayout) findViewById(R.id.layout_blacklist);
		layout_info = (RelativeLayout) findViewById(R.id.layout_info);
		
		rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
		rl_switch_voice = (RelativeLayout) findViewById(R.id.rl_switch_voice);
		rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
		rl_switch_notification.setOnClickListener(this);
		rl_switch_voice.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);
		
		iv_close_notification = (ImageView) findViewById(R.id.iv_close_notification);
		iv_open_notification = (ImageView) findViewById(R.id.iv_open_notification);
		iv_close_vibrate = (ImageView) findViewById(R.id.iv_close_vibrate);
		iv_open_vibrate = (ImageView) findViewById(R.id.iv_open_vibrate);
		iv_close_voice = (ImageView) findViewById(R.id.iv_close_voice);
		iv_open_voice = (ImageView) findViewById(R.id.iv_open_voice);
		
		view1 = (View) findViewById(R.id.view1);
		view2 = (View) findViewById(R.id.view2);
		
		tv_set_name = (TextView) findViewById(R.id.tv_set_name);
		btn_logout = (Button) findViewById(R.id.btn_logout);
		
		layout_blacklist.setOnClickListener(this);
		layout_info.setOnClickListener(this);
		btn_logout.setOnClickListener(this);
		
		//初始化新消息的通知
		
		//boolean isAllowNotify = mSharedUtil.isAllowPushNotify();
		//Log.i("SettingsFragment", mSharedUtil + "mSharedUtil*****");
		//Log.i("SettingsFragment", isAllowNotify + "isAllowNotify*****");
		//if(isAllowNotify) {
//			iv_open_notification.setVisibility(View.VISIBLE);
//			iv_close_notification.setVisibility(View.INVISIBLE);
//		} else {
//			iv_open_notification.setVisibility(View.INVISIBLE);
//			iv_close_notification.setVisibility(View.VISIBLE);
//		}
//		//初始化声音
//		boolean isAllowVoice = mSharedUtil.isAllowVoice();
//		if(isAllowVoice) {
//			iv_open_voice.setVisibility(View.VISIBLE);
//			iv_close_voice.setVisibility(View.INVISIBLE);
//		} else {
//			iv_open_voice.setVisibility(View.INVISIBLE);
//			iv_close_voice.setVisibility(View.VISIBLE);
//		}
//		//初始化震动
//		boolean isAllowVibrate = mSharedUtil.isAllowVibrate();
//		if(isAllowVibrate) {
//			iv_open_vibrate.setVisibility(View.VISIBLE);
//			iv_close_vibrate.setVisibility(View.INVISIBLE);
//		} else {
//			iv_open_vibrate.setVisibility(View.INVISIBLE);
//			iv_close_vibrate.setVisibility(View.VISIBLE);
//		}
	}

	private void initData() {
		tv_set_name.setText(BmobUserManager.getInstance(getActivity())
				.getCurrentUser().getUsername());
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_blacklist:
			startAnimActivity(new Intent(getActivity(), BlackListActivity.class));
			break;
		case R.id.layout_info:
			Log.i("SetMyInfoActivity", R.id.layout_info + "+*******");
			
			Intent intent = new Intent(getActivity(), SetMyInfoActivity.class);
			intent.putExtra("from", "me");
			startActivity(intent);
			break;
		case R.id.btn_logout:
			CustomApplication.getInstance().logout();
			getActivity().finish();
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;
		case R.id.rl_switch_notification:
			//点击关闭接收新消息通知后，声音震动都会被隐藏掉
			if(iv_open_notification.getVisibility() == View.VISIBLE) {
				iv_open_notification.setVisibility(View.INVISIBLE);
				iv_close_notification.setVisibility(View.VISIBLE);
				//mSharedUtil.setPushNotifyEnable(false);
				rl_switch_vibrate.setVisibility(View.GONE);
				rl_switch_voice.setVisibility(View.GONE);
				view1.setVisibility(View.GONE);
				view2.setVisibility(View.GONE);
			} else { //当允许接收新消息的通知时，声音震动都显示出来
				iv_open_notification.setVisibility(View.VISIBLE);
				iv_close_notification.setVisibility(View.INVISIBLE);
				//mSharedUtil.setPushNotifyEnable(true);
				rl_switch_voice.setVisibility(View.VISIBLE);
				rl_switch_vibrate.setVisibility(View.VISIBLE);
				view1.setVisibility(View.VISIBLE);
				view2.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.rl_switch_voice:
			if(iv_open_voice.getVisibility() == View.VISIBLE) {
				iv_close_voice.setVisibility(View.VISIBLE);
				iv_open_voice.setVisibility(View.INVISIBLE);
				//mSharedUtil.setAllowVoiceEnable(false);
			} else {
				iv_close_voice.setVisibility(View.INVISIBLE);
				iv_open_voice.setVisibility(View.VISIBLE);
				//mSharedUtil.setAllowVoiceEnable(true);
			}
			break;
		case R.id.rl_switch_vibrate:
			if(iv_open_vibrate.getVisibility() == View.VISIBLE) {
				iv_close_vibrate.setVisibility(View.VISIBLE);
				iv_open_vibrate.setVisibility(View.INVISIBLE);
				//mSharedUtil.setAllowVibrateEnable(false);
			} else {
				iv_close_vibrate.setVisibility(View.INVISIBLE);
				iv_open_vibrate.setVisibility(View.VISIBLE);
				//mSharedUtil.setAllowVibrateEnable(true);
			}
			break;
		default:
			break;
		}
		
	}

}
