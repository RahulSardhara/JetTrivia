package com.rahul.jettrivia.model

data class QuestionItem(
    val answer: String="",
    val category: String="",
    val choices: List<String> =emptyList(),
    val question: String=""
)