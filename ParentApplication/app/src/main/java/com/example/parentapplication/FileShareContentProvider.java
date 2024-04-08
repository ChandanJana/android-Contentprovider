package com.example.parentapplication;

/**
 * Created by Chandan Jana on 28-10-2022.
 * Company name: Mindteck
 * Email: chandan.jana@mindteck.com
 */
/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.parentapplication.data.Cheese;
import com.example.parentapplication.data.Launcher;
import com.example.parentapplication.data.LauncherDao;
import com.example.parentapplication.data.SampleDatabase;
import com.example.parentapplication.data.file.FileShare;
import com.example.parentapplication.data.file.FileShareDao;


/**
 * A {@link ContentProvider} based on a Room database.
 *
 * <p>Note that you don't need to implement a ContentProvider unless you want to expose the data
 * outside your process or your application already uses a ContentProvider.</p>
 */
public class FileShareContentProvider extends ContentProvider {

    /**
     * The authority of this content provider.
     */
    public static final String AUTHORITY = "com.example.android.fileshare.provider";

    /**
     * The URI for the Cheese table.
     */
    public static final Uri URI_CHEESE = Uri.parse("content://" + AUTHORITY + "/" + Cheese.TABLE_NAME);
    public static final Uri URI_LAUNCHER = Uri.parse("content://" + AUTHORITY + "/" + Launcher.TABLE_NAME);
    public static final Uri URI_FILE_SHARE = Uri.parse("content://" + AUTHORITY + "/" + FileShare.TABLE_NAME);

    /**
     * The match code for some items in the Cheese table.
     */
    private static final int CODE_DIR = 1;

    /**
     * The match code for an item in the Cheese table.
     */
    public static final int CODE_ITEM = 2;

    /**
     * The URI matcher.
     */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        //MATCHER.addURI(AUTHORITY, Cheese.TABLE_NAME, CODE_CHEESE_DIR);
        //MATCHER.addURI(AUTHORITY, Cheese.TABLE_NAME + "/*", CODE_CHEESE_ITEM);

        //MATCHER.addURI(AUTHORITY, Launcher.TABLE_NAME, CODE_DIR);
        //MATCHER.addURI(AUTHORITY, Launcher.TABLE_NAME + "/*", CODE_ITEM);

        MATCHER.addURI(AUTHORITY, FileShare.TABLE_NAME, CODE_DIR);
        MATCHER.addURI(AUTHORITY, FileShare.TABLE_NAME + "/*", CODE_ITEM);

    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_DIR || code == CODE_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            //CheeseDao cheese = SampleDatabase.getInstance(context).cheese();
            //LauncherDao launcherDao = SampleDatabase.getInstance(context).launcherDao();
            FileShareDao launcherDao = SampleDatabase.getInstance(context).fileShareDao();
            final Cursor cursorCheese;
            final Cursor cursorLauncher;
            if (code == CODE_DIR) {
                //cursorCheese = cheese.selectAll();
                cursorLauncher = launcherDao.selectAll();
            } else {
                //cursorCheese = cheese.selectById(ContentUris.parseId(uri));
                cursorLauncher = launcherDao.selectById(selectionArgs[0]);
            }
            //cursorCheese.setNotificationUri(context.getContentResolver(), uri);
            cursorLauncher.setNotificationUri(context.getContentResolver(), uri);
            //return cursorCheese;
            return cursorLauncher;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_DIR: {
                //return "vnd.android.cursor.dir/" + AUTHORITY + "." + Cheese.TABLE_NAME;
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + FileShare.TABLE_NAME;
            }
            case CODE_ITEM: {
                //return "vnd.android.cursor.item/" + AUTHORITY + "." + Cheese.TABLE_NAME;
                return "vnd.android.cursor.item/" + AUTHORITY + "." + FileShare.TABLE_NAME;
            }
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                //final long id = SampleDatabase.getInstance(context).cheese().insert(Cheese.fromContentValues(values));
                final long id = SampleDatabase.getInstance(context).fileShareDao().insert(FileShare.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID: " + uri);
            case CODE_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                //final int count = SampleDatabase.getInstance(context).cheese().deleteById(ContentUris.parseId(uri));
                final int count = SampleDatabase.getInstance(context).fileShareDao().deleteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID: " + uri);
            case CODE_ITEM:

                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                //final Cheese cheese = Cheese.fromContentValues(values);
                final FileShare launcher = FileShare.fromContentValues(values);
                //cheese.id = ContentUris.parseId(uri);
                launcher.id = ContentUris.parseId(uri);
                //final int count = SampleDatabase.getInstance(context).cheese().update(cheese);
                final int count = SampleDatabase.getInstance(context).fileShareDao().update(launcher);
                //final int count = SampleDatabase.getInstance(context).launcherDao().updateIsDemo(launcher.name, launcher.isDemo);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
    /* This gets propagated up from the Callable */
    /*@NonNull
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull final ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final SampleDatabase database = SampleDatabase.getInstance(context);
        return database.runInTransaction(new Callable<ContentProviderResult[]>() {
            @Override
            public ContentProviderResult[] call() throws OperationApplicationException {
                return SampleContentProvider.super.applyBatch(operations);
            }
        });
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] valuesArray) {
        switch (MATCHER.match(uri)) {
            case CODE_CHEESE_DIR:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final SampleDatabase database = SampleDatabase.getInstance(context);
                final Cheese[] cheeses = new Cheese[valuesArray.length];
                for (int i = 0; i < valuesArray.length; i++) {
                    cheeses[i] = Cheese.fromContentValues(valuesArray[i]);
                }
                return database.cheese().insertAll(cheeses).length;
            case CODE_CHEESE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }*/

}
