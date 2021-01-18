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

/**
 * A countdown timer that derives from the Android [Chronometer]
 *
 * This class
 *
 * @property elapsedTimeLimit
 * @property remainingTimeLimit
 *
 */
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

    /**
     * Starts the timer.
     *
     * It executes [CountdownTimerListener.onStart] and [Chronometer.start].
     *
     * Sets [state] to [TimerState.RUNNING].
     * Sets [elapsedFlag] to true.
     * Sets [remainingFlag] to true.
     * Sets [startTime] to the result of [SystemClock.elapsedRealtime].
     *
     */
    override fun start() {
        this.state = TimerState.RUNNING

        this.elapsedFlag = true
        this.remainingFlag = true

        this.countdownCountdownTimerListener.onStart()
        this.startTime = SystemClock.elapsedRealtime()
        super.start()
    }

    /**
     * Stops the timer.
     *
     * It executes [CountdownTimerListener.onStop] and [Chronometer.stop].
     *
     * Sets [state] to [TimerState.STOPPED]
     */
    override fun stop() {
        this.state = TimerState.STOPPED
        this.countdownCountdownTimerListener.onStop()
        super.stop()
    }

    fun setTimerListener(listenerCountdown: CountdownTimerListener) {
        this.countdownCountdownTimerListener = listenerCountdown
    }

    /**
     * Get elapsed time in milliseconds.
     *
     * @return [Long]
     */
    fun getElapsedTimeMs(): Long =
        this.startTime?.let { ((SystemClock.elapsedRealtime()) - it) } ?: 0L

    /**
     * Get elapsed time in seconds.
     *
     * @return [Long]
     */
    fun getElapsedTimeSec(): Long = TimeUnit.MILLISECONDS.toSeconds(this.getElapsedTimeMs())

    /**
     * Get time left in milliseconds.
     *
     * @return [Long]
     */
    fun getRemainingTimeMs(): Long = (this.base - SystemClock.elapsedRealtime())

    /**
     * Get time left in seconds.
     *
     * @return [Long]
     */
    fun getRemainingTimeSec(): Long = TimeUnit.MILLISECONDS.toSeconds(this.getRemainingTimeMs())

    /**
     * Check if timer is running.
     *
     * @return True if running.
     */
    fun isRunning(): Boolean = state == TimerState.RUNNING

    companion object {
        const val DEFAULT_TIME_ELAPSED = 60L
        const val DEFAULT_TIME_REMAINING = 90L
    }
}