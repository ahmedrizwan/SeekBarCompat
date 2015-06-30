package app.minimize.com.seek_bar_compat;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.SeekBar;

import java.util.concurrent.Callable;


/**
 * Created by ahmedrizwan on 6/21/15.
 * SeekBarCompat : A simple view implementation for consistent SeekBar design on APIs 16 and above
 */
public class SeekBarCompat extends SeekBar implements View.OnTouchListener {

    /***
     * Thumb and Progress colors
     */
    int mThumbColor, mProgressColor;

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
     * ColorStateList objects
     */
    ColorStateList mColorStateListThumb, mColorStateListProgress;

    /***
     * Updates the thumbColor dynamically
     *
     * @param thumbColor Color representing thumb drawable
     */
    public void setThumbColor(final int thumbColor) {
        mThumbColor = thumbColor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorsThumb[0] = thumbColor;
            colorsThumb[1] = thumbColor;
            mColorStateListThumb = new ColorStateList(states, colorsThumb);
            setThumbTintList(mColorStateListThumb);
        } else {
            //load up the drawable and apply color
            updateThumb(thumbColor);
        }
        invalidate();
        requestLayout();
    }

    /***
     * Updates the progressColor dynamically
     *
     * @param progressColor Color representing progress drawable
     */
    public void setProgressColor(final int progressColor) {
        mProgressColor = progressColor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorsProgress[0] = progressColor;
            colorsProgress[1] = progressColor;
            mColorStateListProgress = new ColorStateList(states, colorsProgress);
            setProgressTintList(mColorStateListProgress);
        } else {
            //load up the drawable and apply color
            LayerDrawable ld = (LayerDrawable) getProgressDrawable();
            ld.setColorFilter(mProgressColor, PorterDuff.Mode.SRC_IN);
        }
        invalidate();
        requestLayout();
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

            colorsThumb[0] = mThumbColor;
            colorsThumb[1] = mThumbColor;
            mColorStateListThumb = new ColorStateList(states, colorsThumb);
            colorsProgress[0] = mProgressColor;
            colorsProgress[1] = mProgressColor;
            mColorStateListProgress = new ColorStateList(states, colorsProgress);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setThumbTintList(mColorStateListThumb);
                setProgressTintList(mColorStateListProgress);
            } else {
                //load up the drawable and apply color
                mThumb = ContextCompat.getDrawable(context, R.drawable.ic_circle);

                updateThumb(mThumbColor);

                LayerDrawable ld = (LayerDrawable) getProgressDrawable();
                ld.setColorFilter(mProgressColor, PorterDuff.Mode.SRC_IN);

                setOnTouchListener(this);

                triggerMethodOnceViewIsDisplayed(this, new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ViewGroup.LayoutParams layoutParams = getLayoutParams();
                        layoutParams.height = (int) (mThumb.getIntrinsicHeight() * 1.5);
                        setLayoutParams(layoutParams);

                        return null;
                    }
                });
            }
        } finally {
            a.recycle();
        }
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
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                // Pressed state
                updateThumb(mThumbColor, (float) 1.1);
                break;

            case MotionEvent.ACTION_UP:
                expandThumb(getHeight());
                mThumb.setColorFilter(mThumbColor, PorterDuff.Mode.MULTIPLY);
                setThumb(mThumb);
                break;
        }

        return false;
    }

    /***
     * Expands thumb size to be that of the SeekBar
     *
     * @param height Height of the SeekBar
     */
    private void expandThumb(int height) {
        mThumb.setBounds(0, 0, mThumb.getIntrinsicWidth(), height);
        //force a redraw
        int progress = getProgress();
        setProgress(0);
        setProgress(progress);
    }

    /***
     * Draws scaled version of the thumb from bitmap
     * @param thumbColor color
     * @param scale scale value
     */
    private void updateThumb(final int thumbColor, final float scale) {
        int h = (int) (mThumb.getIntrinsicHeight()*scale); //scale the size of thumb to 1.1
        Bitmap bmpOrg = ((BitmapDrawable) mThumb).getBitmap();
        Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, h, h, true); //height=width
        Drawable newThumb = new BitmapDrawable(getResources(), bmpScaled);
        newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
        setThumb(newThumb);
        getThumb().setColorFilter(thumbColor, PorterDuff.Mode.MULTIPLY);
    }

    /***
     * Draws the thumb from bitmap
     * @param thumbColor color
     */
    private void updateThumb(final int thumbColor){
        Bitmap bmpOrg = ((BitmapDrawable) mThumb).getBitmap();
        Drawable newThumb = new BitmapDrawable(getResources(), bmpOrg);
        newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
        setThumb(newThumb);
        getThumb().setColorFilter(thumbColor, PorterDuff.Mode.MULTIPLY);
    }

}
