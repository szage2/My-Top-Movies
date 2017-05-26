package com.example.szage.mytopmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by szage on 2017. 03. 22..
 */

public class MovieResponse {

    // List of the movies (result of server communication)
    @SerializedName("results")
    private ArrayList<Movie> movieResults;

    /**
     * @return list of movies
     */
    public ArrayList<Movie> getMovieResults() {
        return movieResults;
    }
}
