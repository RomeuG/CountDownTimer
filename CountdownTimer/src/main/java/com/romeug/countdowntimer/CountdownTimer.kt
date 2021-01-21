package com.romeug.countdowntimer

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.widget.Chronometer
import android.widget.Chronometer.OnChronometerTickListener
import java.util.concurrent.TimeUnit

/**
 * A countdown timer that derives from the Android [Chronometer].
 */
class CountdownTimer : Chronometer {

    private var elapsedFlag: Boolean = true
    private var remainingFlag: Boolean = true

    private var state: TimerState = TimerState.STOPPED

    private var startTime: Long? = null

    private var elapsedTimeLimit: Long = DEFAULT_TIME_ELAPSED
    private var remainingTimeLimit: Long = DEFAULT_TIME_REMAINING

    private var countdownTimerListener: CountdownTimerListener = object : CountdownTimerListener {
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
                    countdownTimerListener.onTimeElapsed()
                    this.elapsedFlag = false
                }
            }

            this.remainingTimeLimit.let { remainingTime ->
                if (this.remainingFlag && this.getRemainingTimeSec() <= remainingTime) {
                    countdownTimerListener.onTimeRemaining()
                    this.remainingFlag = false
                }
            }

            if (it.base <= SystemClock.elapsedRealtime()) {
                this.stop()
                countdownTimerListener.onFinished()
            }

            countdownTimerListener.onTick()
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

        this.countdownTimerListener.onStart()
        this.startTime = SystemClock.elapsedRealtime()
        super.start()
    }

    /**
     * Starts the timer with provided start time value.
     *
     * It executes [CountdownTimerListener.onStart] and [Chronometer.start].
     *
     * Sets [state] to [TimerState.RUNNING].
     * Sets [elapsedFlag] to true.
     * Sets [remainingFlag] to true.
     * Sets [startTime] to the result of [SystemClock.elapsedRealtime].
     *
     * @param ms Initial time in milliseconds.
     */
    fun start(ms: Long) {
        this.setInitialTime(ms)

        this.state = TimerState.RUNNING

        this.elapsedFlag = true
        this.remainingFlag = true

        this.countdownTimerListener.onStart()
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
        this.countdownTimerListener.onStop()
        super.stop()
    }

    /**
     * Sets [countdownTimerListener].
     *
     * @param listener Listener as [CountdownTimerListener].
     */
    fun setTimerListener(listener: CountdownTimerListener) {
        this.countdownTimerListener = listener
    }

    /**
     * Sets [elapsedTimeLimit].
     *
     * @param ms Time in seconds.
     */
    fun setElapsedTimeLimit(ms: Long) {
        this.elapsedTimeLimit = ms
    }

    /**
     * Sets [remainingTimeLimit].
     *
     * @param ms Time in seconds.
     */
    fun setRemainingTimeLimit(ms: Long) {
        this.remainingTimeLimit = ms
    }

    /**
     * Set [Chronometer] base (start time).
     *
     * @param ms Initial time in milliseconds.
     */
    fun setInitialTime(ms: Long) {
        this.base = ms
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

    /**
     * Listener used in [CountdownTimer].
     */
    interface CountdownTimerListener {

        /**
         * Executes when timer starts by calling [start].
         */
        fun onStart()

        /**
         * Executes when timer is forced to stop by calling [stop].
         */
        fun onStop()

        /**
         * Executes when time elapsed matches [elapsedTimeLimit].
         */
        fun onTimeElapsed()

        /**
         * Executes when time remaining matches [remainingTimeLimit].
         */
        fun onTimeRemaining()

        /**
         * Executes when timer finishes normally.
         */
        fun onFinished()

        /**
         * Executes every time [OnChronometerTickListener] executes.
         */
        fun onTick()
    }

    /**
     * Enum to define timer state.
     */
    enum class TimerState {
        RUNNING, STOPPED
    }
}