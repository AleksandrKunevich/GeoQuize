package com.aleksandrkunevich.android.geoquize

import android.app.Activity
import android.content.Intent
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
private const val KEY_IS_CHEAT = "keyIsCheat"
private const val REQUEST_CODE_CHEAT = 0

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
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex
        val isCheat = savedInstanceState?.getBoolean(KEY_IS_CHEAT, false) ?: false
        quizViewModel.isCheater = isCheat

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
//            val intent = Intent(this, CheatActivity::class.java)
//            startActivity(intentCheatButton)
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        checkFinishGame()
        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (resultCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        if (quizViewModel.isGameContinue) {
            when (quizViewModel.userHaveQuestionAnswer[quizViewModel.currentIndex]) {
                0 -> {
                    val correctAnswer = quizViewModel.currentQuestionAnswer
                    quizViewModel.userHaveQuestionAnswer[quizViewModel.currentIndex] = 1
                    val messageResId = when {
                        userAnswer == correctAnswer -> R.string.correct_toast
                        quizViewModel.isCheater -> R.string.judgment_toast
                        else -> R.string.incorrect_toast
                    }
                    if (userAnswer == correctAnswer)
                        quizViewModel.countCorrectAnswer++
                    val toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 400)
                    toast.show()
                    Log.d(TAG, "MainActivity isCheat = ${quizViewModel.isCheater}")
                }
                1 -> Toast.makeText(this, "You get answer in this question", Toast.LENGTH_SHORT)
                    .show()
            }
            checkFinishGame()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
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
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        outState.putBoolean(KEY_IS_CHEAT, quizViewModel.isCheater)
    }
}