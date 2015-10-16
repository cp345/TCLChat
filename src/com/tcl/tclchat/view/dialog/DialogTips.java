package com.tcl.tclchat.view.dialog;

import android.content.Context;

/**
 * 提示对话框
 * @author song.chen
 *
 */
public class DialogTips extends DialogBase {

	boolean hasTitle;
	boolean hasNegative;
	
	/**构造函数*/
	public DialogTips(Context context, String title, String message, 
			String buttonText, boolean hasNegative, boolean hasTitle) {
		// TODO Auto-generated constructor stub
		super(context);
		super.setTitle(title);  //对话框标题
		super.setMessage(message);//对话框信息
		super.setNamePositiveButton(buttonText); //对话框确认按钮
		this.hasTitle = hasTitle;
		this.hasNegative = hasNegative;
	}
	
	public DialogTips(Context context,	String buttonText, String negativeText, 
			String title, boolean isCancel) {
		super(context);
		super.setNamePositiveButton(buttonText);
		super.setNameNegativeButton(negativeText);
		super.setTitle(title);
		super.setCancel(isCancel);
		this.hasNegative = false;
		this.hasTitle = true;
	}
	
	/**下线通知的对话框样式*/
	public DialogTips(Context context,String message,String buttonText) {
		// TODO Auto-generated constructor stub
		super(context);
		super.setMessage(message);
		super.setNamePositiveButton(buttonText);
		this.hasNegative = false;
		this.hasTitle = true;
		super.setTitle("提示");
		super.setCancel(false);
	}
	
	/**创建对话框*/
	@Override
	protected void onBuilding() {
		// TODO Auto-generated method stub
		super.setWidth(dip2px(mainContext,300));
		if(hasNegative) {
			super.setNameNegativeButton("取消");
		} 
		if(!hasTitle) {
			super.setHasTitle(false);
		}
	}

	private int dip2px(Context context, float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;		
		return (int) (scale * dipValue + 0.5f);		
	}

	/**确认按钮的单击方法*/
	@Override
	protected boolean OnClickPositiveButton() {
		// TODO Auto-generated method stub
		if(onSuccessListener != null) {
			onSuccessListener.onClick(this, 1);
		}
		return true;
	}

	/**取消按钮的单击方法*/
	@Override
	protected void OnClickNegativeButton() {
		// TODO Auto-generated method stub
		if(onCancelListener != null) {
			onCancelListener.onClick(this, 0);
		}
	}

	/**关闭对话框*/
	@Override
	protected void onDismiss() {
		// TODO Auto-generated method stub
	}
}
