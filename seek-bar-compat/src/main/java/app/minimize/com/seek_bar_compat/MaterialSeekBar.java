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
import android.support.annotation.ColorInt;
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
 */
public class MaterialSeekBar extends SeekBar implements View.OnTouchListener {

    private static final String TAG = "Material SeekBar";
    @ColorInt
    int mThumbColor, mProgressColor;


    Drawable mThumb;
    int[][] states = new int[][]{
            new int[]{android.R.attr.state_enabled}, // enabled
            new int[]{android.R.attr.state_pressed}  // pressed
    };

    int[] colorsThumb = new int[]{
            Color.BLACK,
            Color.BLACK
    };

    int[] colorsProgress = new int[]{
            Color.BLACK,
            Color.BLACK
    };


    ColorStateList mColorStateListThumb, mColorStateListProgress;

    public void setThumbColor(final int thumbColor) {
        mThumbColor = thumbColor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorsThumb[0] = thumbColor;
            colorsThumb[1] = thumbColor;
            mColorStateListThumb = new ColorStateList(states, colorsThumb);

            setThumbTintList(mColorStateListThumb);
            setProgressTintList(mColorStateListProgress);
        } else {
            //load up the drawable and apply color
            mThumb.setColorFilter(thumbColor, PorterDuff.Mode.MULTIPLY);
            setThumb(mThumb);
        }
        invalidate();
        requestLayout();
    }

    public void setProgressColor(final int progressColor) {
        mProgressColor = progressColor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            colorsProgress[0] = progressColor;
            colorsProgress[1] = progressColor;
            mColorStateListProgress = new ColorStateList(states, colorsProgress);
            setThumbTintList(mColorStateListThumb);
            setProgressTintList(mColorStateListProgress);
        } else {
            //load up the drawable and apply color
            LayerDrawable ld = (LayerDrawable) getProgressDrawable();
            ld.setColorFilter(mProgressColor, PorterDuff.Mode.SRC_IN);
        }
        invalidate();
        requestLayout();
    }

    public MaterialSeekBar(final Context context) {
        super(context);
    }

    public MaterialSeekBar(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme()
                .obtainStyledAttributes(
                        attrs,
                        R.styleable.MaterialSeekBar,
                        0, 0);
        try {
            mThumbColor = a.getColor(R.styleable.MaterialSeekBar_thumbColor, getPrimaryColorFromSelectedTheme(context));
            mProgressColor = a.getColor(R.styleable.MaterialSeekBar_progressColor, getPrimaryColorFromSelectedTheme(context));
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
                mThumb.setColorFilter(mThumbColor, PorterDuff.Mode.MULTIPLY);
                setThumb(mThumb);
                LayerDrawable ld = (LayerDrawable) getProgressDrawable();
                ld.setColorFilter(mProgressColor, PorterDuff.Mode.SRC_IN);
                setOnTouchListener(this);
                triggerMethodOnceViewIsDisplayed(this, new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ViewGroup.LayoutParams layoutParams = getLayoutParams();
//                        Log.e(TAG, "call "+layoutParams.height +" "+mThumb.getIntrinsicHeight());
                        layoutParams.height = (int) (mThumb.getIntrinsicHeight()*1.5);
                        setLayoutParams(layoutParams);
                        return null;
                    }
                });
            }
        } finally {
            a.recycle();
        }
    }

    public MaterialSeekBar(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static int getPrimaryColorFromSelectedTheme(Context context) {
        int[] attrs = {R.attr.colorPrimary, R.attr.colorPrimaryDark};
        TypedArray ta = context.getTheme()
                .obtainStyledAttributes(attrs);
        int primaryColor = ta.getColor(0, Color.BLACK); //1 index for primaryColorDark
        //default value for primaryColor is set to black if primaryColor not found
        ta.recycle();
        return primaryColor;
    }

    public static int getPrimaryColorDarkFromSelectedTheme(Context context) {
        int[] attrs = {R.attr.colorPrimary, R.attr.colorPrimaryDark};
        TypedArray ta = context.getTheme()
                .obtainStyledAttributes(attrs);
        int primaryColorDark = ta.getColor(1, Color.BLACK); //1 index for primaryColorDark
        //default value for primaryColor is set to black if primaryColor not found
        ta.recycle();
        return primaryColorDark;
    }

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

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                // Pressed state
                int h = (int) (mThumb.getIntrinsicHeight() * 1.1); // 8 * 1.5 = 12
                int w = h;
                Bitmap bmpOrg = ((BitmapDrawable) mThumb).getBitmap();
                Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, w, h, true);
                Drawable newThumb = new BitmapDrawable(getResources(), bmpScaled);
                newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                setThumb(newThumb);
                getThumb().setColorFilter(mThumbColor, PorterDuff.Mode.MULTIPLY);
                break;

            case MotionEvent.ACTION_UP:
                expandThumb(getHeight());
                setThumb(mThumb);
                break;
        }

        return false;
    }

    private void expandThumb(int height) {
        mThumb.setBounds(0, 0, mThumb.getIntrinsicWidth(), height);
        //force a redraw
        int progress = getProgress();
        setProgress(0);
        setProgress(progress);
    }

    private void resizeThumb(int width, int height) {
        mThumb.setBounds(-10, -10, width, height);
        //force a redraw
        int progress = getProgress();
        setProgress(0);
        setProgress(progress);
    }


}
