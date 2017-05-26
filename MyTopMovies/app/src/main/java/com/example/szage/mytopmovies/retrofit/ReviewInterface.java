package com.example.szage.mytopmovies.retrofit;

import com.example.szage.mytopmovies.models.ReviewResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Call for Reviews
 */

public interface ReviewInterface {

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("id") String id, @Query("api_key") String apiKey);
}
