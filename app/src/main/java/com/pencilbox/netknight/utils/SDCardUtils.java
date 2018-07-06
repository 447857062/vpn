package com.pencilbox.netknight.utils;

import android.os.Environment;

import java.io.File;

/**
 * SDCard辅助类
 */
public class SDCardUtils
{  
    private SDCardUtils()  
    {  
        /* cannot be instantiated */  
        throw new UnsupportedOperationException("cannot be instantiated");  
    }  

  
    /** 
     * 获取SD卡路径 
     *  
     * @return 
     */  
    public static String getSDCardPath()  
    {  
        return Environment.getExternalStorageDirectory().getAbsolutePath()  
                + File.separator;
    }
  
  
} 