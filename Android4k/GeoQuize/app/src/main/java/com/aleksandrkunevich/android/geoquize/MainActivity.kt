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
import androidx.lifecycle.ViewModelProviders

private const val TAG: String = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private var isGameContinue = true
    private var currentIndex = 0
    private var countCorrectAnswer = 0
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_africa, false),
        Question(R.string.question_asia, true)
    )
    private val userHaveQuestionAnswer = (questionBank.indices).toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val provider: ViewModelProvider = ViewModelProvider(this)
        val quizViewModel = provider.get(QuizViewModel::class.java)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        userHaveQuestionAnswer.fill(0)
        updateQuestion()
        questionTextView.setOnClickListener { view: View ->
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }
        nextButton.setOnClickListener { view: View ->
            currentIndex = if (currentIndex < questionBank.size - 1) {
                (currentIndex + 1) % questionBank.size
            } else {
                questionBank.size - 1
            }
            updateQuestion()
        }
        prevButton.setOnClickListener { view: View ->
            currentIndex = if (currentIndex > 0) {
                (currentIndex - 1) % questionBank.size
            } else {
                0
            }
            updateQuestion()
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        if (isGameContinue) {
            when (userHaveQuestionAnswer[currentIndex]) {
                0 -> {
                    val correctAnswer = questionBank[currentIndex].answer
                    userHaveQuestionAnswer[currentIndex] = 1
                    val messageResId = if (userAnswer == correctAnswer) {
                        countCorrectAnswer++
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
        if (isGameContinue) {
            val questionTextResId = questionBank[currentIndex].textResId
            questionTextView.setText(questionTextResId)
        }
    }

    private fun checkFinishGame() {
        if (userHaveQuestionAnswer.sum() == questionBank.size) {
            val result = "Your result answers = ${100 * countCorrectAnswer / questionBank.size}%"
            Toast.makeText(
                this,
                result,
                Toast.LENGTH_LONG
            ).show()
            isGameContinue = false
            questionTextView.text = result
        }
    }
}