package com.example.demoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageFullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);
        ImageView imageView = findViewById(R.id.selectedImage);
        Intent intent =getIntent();
        imageView.setImageURI(Uri.parse(intent.getStringExtra("imageUri")));
    }
}