package com.tcl.tclchat.view.xlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.tcl.tclchat.R;

/**
 * 下拉刷新及加载更多的类
 * @author song.chen
 *
 */
@SuppressLint("ClickableViewAccessibility")
@SuppressWarnings("deprecation")
public class XListView extends ListView implements OnScrollListener{

	private float mLastY = -1;
	/**用于滚动*/
	private Scroller mScroller;
	/**用户的滚动监听*/
	private OnScrollListener mScrollListener;
	/**触发刷新和加载的接口*/
	private IXListViewListener mListViewListener;
	
	/**刷新的视图*/
	private XListViewHeader mHeaderView;
	/**刷新的内容*/
	private RelativeLayout mHeaderViewContent;
	/**刷新视图的高度*/
	private int mHeaderViewHeight;
	/**启用下拉刷新功能*/
	private boolean mEnablePullRefresh = true;
	/**正在刷新*/
	private boolean mPullRefreshing = false;
	
	/**加载更多的视图*/
	private XListViewFooter mFooterView = null;
	/**启用加载更多*/
	private boolean mEnablePullLoad = false;
	/**在ListView底部加载的总的列表项*/
	private int mTotalItemCount;
	
	/**滚动条*/
	private int mScrollBack;
	/**从头到尾的滚动条*/
	private final static int SCROLLBACK_HEADER = 0;
	/**从尾到头的滚动条*/
	private final static int SCROLLBACK_FOOTER = 1;
	/**滚动时间*/
	private final static int SCROLL_DURATION = 400;
	/**往上拉大于50px，加载更多*/
	private final static int PULL_LOAD_MORE_DELTA = 50;
	
	private final static float OFFSET_RADIO = 1.8f;
	
	
	/**加载中*/
	private boolean mPullLoading = false;
	private Context mContext;
	
	public XListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public XListView(Context context,AttributeSet attr) {
		super(context, attr);
		initWithContext(context);
	}
	
	public XListView(Context context,AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		initWithContext(context);
	}
	
	private void initWithContext(Context context) {
		//使用减速动画效果的方式构建Scroller
		//动画效果accelerated(加速)，decelerated(减速),repeated(重复),bounced(弹跳)
		mScroller = new Scroller(context, new DecelerateInterpolator());
		super.setOnScrollListener(this);
		
		mContext = context;
		//初始化标题视图
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(
				R.id.xlistview_header_content); 
		addHeaderView(mHeaderView);
		
		//初始化标题的高度
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				mHeaderViewHeight = mHeaderViewContent.getHeight();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}
	
	public boolean getEnablePullRefresh() {
		return mEnablePullRefresh;
	}

	/**刷新*/
	public void pullRefreshing() {
		if (!mEnablePullRefresh) {
			return;
		}
		mHeaderView.setVisibleHeight(mHeaderViewHeight);
		mPullRefreshing = true;
		mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
	}

	public boolean getPullRefreshing() {
		return mPullRefreshing;
	}

	public void setPullRefreshing(boolean mPullRefreshing) {
		this.mPullRefreshing = mPullRefreshing;
	}

	public boolean getPullLoading() {
		return this.mPullLoading;
	}

	public void setPullLoading(boolean mPullLoading) {
		this.mPullLoading = mPullLoading;
	}

	/**
	 * 启用或禁用下拉刷新功能
	 * 
	 */
	public void setPullRefreshEnable(boolean enable){
		mEnablePullRefresh = enable;
		//禁用时，隐藏内容
		if (!mEnablePullRefresh) { 
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 启用或禁用加载更多功能
	 * 
	 */
	public void setPullLoadEnable(boolean enable) {
//		if(mEnablePullLoad == enable)
//			return;
//		
		mEnablePullLoad = enable;
		if(!mEnablePullLoad) {
			if(mFooterView == null) {
				this.removeFooterView(mFooterView);
			}
		} else {
			if (mFooterView == null) {
				mFooterView = new XListViewFooter(mContext);
				mFooterView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startLoadMore();
					}
				});
			}
			this.addFooterView(mFooterView);
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}
	
	/**
	 * 停止刷新，重置标题视图
	 */
	public void stopRefresh() {
		Time time = new Time();
		time.setToNow();
		mHeaderView.setRefreshTime(time.format("%Y-%m-%d %T"));
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}
	
	/**
	 * 停止加载，重置底部视图
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener mlistener = (OnXScrollListener) mScrollListener;
			mlistener.onXScrolling(this);
		}
	}
	
	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisibleHeight((int) delta
				+ mHeaderView.getVisibleHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisibleHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		//每次滚动到顶部
		setSelection(0); 
	}
	
	
	/**
	 * 重置顶部的高度
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisibleHeight();
		if (height == 0) // 不可见.
			return;
		// 刷新和标题没有充分显示。什么也不做
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		 // 默认：向后滚动，以排除头。
		int finalHeight = 0;
		// 只需滚动显示所有的标题
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		//触发computescroll
		invalidate();
	}
	
	/**更新加载更多视图的高度*/
	private void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

//		setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	/**
	 * 重置加载更多视图的高度
	 * 
	 */
	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	/**开始加载更多*/
	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	/**
	 * 触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (getFirstVisiblePosition() == 0
					&& (mHeaderView.getVisibleHeight() > 0 || deltaY > 0)) {
				// 第一项是显示，头已显示或向下拉.
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();
			} else if (mEnablePullLoad && (getLastVisiblePosition() == mTotalItemCount - 1)
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
				updateFooterHeight(-deltaY / OFFSET_RADIO);
			}
			break;
		default:
			mLastY = -1; // 重置
			if (getFirstVisiblePosition() == 0) {
				// 调用刷新
				if (mEnablePullRefresh
						&& mHeaderView.getVisibleHeight() > mHeaderViewHeight) {
					mPullRefreshing = true;
					mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						mListViewListener.onRefresh();
					}
				}
				resetHeaderHeight();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// 调用加载更多
				if (mEnablePullLoad) {
					if (mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
						startLoadMore();
					}
					resetFooterHeight();
				}
			}
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisibleHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener scrollListener) {
		mScrollListener = scrollListener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// send to user's listener

		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	public void setXListViewListener(IXListViewListener xlistListener) {
		mListViewListener = xlistListener;
	}

	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}
	
	/**
	 * 实现此接口，以获得刷新/负载更多事件
	 * 
	 */
	public interface IXListViewListener {
		public void onRefresh();

		public void onLoadMore();
	}
}
