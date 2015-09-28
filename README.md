# GeneralUpdateLibrary
The Common application update library
GeneralUpdateLib使用说明：
原理就是在library的LoadActivity中完成了对版本信息的获取，并传递到主界面。若存在更新，主界面接收后即可弹出更新提示。
因为库的使用需要配置所以下面的6个步骤必须做好，其中库中的TestActivty是测试用的，可以将库的勾选去掉放开xml文件即可自行测试啦。

1.导入GeneralUpdateLib库项目，在你的主项目中右键属性引用这个Library。

2.将如下配置信息拷贝到你的主项目AndroidManifest.xml中，需要作为启动加载页面:
	<activity
            android:name="com.hb.generalupdate.LoadActivity"
            android:label="@string/app_name"
            android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
3.在你的主项目中添加权限（网络和读写SD卡权限）
		<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />

4.在GeneralUpdateLib库项目中的LoadActivity中配置你的.JSON文件访问地址，代码56行 。
5.配置LoadActivity加载完后的跳转界面
     在GeneralUpdateLib库项目中的LoadActivity中84行，设置更新加载页面完成后的要跳转到的界面。
     注意：一定要采用隐式调用，例如如下所示：
   ------------------------------------------------------------------------------------ 
   
		// 隐式意图调用，否则无法从library跳转到主工程
		Intent intent = new Intent();
		intent.setAction("com.hb.generalupdate.TestActivity");
		intent.putExtra("aFlag", actionFlag);
		startActivity(intent);
		LoadActivity.this.finish();

	----------------------------------------------------------------------------------
	对应的TestActivity的AndroidManifest.xml中的配置需要改为如下所示的样子：
	<!-- 隐式调用设置 -->
        <activity
            android:name="com.hb.generalupdate.TestActivity"
            android:label="@string/title_activity_test" >
            <intent-filter>
                <action android:name="com.hb.generalupdate.TestActivity" />

                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </activity>
 6.最后一步就是接收更新信息并弹出提示框。
 在你的主项目中LoadActivity页面加载完成后跳转到的界面的Oncreate（）方法中调用checkUpdate()方法。如下所示：
 
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
