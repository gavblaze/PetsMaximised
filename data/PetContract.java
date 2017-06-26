package com.example.android.pets.data;

import android.provider.BaseColumns;

/**
 * Created by Gavin on 26-Jun-17.
 */

public final class PetContract {
    private PetContract() {}

    public static class PetEntry implements BaseColumns {
        public static final String TABLE_NAME = "pets";
        public static final String PET_NAME = "name";
        public static final String PET_BREED = "breed";
        public static final String PET_GENDER = "gender";
        public static final String PET_WEIGHT = "weight";
    }
}
