package com.example.szage.mytopmovies.retrofit;

import com.example.szage.mytopmovies.models.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Call for PopularMovies
 */

public interface PopularMoviesInterface {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey);
}
