package com.xter.player.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.StatFs;

/**
 * Created by XTER on 2016/3/7.
 */
public class SysUtils {

	/**
	 * 获取可用的存储路径
	 * 
	 * @return string
	 */
	public static String getAvailableStorage() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		else
			return null;
	}

	/**
	 * 获取磁盘缓存路径
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getDiskCacheDir(Context context, String fileName) {
		final String cachePath;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return cachePath + File.separator + fileName;
	}

	/* 将系统长整值转为时间格式 */
	public static String getFormatDate(long date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
		return sdf.format(new Date(date));
	}

	/**
	 * 字节转十六进制
	 * 
	 * @param bytes
	 * @return
	 */
	public String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1)
				sb.append("0");
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * 设置闹钟
	 * 
	 * @param context 上下文
	 * @param time 延时
	 */
	public static void setAlarmTime(Context context, long time) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent("com.xter.receiver.Life");
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, sender);
	}

	/**
	 * 取消闹钟
	 * 
	 * @param context 上下文
	 */
	public static void cancelAlarm(Context context) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent("com.xter.receiver.Life");
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.cancel(sender);
	}

	/**
	 * SD卡相关的辅助类
	 * 
	 * 
	 * 
	 * 
	 * /** 判断SDCard是否可用
	 * 
	 * @return
	 */
	public static boolean isSDCardEnable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

	}

	/**
	 * 获取SD卡路径
	 * 
	 * @return
	 */
	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}

	/**
	 * 获取SD卡的剩余容量 单位byte
	 * 
	 * @return
	 */
	@SuppressLint("NewApi")
	public static long getSDCardAllSize() {
		if (isSDCardEnable()) {
			StatFs stat = new StatFs(getSDCardPath());
			// 获取空闲的数据块的数量
			long availableBlocks = (long) stat.getAvailableBlocksLong() - 4;
			// 获取单个数据块的大小（byte）
			long freeBlocks = stat.getAvailableBlocksLong();
			return freeBlocks * availableBlocks;
		}
		return 0;
	}

	/**
	 * 获取指定路径所在空间的剩余可用容量字节数，单位byte
	 * 
	 * @param filePath
	 * @return 容量字节 SDCard可用空间，内部存储可用空间
	 */
	@SuppressLint("NewApi")
	public static long getFreeBytes(String filePath) {
		// 如果是sd卡的下的路径，则获取sd卡可用容量
		if (filePath.startsWith(getSDCardPath())) {
			filePath = getSDCardPath();
		} else {// 如果是内部存储的路径，则获取内存存储的可用容量
			filePath = Environment.getDataDirectory().getAbsolutePath();
		}
		StatFs stat = new StatFs(filePath);
		long availableBlocks = (long) stat.getAvailableBlocksLong() - 4;
		return stat.getBlockSizeLong() * availableBlocks;
	}

	/**
	 * 获取系统存储路径
	 * 
	 * @return
	 */
	public static String getRootDirectoryPath() {
		return Environment.getRootDirectory().getAbsolutePath();
	}

	/**
	 * 获取应用程序名称
	 * 
	 * @param context
	 * @return string
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取应用程序版本名称信息
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String longToTimeStr(long time) {
		SimpleDateFormat sdf;
		if (time < 3600000) {
			sdf = new SimpleDateFormat("mm:ss");
		} else {
			sdf = new SimpleDateFormat("HH:mm:ss");
		}
		return sdf.format(new Date(time));
	}

	public static String getMovieTitle(String uri){
		if(uri.contains(File.separator))
			return uri.substring(uri.lastIndexOf(File.separator)+1);
		else
			return uri.substring(uri.lastIndexOf("/")+1);
	}

}
