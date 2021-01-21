package com.romeug.countdowntimer.example

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.romeug.countdowntimer.CountdownTimer

class MainActivity : AppCompatActivity() {

    private var countdownTimer: CountdownTimer? = null
        get() {
            if (field == null) {
                field = this.findViewById(R.id.countdowntimer)
            }
            return field
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countdownTimer?.setTimerListener(object : CountdownTimer.CountdownTimerListener {
            override fun onStart() {

            }

            override fun onStop() {

            }

            override fun onTimeElapsed() {

            }

            override fun onTimeRemaining() {

            }

            override fun onFinished() {

            }

            override fun onTick() {

            }

        })
    }
}