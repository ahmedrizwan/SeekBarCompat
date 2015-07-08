package app.minimize.com.seek_bar_compat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.SeekBar;

import java.util.concurrent.Callable;


/**
 * Created by ahmedrizwan on 6/21/15.
 * SeekBarCompat : A simple view implementation for consistent SeekBar design on APIs 14 and above
 */
public class SeekBarCompat extends SeekBar implements View.OnTouchListener {

    private static final String TAG = "SeekBarCompat";

    /***
     * Thumb and Progress colors
     */
    int mThumbColor, mProgressColor, mProgressBackgroundColor;

    /***
     * Thumb drawable
     */
    Drawable mThumb;

    /***
     * States for Lollipop ColorStateList
     */
    int[][] states = new int[][]{
            new int[]{android.R.attr.state_enabled}, // enabled
            new int[]{android.R.attr.state_pressed}  // pressed
    };

    /***
     * Default colors to be black for Thumb ColorStateList
     */
    int[] colorsThumb = new int[]{
            Color.BLACK,
            Color.BLACK
    };

    /***
     * Default colors to be black for Progress ColorStateList
     */
    int[] colorsProgress = new int[]{
            Color.BLACK,
            Color.BLACK
    };

    /***
     * Default colors to be black for Progress ColorStateList
     */
    int[] colorsProgressBackground = new int[]{
            Color.BLACK,
            Color.BLACK
    };

    /***
     * ColorStateList objects
     */
    ColorStateList mColorStateListThumb, mColorStateListProgress, mColorStateListProgressBackground;

    /***
     * Used for APIs below 21 to determine height of the seekBar as well as the new thumb drawable
     */
    private int mOriginalThumbHeight;

    /***
     * Updates the thumbColor dynamically
     *
     * @param thumbColor Color representing thumb drawable
     */
    public void setThumbColor(final int thumbColor) {
        mThumbColor = thumbColor;
        if (lollipopAndAbove()) {
            setupThumbColorLollipop();
        }
        invalidate();
        requestLayout();
    }

    /***
     * Method called for APIs 21 and above to setup thumb Color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupThumbColorLollipop() {
        colorsThumb[0] = mThumbColor;
        colorsThumb[1] = mThumbColor;
        mColorStateListThumb = new ColorStateList(states, colorsThumb);
        setThumbTintList(mColorStateListThumb);
    }

    /***
     * Updates the progressColor dynamically
     *
     * @param progressColor Color representing progress drawable
     */
    public void setProgressColor(final int progressColor) {
        mProgressColor = progressColor;
        if (lollipopAndAbove()) {
            setupProgressColorLollipop();
        } else {
            setupProgressColor();
        }

        invalidate();
        requestLayout();
    }

    /***
     * Checks if the device is running API greater than 21
     *
     * @return true if lollipop and above
     */
    private boolean lollipopAndAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /***
     * Method called from APIs below 21 to setup Progress Color
     */
    private void setupProgressColor() {
        //load up the drawable and apply color
        LayerDrawable ld = (LayerDrawable) getProgressDrawable();
        ScaleDrawable shape = (ScaleDrawable) (ld.findDrawableByLayerId(android.R.id.progress));
        shape.setColorFilter(mProgressColor, PorterDuff.Mode.SRC_IN);

        //set the background to transparent
        NinePatchDrawable ninePatchDrawable = (NinePatchDrawable) (ld.findDrawableByLayerId(android.R.id.background));
        ninePatchDrawable.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
    }

    /***
     * Method called from APIs >= 21 to setup Progress Color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupProgressColorLollipop() {
        colorsProgress[0] = mProgressColor;
        colorsProgress[1] = mProgressColor;
        mColorStateListProgress = new ColorStateList(states, colorsProgress);
        setProgressTintList(mColorStateListProgress);
    }

    /***
     * Updates the progressBackgroundColor dynamically
     *
     * @param progressBackgroundColor Color representing progress drawable
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setProgressBackgroundColor(final int progressBackgroundColor) {
        mProgressBackgroundColor = progressBackgroundColor;
        if (lollipopAndAbove()) {
            setupProgressBackgroundLollipop();
        } else {
            setupProgressBackground();
        }
        invalidate();
        requestLayout();
    }

    /***
     * Method called from APIs 21 and above to setup the Progress-background-line Color
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupProgressBackgroundLollipop() {
        colorsProgressBackground[0] = mProgressBackgroundColor;
        colorsProgressBackground[1] = mProgressBackgroundColor;
        mColorStateListProgressBackground = new ColorStateList(states, colorsProgressBackground);
        setProgressBackgroundTintList(mColorStateListProgressBackground);
    }

    /***
     * Method called from APIs below 21 to setup the Progress-background-line Color
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setupProgressBackground() {
        //load up the drawable and apply color
        SeekBarBackgroundDrawable seekBarBackgroundDrawable = new SeekBarBackgroundDrawable(getContext(),
                mProgressBackgroundColor, getResources().getDimension(R.dimen.default_margin));
        if (belowJellybean())
            setBackgroundDrawable(seekBarBackgroundDrawable);
        else
            setBackground(seekBarBackgroundDrawable);

        getBackground().setColorFilter(new PorterDuffColorFilter(mProgressBackgroundColor, PorterDuff.Mode.SRC_IN));

    }

    /***
     * Constructor for creating SeekBarCompat through code
     *
     * @param context Context object
     */
    public SeekBarCompat(final Context context) {
        super(context);
    }

    /***
     * Constructor for creating SeekBarCompat through XML
     *
     * @param context Context Object
     * @param attrs   Attributes passed through XML
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public SeekBarCompat(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme()
                .obtainStyledAttributes(
                        attrs,
                        R.styleable.SeekBarCompat,
                        0, 0);
        try {
            mThumbColor = a.getColor(R.styleable.SeekBarCompat_thumbColor, getPrimaryColorFromSelectedTheme(context));
            mProgressColor = a.getColor(R.styleable.SeekBarCompat_progressColor, getPrimaryColorFromSelectedTheme(context));
            mProgressBackgroundColor = a.getColor(R.styleable.SeekBarCompat_progressBackgroundColor, Color.BLACK);

            if (lollipopAndAbove()) {
                setupThumbColorLollipop();
                setupProgressColorLollipop();
                setupProgressBackgroundLollipop();
            } else {
                setupProgressColor();
                setupProgressBackground();
                setOnTouchListener(this);
                triggerMethodOnceViewIsDisplayed(this, new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ViewGroup.LayoutParams layoutParams = getLayoutParams();
                        mOriginalThumbHeight = mThumb.getIntrinsicHeight();
                        mThumb = new SeekBarThumbDrawable(mThumbColor, mOriginalThumbHeight, getWidth());
                        setThumb(mThumb);
                        layoutParams.height = mOriginalThumbHeight;
                        setLayoutParams(layoutParams);
                        return null;
                    }
                });
            }
        } finally {
            a.recycle();
        }
    }

    private boolean belowJellybean() {
        return Build.VERSION.SDK_INT < 16;
    }

    /***
     * Gets the Primary Color from theme
     *
     * @param context Context Object
     * @return Primary Color
     */
    public static int getPrimaryColorFromSelectedTheme(Context context) {
        int[] attrs = {R.attr.colorPrimary, R.attr.colorPrimaryDark};
        TypedArray ta = context.getTheme()
                .obtainStyledAttributes(attrs);
        int primaryColor = ta.getColor(0, Color.BLACK); //1 index for primaryColorDark
        //default value for primaryColor is set to black if primaryColor not found
        ta.recycle();
        return primaryColor;
    }

    /***
     * Utility method for ViewTreeObserver
     *
     * @param view   View object
     * @param method Method to be called once View is displayed
     */
    public static void triggerMethodOnceViewIsDisplayed(final View view, final Callable<Void> method) {
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    view.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                } else view.getViewTreeObserver()
                        .removeOnGlobalLayoutListener(this);
                try {
                    method.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /***
     * Touch listener for changing Thumb Drawable
     *
     * @param v     View Object
     * @param event Motion Event
     * @return
     */
    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ((SeekBarThumbDrawable) mThumb).updatePosition(x);
                ((SeekBarThumbDrawable) mThumb).expandMode();
                break;
            case MotionEvent.ACTION_MOVE:
                ((SeekBarThumbDrawable) mThumb).updatePosition(x);
                break;
            case MotionEvent.ACTION_UP:
                ((SeekBarThumbDrawable) mThumb).shrinkMode();
                break;
        }
        return false;
    }

    /***
     * Called to substitute getThumb() for APIs below 16
     *
     * @param thumb
     */
    @Override
    public void setThumb(final Drawable thumb) {
        super.setThumb(thumb);
        mThumb = thumb;
    }
}
