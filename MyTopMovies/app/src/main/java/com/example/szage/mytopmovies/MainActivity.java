package com.example.szage.mytopmovies;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.szage.mytopmovies.retrofit.ApiClient;
import com.example.szage.mytopmovies.retrofit.PopularMoviesInterface;
import com.example.szage.mytopmovies.retrofit.TopRatedMoviesInterface;
import com.example.szage.mytopmovies.adapters.FavoriteCursorAdapter;
import com.example.szage.mytopmovies.adapters.ImageAdapter;
import com.example.szage.mytopmovies.data.MovieContract;
import com.example.szage.mytopmovies.models.Movie;
import com.example.szage.mytopmovies.models.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.grid_view)
    GridView mGridView;

    private ImageAdapter mImageAdepter;

    private FavoriteCursorAdapter mFavoriteAdapter;

    private ArrayList<Movie> mMovies = new ArrayList<Movie>();

    private ArrayList<String> posters = new ArrayList<String>();

    private ArrayList<String> titles = new ArrayList<String>();

    private ArrayList<String> releaseDates = new ArrayList<String>();

    private ArrayList<String> ratings = new ArrayList<>();

    private ArrayList<String> overviews = new ArrayList<String>();

    private ArrayList<Integer> ids = new ArrayList<Integer>();

    private static boolean mMovieType = true;

    // Please don't forget to write your API key here
    private final static String API_KEY = "";

    private static final int ID_MOVIE_LOADER = 53;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", mMovies);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // create new adapter for displaying movie posters
        mImageAdepter = new ImageAdapter(MainActivity.this, posters);
        // create new adapter for displaying favorite movies' posters
        mFavoriteAdapter = new FavoriteCursorAdapter(this, null);

        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            // if there is no data inside the saveInstanceState bundle sort the movies by popularity
            sortByPopularity();
        } else if (mMovieType == true){
            // in case that the movie type is true (regular movie), save the movies
            mMovies = savedInstanceState.getParcelableArrayList("movies");
            //mImageAdepter = new ImageAdapter(MainActivity.this, posters);
            createListsForDetailElements();
            Log.i(TAG, "movies are " + mMovies);
            Log.i(TAG, "posterPaths are " + posters);
        } else if (mMovieType == false){
            // in case that the movie type is false (favorite movie), set the cursor adapter
            // on the view.
            mGridView.setAdapter(mFavoriteAdapter);
            getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
        }

        /**
         * Set the setOnItemClickListener on the view to start Detail Activity with an intent
         * and pass some movie data
         **/
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                int selected = position;
                Bundle extras = new Bundle();
                if (mMovieType == true) {
                    // passing movie details of the selected movie if the movieType true
                    extras.putString(String.valueOf(R.string.title_key), titles.get(position));
                    extras.putString(String.valueOf(R.string.poster_path_key), posters.get(position));
                    Log.i(TAG, "poster is " + posters.get(position));
                    extras.putString(String.valueOf(R.string.release_key), releaseDates.get(position));
                    extras.putString(String.valueOf(R.string.rating_key), ratings.get(position));
                    extras.putString(String.valueOf(R.string.overview_key), overviews.get(position));
                    extras.putInt(String.valueOf(R.string.id_key), ids.get(position));
                    detailIntent.putExtras(extras);

                } else {
                    // otherwise passing movie's position of the selected favorite movie
                    Uri currentMovieUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                    extras.putInt(String.valueOf(R.string.position_key), selected);
                    detailIntent.setData(currentMovieUri);
                    detailIntent.putExtras(extras);
                }
                // start the intent
                startActivity(detailIntent);
            }
        });

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    R.string.missing_api_key,
                    Toast.LENGTH_LONG).show();
        }
        //sortByPopularity();
    }

    /**
     *  This method calls the movie server and gets back requested details of the movie and
     *  lists movies sorted by popularity
     */
    public void sortByPopularity() {

        PopularMoviesInterface popularApiService = ApiClient.getClient().create(PopularMoviesInterface.class);

        Call<MovieResponse> callForPopMovies = popularApiService.getPopularMovies(API_KEY);
        callForPopMovies.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                if (response.isSuccessful()) {
                    mMovies = response.body().getMovieResults();

                    createListsForDetailElements();

                    // setting up the adapter on the grid view for displaying movie posters
                    //mGridView.setAdapter(mImageAdepter);
                }
            }
            // Handles failure with server communication
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable error) {
                Log.e(TAG, error.toString());
            }
        });
    }

    /**
     *  This method calls the movie server and gets back requested details of the movie and
     *  lists movies sorted by top rated ones.
     */
    public void sortByTopRated() {
        TopRatedMoviesInterface topRatedApiService = ApiClient.getClient().create(TopRatedMoviesInterface.class);

        Call<MovieResponse> callForTopMovies = topRatedApiService.getTopRatedMovies(API_KEY);
        callForTopMovies.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                if (response.isSuccessful()) {
                    mMovies = response.body().getMovieResults();

                    createListsForDetailElements();

                    // setting up the adapter on the grid view for displaying movie posters
                    //mGridView.setAdapter(mImageAdepter);
                }
            }
            // Handles failure with server communication
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable error) {
                Log.e(TAG, error.toString());
            }
        });
    }

    private void createListsForDetailElements() {
        for (int i = 0; i < mMovies.size(); i++) {

            posters.add(i, mMovies.get(i).getPosterPath());

            titles.add(i, mMovies.get(i).getTitle());

            releaseDates.add(i, mMovies.get(i).getRelease());

            ratings.add(i, String.valueOf(mMovies.get(i).getRating()));

            overviews.add(i, mMovies.get(i).getOverview());

            ids.add(i, Integer.valueOf(mMovies.get(i).getMovieId()));
        }

        mGridView.setAdapter(mImageAdepter);
    }

    /**
     * Creating an options menu with for sorting movies in MainActivity by popularity, top rated
     * movies or ones that the user marked as favorite movies
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Once a sort option selected in the Options Menu a method gets called for each sorting.
     * It also changes the variable movie type. True means simple movies and false means favorite
     * movies stored in the database.
     *
     * @param item option in the Options Menu to be selected
     * @return item selected by the user
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popularity:
                mMovieType = true;
                sortByPopularity();
                return true;
            case R.id.sort_by_rating:
                mMovieType = true;
                sortByTopRated();
                return true;
            case R.id.show_favorites:
                mMovieType = false;
                // set up the adapter on grid view
                mGridView.setAdapter(mFavoriteAdapter);
                // set up the loader
                getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {

        switch (loaderId) {

            case ID_MOVIE_LOADER:
                Uri movieQueryUri = MovieContract.MovieEntry.CONTENT_URI;
                // Define projection that specifies the columns from the table we care about.
                String[] projection = {
                        MovieContract.MovieEntry._ID,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                        MovieContract.MovieEntry.COLUMN_TITLE,
                        MovieContract.MovieEntry.COLUMN_RELEASE,
                        MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                        MovieContract.MovieEntry.COLUMN_RATING,
                        MovieContract.MovieEntry.COLUMN_OVERVIEW
                };
                // This loader will execute the ContentProvider's query method on a background thread
                return new CursorLoader(this,
                        movieQueryUri,
                        projection,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException(String.valueOf(R.string.loader_error + loaderId));
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link ProductCursorAdapter} with this new cursor containing updated product data
        mFavoriteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mFavoriteAdapter.swapCursor(null);
    }

    /**
     *
     * @return mMovieType. If the variable is true, it is a usual movie, if it false it's marked as
     * favorite movie by the user and stored with all the details in a database
     */
    public static boolean getMovieType() {
        return mMovieType;
    }
}