package com.example.szage.mytopmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract for the movies, defines table and column names for movie database
 */

public class MovieContract {

    public MovieContract() {
        super();
    }

    /**
     * The "Content authority" is a name for the entire content provider
     */
    public static final String CONTENT_AUTHORITY = "com.example.szage.mytopmovies";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path that can be appended to BASE_CONTENT_URI to form valid URI's that MyTopMovies
     * can handle.
     */
    public static final String PATH_MOVIE = "mytopmovies";

    /**
     * Inner class that defines constant values for the movie database table.
     * Each entry in the table represents a single movie.
     */
    public static final class MovieEntry implements BaseColumns {

        /** The content URI to access the movie data in the provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;


        /** Name of database table for movies */
        public static final String TABLE_NAME = "movie";

        /** Id for each movie
         *
         * Type: INTEGER
         */
        public static final String COLUMN_MOVIE_ID = "movie_id";

        /** Title of the movie
         *
         * Type: TEXT
         */
        public static final String COLUMN_TITLE = "title";

        /** Release date of the movie
         *
         * Type: TEXT
         */
        public static final String COLUMN_RELEASE = "release_date";

        /** Poster path of the movie's poster
         *
         * Type: TEXT
         */
        public static final String COLUMN_POSTER_PATH = "poster_path";

        /** Rating of the movie
         *
         * Type: INTEGER
         */
        public static final String COLUMN_RATING = "vote_average";

        /** Overview of the movie
         *
         * Type: TEXT
         */
        public static final String COLUMN_OVERVIEW = "overview";
    }
}
