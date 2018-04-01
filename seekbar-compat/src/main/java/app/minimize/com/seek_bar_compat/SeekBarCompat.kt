package app.minimize.com.seek_bar_compat

import android.annotation.TargetApi
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.*
import android.os.Build
import android.support.annotation.IntRange
import android.support.v7.widget.AppCompatSeekBar
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import java.util.concurrent.Callable


/**
 * Created by ahmedrizwan on 6/21/15.
 * SeekBarCompat : A simple view implementation for consistent SeekBar design on APIs 14 and above
 */
class SeekBarCompat : AppCompatSeekBar, View.OnTouchListener {
    private var mActualBackgroundColor: Int = 0

    /***
     * Thumb and Progress colors
     */
    internal var mThumbColor: Int = 0
    internal var mProgressColor: Int = 0
    internal var mProgressBackgroundColor: Int = 0

    /***
     * Thumb drawable
     */
    internal var mThumb: Drawable? = null
    /***
     * States for Lollipop ColorStateList
     */
    internal var states = arrayOf(intArrayOf(android.R.attr.state_enabled), // enabled
            intArrayOf(android.R.attr.state_pressed), // pressed
            intArrayOf(-android.R.attr.state_enabled), // disabled
            intArrayOf() //everything else
    )

    /***
     * Default colors to be black for Thumb ColorStateList
     */
    internal var colorsThumb = intArrayOf(Color.BLACK, Color.BLACK, Color.LTGRAY, Color.BLACK)

    /***
     * Default colors to be black for Progress ColorStateList
     */
    internal var colorsProgress = intArrayOf(Color.BLACK, Color.BLACK, Color.LTGRAY, Color.BLACK)

    /***
     * Default colors to be black for Progress ColorStateList
     */
    internal var colorsProgressBackground = intArrayOf(Color.BLACK, Color.BLACK, Color.LTGRAY, Color.BLACK)

    /***
     * ColorStateList objects
     */
    internal var mColorStateListThumb: ColorStateList? = null
    internal var mColorStateListProgress: ColorStateList? = null
    internal var mColorStateListProgressBackground: ColorStateList? = null

    /***
     * Used for APIs below 21 to determine height of the seekBar as well as the new thumb drawable
     */
    private var mOriginalThumbHeight: Int = 0
    private var mThumbAlpha = 255
    private var mIsEnabled = true

    internal var gradientDrawable = GradientDrawable()

    /***
     * Updates the thumbColor dynamically
     *
     * @param thumbColor Color representing thumb drawable
     */
    fun setThumbColor(thumbColor: Int) {
        mThumbColor = thumbColor
        if (lollipopAndAbove()) {
            setupThumbColorLollipop()
        } else {
            gradientDrawable.setColor(if (mIsEnabled) thumbColor else Color.LTGRAY)
        }
        invalidate()
        requestLayout()
    }

    /***
     * Method called for APIs 21 and above to setup thumb Color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupThumbColorLollipop() {
        if (lollipopAndAbove()) {
            colorsThumb[0] = mThumbColor
            colorsThumb[1] = mThumbColor
            colorsThumb[2] = Color.LTGRAY
            mColorStateListThumb = ColorStateList(states, colorsThumb)
            thumbTintList = mColorStateListThumb
        } else {

        }
    }

    /***
     * Updates the progressColor dynamically
     *
     * @param progressColor Color representing progress drawable
     */
    fun setProgressColor(progressColor: Int) {
        mProgressColor = progressColor
        if (lollipopAndAbove()) {
            setupProgressColorLollipop()
        } else {
            setupProgressColor()
        }

        invalidate()
        requestLayout()
    }

    /***
     * Checks if the device is running API greater than 21
     *
     * @return true if lollipop and above
     */
    private fun lollipopAndAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    /***
     * Method called from APIs below 21 to setup Progress Color
     */
    private fun setupProgressColor() {
        try {
            //load up the drawable and apply color
            val ld = progressDrawable as LayerDrawable
            val shape = ld.findDrawableByLayerId(android.R.id.progress) as ScaleDrawable
            shape.setColorFilter(mProgressColor, PorterDuff.Mode.SRC_IN)

            //set the background to transparent
            val ninePatchDrawable = ld.findDrawableByLayerId(
                    android.R.id.background) as NinePatchDrawable
            ninePatchDrawable.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
        } catch (e: NullPointerException) {
            //TODO: Handle exception
        }

    }

    /***
     * Method called from APIs >= 21 to setup Progress Color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupProgressColorLollipop() {
        colorsProgress[0] = mProgressColor
        colorsProgress[1] = mProgressColor
        mColorStateListProgress = ColorStateList(states, colorsProgress)
        progressTintList = mColorStateListProgress
    }

    /***
     * Updates the progressBackgroundColor dynamically
     *
     * @param progressBackgroundColor Color representing progress drawable
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setProgressBackgroundColor(progressBackgroundColor: Int) {
        mProgressBackgroundColor = progressBackgroundColor
        if (lollipopAndAbove()) {
            setupProgressBackgroundLollipop()
        } else {
            setupProgressBackground()
        }
        invalidate()
        requestLayout()
    }

    /***
     * Method called from APIs 21 and above to setup the Progress-background-line Color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupProgressBackgroundLollipop() {
        colorsProgressBackground[0] = mProgressBackgroundColor
        colorsProgressBackground[1] = mProgressBackgroundColor
        mColorStateListProgressBackground = ColorStateList(states, colorsProgressBackground)
        progressBackgroundTintList = mColorStateListProgressBackground
    }

    /***
     * Method called from APIs below 21 to setup the Progress-background-line Color
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun setupProgressBackground() {
        //load up the drawable and apply color
        val seekBarBackgroundDrawable = SeekBarBackgroundDrawable(
                context, mProgressBackgroundColor, mActualBackgroundColor, paddingLeft.toFloat(),
                paddingRight.toFloat())
        if (belowJellybean())
            setBackgroundDrawable(seekBarBackgroundDrawable)
        else
            background = seekBarBackgroundDrawable
    }

    /***
     * Constructor for creating SeekBarCompat through code
     *
     * @param context Context object
     */
    constructor(context: Context) : super(context) {}

    /***
     * Constructor for creating SeekBarCompat through XML
     *
     * @param context Context Object
     * @param attrs   Attributes passed through XML
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.theme
                .obtainStyledAttributes(attrs, R.styleable.SeekBarCompat, 0, 0)
        val array = intArrayOf(android.R.attr.background, android.R.attr.enabled)
        val b = context.theme.obtainStyledAttributes(attrs, array, 0, 0)
        try {
            mThumbColor = a.getColor(R.styleable.SeekBarCompat_thumbColor,
                    getPrimaryColorFromSelectedTheme(context))
            mProgressColor = a.getColor(R.styleable.SeekBarCompat_progressColor,
                    getPrimaryColorFromSelectedTheme(context))
            mProgressBackgroundColor = a.getColor(R.styleable.SeekBarCompat_progressBackgroundColor,
                    Color.BLACK)
            mThumbAlpha = (a.getFloat(R.styleable.SeekBarCompat_thumbAlpha, 1f) * 255).toInt()
            mActualBackgroundColor = b.getColor(0, Color.TRANSPARENT)
            mIsEnabled = b.getBoolean(1, true)
            if (lollipopAndAbove()) {
                splitTrack = false
                setupThumbColorLollipop()
                setupProgressColorLollipop()
                setupProgressBackgroundLollipop()
                thumb.alpha = mThumbAlpha
            } else {
                Log.e(TAG, "SeekBarCompat isEnabled? $mIsEnabled")
                setupProgressColor()
                setOnTouchListener(this)
                gradientDrawable.shape = GradientDrawable.OVAL
                gradientDrawable.setSize(50, 50)
                gradientDrawable.setColor(if (mIsEnabled) mThumbColor else Color.LTGRAY)
                triggerMethodOnceViewIsDisplayed(this, Callable<Void> {
                    val layoutParams = layoutParams
                    mOriginalThumbHeight = mThumb!!.intrinsicHeight
                    gradientDrawable.setSize(mOriginalThumbHeight / 3,
                            mOriginalThumbHeight / 3)
                    gradientDrawable.alpha = mThumbAlpha
                    thumb = gradientDrawable
                    if (layoutParams.height < mOriginalThumbHeight)
                        layoutParams.height = mOriginalThumbHeight
                    setupProgressBackground()
                    null
                })
            }
        } finally {
            a.recycle()
            b.recycle()
        }
    }

    private fun belowJellybean(): Boolean {
        return Build.VERSION.SDK_INT < 16
    }

    /***
     * Touch listener for changing Thumb Drawable
     *
     * @param v     View Object
     * @param event Motion Event
     * @return
     */
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (!lollipopAndAbove())
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    gradientDrawable = GradientDrawable()
                    gradientDrawable.shape = GradientDrawable.OVAL
                    gradientDrawable.setSize(mOriginalThumbHeight / 2, mOriginalThumbHeight / 2)
                    gradientDrawable.setColor(if (mIsEnabled) mThumbColor else Color.LTGRAY)
                    gradientDrawable.setDither(true)
                    gradientDrawable.alpha = mThumbAlpha
                    thumb = gradientDrawable
                }
                MotionEvent.ACTION_UP -> {
                    gradientDrawable = GradientDrawable()
                    gradientDrawable.shape = GradientDrawable.OVAL
                    gradientDrawable.setSize(mOriginalThumbHeight / 3, mOriginalThumbHeight / 3)
                    gradientDrawable.setColor(if (mIsEnabled) mThumbColor else Color.LTGRAY)
                    gradientDrawable.setDither(true)
                    gradientDrawable.alpha = mThumbAlpha
                    thumb = gradientDrawable
                }
            }
        return false
    }


    /***
     * Called to substitute getThumb() for APIs below 16
     *
     * @param thumb
     */
    override fun setThumb(thumb: Drawable) {
        super.setThumb(thumb)
        mThumb = thumb
    }

    /***
     * Sets the thumb alpha (Obviously)
     *
     * @param alpha
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setThumbAlpha(@IntRange(from = 0, to = 255) alpha: Int) {
        mThumbAlpha = alpha
        if (!belowJellybean())
            thumb.alpha = mThumbAlpha
        layoutParams = layoutParams
    }

    /***
     * Enables or disables the whole seekBar!
     *
     * @param enabled
     */
    override fun setEnabled(enabled: Boolean) {
        mIsEnabled = enabled
        triggerMethodOnceViewIsDisplayed(this, Callable<Void> {
            if (!lollipopAndAbove()) {
                gradientDrawable = GradientDrawable()
                gradientDrawable.shape = GradientDrawable.OVAL
                gradientDrawable.setSize(mOriginalThumbHeight / 3, mOriginalThumbHeight / 3)
                gradientDrawable.setColor(if (mIsEnabled) mThumbColor else Color.LTGRAY)
                gradientDrawable.setDither(true)
                gradientDrawable.alpha = mThumbAlpha
                thumb = gradientDrawable
                //load up the drawable and apply color
                val ld = progressDrawable as LayerDrawable
                val shape = ld.findDrawableByLayerId(
                        android.R.id.progress) as ScaleDrawable
                shape.setColorFilter(if (mIsEnabled) mProgressColor else Color.LTGRAY,
                        PorterDuff.Mode.SRC_IN)
                //set the background to transparent
                val ninePatchDrawable = ld.findDrawableByLayerId(
                        android.R.id.background) as NinePatchDrawable
                ninePatchDrawable.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
                //background
                //load up the drawable and apply color
                val seekBarBackgroundDrawable = SeekBarBackgroundDrawable(
                        context, if (mIsEnabled) mProgressBackgroundColor else Color.LTGRAY,
                        mActualBackgroundColor, paddingLeft.toFloat(), paddingRight.toFloat())
                if (belowJellybean())
                    setBackgroundDrawable(seekBarBackgroundDrawable)
                else
                    background = seekBarBackgroundDrawable
            }
            super@SeekBarCompat.setEnabled(enabled)
            null
        })

    }

    companion object {

        private val TAG = "SeekBarCompat"

        /***
         * Gets the Primary Color from theme
         *
         * @param context Context Object
         * @return Primary Color
         */
        fun getPrimaryColorFromSelectedTheme(context: Context): Int {
            val attrs = intArrayOf(R.attr.colorPrimary, R.attr.colorPrimaryDark)
            val ta = context.theme.obtainStyledAttributes(attrs)
            val primaryColor = ta.getColor(0, Color.BLACK) //1 index for primaryColorDark
            //default value for primaryColor is set to black if primaryColor not found
            ta.recycle()
            return primaryColor
        }

        /***
         * Utility method for ViewTreeObserver
         *
         * @param view   View object
         * @param method Method to be called once View is displayed
         */
        fun triggerMethodOnceViewIsDisplayed(view: View,
                                             method: Callable<Void>) {
            val observer = view.viewTreeObserver
            observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < 16) {
                        view.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    } else
                        view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    try {
                        method.call()
                    } catch (e: Exception) {
                        Log.e(TAG, "onGlobalLayout " + e.toString())
                    }

                }
            })
        }
    }
}
