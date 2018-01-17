package com.example.showinputcharacterlengthedittext;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**

*description:分辨率转换的工具类
*/

public class DensityUtils {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(float pxValue, float fontScale) {
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
	}

	public static int getScreenWidth(Context context){
		return context.getResources().getDisplayMetrics().widthPixels;
	}


	/**
	 * 掩藏软键盘
	 *
	 * @param view
	 */
	public static void hideSoftKeyboard(View view) {
		if (view == null)
			return;
		View focusView = null;
		if (view instanceof EditText)//判断view是否是EditText
			focusView = view;
		Context context = view.getContext();
		if (context != null && context instanceof Activity) {//context得是Activity
			Activity activity = ((Activity) context);
			focusView = activity.getCurrentFocus();//获取当前activity中获得焦点的view
		}

		if (focusView != null) {//当前activity中有获得焦点的view，才进行掩藏软键盘
                /*
                if (focusView.isFocusable()) {
                    focusView.setFocusable(false);
                    focusView.setFocusable(true);
                }
                */
			if (focusView.isFocused()) {//取消获得焦点view的获取状态
				focusView.clearFocus();
			}
			//掩藏软键盘
			InputMethodManager manager = (InputMethodManager) focusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			manager.hideSoftInputFromInputMethod(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}



}
