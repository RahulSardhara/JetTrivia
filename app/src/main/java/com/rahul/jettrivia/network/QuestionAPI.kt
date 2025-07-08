package com.rahul.jettrivia.network

import com.rahul.jettrivia.model.Question
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionAPI {

    @GET(value = "world.json")
    suspend fun getAllQuestion(): Question

}