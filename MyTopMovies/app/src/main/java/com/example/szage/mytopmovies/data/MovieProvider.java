package com.example.szage.mytopmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.szage.mytopmovies.R;

/**
 * This class serves the content provider for MyTopMovies data.
 * Enables querying, inserting and deleting data.
 */

public class MovieProvider extends ContentProvider{

    /** URI matcher code for the content URI for the movie table */
    public static final int ALL_MOVIES = 100;
    /** URI matcher code for the content URI for a single movie */
    public static final int MOVIE_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     */
    private static final UriMatcher mUriMatcher = buildUriMatcher();
    private MovieDbHelper mdbHelper;


    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, ALL_MOVIES);

        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIE_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mdbHelper = new MovieDbHelper(getContext());
        return true;
    }

    /**
     * Implements ContentProvider.query()
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // Get readable database
        SQLiteDatabase database = mdbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = mUriMatcher.match(uri);
        switch (match) {

            case ALL_MOVIES:
                cursor = database.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null, sortOrder);
                break;

            case MOVIE_ID:

                selection = MovieContract.MovieEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new IllegalArgumentException(String.valueOf(R.string.unknown_uri) + uri);
        }

        // If the data at this URI changes, need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = mUriMatcher.match(uri);

        switch (match) {
            case ALL_MOVIES:
                return MovieContract.MovieEntry.CONTENT_LIST_TYPE;
            case MOVIE_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException(String.valueOf(R.string.unknown_uri) + uri);
        }
    }

    /**
     * Insert new row into the provider
     * @param uri for the newly-inserted row
     * @param values added to the new row
     * @return the content uri
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int match = mUriMatcher.match(uri);
        switch (match) {
            case ALL_MOVIES:
                return insertMovie(uri, values);
            default:
                throw new IllegalArgumentException(String.valueOf(R.string.not_supported) + uri);
        }
    }

    private Uri insertMovie(Uri uri, ContentValues values) {

        SQLiteDatabase database = mdbHelper.getWritableDatabase();

        long id = database.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
        if (id == -1) {
            throw new IllegalArgumentException(String.valueOf(R.string.id_required));
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase database = mdbHelper.getWritableDatabase();

        int rowsDeleted;
        final int match = mUriMatcher.match(uri);

        switch (match) {
            // Deleting all rows
            case ALL_MOVIES:
                rowsDeleted = database.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            // Deleting a single row
            case MOVIE_ID:

                selection = MovieContract.MovieEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                rowsDeleted = database.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException(String.valueOf(R.string.unsupported_operation) + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException(String.valueOf(R.string.runtime_error));
    }
}
