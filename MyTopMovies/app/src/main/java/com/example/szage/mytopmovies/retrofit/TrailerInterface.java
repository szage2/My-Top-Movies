package com.example.szage.mytopmovies.retrofit;

import com.example.szage.mytopmovies.models.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Call for Trailers
 */

public interface TrailerInterface {

    @GET("movie/{id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("id") String id, @Query("api_key") String apiKey);
}
