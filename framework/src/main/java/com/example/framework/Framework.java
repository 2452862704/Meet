package com.example.framework;

import com.example.framework.utils.LogUtils;

public class Framework {
    private volatile static Framework framework;
    private Framework(){
        LogUtils.i("xxx");
    }
    public static Framework getFramework(){
        if (framework==null){
            synchronized (Framework.class){
                if (framework==null){
                    framework=new Framework();
                }
            }
        }
        return framework;
    }
}
