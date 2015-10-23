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
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.inteface.MsgTag;
import cn.bmob.v3.listener.PushListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tcl.tclchat.R;
import com.tcl.tclchat.adapter.base.BaseListAdapter;
import com.tcl.tclchat.adapter.base.ViewHolder;
import com.tcl.tclchat.util.ImageLoadOptions;

/**
 * 查找好友并添加
 * @author song.chen
 *
 */
public class AddFriendAdapter extends BaseListAdapter<BmobChatUser> {

	public AddFriendAdapter(Context context, List<BmobChatUser> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("InflateParams")
	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView != null) {
			convertView = mInflater.inflate(R.layout.item_add_friend, null);
		}
		
		final BmobChatUser contract = getList().get(position);
		
		TextView tv_name = ViewHolder.get(convertView, R.id.name);
		ImageView iv_avatar = ViewHolder.get(convertView, R.id.avatar);
		Button btn_add = ViewHolder.get(convertView, R.id.btn_add);
		
		String avatar = contract.getAvatar();
		
		if(avatar != null && !avatar.equals("")) {
			ImageLoader.getInstance().displayImage(avatar, iv_avatar, 
					ImageLoadOptions.getOptions());
		} else {
			iv_avatar.setImageResource(R.drawable.default_head);
		}
		tv_name.setText(contract.getUsername());
		btn_add.setText("添加");
		btn_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//创建进度对话框
				final ProgressDialog mProgress = new ProgressDialog(mContext);
				mProgress.setMessage("正在添加...");//对话框的信息
				mProgress.setCanceledOnTouchOutside(false);//触摸对话框以外不取消
				mProgress.show();//显示对话框
				
				BmobChatManager.getInstance(mContext).sendTagMessage(MsgTag.ADD_CONTACT, 
						contract.getObjectId(), new PushListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						mProgress.dismiss();//关闭对话框
						showToast("发送请求成功，等待对方验证！");
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						mProgress.dismiss();
						showToast("发送请求失败，请重新添加！");
						showLog("发送请求失败" + arg1);
					}
				});
				
			}
		});
		return convertView;
	}

}
