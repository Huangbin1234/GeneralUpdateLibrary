package com.way.weather.update;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.hb.generalupdate.R;

public class Config {
	// 服务器JSON文件地址
	public String UPDATE_SERVER = "";
	public static int newVerCode = 0;
	public static String newVerName;
	public static String UpDate_URL;

	public Config(String jsonUrl) {
		this.UPDATE_SERVER = jsonUrl;
	}

	/**
	 * 获取当前版本号
	 * 
	 * @param context
	 * @return
	 */
	public int getVerCode(Context context) {
		int verCode = -1;
		try {
			// 获取packagemanager的实例
			PackageManager packageManager = context.getPackageManager();
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			verCode = packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verCode;
	}

	/**
	 * 获取版本名称
	 * 
	 * @param context
	 * @return
	 */
	public String getVerName(Context context) {
		String verName = "";

		try {
			// 获取packagemanager的实例
			PackageManager packageManager = context.getPackageManager();
			// 0代表是获取版本信息
			PackageInfo packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			verName = packInfo.versionName;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return verName;
	}

	/**
	 * 获取APP名
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		String verName = context.getResources().getText(R.string.app_name)
				.toString();
		return verName;
	}

	/**
	 * 获取服务器的版本
	 * 
	 * @return
	 */
	public int getServerVerCode() {
		try {
			HashMap<String, String> map = updateVersion();
			if (map == null) {
				return newVerCode;
			}
			newVerCode = Integer.parseInt(map.get("verCode"));
			UpDate_URL = map.get("downloadUrl");
			newVerName = map.get("verName");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newVerCode;
	}

	/**
	 * 客户端版本更新
	 * 
	 * @param res
	 * @return
	 */
	public HashMap<String, String> updateVersion() {
		HashMap<String, String> map = new HashMap<String, String>();
		String result = UpdateRequest.sendPost(null, UPDATE_SERVER);
		if (result != null && !"".equals(result)) {
			try {
				// 解决UTF-8 另存文件是BOM打头造成解析JSON错误的问题
				if (result.startsWith("\ufeff")) {
					result = result.substring(1);
				}
				JSONObject object = new JSONObject(result);
				map.put("verCode", object.getString("verCode"));// 版本号
				map.put("downloadUrl", object.getString("downloadUrl"));// 版本地址
				map.put("verName", object.getString("verName"));// 版本地址

				return map;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
