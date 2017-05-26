package com.example.szage.mytopmovies.retrofit;

import com.example.szage.mytopmovies.models.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Cll for Top Rated Movies
 */

public interface TopRatedMoviesInterface {

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);
}
