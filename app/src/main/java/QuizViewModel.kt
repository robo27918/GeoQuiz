import android.util.Log
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.geoquiz.Question
import com.bignerdranch.android.geoquiz.R

private const val TAG = "QuizViewModel"
class QuizViewModel: ViewModel() {
    private val questionBank =   listOf(
        Question(R.string.personalized_question1,true),
        Question(R.string.personalized_question2,false),
        Question(R.string.personalized_question3,true),
        Question(R.string.question_australia,true),
        Question(R.string.question_oceans,true),
        Question(R.string.question_mideast,true),
        Question(R.string.question_africa,true),
        Question(R.string.question_americas,true),
        Question(R.string.question_asia,true)


    )
    var currentIndex= 0;
    var isCheater = false
    val currentQuestionAnswer:Boolean
    get() = questionBank[currentIndex].answer

    val currentQuestionText:Int
    get() = questionBank[currentIndex].textResId

    fun moveToNext(){
        currentIndex = (currentIndex+1)%questionBank.size
    }
    fun moveToPrev () {
        currentIndex = (questionBank.size + currentIndex -1) % questionBank.size

    }
}