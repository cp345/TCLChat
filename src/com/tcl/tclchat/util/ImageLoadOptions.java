package com.tcl.tclchat.util;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**显示图片的配置*/
public class ImageLoadOptions {
	
	/**图片加载的配置*/
	public static DisplayImageOptions getOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		/*
		.showImageOnLoading(R.drawable.loading) //设置图片在下载期间显示的图片 
        .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片 
        .showImageOnFail(R.drawable.error)  //设置图片加载/解码过程中错误时候显示的图片
		*/
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中
		.cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
		.considerExifParams(true) //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
		.displayer(new FadeInBitmapDisplayer(100)) //是否图片加载好后渐入的动画时间，可能会出现闪动
		.build(); //构建完成
		
		return options;
	}
}