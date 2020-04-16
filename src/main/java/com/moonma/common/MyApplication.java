package com.moonma.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.moonma.common.AdConfig;
import com.moonma.common.AdConfigXiaomi;
import com.moonma.common.AdSplashXiaomi;
// import com.moonma.common.IAPXiaomi;
import com.moonma.common.Source;
//import com.moonma.common.AdConfigMobVista;

public class MyApplication extends Application
{
	private static Context context;
	private Activity mainActivity;
	static private MyApplication _main;

	public static MyApplication main() {
		return _main;
	}

	public static Context getAppContext() {
		return MyApplication.context;
	}

	public Activity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(Activity ac) {
		mainActivity = ac;
	}
	@Override
	public void onCreate()
	{
		super.onCreate();
		MyApplication.context = getApplicationContext();
		_main = this;

        //小米广告sdk初始化

		AdConfig adConfig = AdConfig.Main(this);
		{
        String appid = adConfig.GetAppId(Source.XIAOMI);
       // appid = "2882303761517411490"; //demo
			AdConfigXiaomi.main().initSdk(this,appid);

		}
		//xiaomi IAP
		//IAPXiaomi.initSDK(this);

		//mobvista sdk init
		// {
		// 	String  appid = adConfig.GetAppId(Source.MobVista);
		// 	String appkey = adConfig.GetAppId(Source.MobVista);
		//    AdConfigMobVista.main().init(this,appid,appkey);
		// }
		
	}

}
