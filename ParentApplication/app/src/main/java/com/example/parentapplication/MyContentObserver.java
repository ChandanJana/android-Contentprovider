package com.example.parentapplication;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Chandan Jana on 28-10-2022.
 * Company name: Mindteck
 * Email: chandan.jana@mindteck.com
 */
class MyContentObserver extends ContentObserver {
    public MyContentObserver(Handler h) {
        super(h);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return true;
    }

    @Override
    public void onChange(boolean selfChange) {
        Log.d("LOG_TAG", "MyContentObserver.onChange("+selfChange+")");
        //super.onChange(selfChange);

        // here you call the method to fill the list
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        //Write your code here
        Log.d("LOG_TAG", "MyContentObserver.onChange("+selfChange+", "+uri+")");
    }

}
