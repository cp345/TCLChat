/**************************************************************************/
/**                                                  @Date:  Oct 16, 2015*/
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
package com.tcl.tclchat.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.UpdateListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tcl.tclchat.R;
import com.tcl.tclchat.adapter.base.BaseListAdapter;
import com.tcl.tclchat.adapter.base.ViewHolder;
import com.tcl.tclchat.util.ImageLoadOptions;

/**
 * 新的好友请求
 * @author song.chen
 *
 */
public class NewFriendAdapter extends BaseListAdapter<BmobInvitation> {

	BmobInvitation msg;
	String avatar;
	/**請求状态*/
	int status;
	
	public NewFriendAdapter(Context context, List<BmobInvitation> list) {
		// TODO Auto-generated constructor stub
		super(context, list);
	}
	
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public View bindView(int arg0, View contentView, ViewGroup arg2) {
		
		if(contentView != null) {
			contentView = mInflater.inflate(R.layout.item_add_friend, null);
		}
		
		msg = getList().get(arg0);
		
		TextView name = ViewHolder.get(contentView, R.id.name);
		ImageView iv_avatar = ViewHolder.get(contentView, R.id.avatar);
		final Button btn_add = ViewHolder.get(contentView, R.id.btn_add);
		
		avatar = msg.getAvatar();
		
		//判断头像是否为空
		if (avatar != null && !avatar.equals("")) {
			//头像不为空时，加载图片
			ImageLoader.getInstance().displayImage(avatar, iv_avatar, 
			ImageLoadOptions.getOptions());
		} else {
			//为空是使用默认的图片
			iv_avatar.setImageResource(R.drawable.default_head);
		}
	    status = msg.getStatus();
		
		//如果状态是邀请没有得到验证或没有接收
		if(status == BmobConfig.INVITE_ADD_NO_VALIDATION || status == 
				BmobConfig.INVITE_ADD_NO_VALI_RECEIVED) {
			//监听按钮事件
			btn_add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BmobLog.i("点击同意按钮:" + msg.getFromid());
					//同意加为好友
					agreeAdd(btn_add, msg);
				}
			});
			
		}else {
			//改变按钮的内容
			btn_add.setText("已同意");
			btn_add.setBackgroundDrawable(null);
			//文字颜色
			btn_add.setTextColor(mContext.getResources().getColor(R.color.base_color_text_black));
			//按钮不可点击
			btn_add.setEnabled(false);
		}
		name.setText(msg.getFromname());
		
		return contentView;
	}

	protected void agreeAdd(final Button btn_add, BmobInvitation msg) {
		//进度对话框
		final ProgressDialog progress = new ProgressDialog(mContext);
		progress.setMessage("正在添加...");
		//加載時触摸其他区域不会被取消
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		
		try {
			BmobUserManager.getInstance(mContext).agreeAddContact(msg, new UpdateListener() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void onSuccess() {
					// 添加成功后关闭进度对话框
					progress.dismiss();
					btn_add.setText("已同意");
					btn_add.setBackgroundDrawable(null);
					btn_add.setTextColor(mContext.getResources().getColor(R.color.base_color_text_black));
					btn_add.setEnabled(false);
				}
				
				@Override
				public void onFailure(int arg0, final String arg1) {
					// 添加失败
					progress.dismiss();
					// 显示信息
					showToast("添加失败: " + arg1);
				}
			});
		} catch (final Exception e) {
			progress.dismiss();
			showToast("添加失败: " + e.getMessage());
		}
	}
}
