package com.example.demoapp;

import android.provider.BaseColumns;

public final class MyDataBaseContract {
    private MyDataBaseContract(){

    }

    public static class MyDatabase implements BaseColumns{
        public static final String TABLE_NAME = "employee";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESIGNATION = "age";
        public static final String COLUMN_NAME_URI = "image_uri";
    }
}
