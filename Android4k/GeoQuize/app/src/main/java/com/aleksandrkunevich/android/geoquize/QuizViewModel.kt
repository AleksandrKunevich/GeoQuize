package com.aleksandrkunevich.android.geoquize

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_africa, false),
        Question(R.string.question_asia, true)
    )
    val getQuestionBankSize = questionBank.size
    val userHaveQuestionAnswer = mutableListOf(0, 0, 0, 0)
    var currentIndex = 0
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    var isGameContinue = true
    fun moveToNextQuestion() {
        currentIndex = if (currentIndex < getQuestionBankSize - 1) {
            currentIndex + 1
        } else {
            getQuestionBankSize - 1
        }
    }

    fun moveToPrevQuestion() {
        currentIndex = if (currentIndex > 0) {
            currentIndex - 1
        } else {
            0
        }
    }
}