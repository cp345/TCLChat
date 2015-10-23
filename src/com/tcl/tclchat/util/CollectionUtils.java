package com.tcl.tclchat.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.bmob.im.bean.BmobChatUser;

public class CollectionUtils {

	public static boolean isNotNull(List<BmobChatUser> collection) {
		if (collection != null && collection.size() > 0) {
			return true;
		}
		return false;
	}
	

	/** list转map
	 *  以用户名为key
	 */
	public static Map<String,BmobChatUser> listTomap(List<BmobChatUser> users){
		Map<String,BmobChatUser> friends = new HashMap<String, BmobChatUser>();
		for(BmobChatUser user : users){
			friends.put(user.getUsername(), user);
		}
		return friends;
	}
	
	
	/** 
	 * map转list
	 * 
	 */
	public static List<BmobChatUser> mapTolist(Map<String,BmobChatUser> maps){
		List<BmobChatUser> users = new ArrayList<BmobChatUser>();
		Iterator<Entry<String, BmobChatUser>> iterator = maps.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, BmobChatUser> entry = iterator.next();
			users.add(entry.getValue());
		}
		return users;
	}

}
