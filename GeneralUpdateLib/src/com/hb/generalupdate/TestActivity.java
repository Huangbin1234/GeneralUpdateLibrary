package com.hb.generalupdate;

import android.app.Activity;
import android.os.Bundle;

import com.way.weather.update.UpdateManager;
/**
 * 测试activity，正式发布时可以删除。
 * @author huangbin 
 *
 */
public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		// 检查版本升级
		checkUpdate();
	}

	/**
	 * 检查升级
	 */
	private void checkUpdate() {
		try {
			String aFlag = getIntent().getExtras().getString("aFlag");
			if ("1".equals(aFlag)) {
				UpdateManager.newUpdate(TestActivity.this);
				aFlag = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
