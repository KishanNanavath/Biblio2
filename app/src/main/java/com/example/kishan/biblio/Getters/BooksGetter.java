package com.example.kishan.biblio.Getters;

import java.io.Serializable;

/**
 * Created by Kishan on 2/16/2016.
 */
public class BooksGetter implements Serializable {
    public int rowId;
    public String selfLink;
    public String title;
    public String authors;
    public String rating;
    public String publishDate;
    public String categories;
    public String imageLinks;
    public String language;
    public String description;
    public byte[] imageByteArray;

    public BooksGetter(int rowID,String selfLink,String title, String authors, String rating, String publishDate, String categories, String imageLinks, String language, String description,byte[] byteArray) {
        this.rowId = rowID;
        this.selfLink = selfLink;
        this.title = title;
        this.authors = authors;
        this.rating = rating;
        this.publishDate = publishDate;
        this.categories = categories;
        this.imageLinks = imageLinks;
        this.language = language;
        this.description = description;
        this.imageByteArray=byteArray;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public int getRowId() {
        return rowId;
    }

    public byte[] getImageByteArray() {
        return imageByteArray;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getRating() {
        return rating;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getCategories() {
        return categories;
    }

    public String getImageLinks() {
        return imageLinks;
    }

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }

}

