package com.example.buoi_8;

import android.net.Uri;

public class MusicList {
    String title;
    Uri file;

    public MusicList(String title, Uri file) {
        this.title = title;
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getFile() {
        return file;
    }

    public void setFile(Uri file) {
        this.file = file;
    }
}
