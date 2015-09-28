package com.way.weather.update;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.hb.generalupdate.R;

/**
 * 自定义对话框类
 * 
 * @author TXG
 * 
 */
public class AlertDialog {
	private Context context;
	private android.app.AlertDialog adialog;
	private TextView titleView;
	private TextView messageView;
	private LinearLayout buttonLayout;
	private ProgressBar progressx;

	public AlertDialog(Context context) {
		this.context = context;
		adialog = new Builder(context).create();
		// 返回取消对话框
		adialog.setCancelable(false);
		adialog.show();
		initView();
	}

	private void initView() {
		// 使用window.setContentView,替换对话框窗口布局
		Window window = adialog.getWindow();
		window.setContentView(R.layout.alert_dialog);
		titleView = (TextView) window.findViewById(R.id.title);
		messageView = (TextView) window.findViewById(R.id.message);
		buttonLayout = (LinearLayout) window.findViewById(R.id.buttonLayout);
		progressx = (ProgressBar) window.findViewById(R.id.progressx);
	}

	public void setTitle(int resId) {
		titleView.setText(resId);
	}

	public void setTitle(String title) {
		titleView.setText(title);
	}

	public void setMessage(int resId) {
		messageView.setText(resId);
	}

	public void setMessage(String message) {
		messageView.setText(message);
	}

	// 显示或隐藏进度条
	public void setViewProgress(boolean flag) {
		if (flag) {
			progressx.setVisibility(View.VISIBLE);
		} else {
			progressx.setVisibility(View.GONE);
		}
	}

	// 获取进度条ProgressBar
	public ProgressBar getProcessbar() {
		return progressx;
	}

	// 获取进度百分比TextView
	public TextView getTextView() {
		return messageView;
	}

	/**
	 * 确认按钮
	 * 
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text,
			final View.OnClickListener listener) {
		Button button = new Button(context);
		LinearLayout.LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		button.setLayoutParams(params);
		button.setBackgroundResource(R.drawable.alertdialog_button);
		button.setText(text);
		button.setTextColor(Color.WHITE);
		button.setPadding(30, 0, 30, 0);

		button.setTextSize(16);
		button.setOnClickListener(listener);
		buttonLayout.addView(button);
	}

	/**
	 * 取消按钮
	 * 
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text,
			final View.OnClickListener listener) {
		Button button = new Button(context);
		LinearLayout.LayoutParams params = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		button.setLayoutParams(params);
		button.setBackgroundResource(R.drawable.alertdialog_button);
		button.setText(text);
		button.setTextColor(Color.WHITE);
		button.setPadding(30, 0, 30, 0);
		button.setTextSize(16);
		button.setOnClickListener(listener);
		if (buttonLayout.getChildCount() > 0) {
			params.setMargins(20, 0, 0, 0);
			button.setLayoutParams(params);
			buttonLayout.addView(button, 1);
		} else {
			button.setLayoutParams(params);
			buttonLayout.addView(button);
		}
	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		adialog.dismiss();
	}
}