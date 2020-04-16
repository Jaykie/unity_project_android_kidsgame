package com.moonma.common;
import android.content.Context;
import com.moonma.common.AdConfigBase;
/**
 * Created by jaykie on 16/5/24.
 */
public class AdConfigXiaomi extends AdConfigBase{  
    private static AdConfigXiaomi _mian = null; 
    public static AdConfigXiaomi main() {
        if(_mian==null){
            _mian = new AdConfigXiaomi();  
        }
        return _mian;
    }
     public void initSdk(Context ctx, String strAppId)
    {
    }
}
