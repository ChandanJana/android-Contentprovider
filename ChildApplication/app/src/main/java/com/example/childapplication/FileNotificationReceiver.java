package com.example.childapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class FileNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent != null && intent.getAction().equals("com.zebra.configFile.action.notify")) {

            Bundle extras = intent.getExtras();

            if(extras != null && !extras.isEmpty()) {
                String secure_file_uri = extras.getString("secure_file_uri");
                String secure_file_name = extras.getString("secure_file_name");
                String secure_is_dir = extras.getString("secure_is_dir");
                String secure_file_crc = extras.getString("secure_file_crc");
                String secure_file_persist = extras.getString("secure_file_persist");
                StringBuilder strBuild = new StringBuilder();

                strBuild.append("fileURI - "+secure_file_uri).append("\n").
                        append("fileName - "+ secure_file_name).append("\n").
                        append("isDirectory - "+ secure_is_dir).append("\n").
                        append("CRC - "+ secure_file_crc).
                        append("Persist - "+ secure_file_persist);
                Log.e("TAGG", "ChildApplication Data "+ strBuild);
            }
        }
    }
}