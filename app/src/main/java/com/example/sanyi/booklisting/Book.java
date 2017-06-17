package com.example.sanyi.booklisting;

import android.graphics.Bitmap;

/**
 * Created by Sanyi on 17/06/2017.
 */

public class Book {

    private String title;
    private String description;
    private String author;
    private Bitmap image;

    public Book(Bitmap image, String author, String title, String description){
        this.image=image;
        this.author=author;
        this.title=title;
        this.description=description;

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public String getAuthor() {
        return author;
    }

    public Bitmap getImage() {
        return image;
    }
}
