package com.aleksandrkunevich.android.geoquize

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG: String = "MainActivity"
private const val KEY_INDEX = "keyCurrentIndex"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        questionTextView.setOnClickListener { view: View ->
            quizViewModel.moveToNextQuestion()
            updateQuestion()
        }
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }
        nextButton.setOnClickListener { view: View ->
            quizViewModel.moveToNextQuestion()
            updateQuestion()
        }
        prevButton.setOnClickListener { view: View ->
            quizViewModel.moveToPrevQuestion()
            updateQuestion()
        }

        cheatButton.setOnClickListener { view: View ->

        }

        checkFinishGame()
        updateQuestion()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        if (quizViewModel.isGameContinue) {
            when (quizViewModel.userHaveQuestionAnswer[quizViewModel.currentIndex]) {
                0 -> {
                    val correctAnswer = quizViewModel.currentQuestionAnswer
                    quizViewModel.userHaveQuestionAnswer[quizViewModel.currentIndex] = 1
                    val messageResId = if (userAnswer == correctAnswer) {
                        quizViewModel.countCorrectAnswer++
                        R.string.correct_toast
                    } else {
                        R.string.incorrect_toast
                    }
                    val toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 400)
                    toast.show()
                }
                1 -> Toast.makeText(this, "You get answer in this question", Toast.LENGTH_SHORT)
                    .show()
            }
            checkFinishGame()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        if (quizViewModel.isGameContinue) {
            val questionTextResId = quizViewModel.currentQuestionText
            questionTextView.setText(questionTextResId)
        }
    }

    private fun checkFinishGame() {
        if (quizViewModel.userHaveQuestionAnswer.sum() == quizViewModel.getQuestionBankSize) {
            val result =
                "Your result answers = ${100 * quizViewModel.countCorrectAnswer / quizViewModel.getQuestionBankSize}%"
            Toast.makeText(
                this,
                result,
                Toast.LENGTH_LONG
            ).show()
            quizViewModel.isGameContinue = false
            questionTextView.text = result
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState override")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }
}