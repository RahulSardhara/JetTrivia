package com.rahul.jettrivia.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahul.jettrivia.component.Questions

@Composable
fun TriviaHome(paddingValues : PaddingValues, questionViewModel: QuestionViewModel = hiltViewModel()) {
    Questions(paddingValues,questionViewModel)
}