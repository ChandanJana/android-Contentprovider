package com.example.childapplication;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SecondActivity extends AppCompatActivity {
    private String TAGG = SecondActivity.class.getSimpleName();
    Uri yourURI = Uri.parse("content://com.example.android.contentprovidersample.provider/launcher");
    Uri yourURI1 = Uri.parse("content://com.example.storage.provider/fileshare");
    ContentProviderClient yourCR;
    TextView resultView;

    static final Integer WRITE_EXST = 1;
    static final Integer READ_EXST = 2;
    static final Integer PERMISSION_REQUEST_ID = 4;


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(SecondActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SecondActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(SecondActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SecondActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // creating a cursor object of the content URI
        //yourCR = getContentResolver().acquireContentProviderClient(yourURI1);
        // Check Permission.
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
        deleteFile(SecondActivity.this);
        resultView = findViewById(R.id.res);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_ID) {

            if (grantResults.length > 1
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Log.e("permission", "Permission not granted");

            }
        }
    }

    private String AUTHORITY_FILE = "content://com.zebra.securestoragemanager.securecontentprovider/file/*";

    public void queryFile() {

        Uri cpUriQuery = Uri.parse(AUTHORITY_FILE);
        String selection = "target_app_package" + " = '" + getPackageName() + "'" + " AND " + "data_persist_required" + " = '" + false + "'";

        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(cpUriQuery, null, selection, null, null);

        } catch (Exception e) {
            Log.e(TAGG, "Error: " + e.getMessage());
        }

        try {
            if (cursor != null && cursor.moveToFirst()) {

                StringBuilder strBuild = new StringBuilder();
                String uriString = null;
                while (!cursor.isAfterLast()) {

                    uriString = cursor.getString(cursor.getColumnIndex("secure_file_uri"));
                    String fileName = cursor.getString(cursor.getColumnIndex("secure_file_name"));
                    String isDir = cursor.getString(cursor.getColumnIndex("secure_is_dir"));
                    String crc = cursor.getString(cursor.getColumnIndex("secure_file_crc"));

                    strBuild.append("FileURI - " + uriString).append("\n").
                            append("FileName - " + fileName).append("\n").
                            append("isDirectory - " + isDir).append("\n").
                            append("CRC - " + crc).append("\n");

                    cursor.moveToNext();
                }
                String extStorageDirectory = getFilesDir().toString();
                String sourceFileName = getFileName(Uri.parse(uriString));
                saveFile(this, "", Uri.parse(uriString), extStorageDirectory, sourceFileName);
                //readFile(this, uriString);
                Log.d(TAGG, "strBuild " + strBuild);
                resultView.setText(strBuild);
            }
        } catch (Exception e) {
            Log.e(TAGG, "Query data error: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean saveFile(Context context, String name, Uri sourceuri, String destinationDir, String destFileName) {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream input = null;
        boolean hasError = false;

        try {
            if (isVirtualFile(context, sourceuri)) {
                input = getInputStreamForVirtualFile(context, sourceuri, getMimeType(name));
            } else {
                input = context.getContentResolver().openInputStream(sourceuri);
            }

            boolean directorySetupResult;
            File destDir = new File(destinationDir);
            if (!destDir.exists()) {
                directorySetupResult = destDir.mkdirs();
            } else if (!destDir.isDirectory()) {
                directorySetupResult = replaceFileWithDir(destinationDir);
            } else {
                directorySetupResult = true;
            }

            if (!directorySetupResult) {
                hasError = true;
            } else {
                String destination = destinationDir + File.separator + destFileName;
                int originalsize = input.available();

                bis = new BufferedInputStream(input);
                bos = new BufferedOutputStream(new FileOutputStream(destination));
                byte[] buf = new byte[originalsize];
                bis.read(buf);
                do {
                    bos.write(buf);
                } while (bis.read(buf) != -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                    //unpackZip(destinationDir, name);
                    Log.e("TAGG", "File name " + name);
                    Log.e("TAGG", "File name without ext " + getNameWithoutExtension(name));
                    Log.e("TAGG", "File destinationDir " + destinationDir);
                    File destinationFilePath = new File(getFilesDir(), getFileName(sourceuri));
                    File destinationFilePathAfterUnzip = new File(getFilesDir(), "");
                    unzip(destinationFilePath, destinationFilePathAfterUnzip);
                }
            } catch (Exception ignored) {
            }
        }

        return !hasError;
    }

    private boolean replaceFileWithDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return true;
            }
        } else if (file.delete()) {
            File folder = new File(path);
            if (folder.mkdirs()) {
                return true;
            }
        }
        return false;
    }

    private boolean isVirtualFile(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!DocumentsContract.isDocumentUri(context, uri)) {
                return false;
            }
            Cursor cursor = context.getContentResolver().query(
                    uri,
                    new String[]{DocumentsContract.Document.COLUMN_FLAGS},
                    null, null, null);
            int flags = 0;
            if (cursor.moveToFirst()) {
                flags = cursor.getInt(0);
            }
            cursor.close();
            return (flags & DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT) != 0;
        } else {
            return false;
        }
    }

    private InputStream getInputStreamForVirtualFile(Context context, Uri uri, String mimeTypeFilter)
            throws IOException {

        ContentResolver resolver = context.getContentResolver();
        String[] openableMimeTypes = resolver.getStreamTypes(uri, mimeTypeFilter);
        if (openableMimeTypes == null || openableMimeTypes.length < 1) {
            throw new FileNotFoundException();
        }
        return resolver
                .openTypedAssetFileDescriptor(uri, openableMimeTypes[0], null)
                .createInputStream();
    }

    private static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                    //deleteFile(SecondActivity.this);
                }
        /* if time should be restored as well
        long time = ze.getTime();
        if (time > 0)
            file.setLastModified(time);
        */
            }

            zipFile.delete();
        } finally {
            zis.close();
        }
    }

    private String getFileName(Uri uri) {
        try {
            String path = uri.getLastPathSegment();
            return path != null ? path.substring(path.lastIndexOf("/") + 1) : "unknown";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "unknown";
    }

    public static String getNameWithoutExtension(String file) {
        int dotIndex = file.lastIndexOf('.');
        return (dotIndex == -1) ? file : file.substring(0, dotIndex);
    }


    private String AUTHORITY_FILE1 = "content://com.zebra.securestoragemanager.securecontentprovider/files/";

    public void deleteFile(Context context) {
        Uri cpUriQuery = Uri.parse(AUTHORITY_FILE1 + getPackageName());

        try {
            String selection = "target_app_package" + " = '" + getPackageName() + "'" + " AND " + "data_persist_required" + " = '" + false + "'";
            int deleteStatus = context.getContentResolver().delete(cpUriQuery, selection, null);
            Log.d(TAGG, "File deleted, status = " + deleteStatus);   // 0 means success
        } catch (Exception e) {
            Log.d(TAGG, "Delete file - error: " + e.getMessage());
        }
    }

    private void readFile(Context context, String fileUri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(fileUri));
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder strBuilder = new StringBuilder();
        String line;
        while (null != (line = bufferedReader.readLine())) {
            strBuilder.append(line);
        }
        Log.d(TAGG, "readFile " + strBuilder);
        // Perform any action with the file content
    }

    public void onClickShowDetails(View view) throws RemoteException {

        /*String selection = "target_app_package = ?";
        Cursor cursor = yourCR.query(Uri.parse(yourURI1 +"/2"), null, selection, new String[]{ "com.example.childapplication"}, null);

        // creating a cursor object of the content URI
        //Cursor yourCursor = getContentResolver().query(Uri.parse("content://com.demo.user.provider/users"), null, null, null, null);

        // iteration of the cursor
        // to print whole table
        if (cursor.moveToFirst()) {
            StringBuilder strBuild = new StringBuilder();
            while (!cursor.isAfterLast()) {
                String uriString = cursor.getString (cursor.getColumnIndex("secure_file_uri"));
                String fileName = cursor.getString(cursor.getColumnIndex("secure_file_name"));
                String isDir = cursor.getString(cursor.getColumnIndex("secure_is_dir"));
                String packageName = cursor.getString(cursor.getColumnIndex("target_app_package"));

                strBuild.append("\nFileURI - "+uriString).append("\n").
                        append("\nFileName - "+ fileName).append("\n").
                        append("\nisDirectory - "+ isDir).append("\n").
                        append("\nPackageName - "+ packageName).append("\n");

                cursor.moveToNext();
            }
            //resultView.setText(strBuild);
            queryFile();
        } else {
            resultView.setText("No Records Found");
        }*/
        //deleteFile(this);
        queryFile();
    }

    public void onClickUpdateDetails(View view) throws RemoteException {
        // class to add values in the database
        ContentValues values = new ContentValues();

        // fetching text from user
        values.put("name", ((EditText) findViewById(R.id.upadteName)).getText().toString());
        values.put("isdemo", new Boolean(((EditText) findViewById(R.id.updateValue)).getText().toString()));

        String selection = "name=?";
        Cursor cursor = yourCR.query(Uri.parse(yourURI + "/2"), null, selection, new String[]{((EditText) findViewById(R.id.upadteName)).getText().toString()}, null);


        if (cursor.moveToFirst()) {
            // update into database through content URI
            int ret = yourCR.update(Uri.parse(yourURI + "/" + cursor.getString(cursor.getColumnIndexOrThrow("_id"))), values, null, null);
            Log.d("TAGG", "update " + ret);
            onClickShowDetails(view);
        }
    }
}