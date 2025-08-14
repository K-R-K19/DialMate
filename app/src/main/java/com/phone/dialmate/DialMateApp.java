package com.phone.dialmate;

import android.app.Application;
import com.phone.dialmate.util.Logger;

public class DialMateApp extends Application {
    @Override public void onCreate() {
        super.onCreate();
        Logger.init(this);

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            Logger.logException(e);
            // Let system handle the crash after logging
            System.exit(1);
        });
    }
}
