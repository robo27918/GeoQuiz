package com.bignerdranch.android.geoquiz

import QuizViewModel
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0
class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {

            result -> if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                Log.i("MainActivity", "inside resultLauncher ${Activity.RESULT_OK.toString()}" )
                quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            }
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
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }
        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        cheatButton.setOnClickListener {
            //Start CheatActivity
            Log.i("MainActivity", "before cheat activity")
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            //startActivityForResult(intent, REQUEST_CODE_CHEAT)
            resultLauncher.launch(intent)
            Log.i("MainActivity", "finish cheat activity")

        }
        updateQuestion()

    }

    /***
    override fun onActivityResult (requestCode:Int
                                ,resultCode: Int,data:Intent? )
    {
        super.onActivityResult(requestCode, resultCode, data)
         if (resultCode != Activity.RESULT_OK){
             Log.i("MainActivity", "inside first if condition")
            return
         }
        if (resultCode == REQUEST_CODE_CHEAT){
            Log.i("MainActivity", "inside onActivityResult ${Activity.RESULT_OK.toString()}" )
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
         }

    }
    ***/


    /***
     * The following functions are to log the different callback functions
     *  as described in chapter 3
     */

    override fun onStart ()
    {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume()
    {
        super.onResume()
        Log.d(TAG,"onResume() called")
    }
    override fun onPause()
    {
        super.onPause()
        Log.d(TAG,"onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState:  Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG,"onSaveInstatnceState")
        savedInstanceState.putInt(KEY_INDEX,quizViewModel.currentIndex)

    }
    override fun onStop()
    {
        super.onStop()
        Log.d(TAG,"onStop() called")
    }
    override fun onDestroy()
    {
        super.onDestroy()
        Log.d(TAG,"onDestroy() called")
    }
    private fun updateQuestion ()
    {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }
    private fun checkAnswer (userAnswer:Boolean)
    {
        val correctAnswer:Boolean = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgement_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId,Toast.LENGTH_SHORT).show()
    }

}