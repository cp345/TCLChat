package com.tcl.tclchat.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tcl.tclchat.R;
import com.tcl.tclchat.ui.fragment.ContactFragment;
import com.tcl.tclchat.ui.fragment.RecentFragment;
import com.tcl.tclchat.ui.fragment.SettingsFragment;


public class MainActivity extends ActivityBase {
	
	//三个底部按钮
	private Button[] mTabs;
	private ContactFragment contactFragment;
	private RecentFragment recentFragment;
	private SettingsFragment settingsFragment;
	private Fragment[]  fragments;	
	//两个imageview
	ImageView iv_recent_tips,iv_contact_tips;//消息提示
	private int index;
	private int currentTabIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		initTab();
	}

	private void initView(){
		mTabs = new Button[3];
		mTabs[0] = (Button)findViewById(R.id.btn_message);
		mTabs[1] = (Button)findViewById(R.id.btn_contract);
		mTabs[2] = (Button)findViewById(R.id.btn_set);
		iv_recent_tips = (ImageView)findViewById(R.id.iv_recent_tips);
		iv_contact_tips = (ImageView)findViewById(R.id.iv_contact_tips);
		//把第一个tab设为选中状态
		mTabs[0].setSelected(true);
	}
	
	private void initTab(){
		contactFragment = new ContactFragment();
		recentFragment = new RecentFragment();
		settingsFragment = new SettingsFragment();
		fragments = new Fragment[]{recentFragment, contactFragment, settingsFragment};
		//添加显示第一个fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, recentFragment).
		add(R.id.fragment_container, contactFragment).hide(contactFragment).show(recentFragment).commit();
	}
	
	/**
	 * 
	 */
	public void onTabSelect(View view){
		switch(view.getId()){
		case R.id.btn_message: 
			index = 0;
			break;
		case R.id.btn_contract:
			index = 1;
			break;		
		case R.id.btn_set:
			index = 2;
			break;
		}
		if(currentTabIndex != index){
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			if(!fragments[index].isAdded()){
				trx.add(R.id.fragment_container, fragments[index]);
			}
			trx.show(fragments[index]).commit();
		}
		mTabs[currentTabIndex].setSelected(false);
		//把当前tab设为选中状态
		mTabs[index].setSelected(true);
		currentTabIndex = index;
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
