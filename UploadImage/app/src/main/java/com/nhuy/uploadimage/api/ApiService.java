package com.nhuy.uploadimage.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nhuy.uploadimage.Accounts;
import com.nhuy.uploadimage.AddAccount;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    // https://c536-171-247-191-218.ap.ngrok.io/accounts

    public static final String DOMAIN ="https://5c9c-171-247-191-218.ap.ngrok.io/";

    Gson gson = new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @Multipart //up file
    @POST("accounts")
    Call<Accounts> registerAccount(@Part(Const.KEY_USERNAME) RequestBody username,
                                     @Part(Const.KEY_PASSWORD) RequestBody password,
                                     @Part MultipartBody.Part avt);
}
