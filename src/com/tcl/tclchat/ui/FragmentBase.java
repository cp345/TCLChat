/**************************************************************************/
/**                                                @Date:  Oct 20, 2015  */
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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.util.BmobLog;

import com.tcl.tclchat.R;
import com.tcl.tclchat.view.HeaderLayout;
import com.tcl.tclchat.view.HeaderLayout.HeaderStyle;
import com.tcl.tclchat.view.HeaderLayout.onLeftImageButtonClickListener;
import com.tcl.tclchat.view.HeaderLayout.onRightImageButtonClickListener;
//import android.app.Fragment;

public abstract class FragmentBase extends Fragment {

	/**用户管理*/
	public BmobUserManager userManager;
	/**聊天管理*/
	public BmobChatManager chatManager;
	
	/**自定义的应用类*/
	//public CustomApplication mApplication;
	
	public HeaderLayout mHeaderLayout;

	protected View contentView;
	
	public LayoutInflater mInflater;
	
	Toast mToast;
	
	private Handler handler = new Handler();
	
	@SuppressWarnings("unused")
	private void runOnWorkThread(Runnable action) {
		new Thread(action).start();
	}
	
	public void runOnUiThread(Runnable action) {
		handler.post(action);
	}
	
	public FragmentBase() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		//mApplication = CustomApplication.getInstance();
		userManager = BmobUserManager.getInstance(getActivity());
		chatManager = BmobChatManager.getInstance(getActivity());
		mInflater = LayoutInflater.from(getActivity());
		
	}
	
	
	public void showToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}

	public void showToast(int text) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}

	public void showLog(String msg){
		BmobLog.i(msg);
	}
	
	public void initTopBarForOnlyTitle(String titleName) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle(titleName);
	}
	
	/**初始化标题栏 带左右按钮*/
	public void initTopBarForBoth(String titleName, int rightDrawableId,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
	}
	
	public View findViewById(int paramInt) {
		return getView().findViewById(paramInt);
	}
	
	/**左边按钮加标题*/
	public void initTopBarForLeft(String titleName) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_LIFT_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
	}
	
	/**右边按钮加标题*/
	public void initTopBarForRight(String titleName,int rightDrawableId,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_RIGHT_IMAGEBUTTON);
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
	}
	
	
	// 左边按钮的点击事件
	public class OnLeftButtonClickListener implements
		onLeftImageButtonClickListener {

		public void onClick() {
			getActivity().finish();
		}
	}
	
	/**
	 * 动画启动页面 startAnimActivity
	 * @throws
	 */
	public void startAnimActivity(Intent intent) {
		this.startActivity(intent);
	}
	
	public void startAnimActivity(Class<?> cla) {
		getActivity().startActivity(new Intent(getActivity(), cla));
	}
}
