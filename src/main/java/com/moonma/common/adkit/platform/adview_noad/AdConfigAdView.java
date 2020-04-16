package com.moonma.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;


import com.moonma.common.AdConfigBase;
 
/**
 * Created by jaykie on 16/5/24.
 */
public class AdConfigAdView extends AdConfigBase {
	private static AdConfigAdView _mian = null;

	public static AdConfigAdView main() {
		if (_mian == null) {
			_mian = new AdConfigAdView();
		}
		return _mian;
	}
 
	public void onInit(final Activity ac, String appId) {
	 
	}

}
