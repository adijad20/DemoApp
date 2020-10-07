package com.example.demoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyDatabase.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MyDataBaseContract.MyDatabase.TABLE_NAME + " (" +
                    MyDataBaseContract.MyDatabase._ID + " INTEGER PRIMARY KEY," +
                    MyDataBaseContract.MyDatabase.COLUMN_NAME_NAME + " TEXT," +
                    MyDataBaseContract.MyDatabase.COLUMN_NAME_URI + " TEXT," +
                    MyDataBaseContract.MyDatabase.COLUMN_NAME_DESIGNATION + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MyDataBaseContract.MyDatabase.TABLE_NAME;

    public MyDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }


}
