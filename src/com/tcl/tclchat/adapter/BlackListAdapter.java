package com.tcl.tclchat.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.im.bean.BmobChatUser;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tcl.tclchat.R;
import com.tcl.tclchat.adapter.base.BaseListAdapter;
import com.tcl.tclchat.adapter.base.ViewHolder;
import com.tcl.tclchat.util.ImageLoadOptions;

/**
 * 黑名单
 * @author song.chen
 *
 */
public class BlackListAdapter extends BaseListAdapter<BmobChatUser> {

	public BlackListAdapter(Context context, List<BmobChatUser> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	/**绘制黑名单中用户的相应控件*/
	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView != null) {
			convertView = mInflater.inflate(R.layout.item_blacklist, null);
		}
		final BmobChatUser contract = getList().get(position);
		TextView tv_friend_name = ViewHolder.get(convertView, R.id.tv_friend_name);
		ImageView iv_avatar = ViewHolder.get(convertView, R.id.img_friend_avatar);
		
		String avatar = contract.getAvatar();
		if (avatar != null && !avatar.equals("")) {
			ImageLoader.getInstance().displayImage(avatar, iv_avatar, ImageLoadOptions.getOptions());
		} else {
			iv_avatar.setImageResource(R.drawable.default_head);
		}
		tv_friend_name.setText(contract.getUsername());
		return convertView;
	}
	
}