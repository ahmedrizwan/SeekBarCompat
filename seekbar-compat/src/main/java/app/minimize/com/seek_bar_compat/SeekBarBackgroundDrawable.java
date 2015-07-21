package app.minimize.com.seek_bar_compat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/***
 * A drawable used as a ProgressBackgroundDrawable
 */
public class SeekBarBackgroundDrawable extends Drawable {

    private Paint mPaintLine = new Paint();
    private Paint mPaintBackground = new Paint();
    private float dy;
    private float mPaddingLeft;
    private float mPaddingRight;


    public SeekBarBackgroundDrawable(Context ctx, int lineColor, int backgroundColor, final float paddingLeft, final float paddingRight) {
        mPaddingLeft = paddingLeft;
        mPaddingRight = paddingRight;
        mPaintLine.setColor(lineColor);

        mPaintBackground.setColor(backgroundColor);
        dy = ctx.getResources()
                .getDimension(R.dimen.one_dp);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(getBounds().left,
                getBounds().top,
                getBounds().right,
                getBounds().bottom,
                mPaintBackground);

        canvas.drawRect(getBounds().left+mPaddingLeft,
                getBounds().centerY() - dy / 2,
                getBounds().right-mPaddingRight,
                getBounds().centerY() + dy / 2,
                mPaintLine);
    }


    @Override
    public void setAlpha(final int alpha) {

    }


    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

}

