package net.thejuggernaut.gatecam.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AlertApi {

    @GET("/motion")
    Call<Motion[]> getMotions();

    @DELETE("/motion/{code}")
    Call<ResponseBody> deleteMotion(@Path("code") String code);

}
