package com.tcl.tclchat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.datatype.BmobGeoPoint;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tcl.tclchat.util.CollectionUtils;
import com.tcl.tclchat.util.SharePreferenceUtil;


/**
 * 自定义的Application类
 * @author song.chen
 *
 */
public class CustomApplication extends Application {
	
	public static CustomApplication mApplication;
	public LocationClient mlocationClient;
	/**上一次定位到的经纬度*/
	public static BmobGeoPoint lastPoint = null;
	MediaPlayer mediaPlayer;
	public final String PREF_LONGTITUDE = "longtitude"; 
	/**经度*/
	private String longtitude;
	public final String PREF_LATITUDE = "latitude"; 
	/**纬度*/
	private String latitude;
	/**联系人列表*/
	private Map<String, BmobChatUser> contactList = new 
			HashMap<String, BmobChatUser>();
	/**状态栏管理器*/
	NotificationManager mNotificationManager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		/*此为调试模式，会打印出相应的log，正式发布时需要注释掉*/
		BmobChat.DEBUG_MODE = true;
		mApplication = this;
		init();
	}
	
	public void init() {
		mediaPlayer = MediaPlayer.create(this, R.raw.notify);
		mNotificationManager = (NotificationManager) getSystemService(
				NOTIFICATION_SERVICE);
		initImageLoader(getApplicationContext());
		//若用户登录过，则先从好友数据库中获取好友列表存入到内存中
		if(BmobUserManager.getInstance(getApplicationContext())
				.getCurrentUser() != null) {
			// TODO Auto-generated method stub
			//获取本地好友列表到内存中，方便以后获取好友列表
			contactList = CollectionUtils.listTomap(BmobDB.create(
					getApplicationContext()).getContactList());
		}
		initBaidu();
	}
	
	/**初始化百度相关的sdk*/
	private void initBaidu() {
		// 初始化地图Sdk
		SDKInitializer.initialize(this);
		// 初始化定位sdk
		initBaiduLocClient();
	}

	private void initBaiduLocClient() {
		
		mlocationClient = new LocationClient(this.getApplicationContext());
		mlocationClient.registerLocationListener(new MyLocationListener());
	}
	
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			double latitude = location.getLatitude();
			double longtitude = location.getLongitude();
			if(lastPoint != null) {
				if(lastPoint.getLatitude() == latitude && 
						lastPoint.getLongitude() == longtitude) {
					mlocationClient.stop();
					return ;
				}
			}
			lastPoint = new BmobGeoPoint(longtitude, latitude);
		}
		
	}

	/**初始化ImageLoader*/
	private static void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"bmobim/Cache");// 获取到缓存的目录地址
		// 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				// 线程池内加载的数量
				.threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(new WeakMemoryCache())
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
				.writeDebugLogs() 
				.build();
		ImageLoader.getInstance().init(config);// 全局初始化此配置
		
	}

	public static CustomApplication getInstance() {
		return mApplication;
		
	}
	
	// 单例模式，才能及时返回数据
	SharePreferenceUtil mSpUtil;
	public static final String PREFERENCE_NAME = "_sharedinfo";

	public synchronized SharePreferenceUtil getSpUtil() {
		if (mSpUtil == null) {
			
			String currentId = BmobUserManager.getInstance(
					getApplicationContext()).getCurrentUserObjectId();
			String sharedName = currentId + PREFERENCE_NAME;
			
			mSpUtil = new SharePreferenceUtil(this, sharedName);
		}
		return mSpUtil;
	}

	
	/**状态栏管理器*/
	public NotificationManager getNotificationManager() {
		if(mNotificationManager == null) {
			mNotificationManager = (NotificationManager) getSystemService(
					android.content.Context.NOTIFICATION_SERVICE);
		}
		
		return mNotificationManager;
	}
	
	@SuppressWarnings("static-access")
	public synchronized MediaPlayer getMediaPlayer() {
		if(mediaPlayer == null) {
			mediaPlayer.create(this, R.raw.notify);
		}
		return mediaPlayer;
		
	}
	
	/**获取到经度*/
	public String getLongtitude() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		longtitude = preferences.getString(PREF_LONGTITUDE, "");
		return longtitude;
	}

	/**设置经度*/
	public void setLongtitude(String longtitude) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		
		if(editor.putString(PREF_LONGTITUDE, longtitude).commit()) {
			this.longtitude = longtitude;
		}
	}

	/**获取纬度*/
	public String getLatitude() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		latitude = preferences.getString(PREF_LATITUDE, "");
		return latitude;
	}

	/**设置纬度*/
	public void setLatitude(String latitude) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		if(editor.putString(PREF_LATITUDE, latitude).commit()) {
			this.latitude = latitude;
		}
	}

	/**获取联系人列表*/
	public Map<String, BmobChatUser> getContactList() {
		return contactList;
	}

	/**设置联系人列表*/
	public void setContactList(Map<String, BmobChatUser> contactList) {
		if(this.contactList != null ) {
			this.contactList.clear();
		}
		this.contactList = contactList;
	}

	/**退出登录时清空缓冲*/
	public void logout() {
		BmobUserManager.getInstance(getApplicationContext()).logout();
		setContactList(null);
		setLatitude(null);
		setLongtitude(null);
	}
}