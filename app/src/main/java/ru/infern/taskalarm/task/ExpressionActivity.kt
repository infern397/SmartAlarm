package ru.infern.taskalarm.task

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.infern.taskalarm.alarmsignal.MusicPlayer
import ru.infern.taskalarm.R
import ru.infern.taskalarm.alarmsignal.VibrationPlayer

/**
 * Активити, отображающая выражение и позволяющая пользователю вводить ответ.
 *
 */
class ExpressionActivity : AppCompatActivity() {
    private lateinit var musicPlayer: MusicPlayer //
    private lateinit var vibrationPlayer: VibrationPlayer //
    private lateinit var expressionTextView: TextView //
    private lateinit var answerEditText: EditText //
    private lateinit var submitButton: Button //

    private lateinit var viewModel: ExpressionViewModel //

    /**
     * Метод, вызываемый при создании активити.
     *
     * @param savedInstanceState сохраненное состояние активити (если есть)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicPlayer = MusicPlayer.getInstance()
        vibrationPlayer = VibrationPlayer.getInstance()
        setContentView(R.layout.activity_alarm_call)
        initViews()
        setListeners()
        initViewModel()
    }

    /**
     * Инициализация представлений (виджетов) активити.
     *
     */
    private fun initViews() {
        expressionTextView = findViewById(R.id.expression_textView)
        answerEditText = findViewById(R.id.answer_editText)
        submitButton = findViewById(R.id.submit_button)
    }

    /**
     * Настройка слушателей событий.
     *
     */
    private fun setListeners() {
        answerEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val answer = s.toString().toIntOrNull() ?: 0
                viewModel.currentAnswerLiveData.value = answer
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        submitButton.setOnClickListener {
            val currentAnswer = viewModel.currentAnswerLiveData.value ?: 0
            if (viewModel.submitAnswer(currentAnswer)) {
                musicPlayer.stop()
                vibrationPlayer.stop()
                val notificationManager = applicationContext.getSystemService(
                    Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(0)
                finish()
            }
        }
    }

    /**
     * Инициализация ViewModel.
     *
     */
    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[ExpressionViewModel::class.java]
        viewModel.expressionLiveData.observe(this) { expression ->
            updateExpressionTextView(expression)
        }
    }

    /**
     * Обновление текста в TextView с отображением выражения.
     *
     * @param expression выражение для отображения
     */
    private fun updateExpressionTextView(expression: Expression) {
        val expressionText = getString(
            R.string.expression,
            expression.firstNumber,
            expression.sign,
            expression.secondNumber
        )
        expressionTextView.text = expressionText
        answerEditText.setText("")
    }
}