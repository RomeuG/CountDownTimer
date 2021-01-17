package com.romeug.countdowntimer

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.widget.Chronometer
import android.widget.Chronometer.OnChronometerTickListener
import java.util.concurrent.TimeUnit

enum class TimerState {
    RUNNING, STOPPED
}

interface CountdownTimerListener {
    fun onStart()
    fun onStop()

    fun onTimeElapsed()
    fun onTimeRemaining()

    fun onFinished()

    fun onTick()
}

class CountdownTimer : Chronometer {

    private var elapsedFlag: Boolean = true
    private var remainingFlag: Boolean = true

    private var state: TimerState = TimerState.STOPPED

    private var startTime: Long? = null

    var elapsedTimeLimit: Long = DEFAULT_TIME_ELAPSED
    var remainingTimeLimit: Long = DEFAULT_TIME_REMAINING

    private var countdownCountdownTimerListener: CountdownTimerListener = object : CountdownTimerListener {
        override fun onStart() {
            //
        }

        override fun onStop() {
            //
        }

        override fun onTimeElapsed() {
            //
        }

        override fun onTimeRemaining() {
            //
        }

        override fun onFinished() {
            //
        }

        override fun onTick() {
            //
        }
    }

    private val defaultOnChronometerTickListener: OnChronometerTickListener =
        OnChronometerTickListener {

            this.elapsedTimeLimit.let { elapsedTime ->
                if (this.elapsedFlag && this.getElapsedTimeSec() >= elapsedTime) {
                    countdownCountdownTimerListener.onTimeElapsed()
                    this.elapsedFlag = false
                }
            }

            this.remainingTimeLimit.let { remainingTime ->
                if (this.remainingFlag && this.getRemainingTimeSec() <= remainingTime) {
                    countdownCountdownTimerListener.onTimeRemaining()
                    this.remainingFlag = false
                }
            }

            if (it.base <= SystemClock.elapsedRealtime()) {
                this.stop()
                countdownCountdownTimerListener.onFinished()
            }

            countdownCountdownTimerListener.onTick()
        }

    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        this.onChronometerTickListener = defaultOnChronometerTickListener
    }

    override fun start() {
        this.state = TimerState.RUNNING

        this.elapsedFlag = true
        this.remainingFlag = true

        this.countdownCountdownTimerListener.onStart()
        this.startTime = SystemClock.elapsedRealtime()
        super.start()
    }

    override fun stop() {
        this.state = TimerState.STOPPED
        this.countdownCountdownTimerListener.onStop()
        super.stop()
    }

    fun setTimerListener(listenerCountdown: CountdownTimerListener) {
        this.countdownCountdownTimerListener = listenerCountdown
    }

    fun getElapsedTimeMs(): Long =
        this.startTime?.let { ((SystemClock.elapsedRealtime()) - it) } ?: 0L

    fun getElapsedTimeSec(): Long = TimeUnit.MILLISECONDS.toSeconds(this.getElapsedTimeMs())

    fun getRemainingTimeMs(): Long = (this.base - SystemClock.elapsedRealtime())
    fun getRemainingTimeSec(): Long = TimeUnit.MILLISECONDS.toSeconds(this.getRemainingTimeMs())

    fun isRunning(): Boolean = state == TimerState.RUNNING

    companion object {
        const val DEFAULT_TIME_ELAPSED = 60L
        const val DEFAULT_TIME_REMAINING = 90L
    }
}