package com.moonma.common;

import android.content.Context;

import com.moonma.common.AdConfigBase;

/**
 * Created by jaykie on 16/5/24.
 */
public class AdConfigMobVista extends AdConfigBase {
    private static AdConfigMobVista _mian = null;

    public static AdConfigMobVista main() {
        if (_mian == null) {
            _mian = new AdConfigMobVista();
        }
        return _mian;
    }

    public void init(Context context, String appId, String appKey) {

    }
}
