/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetDBHelper;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {
    private SQLiteDatabase mDb;

    PetDBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new PetDBHelper(this);

        mDb = mDbHelper.getWritableDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
                addDummyData();
                readFromDataBase();


                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addDummyData() {
        mDb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.PET_NAME, "Bear");
        values.put(PetContract.PetEntry.PET_BREED, "Great Dane");
        values.put(PetContract.PetEntry.PET_GENDER, 1);
        values.put(PetContract.PetEntry.PET_WEIGHT, 15);

        long newRowId = mDb.insert(PetContract.PetEntry.TABLE_NAME, null, values);
    }

    private void readFromDataBase() {
        mDb = mDbHelper.getReadableDatabase();
        String[] projection = {
                PetContract.PetEntry._ID,
                PetContract.PetEntry.PET_NAME};

        Cursor cursor = mDb.query(
                PetContract.PetEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );


        TextView display = (TextView) findViewById(R.id.text_view_pet);
        try {
            display.setText("Total values entered: " + cursor.getCount() + "\n");
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                int nameIndex = cursor.getColumnIndex(PetContract.PetEntry.PET_NAME);
                String name = cursor.getString(nameIndex);
                display.append("\n" + name);
            }
        } finally {
            cursor.close();
        }
    }
}
