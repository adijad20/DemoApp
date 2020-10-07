package com.example.demoapp;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class ImageFile {
    private final Uri uri;
    private final String title;
    private final String path;

    public ImageFile(Uri uri, String title, String path){
        this.uri = uri;
        this.title = title;
        this.path = path;
    }

    public String getTitle(){
        return this.title;
    }
    public Uri getUri() {
        return this.uri;
    }

    public String getPath(){
        return this.path;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static List<ImageFile> getImageFiles(Activity activity){
        List<ImageFile> images = new ArrayList<ImageFile>();
        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME};

        try (Cursor cursor = activity.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        )){
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int indexDataColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            int folderNameIndexColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

            Log.i("MyImageMediaStore","Number of rows returned: "+Integer.toString(cursor.getCount()));
            while(cursor.moveToNext()){
                long id = cursor.getLong(idColumn);
                String path = cursor.getString(indexDataColumn);
                String name = cursor.getString(displayNameColumn);
                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,id);


                images.add(new ImageFile(contentUri, name, path));
            }
        }
        return images;
    }
}
