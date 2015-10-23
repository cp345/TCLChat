package com.tcl.tclchat.ui;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.tcl.tclchat.CustomApplication;
import com.tcl.tclchat.R;
import com.tcl.tclchat.bean.User;
import com.tcl.tclchat.util.CollectionUtils;
import com.tcl.tclchat.view.HeaderLayout;
import com.tcl.tclchat.view.HeaderLayout.HeaderStyle;
import com.tcl.tclchat.view.HeaderLayout.onLeftImageButtonClickListener;
import com.tcl.tclchat.view.HeaderLayout.onRightImageButtonClickListener;
import com.tcl.tclchat.view.dialog.DialogTips;

/**
 * 基类
 * @author song.chen
 *
 */

public class BaseActivity extends FragmentActivity{
	
	/**用户管理类，所有与用户有关的操作*/
	BmobUserManager userManager;
	/**消息发送接收*/
	BmobChatManager chatManager;
	
	CustomApplication mApplication;
	protected HeaderLayout mHeaderLayout;
	
	/**屏幕的宽度*/
	protected int mScreenWidth;
	/**屏幕的高度*/
	protected int mScreenHeight;
	
	/**提示*/
	Toast mToast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		userManager = BmobUserManager.getInstance(this);
		chatManager = BmobChatManager.getInstance(this);
		mApplication = CustomApplication.getInstance();
		//获取屏幕的参数
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
	}

	public void showToast(final String text) {
		//TextUtils字符串处理的简单类
		if (!TextUtils.isEmpty(text)) {
			//UI线程更新
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mToast == null) {
						mToast = Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_LONG);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});
			
		}
	}

	/**输出提示信息*/
	public void showToast(final int resId) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mToast == null) {
					mToast = Toast.makeText(BaseActivity.this.getApplicationContext(), 
							resId, Toast.LENGTH_LONG);
				} else {
					mToast.setText(resId);
				}
				mToast.show();
			}
		});
	}

	/**输出Log*/
	public void showLog(String msg){
		BmobLog.i(msg);
	}
	
	/**
	 * 只有title initTopBarLayoutByTitle*/
	public void initTopBarForOnlyTitle(String titleName) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle(titleName);
	}

	/**
	 * 初始化标题栏-带左右按钮*/
	public void initTopBarForBoth(String titleName, int rightDrawableId, String text,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightButton(titleName, rightDrawableId,text,
				listener);
	}
	
	/**左右按钮*/
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

	/**
	 * 只有左边按钮和Title initTopBarLayout*/
	public void initTopBarForLeft(String titleName) {
		mHeaderLayout = (HeaderLayout)findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
	}
	
	/** 显示下线的对话框 showOfflineDialog*/
	public void showOfflineDialog(final Context context) {
		DialogTips dialog = new DialogTips(this,"您的账号已在其他设备上登录!", "重新登录");
		// 设置成功事件
		dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int userId) {
				CustomApplication.getInstance().logout();
				startActivity(new Intent(context, LoginActivity.class));
				finish();
				dialogInterface.dismiss();
			}
		});
		// 显示确认对话框
		dialog.show();
		dialog = null;
	}
	
	/**左边按钮的点击事件*/
	public class OnLeftButtonClickListener implements
			onLeftImageButtonClickListener {

		@Override
		public void onClick() {
			finish();
		}
	}
	
	/**跳转到下一个页面*/
	public void startAnimActivity(Class<?> cla) {
		this.startActivity(new Intent(this, cla));
	}
	
	public void startAnimActivity(Intent intent) {
		this.startActivity(intent);
	}
	
	/** 用于登陆或者自动登陆情况下的用户资料及好友资料的检测更新*/
	public void updateUserInfos(){
		//更新地理位置信息
		updateUserLocation();
		//查询该用户的好友列表(这个好友列表是去除黑名单用户的哦),目前支持的查询好友个数为100，
		//如需修改请在调用这个方法前设置BmobConfig.LIMIT_CONTACTS即可。
		//这里默认采取的是登陆成功之后即将好于列表存储到数据库中，并更新到当前内存中,
		userManager.queryCurrentContactList(new FindListener<BmobChatUser>() {

					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						if(arg0 == BmobConfig.CODE_COMMON_NONE){
							showLog(arg1);
						}else{
							showLog("查询好友列表失败："+arg1);
						}
					}

					@Override
					public void onSuccess(List<BmobChatUser> arg0) {
						// 保存到application中方便比较
						CustomApplication.getInstance().setContactList(
						CollectionUtils.listTomap(arg0));
					}
				});
	}
	/** 更新用户的经纬度信息*/
	public void updateUserLocation(){
		if(CustomApplication.lastPoint!=null){
			//经度
			String saveLatitude  = mApplication.getLatitude();
			//纬度
			String saveLongtitude = mApplication.getLongtitude();
			//新的经度
			String newLat = String.valueOf(CustomApplication.lastPoint.getLatitude());
			//新的纬度
			String newLong = String.valueOf(CustomApplication.lastPoint.getLongitude());
			showLog("saveLatitude =" + saveLatitude+",saveLongtitude = " + saveLongtitude);
			showLog("newLat =" + newLat + ",newLong = " + newLong);
			//只有位置有变化就更新当前位置，达到实时更新的目的
			if(!saveLatitude.equals(newLat) || !saveLongtitude.equals(newLong)){
				final User user = (User) userManager.getCurrentUser(User.class);
				user.setLocation(CustomApplication.lastPoint);
				
				user.update(this, new UpdateListener() {
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						CustomApplication.getInstance().setLatitude(
								String.valueOf(user.getLocation().getLatitude()));
						CustomApplication.getInstance().setLongtitude(
								String.valueOf(user.getLocation().getLongitude()));
						showLog("经纬度更新成功");
					}
					@Override
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						showLog("经纬度更新 失败:" + msg);
					}
				});
			}else{
				showLog("用户位置未发生过变化");
			}
		}
	}
}
