package com.example.szage.mytopmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.szage.mytopmovies.R;
import com.example.szage.mytopmovies.data.MovieContract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * {@link FavoriteCursorAdapter} is an adapter for grid view
 * that uses a {@link Cursor} of favorite movie data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}
 */

public class FavoriteCursorAdapter extends CursorAdapter{

    public FavoriteCursorAdapter(Context context, Cursor cursor) {

        super(context, cursor, 0);
    }

    /**
     *
     * @param context is app context
     * @param cursor The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent The parent to which the new view is attached to
     * @return the newly created item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.activity_main, parent, false);
    }

    /**
     *
     * @param view Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find individual view that want to modify in the item layout
        ImageView moviePosterImage = (ImageView) view.findViewById(R.id.poster_image_view);
        // Find the column of movie attributes
        int posterColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        // Read the movie attribute from the Cursor for the current movie
        String moviePosterPath = cursor.getString(posterColumnIndex);
        // Displaying the movie poster
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + moviePosterPath).into(moviePosterImage);
    }
}
