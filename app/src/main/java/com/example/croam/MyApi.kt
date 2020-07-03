package com.example.croam

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

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
    @POST("users/addreport")
    fun upload(
            @Part("description") description: RequestBody?,
            @Part file: MultipartBody.Part?,
            @Part("latitude") latitude: RequestBody?,
            @Part("longitude") longitude: RequestBody?,
            @Part("country") country: RequestBody?,
            @Part("state") state: RequestBody?,
            @Part("city") city: RequestBody?,
            @HeaderMap headers: MutableMap<String, String>
    ): Call<ResponseBody>?

    @Multipart
    @POST("users/uploadfile")
    fun uploadfile(
            @Part("description") description: RequestBody?,
            @Part file: MultipartBody.Part?,
            @Part("latitude") latitude: RequestBody?,
            @Part("longitude") longitude: RequestBody?,
            @Part("country") country: RequestBody?,
            @Part("state") state: RequestBody?,
            @Part("city") city: RequestBody?,
            @HeaderMap headers: MutableMap<String, String>
    ): Call<ResponseBody>?

    @FormUrlEncoded
    @POST("users/register")
    fun signUp(@Field("number") number: String, @Field("name") name: String,
               @Field("password") password: String, @Field("email") email: String,
               @Field("age") age: String, @Field("gender") gender: String): Call<ResponseBody?>?

    @FormUrlEncoded
    @POST("users/login")
    fun logIn(@Field("number") number: String, @Field("password") password: String): Call<ResponseBody?>?

    @GET("users/forgetPassword")
    fun forgotPass( @QueryMap query: Map<String,String>): Call<ResponseBody?>?

    @GET("users/verify")
    fun verifyOtp(@QueryMap query: Map<String,String>): Call<ResponseBody?>?

    @FormUrlEncoded
    @PUT("users/resetPassword")
    fun resetPass(@Field("number") number: String, @Field("newPass") password: String): Call<ResponseBody?>?

    companion object {
        operator fun invoke(
        ): MyApi {

//            val okkHttpclient = OkHttpClient.Builder()
//                .addInterceptor(networkConnectionInterceptor)
//                .build()
            val logging = HttpLoggingInterceptor()
// set your desired log level
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val httpClient = OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
// add your other interceptors …

// add logging as last interceptor
// add your other interceptors …

// add logging as last interceptor
            httpClient.addInterceptor(logging) // <-- this is the important line!


            return Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl("https://backend-279606.el.r.appspot.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(MyApi::class.java)
        }
    }

}
