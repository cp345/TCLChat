/**
 * 
 */
package com.tcl.tclchat.bean;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class User extends BmobChatUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**性别*/
	private boolean sex;
	/**根据字母排序*/
	private String sortLetters;
	/**地理位置*/
	private BmobGeoPoint location;
	
	public BmobGeoPoint getLocation() {
		return location;
	}
	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}
	public boolean getSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	
}