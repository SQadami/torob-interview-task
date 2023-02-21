@file:Suppress("DEPRECATION")

package ir.torob.ui.extension

import android.graphics.PorterDuff
import com.google.android.material.progressindicator.LinearProgressIndicator

fun LinearProgressIndicator.setProgressColor(color: Int) {
    indeterminateDrawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    progressDrawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
}