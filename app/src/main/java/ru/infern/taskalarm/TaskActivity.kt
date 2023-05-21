package ru.infern.taskalarm

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TaskActivity : AppCompatActivity() {
    private lateinit var musicPlayer: MusicPlayer
    private lateinit var expressionTextView: TextView
    private lateinit var answerExitText: EditText
    private lateinit var submitButton: Button

    private var currentAnswer = 0

    private val expressionMaker = ExpressionMaker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicPlayer = MusicPlayer.getInstance()
        setContentView(R.layout.activity_alarm_call)
        initViews()
        setListeners()
    }

    private fun initViews() {
        expressionTextView = findViewById(R.id.expression_textView)
        answerExitText = findViewById(R.id.answer_editText)
        submitButton = findViewById(R.id.submit_button)

        expressionTextView.text = "${expressionMaker.firstNumber}${expressionMaker.sign}${expressionMaker.secondNumber}"
    }

    private fun setListeners() {
        answerExitText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentAnswer = s.toString().toIntOrNull() ?: 0
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        submitButton.setOnClickListener {
            if (currentAnswer == expressionMaker.answer) {
                stopMusicAndDismissNotification()
                musicPlayer.stop()
                finish()
            } else {
                expressionMaker.makeFields()
                expressionTextView.text = "${expressionMaker.firstNumber}${expressionMaker.sign}${expressionMaker.secondNumber}"
                answerExitText.setText("")
            }

        }
    }
    private fun stopMusicAndDismissNotification() {
//        val alarmReceiverIntent = Intent(applicationContext, AlarmReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            applicationContext,
//            0,
//            alarmReceiverIntent,
//            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager.cancel(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(0)
    }
}