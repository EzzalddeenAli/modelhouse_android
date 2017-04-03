package com.platformstory.modelhouse.DTO;

import com.platformstory.modelhouse.Common.Network;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface NetworkService {
    @GET("estates/{id}")
    Call<List<Estate>> estateDetail(@Path("id") int id);

    @POST("search")
    Call<ArrayList<Estate>> estateSearchResult(
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("estate_type") String estate_type,
            @Query("deal_type") String deal_type,
            @Query("price_type") String price_type,
            @Query("price_from") String price_from,
            @Query("price_to") String price_to,
            @Query("monthly_from") String monthly_from,
            @Query("monthly_to") String monthly_to,
            @Query("extent_from") String extent_from,
            @Query("extent_to") String extent_to,
            @Query("monthly_annual") String monthly_annual
    );

    @GET("user")
    Call<List<User>> isLogon();

    @POST("user/login")
    Call<List<User>> login(@Query("email") String email, @Query("password") String password);



    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Network.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
