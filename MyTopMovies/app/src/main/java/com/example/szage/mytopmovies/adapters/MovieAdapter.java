package com.example.szage.mytopmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.szage.mytopmovies.R;
import com.example.szage.mytopmovies.models.Review;
import com.example.szage.mytopmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link MovieAdapter} exposes a list of reviews and trailers for movies
 * to a {@link android.support.v7.widget.RecyclerView}.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_TRAILER = 0;
    private static final int VIEW_TYPE_REVIEW = 1;

    private static final String TAG = MovieAdapter.class.getSimpleName();

    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    // List of movie trailers
    private List<Trailer> mTrailers;
    // List of movie reviews
    private List<Review> mReviews;

    /**
     *
     * @param context Used to talk to the UI and app resources
     * @param trailers movie trailers for current movie
     * @param reviews movie reviews for current movie
     */
    public MovieAdapter(@NonNull Context context, List<Trailer> trailers, List<Review> reviews) {
        mContext = context;
        mTrailers = trailers;
        mReviews = reviews;
    }

    /**
     *
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType fpr different viewTypes use this viewType integer to provide a different layout.
     * @return A new ViewHolder that holds the View for each list item
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {

            case VIEW_TYPE_TRAILER:
                return new TrailerViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.trailer_list_item, parent, false));

            case VIEW_TYPE_REVIEW:
                return new ReviewViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.review_list_item, parent, false));

            default:
                throw new IllegalArgumentException("Invalid view type, value of" + viewType);
        }
    }

    /**
     * Called by the RecyclerView to display the data at the specified
     * position.
     *
     * @param movieAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder movieAdapterViewHolder, int position) {
        int viewType = getItemViewType(position);

        switch (viewType) {

            case VIEW_TYPE_TRAILER:
                ((TrailerViewHolder) movieAdapterViewHolder).bindTrailerData(position);
                break;

            case VIEW_TYPE_REVIEW:
                ((ReviewViewHolder) movieAdapterViewHolder).bindReviewData(position);
                break;

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
    }

    /**
     * This method simply returns the number of items to display.
     *
     * @return The number of items available
     */
    @Override
    public int getItemCount() {
        return mTrailers.size() + mReviews.size();
    }

    /**
     * Returns an integer code related to the type of View we want the ViewHolder to be at a given
     * position.
     *
     * @param position index within our RecyclerView
     * @return the view type
     */
    @Override
    public int getItemViewType(int position) {
        if (position < mTrailers.size()) {
            return VIEW_TYPE_TRAILER;
        } else return VIEW_TYPE_REVIEW;
    }

    /**
     * A ViewHolder class for Trailers defining needed methods
     */
    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trailer_icon) ImageView playImage;
        @BindView(R.id.trailer_name) TextView trailerText;
        String movieKey;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Binding all the needed trailer data to the view holder
         *
         * @param position The position of the item within the adapter's data set.
         */
        public void bindTrailerData(final int position) {
            Picasso.with(mContext).load(mTrailers.get(position).getKey()).into(playImage);
            trailerText.setText(mTrailers.get(position).getName());
            movieKey = mTrailers.get(position).getKey().toString();
            trailerOnclick();
        }

        /**
         * Once the list item it being clicked it starts an intent to play the selected trailer
         * on YouTube
         */
        public void trailerOnclick() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                    trailerIntent.setData(Uri.parse("https://m.youtube.com/watch?v=" + movieKey));
                    if (trailerIntent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(trailerIntent);
                    }
                }
            });
        }
    }

    /**
     * A ViewHolder class for Reviews defining needed methods
     */
    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.author) TextView authorText;
        @BindView(R.id.content) TextView contentText;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Binding all the needed reviews data to the view holder
         *
         * @param position The position of the item within the adapter's data set.
         */
        public void bindReviewData(final int position) {
            authorText.setText(mReviews.get(position - mTrailers.size()).getAuthor());
            contentText.setText(mReviews.get(position - mTrailers.size()).getContent());
        }
    }
}
