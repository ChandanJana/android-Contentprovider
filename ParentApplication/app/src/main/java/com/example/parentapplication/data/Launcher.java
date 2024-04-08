package com.example.parentapplication.data;

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
@Entity(tableName = Launcher.TABLE_NAME)
public class Launcher {
    /** The name of the Cheese table. */
    public static final String TABLE_NAME = "launcher";

    /** The name of the ID column. */
    public static final String LAUNCHER_ID = BaseColumns._ID;

    /** The name of the name column. */
    public static final String LAUNCHER_NAME = "name";

    /** The name of the name column. */
    public static final String IS_DEMO = "isdemo";

    /** The unique ID of the cheese. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = LAUNCHER_ID)
    public long id;

    /** The name of the cheese. */
    @ColumnInfo(name = LAUNCHER_NAME)
    public String name;

    /** The name of the cheese. */
    @ColumnInfo(name = IS_DEMO)
    public boolean isDemo;

    /**
     * Create a new {@link Cheese} from the specified {@link ContentValues}.
     *
     * @param values A {@link ContentValues} that at least contain {@link #LAUNCHER_NAME}.
     * @return A newly created {@link Cheese} instance.
     */
    @NonNull
    public static Launcher fromContentValues(@Nullable ContentValues values) {
        final Launcher launcher = new Launcher();
        if (values != null && values.containsKey(LAUNCHER_ID)) {
            launcher.id = values.getAsLong(LAUNCHER_ID);
        }
        if (values != null && values.containsKey(LAUNCHER_NAME)) {
            launcher.name = values.getAsString(LAUNCHER_NAME);
        }
        if (values != null && values.containsKey(IS_DEMO)) {
            launcher.isDemo = Boolean.parseBoolean(values.getAsString(IS_DEMO));
        }
        return launcher;
    }
}
