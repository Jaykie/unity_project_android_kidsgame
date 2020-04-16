package com.moonma.common;
import com.moonma.common.AdConfigBase;
/**
 * Created by jaykie on 16/5/24.
 */
public class AdConfigAdmob extends AdConfigBase{  
    private static AdConfigAdmob _mian = null; 
    public static AdConfigAdmob main() {
        if(_mian==null){
            _mian = new AdConfigAdmob();  
        }
        return _mian;
    }
}
