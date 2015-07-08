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
    private final int mHeight;
    private Paint mPaint = new Paint();
    private int mProgress;
    private int mTotalWidth;
    private int shrinkScale=5;

    public SeekBarThumbDrawable(final int thumbColor, int height, int totalWidth){
        mHeight = height;
        mTotalWidth = totalWidth;
        mProgress = height/2;
        mPaint.setColor(thumbColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawCircle (mProgress-mHeight/2, mHeight/2, mHeight/shrinkScale, mPaint);
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


    public void updatePosition(final int xPos) {
        if(xPos>=mHeight/2 && xPos<=mTotalWidth-mHeight/2) {
            mProgress = xPos;
            this.invalidateSelf();
        }
    }

    public void shrinkMode(){
        shrinkScale = 5;
        this.invalidateSelf();
    }

    public void expandMode(){
        shrinkScale = 3;
        this.invalidateSelf();
    }

}
