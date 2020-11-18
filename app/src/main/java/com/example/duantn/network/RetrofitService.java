package com.example.duantn.network;



import com.example.duantn.api_map_direction.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("maps/api/directions/json")
    Call<Example> getHttp(@Query("origin") String origin,
                          @Query("destination") String destination,
                          @Query("waypoints") String waypoints,
                          @Query("key") String key);

}
