package net.thejuggernaut.gatecam.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AlertApi {

    @GET("/motion")
    Call<Motion[]> getMotions();

}
