package com.example.szage.mytopmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by szage on 2017. 03. 22..
 */

public class Trailer {

    // Title of the trailer
    @SerializedName("name")
    private String mName;

    // key of the trailer for youtube page
    @SerializedName("key")
    private String mKey;

    /**
     * Constructor
     *
     * @param name of the trailer
     * @param key for the youtube site finding the video
     */
    public Trailer(String name, String key) {
        mName = name;
        mKey = key;
    }

    /**
     * @return the name of the trailer
     */
    public String getName() { return mName; }
    /**
     * @return the key for the trailer
     */
    public String getKey() { return mKey; }
}
