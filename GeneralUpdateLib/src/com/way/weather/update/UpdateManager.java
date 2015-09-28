package com.way.weather.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateManager {
	private Context mContext;

	// 返回的安装包url
	private String apkUrl = "";
	// 下载包存放路径
	@SuppressLint("SdCardPath")
	private static final String savePath = "/sdcard/update/";
	@SuppressLint("SdCardPath")
	private static final String savePath2 = "/sdcard1/update/";
	private static final String saveFileName = savePath + "update.apk";
	private static final String saveFileName2 = savePath2 + "update.apk";
	// 进度条
	private ProgressBar mProgress;
	// 显示百分比文本
	private TextView messageView;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				messageView.setText("正在下载更新包："+progress + "%");
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context) {
		this.mContext = context;
		this.apkUrl = Config.UpDate_URL;
	}

	// 外部接口让主Activity调用
	public void checkUpdateInfo() {
		showNoticeDialog();
	}

	private void showNoticeDialog() {
		final AlertDialog ad = new AlertDialog(mContext);
		ad.setTitle(Config.getAppName(mContext) + "版本升级");
		ad.setMessage(Config.newVerName);
		ad.setViewProgress(false);
		ad.setPositiveButton("立即升级", new OnClickListener() {
			@Override
			public void onClick(View v) {
				ad.dismiss();
				showDownloadDialog();
			}
		});
		ad.setNegativeButton("下次再说", new OnClickListener() {
			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});

	}

	private void showDownloadDialog() {
		final AlertDialog ad = new AlertDialog(mContext);
		ad.setTitle(Config.getAppName(mContext) + "版本升级");
		ad.setViewProgress(true);
		mProgress = ad.getProcessbar();
		messageView = ad.getTextView();
		ad.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
				ad.dismiss();
				interceptFlag = true;
			}
		});
		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				boolean isSuc = false;
				File file = new File(savePath);
				File ApkFile;
				if (!file.exists()) {
					isSuc = file.mkdir();
					if (isSuc) {
						String apkFile = saveFileName;
						ApkFile = new File(apkFile);
					} else {
						File file2 = new File(savePath2);
						if (!file2.exists()) {
							isSuc = file2.mkdir();
						}
						String apkFile = saveFileName2;
						ApkFile = new File(apkFile);
					}
				} else {
					String apkFile = saveFileName;
					ApkFile = new File(apkFile);

				}
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					if(numread!=-1){
						count += numread;
					}
					
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 下载apk
	 * 
	 * @param url
	 */

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			File apkfile2 = new File(saveFileName2);
			if (!apkfile2.exists()) {
				return;
			} else {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setDataAndType(Uri.parse("file://" + apkfile2.toString()),
						"application/vnd.android.package-archive");
				mContext.startActivity(i);
			}
		} else {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
					"application/vnd.android.package-archive");
			mContext.startActivity(i);
		}
	}

	public void delFile() {
		File myFile = new File(saveFileName);
		if (myFile.exists()) {
			myFile.delete();
		}
		File myFile2 = new File(saveFileName2);
		if (myFile2.exists()) {
			myFile2.delete();
		}
	}

	/**
	 * 是否存在新版本
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getUpdateInfo(Context context,String jsonUrl) {
		Config cof = new Config(jsonUrl);
		int newCode = cof.getServerVerCode();
		int oldCode = cof.getVerCode(context);
		System.out.println("oldCode:" + oldCode + "newCode:" + newCode);
		if (oldCode < newCode) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 马上更新
	 * 
	 * @param context
	 */
	public static void newUpdate(Context context) {
		UpdateManager mUpdateManager = new UpdateManager(context);
		mUpdateManager.checkUpdateInfo();
	}
}
