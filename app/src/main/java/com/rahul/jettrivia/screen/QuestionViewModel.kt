package com.rahul.jettrivia.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.jettrivia.data.DataOrException
import com.rahul.jettrivia.model.QuestionItem
import com.rahul.jettrivia.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val questionRepository: QuestionRepository) : ViewModel() {


    val data: MutableState<DataOrException<ArrayList<QuestionItem>, Boolean, Exception>> = mutableStateOf(DataOrException(null, true, Exception("")))

    init {
        getAllQuestions()
    }

    private fun getAllQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            data.value.loading = true
            data.value = questionRepository.getAllQuestions()
            data.value.loading = false
            if (data.value.data.toString().isEmpty()) {
                data.value.e = Exception("No Data Found")
            }
        }
    }
}