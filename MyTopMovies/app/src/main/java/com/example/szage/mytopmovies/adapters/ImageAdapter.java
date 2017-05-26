package com.example.szage.mytopmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.szage.mytopmovies.DetailActivity;
import com.example.szage.mytopmovies.MainActivity;
import com.example.szage.mytopmovies.R;
import com.example.szage.mytopmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * List of movie poster images to display them in MainActivity
 */
public class ImageAdapter extends BaseAdapter{

    private static String TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;

    private ArrayList<String> mPosters;

    /**
     * Creates an ImageAdapter
     *
     * @param context is the current context
     * @param postersList list of movie posters
     */
    public ImageAdapter(Context context, ArrayList<String> postersList) {
        mContext = context;
        mPosters = postersList;
        Log.i("ImageAdapter", "ImageAdapter is working");
    }

    /**
     * @return size of posters' list
     */
    @Override
    public int getCount() {
        if (mPosters != null) {
            Log.i(TAG, "posters size " + mPosters.size());
            return mPosters.size();
        }
        else {
            return 0;
        }
    }

    /**
     * @param position is current poster's place in the items
     * @return list of posters
     */
    @Override
    public Object getItem(int position) {
        Log.i(TAG, "posters position " + position);
        return mPosters;
    }


    /**
     * @param position is current poster's place in the items
     * @return  position
     */
    @Override
    public long getItemId(int position) {
        Log.i(TAG, "posters position " + position);
        return position;
    }

    /**
     * Displays images of the list at the the given position
     *
     * @param position is current poster's place in the items
     * @param convertView The old view to reuse
     * @param parent The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView posterImageView;

        if (convertView == null) {
            posterImageView = new ImageView(mContext);

        } else {
            posterImageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185" + mPosters.get(position)).into(posterImageView);
        Log.i(TAG, "posters in adapter " + String.valueOf(mPosters));
        return posterImageView;
    }
}
