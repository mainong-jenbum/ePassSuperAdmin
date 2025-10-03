package com.jenbumapps.core.api;

import com.jenbumapps.core.model.viewmodel.HomeModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ViewModelApi {
    @GET("/view-model/home-data")
    Call<HomeModel> fetchHomeData();
}
