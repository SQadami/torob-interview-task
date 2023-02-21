package ir.torob.ui.widget.snackbar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.ContentViewCallback
import ir.torob.ui.R

class MaterialSnackBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr), ContentViewCallback {

    var tvMsg: TextView
    var actionBtn: Button
    var imLeft: ImageView
    var container: LinearLayoutCompat

    init {
        View.inflate(context, R.layout.view_snack_bar, this)
        clipToPadding = false
        this.tvMsg = findViewById(R.id.message)
        this.actionBtn = findViewById(R.id.action_right)
        this.imLeft = findViewById(R.id.action_left)
        this.container = findViewById(R.id.snack_bar_container)
    }

    override fun animateContentIn(delay: Int, duration: Int) {
        val scaleX = ObjectAnimator.ofFloat(imLeft, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(imLeft, View.SCALE_Y, 0f, 1f)
        val animatorSet = AnimatorSet().apply {
            interpolator = OvershootInterpolator()
            setDuration(500)
            playTogether(scaleX, scaleY)
        }
        animatorSet.start()
    }

    override fun animateContentOut(delay: Int, duration: Int) {
    }
}