package app.minimize.com.seek_bar_compat

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

/***
 * A drawable used as a ProgressBackgroundDrawable
 */
class SeekBarBackgroundDrawable(ctx: Context, lineColor: Int, backgroundColor: Int, private val mPaddingLeft: Float, private val mPaddingRight: Float) : Drawable() {

    private val mPaintLine = Paint()
    private val mPaintBackground = Paint()
    private val dy: Float

    init {
        mPaintLine.color = lineColor

        mPaintBackground.color = backgroundColor
        dy = ctx.resources
                .getDimension(R.dimen.one_dp)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(bounds.left.toFloat(),
                bounds.top.toFloat(),
                bounds.right.toFloat(),
                bounds.bottom.toFloat(),
                mPaintBackground)

        canvas.drawRect(bounds.left + mPaddingLeft,
                bounds.centerY() - dy / 2,
                bounds.right - mPaddingRight,
                bounds.centerY() + dy / 2,
                mPaintLine)
    }


    override fun setAlpha(alpha: Int) {

    }


    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

}

