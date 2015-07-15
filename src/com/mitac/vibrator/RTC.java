package com.mitac.vibrator;

import java.io.FileDescriptor;

public class RTC {

    public native void getTime();

    static {
        System.loadLibrary("RTC");
    }
}
