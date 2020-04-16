package com.moonma.common;
import com.moonma.common.AdConfigBase;
/**
 * Created by jaykie on 16/5/24.
 */
public class AdConfigUnity extends AdConfigBase{  
    private static AdConfigUnity _mian = null; 
    public static AdConfigUnity main() {
        if(_mian==null){
            _mian = new AdConfigUnity();  
        }
        return _mian;
    }
}
