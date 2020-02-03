package com.thesummitdev.ciliapp.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface apirotclean {
    @GET
    Call<String> callMaps(@Url String url);

}
