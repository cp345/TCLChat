package com.tcl.tclchat.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;

import com.tcl.tclchat.R;
import com.tcl.tclchat.adapter.BlackListAdapter;
import com.tcl.tclchat.view.HeaderLayout;
import com.tcl.tclchat.view.dialog.DialogTips;

/**
 * 黑名单列表
 * @author song.chen
 *
 */
public class BlackListActivity extends ActivityBase implements OnItemClickListener{
	
	ListView listView;
	BlackListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blacklist);
		
		initView();
	}

	public void initView() {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		initTopBarForLeft("黑名单");
		adapter = new BlackListAdapter(this, BmobDB.create(this).getBlackList());
		listView = (ListView) findViewById(R.id.list_blacklist);
		listView.setOnItemClickListener(this);
		listView.setAdapter(adapter);
	}
	
	/**
	 * 显示移除黑名单对话框
	 */
	public void showRemoveBlackDialog(final int position, 
			final BmobChatUser user) {
		DialogTips dialog = new DialogTips(this, "移除黑名单", 
				"你确定将" + user.getUsername() + "移除黑名单吗？", 
				"确定", true, true);
		//成功事件
		dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int userId) {
				// TODO Auto-generated method stub
				adapter.remove(position);
				userManager.removeBlack(user.getUsername(), new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						showToast("移除黑名单成功");
//						CustomApplcation.getInstance().setContactList(CollectionUtils.list2map(
//								BmobDB.create(getApplicationContext()).getContactList()));	
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						showToast("移除黑名单失败" + arg1);
					}
				});
			}
		});
		//显示确认对话框
		dialog.show();
		dialog = null;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		BmobChatUser invite = (BmobChatUser) adapter.getItem(position);
		showRemoveBlackDialog(position, invite);
	}
	
}
