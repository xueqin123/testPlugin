package com.xue.plugins.log;

import com.xue.plugins.MyPlugin;

public class LogUtil {
    public static void i(String tag, String message) {
        MyPlugin.logger.quiet("[" + tag + "]" + " " + message);
    }
}
