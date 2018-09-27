package io.github.chhch.autoliefer.tasks.api

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object CamundaService : CamundaServiceApi by Retrofit
        .Builder()
        .baseUrl("http://localhost:8080/rest/")
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(CamundaServiceApi::class.java)