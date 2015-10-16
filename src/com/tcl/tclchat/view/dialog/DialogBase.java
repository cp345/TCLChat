package com.tcl.tclchat.view.dialog;

import com.tcl.tclchat.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @description  自定义对话框基类 对话框全屏显示控制、title显示控制，一个button或两个
 * @author song.chen
 *
 */
public abstract class DialogBase extends Dialog {

	/**成功的事件监听*/
	protected OnClickListener onSuccessListener;
	/**取消事件监听*/
	protected OnClickListener onCancelListener;
	/**解除监听*/
	protected OnDismissListener onDismissListener;
	protected Context mainContext;
	/**对话框的View*/
	protected View view;
	protected Button positiveButton,negativeButton;
	protected LinearLayout dialog_top;
	protected View title_red_line;
	protected TextView titleTextView;
	protected TextView messageTextView;
	
	/**是否全屏*/
	private boolean isFullScreen = false;
	/**提示框是否有标题*/
	private boolean hasTitle = true;
	
	/**对话框的宽度，高度，X坐标，Y坐标*/
	private int width = 0, height = 0, x = 0, y = 0;
	/**标题图标的额资源ID*/
	private int iconTitle = 0;
	/**对话框提示信息，标题*/
	private String message, title;
	private String namePositiveButton, nameNegativeButton;
	
	private final int MATCH_PARENT = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
	
	/**默认可点击back按键或外部区域取消对话框*/
	private boolean isCancel = true;
	
	//构造函数
	public DialogBase(Context context) {
		super(context);
		this.mainContext = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.v2_dialog_base);
		this.onBuilding();
		
		initView();
		
	}
	
	/**初始化控件*/
	public void initView() {
		//设置标题和消息
		dialog_top = (LinearLayout) findViewById(R.id.dialog_top);
		title_red_line = (View)findViewById(R.id.title_red_line);
		
		//判断是否有Title
		if(hasTitle ) {
			//设置可见
			dialog_top.setVisibility(View.VISIBLE);
			title_red_line.setVisibility(View.VISIBLE);
		} else {
			dialog_top.setVisibility(View.GONE);
			title_red_line.setVisibility(View.GONE);
		}
		
		titleTextView = (TextView) findViewById(R.id.dialog_title);
		messageTextView = (TextView) findViewById(R.id.dialog_message);
		titleTextView.setText(this.getTitle());
		messageTextView.setText(this.getMessage());
		
		if(view != null) {
			FrameLayout custom = (FrameLayout) findViewById(R.id.dialog_custom);
			custom.addView(view, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
			findViewById(R.id.dialog_contentPanel).setVisibility(View.GONE);
		} else {
			findViewById(R.id.dialog_customPanel).setVisibility(View.GONE);
		}
		//设置按钮事件监听
		positiveButton = (Button)findViewById(R.id.dialog_positivebutton);
		negativeButton = (Button)findViewById(R.id.dialog_negativebutton);
				
		if(namePositiveButton != null && namePositiveButton.length()>0){
			positiveButton.setText(namePositiveButton);
			positiveButton.setOnClickListener(GetPositiveButtonOnClickListener());
		} else {
			positiveButton.setVisibility(View.GONE);
			findViewById(R.id.dialog_leftspacer).setVisibility(View.VISIBLE);
			findViewById(R.id.dialog_rightspacer).setVisibility(View.VISIBLE);
		}
		if(nameNegativeButton != null && nameNegativeButton.length()>0){
			negativeButton.setText(nameNegativeButton);
			negativeButton.setOnClickListener(GetNegativeButtonOnClickListener());
		} else {
			negativeButton.setVisibility(View.GONE);
		}
		
		// 设置对话框的位置和大小
		LayoutParams params = this.getWindow().getAttributes();  
		if(this.getWidth()>0)
			params.width = this.getWidth();  
		if(this.getHeight()>0)
			params.height = this.getHeight();  
		if(this.getX()>0)
			params.width = this.getX();  
		if(this.getY()>0)
			params.height = this.getY();  
		
		// 如果设置为全屏
		if(isFullScreen) {
			params.width = WindowManager.LayoutParams.MATCH_PARENT;
			params.height = WindowManager.LayoutParams.MATCH_PARENT;
		}
		
		//设置点击dialog外部区域可取消
		if(isCancel){
			setCanceledOnTouchOutside(true);
			setCancelable(true);
		}else{
			setCanceledOnTouchOutside(false);
			setCancelable(false);
		}
	    getWindow().setAttributes(params);  
		this.setOnDismissListener(GetOnDismissListener());
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	/**获取OnDismiss事件监听，释放资源*/
	protected OnDismissListener GetOnDismissListener() {
		// TODO Auto-generated method stub
		return new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				DialogBase.this.onDismiss();
				DialogBase.this.setDismissMessage(null);
				view = null;
				mainContext = null;
				positiveButton = null;
				negativeButton = null;
				if(onDismissListener != null){
					onDismissListener.onDismiss(null);
				}
			}
		};
	}

	/**监听确认按钮的单击事件*/
	protected View.OnClickListener GetPositiveButtonOnClickListener() {
		// TODO Auto-generated method stub
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(OnClickPositiveButton()) {
					DialogBase.this.onDismiss();
				}
			}
		};
	}


	/**监听取消按钮的单击事件*/
	protected View.OnClickListener GetNegativeButtonOnClickListener() {
		// TODO Auto-generated method stub
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				OnClickNegativeButton();
				DialogBase.this.onDismiss();
			}
		};
	}
	/** 监听获取焦点改变事件，设置EditText文本默认全选*/
	protected OnFocusChangeListener GetOnFocusChangeListener() {
		return new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && v instanceof EditText) {
					((EditText) v).setSelection(0, ((EditText) v).getText().length());
				}
			}
		};
	}
	/**设置成功事件监听，用于提供给调用者的回调函数*/
	public void SetOnSuccessListener(OnClickListener listener){
		onSuccessListener = listener;
	}
	
	/** 设置关闭事件监听，用于提供给调用者的回调函数 */
	public void SetOnDismissListener(OnDismissListener listener){
		onDismissListener = listener;
	}

	/**提供给取消按钮，用于实现类定制*/
	public void SetOnCancelListener(OnClickListener listener){
		onCancelListener = listener;
	}
	
	/**创建的抽象方法，用于子类定制创建过程*/
	protected abstract void onBuilding();

	/** 确认按钮单击的抽象方法，用于子类定制*/
	protected abstract boolean OnClickPositiveButton();

	/** 取消按钮单击的抽象方法，用于子类定制*/
	protected abstract void OnClickNegativeButton();

	/**关闭的抽象方法，用于子类定制*/
	protected abstract void onDismiss();
	
	public boolean getCancle(){
		return isCancel;
	}
	
	public void setCancle(boolean isCancle){
		this.isCancel = isCancle;
	}
	
	
	/**对话框宽度*/
	public int getWidth() {
		return width;
	}
	
	/**对话框宽度*/
	public void setWidth(int width) {
		this.width = width;
	}
	/**对话框高度*/
	public int getHeight() {
		return height;
	}
	
	/**对话框高度*/
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**对话框X坐标*/
	public int getX() {
		return x;
	}
	
	/**对话框X坐标*/
	public void setX(int x) {
		this.x = x;
	}
	
	/**对话框Y坐标*/
	public int getY() {
		return y;
	}
	
	/**对话框Y坐标*/
	public void setY(int y) {
		this.y = y;
	}
	
	/**对话框提示信息*/
	public String getMessage() {
		return message;
	}
	
	/**对话框提示信息*/
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**对话框的View*/
	public View getView() {
		return view;
	}
	
	/**对话框的View*/
	public void setView(View view) {
		this.view = view;
	}
	
	/**是否全屏*/
	public boolean isFullScreen() {
		return isFullScreen;
	}
	
	/**是否全屏*/
	public void setFullScreen(boolean isFullScreen) {
		this.isFullScreen = isFullScreen;
	}

	/**是否有标题*/
	public boolean isHasTitle() {
		return hasTitle;
	}

	/**是否有标题*/
	public void setHasTitle(boolean hasTitle) {
		this.hasTitle = hasTitle;
	}

	/**iconTitle 标题图标的资源Id*/
	public int getIconTitle() {
		return iconTitle;
	}

	/**iconTitle 标题图标的资源Id*/
	public void setIconTitle(int iconTitle) {
		this.iconTitle = iconTitle;
	}

	/**是否取消*/
	public boolean isCancel() {
		return isCancel;
	}

	/**是否取消*/
	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

	/**对话框的标题*/
	public String getTitle() {
		return title;
	}

	/**对话框的标题*/
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Button getPositiveButton() {
		return positiveButton;
	}

	public void setPositiveButton(Button positiveButton) {
		this.positiveButton = positiveButton;
	}

	public Button getNegativeButton() {
		return negativeButton;
	}

	public void setNegativeButton(Button negativeButton) {
		this.negativeButton = negativeButton;
	}
	
	/**namePositiveButton 确认按钮名称*/
	public String getNamePositiveButton() {
		return namePositiveButton;
	}
	
	/**namePositiveButton 确认按钮名称*/
	public void setNamePositiveButton(String namePositiveButton) {
		this.namePositiveButton = namePositiveButton;
	}
	
	/**nameNegativeButton 取消按钮名称*/
	public String getNameNegativeButton() {
		return nameNegativeButton;
	}
	
    /**nameNegativeButton 取消按钮名称*/
	public void setNameNegativeButton(String nameNegativeButton) {
		this.nameNegativeButton = nameNegativeButton;
	}
}
