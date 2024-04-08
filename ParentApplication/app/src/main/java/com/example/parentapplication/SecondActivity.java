package com.example.parentapplication;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentapplication.data.Cheese;
import com.example.parentapplication.data.Launcher;

import java.io.File;

/**
 * Not very relevant to Room. This just shows data from {@link SampleContentProvider}.
 *
 * <p>Since the data is exposed through the ContentProvider, other apps can read and write the
 * content in a similar manner to this.</p>
 */
public class SecondActivity extends AppCompatActivity {

    private static final int LOADER_CHEESES = 1;

    private String TAGG = SecondActivity.class.getSimpleName();

    private CheeseAdapter mCheeseAdapter;

    Cursor cursor;
    ContentResolver contentResolver;
    //Uri uri = Uri.parse("content://com.demo.user.provider/users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        contentResolver = getContentResolver();
        //contentResolver.registerContentObserver(uri, true, myContentObserver);

//        final RecyclerView list = findViewById(R.id.list);
//        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
//        mCheeseAdapter = new CheeseAdapter();
//        list.setAdapter(mCheeseAdapter);

        //LoaderManager.getInstance(this).initLoader(LOADER_CHEESES, null, mLoaderCallbacks);
    }

    public void onClickAddDetails(View view) {

        // class to add values in the database
        ContentValues values = new ContentValues();

        // fetching text from user
        values.put(Launcher.LAUNCHER_NAME, ((EditText) findViewById(R.id.textName)).getText().toString());
        values.put(Launcher.IS_DEMO, false);

        // inserting into database through content URI
        getContentResolver().insert(SampleContentProvider.URI_LAUNCHER, values);

        // displaying a toast message
        Toast.makeText(getBaseContext(), "New Record Inserted", Toast.LENGTH_LONG).show();
    }

    public void onClickShowDetails(View view) {
        // inserting complete table details in this text field
        TextView resultView = (TextView) findViewById(R.id.res);

        // creating a cursor object of the
        // content URI
        cursor = contentResolver.query(SampleContentProvider.URI_LAUNCHER, null, null, null, null);

        // iteration of the cursor
        // to print whole table
        if (cursor.moveToFirst()) {
            StringBuilder strBuild = new StringBuilder();
            while (!cursor.isAfterLast()) {
                //String tf = cursor.getInt(cursor.getColumnIndex("isdemo")) == 1 ? "true" : "false";
                String tf = cursor.getInt(cursor.getColumnIndexOrThrow(Launcher.IS_DEMO)) == 1 ? "true" : "false";
                strBuild.append("\n" + cursor.getString(cursor.getColumnIndexOrThrow(Launcher.LAUNCHER_ID)) + "-" +
                        cursor.getString(cursor.getColumnIndexOrThrow(Launcher.LAUNCHER_NAME)) + "-" + tf);
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
        values.put(Launcher.LAUNCHER_NAME, ((EditText) findViewById(R.id.upadteName)).getText().toString());
        values.put(Launcher.IS_DEMO, new Boolean(((EditText) findViewById(R.id.updateValue)).getText().toString()));

        String selection1 = Launcher.LAUNCHER_NAME + "=?";
        cursor = contentResolver.query(Uri.parse(SampleContentProvider.URI_LAUNCHER + "/" + SampleContentProvider.CODE_ITEM), null, selection1, new String[]{((EditText) findViewById(R.id.upadteName)).getText().toString()}, null);

        if (cursor.moveToFirst()) {
            // update into database through content URI
            int ret = contentResolver.update(Uri.parse(SampleContentProvider.URI_LAUNCHER + "/" + cursor.getString(cursor.getColumnIndexOrThrow(Launcher.LAUNCHER_ID))), values, null, null);
            Log.d("TAGG", "update " + ret);
            onClickShowDetails(view);
        }
    }

    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                @NonNull
                public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                    /*return new CursorLoader(getApplicationContext(),
                            SampleContentProvider.URI_CHEESE,
                            new String[]{Cheese.COLUMN_NAME},
                            null, null, null);*/
                    return new CursorLoader(getApplicationContext(),
                            SampleContentProvider.URI_LAUNCHER,
                            new String[]{Launcher.LAUNCHER_NAME, Launcher.IS_DEMO},
                            null, null, null);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                    mCheeseAdapter.setCheeses(data);
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                    mCheeseAdapter.setCheeses(null);
                }

            };

    private static class CheeseAdapter extends RecyclerView.Adapter<CheeseAdapter.ViewHolder> {

        private Cursor mCursor;

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (mCursor.moveToPosition(position)) {
                holder.mText.setText(mCursor.getString(
                        mCursor.getColumnIndexOrThrow(Cheese.COLUMN_NAME)));
            }
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        void setCheeses(Cursor cursor) {
            mCursor = cursor;
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mText;

            ViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.simple_list_item_1, parent, false));
                mText = itemView.findViewById(android.R.id.text1);
            }

        }

    }

    /*public void onClickInsertFileAPI(View view) {

        // Replace “sourcePath” with the file path of the file to deploy located on the device, e.g. "/sdcard/A.txt"
        File file = new File("sourcePath");

        Uri contentUri = FileProvider.getUriForFile(this, "com.example.parentapplication.provider", file);
        // The "file" path is passed to the FileProvier() API, which returns the source input uri to deploy the file.
        // Example content uri returned: content://com.zebra.sampleapp.provider/enterprise/usr/A.txt
        getApplicationContext().grantUriPermission("com.zebra.securestoragemanager", contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION); // Needed to grant permission for SSM to read the uri
        Log.i(TAGG, "File Content Uri "+  contentUri);

        //Uri cpUriQuery = Uri.parse(FileShareContentProvider.URI_FILE_SHARE);
        Log.i(TAGG, "authority  "+  FileShareContentProvider.URI_FILE_SHARE.toString());

        try {
            ContentValues values = new ContentValues();
            values.put("target_app_package", String.format("{\"pkgs_sigs\": [{\"pkg\":\"%s\",\"sig\":\"%s\"}]}", "com.demo.filereceiver", signature));
            values.put("data_name", String.valueOf(contentUri)); // Passes the content uri as a input source
            values.put("data_value", "com.demo.filereceiver"+"/demoApp.zip"); // Replace “targetPath” with the package name of the target app that is accessing the deployed file (or retrieve the app package using context.getPackageName()) followed by "/" and the full path of the file, e.g. "context.getPackageName()/A.txt"
            //values.put("data_persist_required", persisFlagSpinner.getSelectedItem().toString());
            Uri createdRow  = this.getContentResolver().insert(FileShareContentProvider.URI_FILE_SHARE, values);
            Log.i(TAGG, "SSM Insert File: " + createdRow.toString());
            Toast.makeText(this, "File insert success", Toast.LENGTH_SHORT).show();
            //resultView.setText("Query Result");
        } catch(Exception e){
            Log.e(TAGG, "SSM Insert File - error: " + e.getMessage() + "\n\n");
        }
        Log.i(TAGG,"*********************************");
    }

    public void queryFile() {

        //Uri cpUriQuery = Uri.parse(AUTHORITY_FILE);
        String selection = "target_app_package" + " = '" + "com.demo.filereceiver" + "'" ;

        Cursor cursor = null;
        try {
            cursor = this.getContentResolver().query(FileShareContentProvider.URI_FILE_SHARE, null, selection, null, null);

        } catch (Exception e) {
            Log.e(TAGG, "Error: "+ e.getMessage());
        }

        try {
            if(cursor !=null && cursor.moveToFirst()){

                StringBuilder strBuild = new StringBuilder();
                String uriString = null;
                while (!cursor.isAfterLast()) {

                    uriString = cursor.getString (cursor.getColumnIndex("secure_file_uri"));
                    String fileName = cursor.getString(cursor.getColumnIndex("secure_file_name"));
                    String isDir = cursor.getString(cursor.getColumnIndex("secure_is_dir"));
                    String crc = cursor.getString(cursor.getColumnIndex("secure_file_crc"));

                    strBuild.append("fileURI - "+uriString).append("\n").
                            append("fileName - "+ fileName).append("\n").
                            append("isDirectory - "+ isDir).append("\n").
                            append("CRC - "+ crc);

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e(TAGG, "Query data error: " + e.getMessage());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }*/

}