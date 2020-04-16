package com.moonma.common;
import com.moonma.common.AdConfigBase;
/**
 * Created by jaykie on 16/5/24.
 */
public class AdConfigGdt extends AdConfigBase{  
    private static AdConfigGdt _mian = null; 
    public static AdConfigGdt main() {
        if(_mian==null){
            _mian = new AdConfigGdt();  
        }
        return _mian;
    }
}
