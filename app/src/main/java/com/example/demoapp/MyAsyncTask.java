package com.example.demoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Adapter;

import java.lang.ref.WeakReference;

public class MyAsyncTask extends AsyncTask<Void,Void,String> {
    private SQLiteDatabase db;
    private MyDatabaseHelper myDatabaseHelper;
    private WeakReference<Context> contextRef;
    private static final String TAG = "MyAsyncTask";
    private StringBuffer buffer;

    public MyAsyncTask(Context context){
        contextRef = new WeakReference<>(context);
    }

    @Override
    protected String doInBackground(Void... voids) {
        Context context = contextRef.get();
        if(context != null) {
            myDatabaseHelper = new MyDatabaseHelper(context);
            db = myDatabaseHelper.getReadableDatabase();
            String[] projection = {
                    BaseColumns._ID,
                    MyDataBaseContract.MyDatabase.COLUMN_NAME_NAME,
                    MyDataBaseContract.MyDatabase.COLUMN_NAME_DESIGNATION,
                    MyDataBaseContract.MyDatabase.COLUMN_NAME_URI
            };
            Cursor cursor = db.query(
                    MyDataBaseContract.MyDatabase.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );

            buffer = new StringBuffer();
            while(cursor.moveToNext()){
                int empID = cursor.getInt(cursor.getColumnIndex(MyDataBaseContract.MyDatabase._ID));
                String name = cursor.getString(cursor.getColumnIndex(MyDataBaseContract.MyDatabase.COLUMN_NAME_NAME));
                String designation = cursor.getString(cursor.getColumnIndex(MyDataBaseContract.MyDatabase.COLUMN_NAME_DESIGNATION));
                String uri = cursor.getString(cursor.getColumnIndex(MyDataBaseContract.MyDatabase.COLUMN_NAME_URI));
                buffer.append(empID+" "+name+" "+designation+" "+uri+"\n");
            }
            Log.i(TAG,buffer.toString());
        }
        return buffer.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        db.close();
        super.onPostExecute(s);
    }
}

