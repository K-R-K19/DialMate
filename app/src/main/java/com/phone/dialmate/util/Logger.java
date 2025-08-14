package com.phone.dialmate.util;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {
    private static final String TAG = "DialMateLog";
    private static File logFile;

    public static void init(Context ctx) {
        try {
            File dir = new File(ctx.getFilesDir(), "logs");
            if (!dir.exists()) dir.mkdirs();
            logFile = new File(dir, "dialmate_log.txt");
            log("===== App Started at " + ts() + " =====");
        } catch (Exception e) {
            Log.e(TAG, "init failed", e);
        }
    }

    public static void log(String msg) {
        Log.d(TAG, msg);
        if (logFile == null) return;
        try (FileWriter w = new FileWriter(logFile, true)) {
            w.append(ts()).append(" : ").append(msg).append("\n");
        } catch (IOException e) {
            Log.e(TAG, "write failed", e);
        }
    }

    public static void logException(Throwable t) {
        log("CRASH:\n" + Log.getStackTraceString(t));
    }

    private static String ts() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
