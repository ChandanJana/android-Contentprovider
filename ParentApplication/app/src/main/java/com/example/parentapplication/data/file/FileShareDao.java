package com.example.parentapplication.data.file;

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

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.parentapplication.data.Launcher;


/**
 * Data access object for Cheese.
 */
@Dao
public interface FileShareDao {

    /**
     * Counts the number of cheeses in the table.
     *
     * @return The number of cheeses.
     */
    @Query("SELECT COUNT(*) FROM " + FileShare.TABLE_NAME)
    int count();

    /**
     * Inserts a cheese into the table.
     *
     * @param fileShare A new cheese.
     * @return The row ID of the newly inserted cheese.
     */
    @Insert
    long insert(FileShare fileShare);

    /**
     * Inserts multiple cheeses into the database
     *
     * @param fileShares An array of new launchers.
     * @return The row IDs of the newly inserted cheeses.
     */
    @Insert
    long[] insertAll(FileShare[] fileShares);

    /**
     * Select all cheeses.
     *
     * @return A {@link Cursor} of all the cheeses in the table.
     */
    @Query("SELECT * FROM " + FileShare.TABLE_NAME)
    Cursor selectAll();

    /**
     * Select a cheese by the ID.
     *
     * @param name The row ID.
     * @return A {@link Cursor} of the selected cheese.
     */
    @Query("SELECT * FROM " + FileShare.TABLE_NAME + " WHERE " + FileShare.FILE_NAME + " = :name")
    Cursor selectById(String name);

    /**
     * Delete a cheese by the ID.
     *
     * @param id The row ID.
     * @return A number of cheeses deleted. This should always be {@code 1}.
     */
    @Query("DELETE FROM " + FileShare.TABLE_NAME + " WHERE " + FileShare.FILE_ID + " = :id")
    int deleteById(long id);

    /**
     * Update the cheese. The cheese is identified by the row ID.
     *
     * @param fileShare The launcher to update.
     * @return A number of cheeses updated. This should always be {@code 1}.
     */
    @Update
    int update(FileShare fileShare);

}
