package com.rahul.jettrivia.repository

import com.rahul.jettrivia.data.DataOrException
import com.rahul.jettrivia.model.Question
import com.rahul.jettrivia.model.QuestionItem
import com.rahul.jettrivia.network.QuestionAPI
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val questionAPI: QuestionAPI) {

    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()


    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = questionAPI.getAllQuestion()
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        } catch (exception: Exception) {
            dataOrException.loading = false
            dataOrException.data = Question()
            dataOrException.e = exception
        }
        return dataOrException
    }


}