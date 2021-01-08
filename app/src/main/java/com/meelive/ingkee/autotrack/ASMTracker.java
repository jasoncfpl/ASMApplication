package com.meelive.ingkee.autotrack;

import android.view.View;

public class ASMTracker {

    public static void track(View view) {
        System.out.println("-------click埋点-------");
    }

    public static void longClickTrack(View view) {
        System.out.println("-------longClick埋点-------");
    }

    public static void itemClickTrack(View view) {
        System.out.println("-------itemClick埋点-------");
    }
}
