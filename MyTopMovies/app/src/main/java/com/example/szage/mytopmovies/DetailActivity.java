package com.example.szage.mytopmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.szage.mytopmovies.retrofit.ApiClient;
import com.example.szage.mytopmovies.retrofit.ReviewInterface;
import com.example.szage.mytopmovies.retrofit.TrailerInterface;
import com.example.szage.mytopmovies.adapters.MovieAdapter;
import com.example.szage.mytopmovies.data.MovieContract;
import com.example.szage.mytopmovies.data.MovieDbHelper;
import com.example.szage.mytopmovies.models.Review;
import com.example.szage.mytopmovies.models.ReviewResponse;
import com.example.szage.mytopmovies.models.Trailer;
import com.example.szage.mytopmovies.models.TrailerResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    private List<Trailer> mTrailers;

    private List<Review> mReviews;

    @BindView(R.id.movie_title) TextView titleTextView;

    String mTitle;

    @BindView(R.id.release_date) TextView releaseTextView;

    String mRelease;

    @BindView(R.id.movie_rating) TextView ratingTextView;

    String mRating;

    @BindView(R.id.poster_image) ImageView pictureImageView;

    String mPoster;

    @BindView(R.id.movie_overview) TextView overviewTextView;

    String mOverview;

    @BindView(R.id.favorite_floatingActionButton)

    FloatingActionButton favoriteButton;

    @BindView(R.id.movie_detail_recyclerview) RecyclerView mRecyclerView;

    private int mId;

    private int mPosition;

    private MovieAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    // Please don't forget to write your API key here
    private final static String API_KEY = "";

    private ArrayList<String> authors = new ArrayList<String >();

    private ArrayList<String> contents = new ArrayList<String >();

    private ArrayList<String> names = new ArrayList<String >();

    private ArrayList<String> keys = new ArrayList<String >();

    ArrayList<Integer> favoriteMoviesIds = new ArrayList<Integer>();

    ArrayList<String> favoriteMoviesTitles = new ArrayList<String >();

    ArrayList<String> favoriteMoviesPosters = new ArrayList<String >();

    ArrayList<String> favoriteMoviesReleaseDates = new ArrayList<String >();

    ArrayList<String> favoriteMoviesRatings = new ArrayList<String >();

    ArrayList<String> favoriteMoviesOverviews = new ArrayList<String >();

    private boolean dataCallFlag = false;

    private MovieDbHelper dbHelper;

    private Uri currentMovieUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        databaseChecker();

        // determines whether the movie is stored in the database
        boolean movieType = MainActivity.getMovieType();

        // create new instance of LinearLayoutManager
        mLayoutManager = new LinearLayoutManager(DetailActivity.this);

        // setting up the LayoutManager on recycler view
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.missing_api_key,
                    Toast.LENGTH_LONG).show();
        }

        // Get the intent which started this activity
        Intent whichStartedDetailsActivity = getIntent();
        currentMovieUri = whichStartedDetailsActivity.getData();
        // Gt the Bundle with all the extra data passed for this activity
        Bundle extras = whichStartedDetailsActivity.getExtras();

        // in case of movie type is true (regular movie)
        if (movieType == true) {

            //If it has other value than null, get all data and display them in the views
            if (whichStartedDetailsActivity != null) {
                mTitle = extras.getString(String.valueOf(R.string.title_key));
                titleTextView.setText(mTitle);
                mPoster = extras.getString(String.valueOf(R.string.poster_path_key));
                Picasso.with(this).load("http://image.tmdb.org/t/p/w185" + mPoster).into(pictureImageView);
                mRelease = extras.getString(String.valueOf(R.string.release_key));
                releaseTextView.setText(mRelease);
                mRating = extras.getString(String.valueOf(R.string.rating_key));
                ratingTextView.setText(mRating);
                mOverview = extras.getString(String.valueOf(R.string.overview_key));
                overviewTextView.setText(mOverview);
                mId = extras.getInt(String.valueOf(R.string.id_key));
            }
        } else {
            // in case of movie type is false (favorite movie)
            // If it has other value than null, get the position of the movie and
            // displayFavoriteMovieDetails gets called
            if (whichStartedDetailsActivity != null) {
                mPosition = extras.getInt(String.valueOf(R.string.position_key));
                displayFavoriteMovieDetails();
            }
        }

        /**
         * Once the favorite button is clicked for the first time the particular movie will be
         * added to the favorites (database).
         * If it clicked for the second time, it will delete the movie from the favorite movies
         */
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "id is " + mId);

                Log.i(TAG, "favoriteMoviesIds" + favoriteMoviesIds);

                if (favoriteMoviesIds.contains(mId)){
                    deleteFavoriteMovie();
                    // checks whether mId (movie) is already in the database
                    databaseChecker();
                    Log.i(TAG, "movie deleted ");
                } else {
                    insertFavoriteMovie();
                    // checks whether mId (movie) is already in the database
                    databaseChecker();
                    Log.i(TAG, "movie saved ");
                }
            }
        });

        // In case of a regular movie, it also shows reviews and trailers for the movie
        if (movieType == true) {
            showReviewData();
            showTrailerData();
        }
    }

    /**
     * Inserts a new row into the movie database where favorite movies are stored
     */
    private void insertFavoriteMovie() {
        mTitle.trim();
        mPoster.trim();
        mRelease.trim();
        mRating.trim();
        mOverview.trim();

        ContentValues values = new ContentValues();

        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mId);
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, mTitle);
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE, mRelease);
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, mPoster);
        values.put(MovieContract.MovieEntry.COLUMN_RATING, mRating);
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mOverview);

        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);

        Toast.makeText(this, R.string.movie_added, Toast.LENGTH_SHORT).show();
    }

    /**
     * Perform the deletion of the movie in the database.
     */
    private void deleteFavoriteMovie() {
        if (currentMovieUri != null) {
            int rowsDeleted = getContentResolver().delete(currentMovieUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, R.string.delete_error, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "movie not deleted ");
            } else {
                Toast.makeText(this, R.string.movie_deleted, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "movie not deleted ");
            }
        }

        finish();
    }

    /**
     * Checks whether mId (movie id) is already in the database.
     * It also adds the existing data into ArrayLists  to be able to find favorite movie details
     * for displaying in case of a favorite movie is shown.
     */
    private void databaseChecker() {

        dbHelper = new MovieDbHelper(getBaseContext());

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(MovieContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);

        startManagingCursor(cursor);

        while (cursor.moveToNext()) {

            int titleColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
            String movieTitle = cursor.getString(titleColumnIndex);
            Log.i(TAG, movieTitle);

            int releaseColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE);
            String movieRelease = cursor.getString(releaseColumnIndex);

            int pictureColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
            String moviePicture = cursor.getString(pictureColumnIndex);

            int ratingColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
            String movieRating = cursor.getString(ratingColumnIndex);

            int overviewColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
            String movieOverview = cursor.getString(overviewColumnIndex);

            int idColumnIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            int movieId = cursor.getInt(idColumnIndex);

            favoriteMoviesTitles.add(movieTitle);
            favoriteMoviesPosters.add(moviePicture);
            favoriteMoviesReleaseDates.add(movieRelease);
            favoriteMoviesRatings.add(movieRating);
            favoriteMoviesOverviews.add(movieOverview);
            favoriteMoviesIds.add(movieId);

            dbHelper.close();
        }
        if (mId != 0) {
            mId = favoriteMoviesIds.get(mPosition);
        }


    }

    // Displays all favorite movie details
    private void displayFavoriteMovieDetails() {
        mTitle = favoriteMoviesTitles.get(mPosition);
        Log.i(TAG, mTitle);
        titleTextView.setText(mTitle);
        mPoster = favoriteMoviesPosters.get(mPosition);
        Picasso.with(this).load("http://image.tmdb.org/t/p/w185" + mPoster).into(pictureImageView);
        mRelease = favoriteMoviesReleaseDates.get(mPosition);
        releaseTextView.setText(mRelease);
        mRating = favoriteMoviesRatings.get(mPosition);
        ratingTextView.setText(mRating);
        mOverview = favoriteMoviesOverviews.get(mPosition);
        overviewTextView.setText(mOverview);
    }

    // Make sure the adapter starts to work after having all details needed
    private void runWhenReady()
    {
        mAdapter = new MovieAdapter(DetailActivity.this, mTrailers, mReviews);

        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     *  This method calls the movie server and gets back requested trailers of the movies and
     *  lists them
     */
    private void showTrailerData() {
        TrailerInterface trailerApiService = ApiClient.getClient().create(TrailerInterface.class);

        Call<TrailerResponse> callForTrailers = trailerApiService.getMovieTrailers(String.valueOf(mId), API_KEY);
        callForTrailers.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {

                if (response.isSuccessful()) {
                    mTrailers = response.body().getTrailerResults();

                    for (int i = 0; i < mTrailers.size(); i++) {
                        names.add(i, mTrailers.get(i).getName());
                        keys.add(i, mTrailers.get(i).getKey());
                    }
                    if (dataCallFlag) {
                        runWhenReady();
                    }
                    else {
                        dataCallFlag = true;
                    }
                }
            }

            // Handles failure with server communication
            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable error) {
                Log.e(TAG, R.string.retrofit_fail_error + error.toString());
            }
        });
    }

    /**
     *  This method calls the movie server and gets back requested reviews of the movies and
     *  lists them
     */
    private void showReviewData() {
        ReviewInterface reviewApiService = ApiClient.getClient().create(ReviewInterface.class);

        Call<ReviewResponse> callForReviews = reviewApiService.getMovieReviews(String.valueOf(mId), API_KEY);
        callForReviews.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {

                if (response.isSuccessful()) {
                    mReviews = response.body().getReviewResults();

                    for (int i = 0; i < mReviews.size(); i++) {
                        authors.add(i, mReviews.get(i).getAuthor());
                        contents.add(i, mReviews.get(i).getAuthor());
                    }
                    if (dataCallFlag) {
                        runWhenReady();
                    }
                    else {
                        dataCallFlag = true;
                    }
                }
            }

            // Handles failure with server communication
            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable error) {
                Log.e(TAG, R.string.retrofit_fail_error + error.toString());
            }
        });
    }
}
