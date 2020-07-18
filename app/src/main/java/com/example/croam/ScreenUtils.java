package com.example.croam;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by yugy on 2014/3/24.
 */
public class ScreenUtils {

    public static float dp2px(Context context, int dp){
       return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());

    }

}