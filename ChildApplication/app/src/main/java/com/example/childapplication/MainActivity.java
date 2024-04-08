package com.example.childapplication;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Uri yourURI = Uri.parse("content://com.demo.user.provider/users");
    ContentProviderClient yourCR;
    TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // creating a cursor object of the content URI
        yourCR = getContentResolver().acquireContentProviderClient(yourURI);
        resultView = findViewById(R.id.res);
    }

    public void onClickShowDetails(View view) throws RemoteException {

        Cursor queryCursor = yourCR.query(yourURI, null, null, null, null);

        // creating a cursor object of the content URI
        //Cursor yourCursor = getContentResolver().query(Uri.parse("content://com.demo.user.provider/users"), null, null, null, null);

        // iteration of the cursor
        // to print whole table
        if (queryCursor.moveToFirst()) {
            StringBuilder strBuild = new StringBuilder();
            while (!queryCursor.isAfterLast()) {
                String tf = queryCursor.getInt(queryCursor.getColumnIndex("isdemo")) == 1 ? "true" : "false";
                strBuild.append("\n" + queryCursor.getString(queryCursor.getColumnIndex("id")) + "-" +
                        queryCursor.getString(queryCursor.getColumnIndex("name")) + "-" + tf);
                queryCursor.moveToNext();
            }
            resultView.setText(strBuild);
        } else {
            resultView.setText("No Records Found");
        }
    }

    public void onClickUpdateDetails(View view) throws RemoteException {
        // class to add values in the database
        ContentValues values = new ContentValues();

        // fetching text from user
        //values.put(MyContentProvider.name, ((EditText) findViewById(R.id.upadteName)).getText().toString());
        values.put("isdemo", new Boolean(((EditText) findViewById(R.id.updateValue)).getText().toString()));

        // update into database through content URI
        String selection = "name = ?";
        int ret = yourCR.update(yourURI, values, selection, new String[]{((EditText) findViewById(R.id.upadteName)).getText().toString()});
        Log.d("TAGG", "update " + ret);
        onClickShowDetails(view);
    }
}