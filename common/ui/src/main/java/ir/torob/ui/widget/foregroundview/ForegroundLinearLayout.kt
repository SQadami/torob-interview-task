package ir.torob.ui.widget.foregroundview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import ir.torob.ui.R

@SuppressLint("CustomViewStyleable")
class ForegroundLinearLayout(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayoutCompat(context, attrs, defStyle) {

    private var mForeground: Drawable? = null

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ForegroundView)
            try {
                val d = a.getDrawable(R.styleable.ForegroundView_android_foreground)
                if (d != null) {
                    foreground = d
                }
            } finally {
                a.recycle()
            }
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === mForeground
    }

    override fun jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState()
        mForeground?.jumpToCurrentState()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        mForeground?.let {
            if (it.isStateful) {
                it.state = drawableState
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mForeground?.setBounds(0, 0, w, h)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        mForeground?.draw(canvas!!)
    }

    override fun drawableHotspotChanged(x: Float, y: Float) {
        super.drawableHotspotChanged(x, y)
        mForeground?.setHotspot(x, y)
    }

    /**
     * Returns the drawable used as the foreground of this layout. The
     * foreground drawable, if non-null, is always drawn on top of the children.
     *
     * @return A Drawable or null if no foreground was set.
     */
    override fun getForeground(): Drawable? {
        return mForeground
    }

    /**
     * Supply a Drawable that is to be rendered on top of all of the child
     * views in this layout.  Any padding in the Drawable will be taken
     * into account by ensuring that the children are inset to be placed
     * inside of the padding area.
     *
     * @param drawable The Drawable to be drawn on top of the children.
     */
    override fun setForeground(drawable: Drawable?) {
        if (mForeground == drawable) return

        mForeground?.let {
            it.callback = null
            unscheduleDrawable(it)
        }

        mForeground = drawable
        if (drawable != null) {
            setWillNotDraw(false)
            drawable.callback = this
            if (drawable.isStateful) {
                drawable.state = drawableState
            }
        } else {
            setWillNotDraw(true)
        }
        requestLayout()
        invalidate()
    }
}