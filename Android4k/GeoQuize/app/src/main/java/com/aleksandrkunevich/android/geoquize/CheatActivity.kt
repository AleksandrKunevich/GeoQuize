package com.aleksandrkunevich.android.geoquize

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

private const val EXTRA_ANSWER_IS_TRUE = "com.aleksandrkunevich.android.geoquize.answer_is_true"
private const val EXTRA_COUNT_TEST = "com.aleksandrkunevich.android.geoquize.countTest"
private lateinit var answerTextView: TextView
private lateinit var showAnswerButton: Button

class CheatActivity : AppCompatActivity() {

    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener { view: View ->
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
        }
    }

    // Эта функция позволяетсоздать объект Intent, дополненный дополнениями, необходимыми для CheatActivity.
    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, countTest: Int): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(EXTRA_COUNT_TEST, countTest)
            }
        }
    }
}