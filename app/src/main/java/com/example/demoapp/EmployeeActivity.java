package com.example.demoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EmployeeActivity extends AppCompatActivity {
    private static final String TAG = "MyDatabaseActivity";
    private static String empName;
    private static String empDesignation;
    private static String imgURi;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase db1;
    Messenger mService;
    boolean bound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        Button pickButton = findViewById(R.id.chooseImageButton);
        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto,0);
            }
        });

        myDatabaseHelper = new MyDatabaseHelper(this);
        Button submitButton = findViewById(R.id.addRowButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db1 = myDatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                EditText nameText = (EditText) findViewById(R.id.empName);
                empName = nameText.getText().toString();
                EditText desigText = (EditText) findViewById(R.id.empDesignation);
                empDesignation = desigText.getText().toString();
                values.put(MyDataBaseContract.MyDatabase.COLUMN_NAME_NAME, empName);
                values.put(MyDataBaseContract.MyDatabase.COLUMN_NAME_DESIGNATION, empDesignation);
                values.put(MyDataBaseContract.MyDatabase.COLUMN_NAME_URI, imgURi);
                long newRowId = db1.insert(MyDataBaseContract.MyDatabase.TABLE_NAME,null,values);
                Log.i(TAG, "new entry added");
                Log.i(TAG, Long.toString(newRowId));
            }
        });
        Button getDetailsButton = findViewById(R.id.getRowsButton);
        getDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyAsyncTask(EmployeeActivity.this).execute();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
            imgURi = data.getData().toString();
        Log.i(TAG,imgURi);
    }

    @Override
    protected void onDestroy() {
        if(db1!=null)
            db1.close();
        super.onDestroy();

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = new Messenger(iBinder);
            bound = true;
            Log.i(TAG,"Bound to the service");
            Log.i(TAG,"Trying to message to the service, bound: "+bound);
            if(bound) {
                Bundle b = new Bundle();
                b.putString("ActivityName", "Database Activity");
                //Log.i(TAG,"Activity name: "+this.getClass().getSimpleName());
                Message msg = Message.obtain(null, LocalService.MSG_GET_ACTIVITY, 0, 0);
                msg.setData(b);
                try {
                    mService.send(msg);
                    Log.i(TAG,"Message sent to the service");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
            bound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
        bound = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}