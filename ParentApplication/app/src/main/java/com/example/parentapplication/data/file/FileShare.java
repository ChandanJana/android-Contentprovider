package com.example.parentapplication.data.file;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Chandan Jana on 28-10-2022.
 * Company name: Mindteck
 * Email: chandan.jana@mindteck.com
 */
@Entity(tableName = FileShare.TABLE_NAME)
public class FileShare {
    /** The name of the Cheese table. */
    public static final String TABLE_NAME = "fileshare";

    /** The name of the ID column. */
    public static final String FILE_ID = BaseColumns._ID;

    /** The name of the name column. */
    public static final String FILE_URI = "secure_file_uri";

    /** The name of the name column. */
    public static final String FILE_NAME = "secure_file_name";

    /** The name of the name column. */
    public static final String FILE_DIR = "secure_is_dir";

    /** The name of the name column. */
    public static final String FILE_CRC = "secure_file_crc";

    /** The unique ID of the cheese. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = FILE_ID)
    public long id;

    /** The name of the cheese. */
    @ColumnInfo(name = FILE_NAME)
    public String name;

    /** The name of the cheese. */
    @ColumnInfo(name = FILE_DIR)
    public String dir;

    /** The name of the cheese. */
    @ColumnInfo(name = FILE_CRC)
    public String crc;

    /** The name of the cheese. */
    @ColumnInfo(name = FILE_URI)
    public String uri;


    @NonNull
    public static FileShare fromContentValues(@Nullable ContentValues values) {
        final FileShare launcher = new FileShare();
        if (values != null && values.containsKey(FILE_ID)) {
            launcher.id = values.getAsLong(FILE_ID);
        }
        if (values != null && values.containsKey(FILE_NAME)) {
            launcher.name = values.getAsString(FILE_NAME);
        }
        if (values != null && values.containsKey(FILE_URI)) {
            launcher.uri = values.getAsString(FILE_URI);
        }
        if (values != null && values.containsKey(FILE_CRC)) {
            launcher.crc = values.getAsString(FILE_CRC);
        }

        if (values != null && values.containsKey(FILE_DIR)) {
            launcher.dir = values.getAsString(FILE_DIR);
        }
        return launcher;
    }
}
