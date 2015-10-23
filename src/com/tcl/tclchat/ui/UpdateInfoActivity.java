/**************************************************************************/
/**                                                 @Date: Oct 20, 2015  */
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

import android.os.Bundle;
import android.widget.EditText;
import cn.bmob.v3.listener.UpdateListener;

import com.tcl.tclchat.R;
import com.tcl.tclchat.bean.User;
import com.tcl.tclchat.view.HeaderLayout.onRightImageButtonClickListener;

public class UpdateInfoActivity extends ActivityBase {
	
	/**昵称*/
	EditText et_nick;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avtivity_set_updateinfo);
		initView();
	}

	private void initView() {
		et_nick = (EditText) findViewById(R.id.edit_nick);
		// 初始化
		initTopBarForBoth("修改昵称", R.drawable.base_action_bar_true_bg_selector,
				new onRightImageButtonClickListener() {// 右边按钮的点击事件

			public void onClick() {
				// TODO Auto-generated method stub
				String nick = et_nick.getText().toString();
				// 输入昵称为空时，提示信息
				if (nick.equals("")) {
					showToast("请填写昵称!");
					return;
				}
				updateInfo(nick);
			}
		});
	}

	/**修改昵称*/
	private void updateInfo(String nick) {
		final User user = userManager.getCurrentUser(User.class);
		user.setNick(nick);
		user.update(this, new UpdateListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				showToast("修改成功");
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				showToast("onFailure:" + arg1);
			}
		});
	}
}
