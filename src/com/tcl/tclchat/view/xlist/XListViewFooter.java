package com.tcl.tclchat.view.xlist;

import com.tcl.tclchat.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 加载更多的类
 * @author song.chen
 *
 */
@SuppressLint("InflateParams")
public class XListViewFooter extends LinearLayout {

	/**加载更多的正常状态*/
	public final static int STATE_NORMAL = 0;
	/**准备加载*/
	public final static int STATE_READY = 1;
	/**加载中*/
	public final static int STATE_LOADING = 2;
	
	@SuppressWarnings("unused")
	private Context mContext;
	/**加载的布局view*/
	private View mContentView;
	/**加载的进度*/
	private View mProgressBar;
	/**加载的提示view*/
	private TextView mHintView;
	
	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	private XListViewFooter(Context context, AttributeSet attr) {
		super(context, attr);
		initView(context);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		//设置布局的参数
		moreView.setLayoutParams(new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,  //宽度
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT)); //高度
		
		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
		mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
	}

	/**设置加载的状态*/
	public void setState(int state) {
		//初始化
		//mContentView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.INVISIBLE);
		mHintView.setVisibility(View.INVISIBLE);
		
		switch (state) {
		case STATE_READY:
			mHintView.setVisibility(VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_ready);
			break;

		case STATE_NORMAL:
			mHintView.setVisibility(VISIBLE);
			mHintView.setText(R.string.xlistview_footer_hint_normal);
			break;
		case STATE_LOADING:
			mProgressBar.setVisibility(VISIBLE);
			break;
		default:
			break;
		}
	}
	
	/**
	 * normal状态
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}
	
	/**
	 * loading状态
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 当禁用时隐藏
	 */
	public void hide() {
		LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) 
				mContentView.getLayoutParams();
		mLayoutParams.height = 0;
		mContentView.setLayoutParams(mLayoutParams);
	}
	
	/**
	 * 当启用时显示
	 */
	@SuppressWarnings("unused")
	private void show() {
		LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams)
				mContentView.getLayoutParams();
		mLayoutParams.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(mLayoutParams);
	}

	/**设置反弹效果*/
	public void setBottomMargin(int height) {
		if(height < 0) {
			return ;
		}
		LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams)
				mContentView.getLayoutParams();
		mLayoutParams.bottomMargin = height;
		mContentView.setLayoutParams(mLayoutParams);
	}

	/**获取反弹的效果*/
	public int getBottomMargin() {
		LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams)
				mContentView.getLayoutParams();
		return mLayoutParams.bottomMargin;
	}
}
