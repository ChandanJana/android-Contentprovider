package com.example.parentapplication;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Cursor cursor;
    ContentResolver contentResolver;
    Uri uri = Uri.parse("content://com.demo.user.provider/users");

    Handler handler = new Handler();

    public static MyContentObserver myContentObserver = new MyContentObserver(new Handler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentResolver = getContentResolver();
        contentResolver.registerContentObserver(uri, true, myContentObserver);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    public void onClickAddDetails(View view) {

        // class to add values in the database
        ContentValues values = new ContentValues();

        // fetching text from user
        values.put(MyContentProvider.name, ((EditText) findViewById(R.id.textName)).getText().toString());
        values.put(MyContentProvider.isdemo, false);

        // inserting into database through content URI
        getContentResolver().insert(MyContentProvider.CONTENT_URI, values);

        // displaying a toast message
        Toast.makeText(getBaseContext(), "New Record Inserted", Toast.LENGTH_LONG).show();
    }

    public void onClickShowDetails(View view) {
        // inserting complete table details in this text field
        TextView resultView = (TextView) findViewById(R.id.res);

        // creating a cursor object of the
        // content URI
        cursor = contentResolver.query(uri, null, null, null, null);

        // iteration of the cursor
        // to print whole table
        if (cursor.moveToFirst()) {
            StringBuilder strBuild = new StringBuilder();
            while (!cursor.isAfterLast()) {
                String tf = cursor.getInt(cursor.getColumnIndex("isdemo")) == 1 ? "true" : "false";
                strBuild.append("\n" + cursor.getString(cursor.getColumnIndex("id")) + "-" +
                        cursor.getString(cursor.getColumnIndex("name")) + "-" + tf);
                cursor.moveToNext();
            }
            resultView.setText(strBuild);
        } else {
            resultView.setText("No Records Found");
        }
    }

    public void onClickUpdateDetails(View view) {
        // class to add values in the database
        ContentValues values = new ContentValues();

        // fetching text from user
        //values.put(MyContentProvider.name, ((EditText) findViewById(R.id.upadteName)).getText().toString());
        values.put(MyContentProvider.isdemo, new Boolean(((EditText) findViewById(R.id.updateValue)).getText().toString()));

        // update into database through content URI
        String selection = "name=?";
        int ret = getContentResolver().update(MyContentProvider.CONTENT_URI, values, selection, new String[]{((EditText) findViewById(R.id.upadteName)).getText().toString()});
        Log.d("TAGG", "update " + ret);
        onClickShowDetails(view);
    }
}