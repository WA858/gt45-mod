package com.g45.pro.modmenu;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TProxyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }
}
