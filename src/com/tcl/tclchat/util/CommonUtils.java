/******************************************************************************/
/*                                                               Oct 16, 2015 */
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

package com.tcl.tclchat.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonUtils {

	/**
	 * 检查是否有网络
	 */
	public static boolean isNetworkAvailable(Context context){
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			return info.isAvailable();
		}
		return false;
	}
	
	/**
	 * 检查是否是WIFI
	 */	
	public static boolean isWifi(Context context){
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 检查是否是移动网络
	 */
	public static boolean isMobile(Context context){
		NetworkInfo info = getNetworkInfo(context);
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				return true;
			}
		}
		return false;
	}	
	
	private static NetworkInfo getNetworkInfo(Context context){
		
		ConnectivityManager cm = (ConnectivityManager) context
			.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();	
	}
	
	/**
	 * 检查SD卡是否存在
	 */
	public static boolean checkSdCard() {
		if (android.os.Environment.getExternalStorageState().
				equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;				
		}else {
			return false;						
		}		
	}
	
}
