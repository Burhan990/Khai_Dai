package com.example.finalyearauthientication.Remote;

import android.database.Observable;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ICloudFunctions {
    @GET("")
    Observable<ResponseBody>getCustomToken(@Query ("access_token") String accessToken);
}
