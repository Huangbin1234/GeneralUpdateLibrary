package com.hb.generalupdate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.way.weather.update.UpdateManager;
/**
 * 需要将此Activity 做为启动项。
 * @author Administrator
 *
 */
public class LoadActivity extends Activity {

	private String actionFlag = "0";// 0不升级 1升级

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);
		checkNetwork();
	}

	/**
	 * 检查网络连接
	 */
	private void checkNetwork() {
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			CheckVerTask task = new CheckVerTask();
			task.start();
		} else {
			Toast.makeText(LoadActivity.this, "网络连接失败，请检查。", Toast.LENGTH_SHORT)
					.show();
			turnIntent();
		}
	}

	/**
	 * 获取版本信息
	 * 
	 * @author TXG
	 * 
	 */
	class CheckVerTask extends Thread {
		@Override
		public void run() {
			Message msg = new Message();
			// 传入服务器JSON文件地址
			boolean isUpdate = UpdateManager.getUpdateInfo(LoadActivity.this,
					"http://你的.json文件存放地址/checkvercode.json");
			if (isUpdate) {
				msg.what = 1;
				actionFlag = "1";
			} else {
				msg.what = 0;
				actionFlag = "0";
			}
			handler.sendMessage(msg);
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			turnIntent();
		}
	};

	/**
	 * 隐式跳转到主工程的主界面
	 */
	private void turnIntent() {
		// 隐式意图调用
		Intent intent = new Intent();
		// 设置跳转的主工程activity
		intent.setAction("com.hb.generalupdate.TestActivity");
		intent.putExtra("aFlag", actionFlag);
		startActivity(intent);
		LoadActivity.this.finish();
	}
}
