package com.romeug.countdowntimer.example

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.romeug.countdowntimer.CountdownTimer

class MainActivity : AppCompatActivity() {

    companion object {
        const val START_TIME = 300000L // 5 mins
    }

    private var tickCounter = 0

    private var countdownTimer: CountdownTimer? = null
        get() {
            if (field == null) {
                field = this.findViewById(R.id.countdowntimer)
            }
            return field
        }

    private var textViewListener: TextView? = null
        get() {
            if (field == null) {
                field = this.findViewById(R.id.tv_listener)
            }
            return field
        }

    private var textViewTick: TextView? = null
        get() {
            if (field == null) {
                field = this.findViewById(R.id.tv_tick)
            }
            return field
        }

    private var buttonStart: Button? = null
        get() {
            if (field == null) {
                field = this.findViewById(R.id.btn_start)
            }
            return field
        }

    private var buttonStop: Button? = null
        get() {
            if (field == null) {
                field = this.findViewById(R.id.btn_stop)
            }
            return field
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countdownTimer?.setTimerListener(object : CountdownTimer.CountdownTimerListener {
            override fun onStart() {
                textViewListener?.text = "onStart() executed!"

                tickCounter = 0
                setTickCounter()
            }

            override fun onStop() {
                textViewListener?.text = "onStop() executed!"
                countdownTimer?.setInitialTime(START_TIME)
            }

            override fun onTimeElapsed() {
                textViewListener?.text = "onTimeElapsed() executed!"
            }

            override fun onTimeRemaining() {
                textViewListener?.text = "onTimeRemaining() executed!"
            }

            override fun onFinished() {
                textViewListener?.text = "onFinished() executed!"
            }

            override fun onTick() {
                setTickCounter()
                tickCounter += 1
            }
        })

        buttonStart?.setOnClickListener {
            countdownTimer?.start(START_TIME)
        }

        buttonStop?.setOnClickListener {
            countdownTimer?.stop()
        }

        countdownTimer?.setInitialTime(START_TIME)
    }

    private fun setTickCounter() {
        textViewTick?.text = "onTick() = $tickCounter"
    }
}