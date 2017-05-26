package com.example.szage.mytopmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by szage on 2017. 03. 22..
 */

public class Review {

    // Author of the review
    @SerializedName("author")
    private String mAuthor;

    // Content of the review
    @SerializedName("content")
    private String mContent;

    public Review(String author, String content) {
        mAuthor = author;
        mContent = content;
    }

    /**
     * @return Author of the review
     */
    public String getAuthor() { return mAuthor; }

    /**
     * @return Content of the review
     */
    public String getContent() { return mContent; }
}
