/**************************************************************************/
/**                                                  @Date:  Oct 20, 2015*/
/*   Copyright 2015 TCL Communication Technology Holdings Limited         */
/*                                                                        */
/* This material is company confidential,cannot be reproduced in any from */
/* without the written permission of TCL Communication Technology Holdings*/
/* Limited                                                                */
/*                                                                        */
/*----------------------------------------------------------------------- */
/* Auther : Song Chen                                                     */
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
package com.tcl.tclchat.util;

import android.content.Context;
import android.content.SharedPreferences;

/**首选项的管理*/
public class SharePreferenceUtil {

	private SharedPreferences mPreferences;
	private static SharedPreferences.Editor mEditor;
	
	private String SHARE_KEY_NOTIFY = "share_key_notify";
	private String SHARE_KEY_VOICE = "shared_key_sound";
	private String SHARE_KEY_VIBRATE = "shared_key_vibrate";
	
	public SharePreferenceUtil(Context context, 	String name) {
		mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();
	}

	/**是否允许推送新消息通知*/
	public boolean isAllowPushNotify() { 
		return mPreferences.getBoolean(SHARE_KEY_NOTIFY, true);
	}
	
	/**设置推送新消息的通知*/
	public void setPushNotifyEnable(boolean ischecked) {
		mEditor.putBoolean(SHARE_KEY_NOTIFY, ischecked);
		mEditor.commit();
	}
	
	/**是否允许声音*/
	public boolean isAllowVoice() {
		return mPreferences.getBoolean(SHARE_KEY_VOICE, true);
	}
	
	/**设置声音*/
	public void setAllowVoiceEnable(boolean ischecked) {
		mEditor.putBoolean(SHARE_KEY_VOICE, ischecked);
		mEditor.commit();
	}
	
	/**是否允许震动*/
	public boolean isAllowVibrate() {
		return mPreferences.getBoolean(SHARE_KEY_VIBRATE, true);
	}
	
	/**设置震动*/
	public void setAllowVibrateEnable(boolean ischecked) {
		mEditor.putBoolean(SHARE_KEY_VIBRATE, true);
		mEditor.commit();
	}
}
