package app.minimize.com.seek_bar_compat;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;

/**
 * Created by ahmedrizwan on 7/8/15.
 */
public class SeekBarThumbDrawable extends ShapeDrawable {
    private static final String TAG = "SeekBarThumb";
    private int mHeight;
    private SeekBarCompat mSeekBarCompat;
    private Paint mPaint = new Paint();
    private float xMultiple;
    private int shrinkScale = 5;
    private float mMax;
    private float mWidth;

    public SeekBarThumbDrawable(final int thumbColor, SeekBarCompat seekBarCompat) {
        mSeekBarCompat = seekBarCompat;
        mPaint.setColor(thumbColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void draw(final Canvas canvas) {
        try {
            canvas.drawCircle(mSeekBarCompat.getProgress() * xMultiple, mHeight / 2, mHeight / shrinkScale, mPaint);
        } catch (Exception e) {
        }
    }

    @Override
    public void setAlpha(final int alpha) {

    }

    @Override
    public void setColorFilter(final ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    public void shrinkMode() {
        shrinkScale = 5;
        this.invalidateSelf();
    }

    public void expandMode() {
        shrinkScale = 3;
        this.invalidateSelf();
    }

    public void setMax(final int max) {
        mMax = max;
        xMultiple = (mWidth - mHeight) / mMax;
    }

    public void setHeight(int height) {
        mHeight = height;
        mWidth = mSeekBarCompat.getWidth();
        xMultiple = (mSeekBarCompat.getWidth() - height) / mMax;
    }

    public void setColor(int thumbColor) {
        mPaint.setColor(thumbColor);
        invalidateSelf();
    }
}
