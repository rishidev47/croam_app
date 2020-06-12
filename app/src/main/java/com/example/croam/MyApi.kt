package com.example.croam

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface MyApi {

//    @POST("user/login/")
//    suspend fun userLogin(
//            @Body id_token: SendToken?
//    ) : Response<AuthResponse>
//
//
//    //    @Headers("Authorization: Token ${}")
//    @GET("trips/popular/")
//    suspend fun getPopularTrips(@HeaderMap headers:Map<String,String?>) : Response<TripsResponse>
//
//    @GET("trips/nearme/")
//    suspend fun getTripsNearMe(@HeaderMap headers:Map<String,String?>, @QueryMap query: Map<String,Double>) : Response<TripsResponse>
//
//    @GET("trips/trips")
//    suspend fun getAllTrips(@HeaderMap headers: Map<String, String?>): Response<List<Trip>>
//
//    @GET("trips/trips/{id}")
//    suspend fun getTrip(@Path("id") id:Int, @HeaderMap headers: Map<String, String?>): Response<Trip>


    @Multipart
    @POST("upload")
    fun upload(
            @Part("description") description: RequestBody?,
            @Part file: MultipartBody.Part?
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @POST("users/register")
    fun signUp(@Field("number") number: String, @Field("name") name: String,
               @Field("password") password: String, @Field("email") email: String,
               @Field("age") age: String, @Field("gender") gender: String): Call<ResponseBody?>?

    @FormUrlEncoded
    @POST("users/login")
    fun logIn(@Field("number") number: String, @Field("password") password: String): Call<ResponseBody?>?

    companion object {
        operator fun invoke(
        ): MyApi {

//            val okkHttpclient = OkHttpClient.Builder()
//                .addInterceptor(networkConnectionInterceptor)
//                .build()

            return Retrofit.Builder()
//                .client(okkHttpclient)
                    .baseUrl("https://backend-279606.el.r.appspot.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MyApi::class.java)
        }
    }

}
