package com.softwarica.formurlheroesapi;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Employee_Interface {
    @GET("heroes")
    Call<List<Employee>> getAllData();

    @FormUrlEncoded
    @POST("heroes")
    Call<Void> putData(@Field("name") String name, @Field("desc") String password);

    @POST("herores")
    Call<Void> putAllData(@FieldMap HashMap<String, String> item);
}
