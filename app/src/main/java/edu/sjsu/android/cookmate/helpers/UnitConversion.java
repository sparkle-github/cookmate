package edu.sjsu.android.cookmate.helpers;

import android.content.Context;
import android.util.TypedValue;

public class UnitConversion {
    public static int dpToPixelConversion(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,context.getResources().getDisplayMetrics());
    }
}
