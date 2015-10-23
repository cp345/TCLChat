/******************************************************************************/
/*                                                               Oct 19, 2015 */
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

import com.tcl.tclchat.CustomApplication;

import android.content.Context;
import android.content.res.Resources;


public class PixelUtil {

	 private static Context mContext = CustomApplication.getInstance();

	    /**
	     * dp转 px.
	     */
	    public static int dpTopx(float value) {
	        final float scale = mContext.getResources().getDisplayMetrics().densityDpi;
	        return (int) (value * (scale / 160) + 0.5f);
	    }

	    /**
	     * dp转 px.
	     */
	    public static int dpTopx(float value, Context context) {
	        final float scale = context.getResources().getDisplayMetrics().densityDpi;
	        return (int) (value * (scale / 160) + 0.5f);
	    }

	    /**
	     * px转dp.
	     */
	    public static int pxTodp(float value) {
	        final float scale = mContext.getResources().getDisplayMetrics().densityDpi;
	        return (int) ((value * 160) / scale + 0.5f);
	    }

	    /**
	     * px转dp.
	     *
	     * @param value   the value
	     * @param context the context
	     * @return the int
	     */
	    public static int pxTodp(float value, Context context) {
	        final float scale = context.getResources().getDisplayMetrics().densityDpi;
	        return (int) ((value * 160) / scale + 0.5f);
	    }

	    /**
	     * sp转px
	     */
	    public static int spTopx(float value) {
	        Resources r;
	        if (mContext == null) {
	            r = Resources.getSystem();
	        } else {
	            r = mContext.getResources();
	        }
	        float spvalue = value * r.getDisplayMetrics().scaledDensity;
	        return (int) (spvalue + 0.5f);
	    }

	    /**
	     * sp转px.
	     */
	    public static int spTopx(float value, Context context) {
	        Resources r;
	        if (context == null) {
	            r = Resources.getSystem();
	        } else {
	            r = context.getResources();
	        }
	        float spvalue = value * r.getDisplayMetrics().scaledDensity;
	        return (int) (spvalue + 0.5f);
	    }

	    /**
	     * px转sp.
	     */
	    public static int pxTosp(float value) {
	        final float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
	        return (int) (value / scale + 0.5f);
	    }

	    /**
	     * px转sp
	     */
	    public static int pxTosp(float value, Context context) {
	        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
	        return (int) (value / scale + 0.5f);
	    }

}
