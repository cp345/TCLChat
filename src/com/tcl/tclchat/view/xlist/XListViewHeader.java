package com.tcl.tclchat.view.xlist;

import com.tcl.tclchat.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下拉刷新的类
 * @author song.chen
 *
 */
@SuppressLint("InflateParams")
@SuppressWarnings("deprecation")
public class XListViewHeader extends LinearLayout {

	private LinearLayout mContair;
	/**刷新的图标*/
	private ImageView mArrowImageView;
	/**刷新的进度*/
	private ProgressBar mProgressBar;
	/**刷新的提示*/
	private TextView mHintTextView;
	/**刷新的时间*/
	private TextView mHeaderTimeView;
	/**刷新的时间标签*/
	private TextView mHeaderTimeLabel;
	
	/**向上旋转的动画*/
	private Animation mRotateUpAnim;
	/**向下旋转的动画*/
	private Animation mRotateDownAnim;
	
	/**旋转动换的持续时间*/
	private final int ROTATE_ANIM_DURATION = 180;
	
	/**正常的状态*/
	public final static int STATE_NORMAL = 0;
	/**准备开始*/
	public final static int STATE_READY = 1;
	/**s正在刷新的状态*/
	public final static int STATE_REFRESHING = 2;
	
	public int mState = STATE_NORMAL;
	public XListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	public XListViewHeader(Context context, AttributeSet attr) {
		super(context, attr);
		initView(context);
	}
	
	private void initView(Context context) {
		//初始情况设置下拉刷新的高度为0
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0);
		mContair = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_header, null);
		addView(mContair, mLayoutParams);
		setGravity(Gravity.BOTTOM);
		
		mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
		mHeaderTimeView = (TextView) findViewById(R.id.xlistview_header_time);
		mHeaderTimeLabel = (TextView) findViewById(R.id.xlistview_header_time_label);
		mProgressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);
		//参数分别表示：X轴顺时针旋转起始点，X轴顺时针旋转终点，x轴旋转的类型，旋转类型的值，Y轴旋转的类型，旋转的值
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		//设置动画的持续时间
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		//设置动画结束后停留在最后的位置
		mRotateDownAnim.setFillAfter(true);
	}
	
	public void setState(int state) {
		if(state == STATE_NORMAL) 
			return ;
		if(state == STATE_REFRESHING) {
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
		}else {
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		
		switch(state){
		case STATE_NORMAL:
			if (mState  == STATE_READY) {
				mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				mArrowImageView.clearAnimation();
			}
			mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateUpAnim);
				mHintTextView.setText(R.string.xlistview_header_hint_ready);
			}
			break;
		case STATE_REFRESHING:
			mHintTextView.setText(R.string.xlistview_header_hint_loading);
			Time time = new Time();
			time.setToNow();
			setRefreshTime(time.format("%Y-%m-%d %T"));
			break;
			default:
		}
		
		mState = state;
	}
	
	public void setVisibleHeight(int height) {
		if(height < 0) {
			height = 0;
		}
		
		LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams)
				mContair.getLayoutParams();
		mLayoutParams.height = height;
		mContair.setLayoutParams(mLayoutParams);
	}
	
	/***/
	public int getVisibleHeight() {
		return mContair.getHeight();
	}

	public void setRefreshTime(String time) {
		mHeaderTimeLabel.setVisibility(View.VISIBLE);
		mHeaderTimeView.setText(time);
	}
}
