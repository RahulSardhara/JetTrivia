package com.rahul.jettrivia.di

import com.rahul.jettrivia.network.QuestionAPI
import com.rahul.jettrivia.repository.QuestionRepository
import com.rahul.jettrivia.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Singleton
    @Provides
    fun provideQuestionAPI(): QuestionAPI {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideQuestionRepository(questionAPI: QuestionAPI)= QuestionRepository(questionAPI)

}