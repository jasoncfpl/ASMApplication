package com.asm.plugin.autotrack;

public class AutoTrackUtils {

    public static boolean isFragmentActivity(String className) {
        //Android
        if ("android/support/v4/app/FragmentActivity.class".equals(className)) {
            return true;
        }
        //AndroidX
        if ("androidx/fragment/app/FragmentActivity.class".equals(className)) {
            return true;
        }
        return false;
    }

    public static boolean isClickView(String clazzName) {
//        if ("android.view.View$OnClickListener".equals(clazzName)) {
//            return true;
//        }
//        return false;
        return true;
    }
}
