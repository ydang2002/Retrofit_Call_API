package com.nhuy.callapi.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nhuy.callapi.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ApiService {
    //Link API: http://localhost:3000/users
    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyy").create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://494f-171-247-191-218.ap.ngrok.io/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("users")
    Call<List<User>> getListUser();
}
