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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetDBHelper;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements PetAdapter.ItemClickListener {
    private SQLiteDatabase mDb;

    private PetDBHelper mDbHelper;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PetAdapter mAdapter;
    private Cursor mCursor;

    private Toast mToast;


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

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PetAdapter(this, readFromDataBase(), this);
        mRecyclerView.setAdapter(mAdapter);

        mCursor = readFromDataBase();


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                removeItem(id);
                mAdapter.swapCursor(CatalogActivity.this, readFromDataBase());
            }
        });

        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    @Override
    protected void onStart() {
        super.onStart();
        readFromDataBase();
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
                mAdapter.swapCursor(CatalogActivity.this, readFromDataBase());
                /*We need to assign read from database to mCursor when an item is clicked
                * So that when we add an item our onClick() method is not still using the old version of data*/
                mCursor = readFromDataBase();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                deleteAllPets();
                mAdapter.swapCursor(this, readFromDataBase());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addDummyData() {
        mDb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.PET_NAME, "Buster");
        values.put(PetContract.PetEntry.PET_BREED, "Doberman Pinscher");
        values.put(PetContract.PetEntry.PET_GENDER, 1);
        values.put(PetContract.PetEntry.PET_WEIGHT, 15);

        long newRowId = mDb.insert(PetContract.PetEntry.TABLE_NAME, null, values);
    }

    private Cursor readFromDataBase() {
        mDb = mDbHelper.getReadableDatabase();
        String[] projection = {
                PetContract.PetEntry._ID,
                PetContract.PetEntry.PET_NAME,
                PetContract.PetEntry.PET_BREED,
                PetContract.PetEntry.PET_GENDER,
                PetContract.PetEntry.PET_WEIGHT};

        Cursor cursor = mDb.query(
                PetContract.PetEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        TextView displayTextView = (TextView) findViewById(R.id.text_view_pet);
        displayTextView.setText("Total pets in the database: " + cursor.getCount());


        return cursor;
    }

    private void deleteAllPets() {
        mDb.delete(PetContract.PetEntry.TABLE_NAME, null, null);
    }

    private void removeItem(long id) {
        String selection = PetContract.PetEntry._ID + " = " + id;
        mDb.delete(PetContract.PetEntry.TABLE_NAME, selection, null);
    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(CatalogActivity.this, DetailActivity.class);

        try {

        mCursor.moveToPosition(position);
        int nameIndex = mCursor.getColumnIndex(PetContract.PetEntry.PET_NAME);
        String name = mCursor.getString(nameIndex).toString();
        intent.putExtra(Intent.EXTRA_TEXT, name);
        startActivity(intent);

        } finally {

        mCursor.close();

        }
    }
}
