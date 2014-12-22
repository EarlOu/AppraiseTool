package com.fnp.appraisetool;

import java.io.Closeable;

public class Util {
    public static void closeSliently(Closeable c) {
        try {
            c.close();
        } catch (Exception e) {};
    }
}
