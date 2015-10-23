package com.tcl.tclchat.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.MsgTag;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tcl.tclchat.CustomApplication;
import com.tcl.tclchat.R;
import com.tcl.tclchat.bean.User;
import com.tcl.tclchat.config.BmobConstants;
import com.tcl.tclchat.util.CollectionUtils;
import com.tcl.tclchat.util.ImageLoadOptions;
import com.tcl.tclchat.view.dialog.DialogTips;

@SuppressLint("ClickableViewAccessibility")
public class SetMyInfoActivity extends ActivityBase implements OnClickListener {

	/**用户账号*/
	TextView tv_setName;
	/**用户昵称*/
	TextView tv_setNick;
	/**用户性别*/
	TextView tv_setGender;
	/**用户头像*/
	ImageView iv_set_avator;
	/**用户提示修改头像*/
	ImageView iv_arraw;
	/**用于提示修改昵称*/
	ImageView iv_nickarraw;
	LinearLayout layout_all;
	
	Button btn_chat, btn_back, btn_add_friend;
	RelativeLayout layout_head, layout_nick, layout_gender, layout_black_tips;
	
	/**查看的时自己或时别人，值为me或other*/
	String from = "";
	String username = "";
	User user;
	
	String []sexs = new String[]{"男","女"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_info);
		
		from = getIntent().getStringExtra("from");
		username = getIntent().getStringExtra("username");
		initView();
	}
	
	//初始化控件
	private void initView() {
		layout_all = (LinearLayout) findViewById(R.id.layout_all);
		tv_setName = (TextView) findViewById(R.id.tv_set_name);
		tv_setNick = (TextView) findViewById(R.id.tv_set_nick);
		tv_setGender = (TextView) findViewById(R.id.tv_set_gender);
		
		iv_set_avator = (ImageView) findViewById(R.id.iv_set_avator);
		iv_arraw = (ImageView) findViewById(R.id.iv_arraw);
		iv_nickarraw = (ImageView) findViewById(R.id.iv_nickarraw);
		
		layout_head = (RelativeLayout) findViewById(R.id.layout_head);
		layout_nick = (RelativeLayout) findViewById(R.id.layout_nick);
		layout_gender = (RelativeLayout) findViewById(R.id.layout_gender);
		/**黑名单提示语*/
		layout_black_tips = (RelativeLayout) findViewById(R.id.layout_black_tips);
		
		btn_chat = (Button) findViewById(R.id.btn_chat);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
		//设置控件按钮不可用
		btn_chat.setEnabled(false);
		btn_back.setEnabled(false);
		btn_add_friend.setEnabled(false);
		
		//如果是自己
		if(from.equals("me")) {
			initTopBarForLeft("个人资料");
			//修改个人的信息
			layout_head.setOnClickListener(this);
			layout_nick.setOnClickListener(this);
			layout_gender.setOnClickListener(this);
			//设置控件可见。即可以修改头像和昵称
			iv_nickarraw.setVisibility(View.VISIBLE);
			iv_arraw.setVisibility(View.VISIBLE);
			//设置不可见，表示不能同自己聊天以及加自己为好友
			btn_back.setVisibility(View.GONE);
			btn_chat.setVisibility(View.GONE);
			btn_add_friend.setVisibility(View.GONE);
			
		}else { //如果是其他人
			initTopBarForLeft("详细资料");
			//设置不可见
			iv_nickarraw.setVisibility(View.INVISIBLE);
			iv_arraw.setVisibility(View.INVISIBLE);
			//设置可见，不管是不是好友都可以进行会话
			btn_chat.setVisibility(View.VISIBLE);
			//监听会话按钮
			btn_chat.setOnClickListener(this);
			
			if(from.equals("add")) { //附近的人会将好友一同显示
				if (mApplication.getContactList().containsKey(username)) {// 是好友
					btn_back.setVisibility(View.VISIBLE);
					btn_back.setOnClickListener(this);
				} else {
					btn_back.setVisibility(View.GONE);//黑名单按钮不可见
					btn_add_friend.setVisibility(View.VISIBLE);//添加好友按钮可见
					btn_add_friend.setOnClickListener(this);
				}
				
			} else {  //是好友时
				//黑名单按钮控件可见
				btn_back.setVisibility(View.VISIBLE);
				//监听黑名单按钮
				btn_back.setOnClickListener(this);
			}
			initOtherData(username);
		}
	}
	
	private void initMyData() {
		User user = userManager.getCurrentUser(User.class);
		initOtherData(user.getUsername());
	}
	
	private void initOtherData(String username) {
		userManager.queryUser(username, new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				showLog("onError onError:" + arg1);
			}

			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				if (arg0 != null && arg0.size() > 0) {
					user = arg0.get(0);
					btn_chat.setEnabled(true);
					btn_back.setEnabled(true);
					btn_add_friend.setEnabled(true);
					updateUser(user);
				} else {
					showLog("onSuccess 查无此人");
				}
			}
		});
	}

	/**更改*/
	public void updateUser(User user) {
		refreshAvatar(user.getAvatar());
		tv_setName.setText(user.getUsername());
		tv_setNick.setText(user.getNick());
		tv_setGender.setText(user.getSex() == true ? "男" : "女");
		// 检测是否为黑名单用户
		if (from.equals("other")) {
			if (BmobDB.create(this).isBlackUser(user.getUsername())) {
				btn_back.setVisibility(View.GONE);
				layout_black_tips.setVisibility(View.VISIBLE);
			} else {
				btn_back.setVisibility(View.VISIBLE);
				layout_black_tips.setVisibility(View.GONE);
			}
		}
	}
	
	/**更新头像*/
	private void refreshAvatar(String avatar) {
		if(avatar != null && !avatar.equals("")) {
			ImageLoader.getInstance().displayImage(avatar, iv_set_avator, 
					ImageLoadOptions.getOptions());
		} else {
			iv_set_avator.setImageResource(R.drawable.default_head);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (from.equals("me")) {
			initMyData();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//发起聊天
		case R.id.btn_chat:
			//Intent intent = new Intent(this,ChatActivity.Class);
			//intent.putExtra("user", user);
			//startAnimActivity(intent);
			//finish();
			break;
		//黑名单
		case R.id.btn_back:
			showBlackDialog(user.getUsername());
			break;
		//添加好友
		case R.id.btn_add_friend:
			addFriend();
			break;
		//头像
		case R.id.layout_head:
			showAvatorPop();
			break;
		//性别
		case R.id.layout_gender:
			showGenderChooseDialog();
			break;
		//昵称
		case R.id.layout_nick:
			startAnimActivity(UpdateInfoActivity.class);
			break;
		}
	}

	/**显示黑名单提示框*/
	private void showBlackDialog(final String username) {
		DialogTips  dialog = new DialogTips(this, "加入黑名单", 
				"加入黑名单，你将不会再收到对方消息", "确定", true, true);
		dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int userId) {
				//添加到黑名单并更新
				userManager.addBlack(username, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						showToast("添加到黑名单成功");
						btn_back.setVisibility(View.GONE);
						layout_black_tips.setVisibility(View.VISIBLE);
						//重新设置内存中的好友那列表
						CustomApplication.getInstance().setContactList(
								CollectionUtils.listTomap(BmobDB.create(
										SetMyInfoActivity.this).getContactList()));
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						showToast("添加到黑名单失败" + arg1);
					}
				});
			}
		});
		dialog.show();
		dialog = null;
	} 
	
	//修改资料后进行信息的更新
	private void updateInfo(int which) {
		final User user = userManager.getCurrentUser(User.class);
		BmobLog.i("updateInfo 性别" + user.getSex());
		
		if(which == 0) {
			user.setSex(true);
		}else {
			user.setSex(false);
		}
		
		user.update(this, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				showToast("修改成功");
				final User u = userManager.getCurrentUser(User.class);
				BmobLog.i("修改成功后的sex = "+u.getSex());
				tv_setGender.setText(user.getSex() == true ? "男" : "女");
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				showToast("onFailure:" + arg1);
			}
		});
	}
	
	/**添加好友请求*/
	private void addFriend() {
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setMessage("正在添加...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		
		BmobChatManager.getInstance(this).sendTagMessage(MsgTag.ADD_CONTACT, 
				user.getObjectId(), new PushListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						progress.dismiss();
						showToast("发送请求成功，等待对方验证！");
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						progress.dismiss();
						showToast("发送请求成功，等待对方验证！");
						showLog("发送请求失败: " + arg1);
					}
				});
		
	}
	RelativeLayout layout_choose;
	RelativeLayout layout_photo;
	//弹出窗口效果
	PopupWindow avatorPop;

	public String filePath = "";
	
	/**修改头像*/
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void showAvatorPop() {
		View view = LayoutInflater.from(this).inflate(R.layout.pop_showavatar,
				null);
		layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
		layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
		//点击照相
		layout_photo.setOnClickListener(new OnClickListener() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View arg0) {
				showLog("点击拍照");
				layout_choose.setBackgroundColor(getResources().getColor(
						R.color.base_color_text_white));
				layout_photo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				File dir = new File(BmobConstants.MyAvatarDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 原图
				File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date()));
				filePath = file.getAbsolutePath();// 获取相片的保存路径
				Uri imageUri = Uri.fromFile(file);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CAMERA);
			}
		});
		//相册选择
		layout_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showLog("点击相册");
				layout_photo.setBackgroundColor(getResources().getColor(
						R.color.base_color_text_white));
				layout_choose.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_LOCATION);
			}
		});

		avatorPop = new PopupWindow(view, mScreenWidth, 600);
		avatorPop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					avatorPop.dismiss();
					return true;
				}
				return false;
			}
		});

		avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		avatorPop.setTouchable(true);
		avatorPop.setFocusable(true);
		avatorPop.setOutsideTouchable(true);
		avatorPop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从底部弹起
		avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);
		avatorPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);
	}
	
	/**性别选择提示框*/
	private void showGenderChooseDialog() {
		new AlertDialog.Builder(this)
		.setTitle("单选框")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setSingleChoiceItems(sexs, 0, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BmobLog.i("点击的是" + sexs[which]);
				updateInfo(which);
				dialog.dismiss();
			}
			//提示框中的取消按钮
		}).setNegativeButton("取消", null)
		.show();
	}

	private void startImageAction(Uri uri, int outputX, int outputY,
			int requestCode, boolean isCrop) {
		Intent intent = null;
		if (isCrop) {
			intent = new Intent("com.android.camera.action.CROP");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	Bitmap newBitmap;
	boolean isFromCamera = false;// 区分拍照旋转
	int degree = 0;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_CAMERA:// 拍照修改头像
			if (resultCode == RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					showToast("SD不可用");
					return;
				}
				isFromCamera = true;
				File file = new File(filePath);
				//degree = PhotoUtil.readPictureDegree(file.getAbsolutePath());
				Log.i("life", "拍照后的角度：" + degree);
				startImageAction(Uri.fromFile(file), 200, 200,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
			}
			break;
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_LOCATION:// 本地修改头像
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			Uri uri = null;
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					showToast("SD不可用");
					return;
				}
				isFromCamera = false;
				uri = data.getData();
				startImageAction(uri, 200, 200,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
			} else {
				showToast("照片获取失败");
			}

			break;
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP:// 裁剪头像返回
			// TODO sent to crop
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			if (data == null) {
				Toast.makeText(this, "取消选择", Toast.LENGTH_SHORT).show();
				return;
			} else {
				saveCropAvator(data);
			}
			// 初始化文件路径
			filePath = "";
			// 上传头像
			uploadAvatar();
			break;
		default:
			break;
		}
	}

	private void uploadAvatar() {
		BmobLog.i("头像地址：" + path);
		final BmobFile bmobFile = new BmobFile(new File(path));
		bmobFile.upload(this, new UploadFileListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				String url = bmobFile.getFileUrl();
				// 更新BmobUser对象
				updateUserAvatar(url);
			}

			@Override
			public void onProgress(Integer arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				showToast("头像上传失败：" + msg);
			}
		});
	}

	private void updateUserAvatar(final String url) {
		User user = (User) userManager.getCurrentUser(User.class);
		user.setAvatar(url);
		user.update(this, new UpdateListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				showToast("头像更新成功！");
				// 更新头像
				refreshAvatar(url);
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				showToast("头像更新失败：" + msg);
			}
		});
	}

	String path;

	/**
	 * 保存裁剪的头像
	 * 
	 * @param data
	 */
	private void saveCropAvator(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			Log.i("life", "avatar - bitmap = " + bitmap);
			if (bitmap != null) {
//				bitmap = PhotoUtil.toRoundCorner(bitmap, 10);
//				if (isFromCamera && degree != 0) {
//					bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
//				}
//				iv_set_avator.setImageBitmap(bitmap);
//				 保存图片
//				String filename = new SimpleDateFormat("yyMMddHHmmss")
//						.format(new Date());
//				path = BmobConstants.MyAvatarDir + filename;
//				PhotoUtil.saveBitmap(BmobConstants.MyAvatarDir, filename,
//						bitmap, true);
//				 上传头像
//				if (bitmap != null && bitmap.isRecycled()) {
//					bitmap.recycle();
//				}
			}
		}
	}
}
