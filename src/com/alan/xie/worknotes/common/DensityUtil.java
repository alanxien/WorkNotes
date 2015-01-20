package com.alan.xie.worknotes.common;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author alan.xie
 * @date 2015-1-20 下午5:42:27
 * @Description: dp和px互转
 */
public class DensityUtil {
	
	/**
	 * @author alan.xie
	 * @date 2015-1-20 下午5:44:15
	 * @Description: 获取屏幕宽度
	 * @param @param context
	 * @param @return
	 * @return int
	 */
	public static int getScreenW(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metric);

		return metric.widthPixels;
	}
	
	/**
	 * @author alan.xie
	 * @date 2015-1-20 下午5:44:30
	 * @Description: 获取屏幕高度
	 * @param @param context
	 * @param @return
	 * @return int
	 */
	public static int getScreenH(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metric);

		return metric.heightPixels;
	}

	/**
     * @author xin.xie
     * @param context
     * @param dpValue
     * @return int
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
   
    /**
     * @author xin.xie
     * @param context
     * @param pxValue
     * @return int
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
}
