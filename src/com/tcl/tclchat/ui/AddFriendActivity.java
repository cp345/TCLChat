package com.tcl.tclchat.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.task.BRequest;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

import com.tcl.tclchat.R;
import com.tcl.tclchat.adapter.AddFriendAdapter;
import com.tcl.tclchat.util.CollectionUtils;
import com.tcl.tclchat.view.xlist.XListView;
import com.tcl.tclchat.view.xlist.XListView.IXListViewListener;

/**
 * 添加好友的类
 * @author song.chen
 *
 */
public class AddFriendActivity extends ActivityBase implements OnClickListener,
	IXListViewListener, OnItemClickListener{
	
	private EditText et_find_name;
	private Button btn_search;

	List<BmobChatUser> users = new ArrayList<BmobChatUser>();
	XListView mListView;
	AddFriendAdapter adapter;
	/**当前页*/
	int curPage;
	/**进度框*/
	ProgressDialog mProgressBar;
	
	String searchName ="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		initView();
	}
	
	private void initView() {
		initTopBarForLeft("查找好友");
		et_find_name = (EditText) findViewById(R.id.et_find_name);
		btn_search = (Button) findViewById(R.id.btn_search);
		
		btn_search.setOnClickListener(this);
		initXListView();
 	}

	private void initXListView() {
		mListView = (XListView) findViewById(R.id.list_search);
		//首先不允许加载更多
		mListView.setPullLoadEnable(false);
		//不允许下拉
		mListView.setPullRefreshEnable(false);
		//设置监听器
		mListView.setXListViewListener(this);
		
		mListView.pullRefreshing();
		adapter = new AddFriendAdapter(this, users);
		mListView.setAdapter(adapter);
		
		mListView.setOnItemClickListener(this);
	}

	private void initSearchList(final boolean isUpdate){
		if(!isUpdate) {
			mProgressBar = new ProgressDialog(AddFriendActivity.this);
			mProgressBar.setMessage("正在搜索...");
			mProgressBar.setCanceledOnTouchOutside(true);
			mProgressBar.show();
			
			userManager.queryUserByPage(isUpdate, 0, searchName, 
					new FindListener<BmobChatUser>() {

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					BmobLog.i("查询错误:"+arg1);
					if(users != null){
						users.clear();
					}
					showToast("用户不存在");
					mListView.setPullLoadEnable(false);
					refreshPull();
					//这样能保证每次查询都是从头开始
					curPage = 0;
				}

				@Override
				public void onSuccess(List<BmobChatUser> arg0) {
					// TODO Auto-generated method stub
					if (CollectionUtils.isNotNull(arg0)) {
						if(isUpdate){
							users.clear();
						}
						adapter.addAll(arg0);
						if(arg0.size() < BRequest.QUERY_LIMIT_COUNT){
							mListView.setPullLoadEnable(false);
							showToast("用户搜索完成!");
						}else{
							mListView.setPullLoadEnable(true);
						}
					}else{
						BmobLog.i("查询成功:无返回值");
						if(users!=null){
							users.clear();
						}
						showToast("用户不存在");
					}
					if(!isUpdate){
						mProgressBar.dismiss();
					}else{
						refreshPull();
					}
					//这样能保证每次查询都是从头开始
					curPage = 0;
				}
			});
		}
	}
	
	protected void refreshPull() {
		if(mListView.getPullRefreshing()) {
			mListView.stopRefresh();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			users.clear();
			searchName = et_find_name.getText().toString();
			if(searchName != null || !searchName.equals("")) {
				initSearchList(false);
			} else {
				showToast("请输入帐号");
			}
			
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		BmobChatUser user = (BmobChatUser) adapter.getItem(position - 1);
		Intent intent = new Intent(this, SetMyInfoActivity.class);
		intent.putExtra("from", "add");
		intent.putExtra("username", user.getUsername());
		startAnimActivity(intent);
	}


	@Override
	public void onLoadMore() {
		userManager.querySearchTotalCount(searchName, new CountListener() {
			
			@Override
			public void onSuccess(int arg0) {
				// TODO Auto-generated method stub
				if(arg0 > users.size()) {
					curPage ++;
					queryMoreSearchList(curPage);
				} else {
					showToast("数据加载完成");
					mListView.setPullLoadEnable(false);
					refreshLoad();
				}
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				showLog("查询附近的人总数失败"+arg1);
				refreshLoad();
			}
		});
	}

	protected void refreshLoad() {
		if(mListView.getPullLoading()) {
			mListView.stopLoadMore();
		}
		
	}

	protected void queryMoreSearchList(int page) {
		userManager.queryUserByPage(true, page, searchName, new FindListener<BmobChatUser>() {
			@Override
			public void onError(int arg0, String arg) {
				showLog("搜索更多用户出错:"+arg0);
				mListView.setPullLoadEnable(false);
				refreshLoad();
			}

			@Override
			public void onSuccess(List<BmobChatUser> arg0) {
				// TODO Auto-generated method stub
				if (CollectionUtils.isNotNull(arg0)) {
					adapter.addAll(arg0);
				}
				refreshLoad();
			}
		});
	}

	@Override
	public void onRefresh() {
		
	}

}
