package com.example.demoapp;

import android.net.Uri;

public class AudioFile {
    private final Uri uri;
    private final String title;
    private final int duration;
    private final int size;
    private final String artist;

    public AudioFile(Uri uri, String title, int duration, int size, String artist){
        this.uri = uri;
        this.title = title;
        this.duration = duration;
        this. size = size;
        this.artist = artist;
    }

    public String getTitle(){
        return this.title;
    }
    public Uri getUri() {
        return this.uri;
    }
}

