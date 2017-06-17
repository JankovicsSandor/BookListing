package com.example.sanyi.booklisting;

import android.graphics.Bitmap;

/**
 * Created by Sanyi on 17/06/2017.
 */

public class Book {

    private String title;
    private String author;
    private String description;
    private String publishDate;
    private Bitmap image;
    private String onClinkLink;

    public Book(Bitmap image,String title,String author,String publishDate, String description,String onClickLink){
        this.image=image;
        this.title=title;
        this.author=author;
        this.publishDate =publishDate;
        this.description=description;
        this.onClinkLink=onClickLink;

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public String getPublishDate() {
        return publishDate;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getOnClinkLink() {
        return onClinkLink;
    }

    public String getAuthor() {
        return author;
    }
}
