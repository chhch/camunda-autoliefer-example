package io.github.chhch.autoliefer.tasks.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

internal interface CamundaServiceApi {
    @POST("process-definition/key/{key}/start")
    @Headers("Content-Type: application/json")
    fun startProcess(@Path("key") key: String, @Body body: RequestBody = RequestBody.create(null, "{}")): Call<ResponseBody>

    @POST("message")
    @Headers("Content-Type: application/json")
    fun sendMessage(@Body body: MessageBody): Call<ResponseBody>
}