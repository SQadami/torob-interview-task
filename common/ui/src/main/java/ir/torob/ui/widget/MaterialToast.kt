package ir.torob.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import ir.torob.ui.R

object MaterialToast {

    fun show(
        context: Context,
        duration: Int = Toast.LENGTH_SHORT,
        @StringRes messageRes: Int
    ) =
        show(context, duration, context.getString(messageRes))

    fun show(
        context: Context,
        duration: Int = Toast.LENGTH_SHORT,
        message: String
    ) =
        Toast.makeText(context, message, duration).apply {
            @Suppress("Deprecation")
            view?.let {
                it.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(context, R.color.colorToast)
                )

                it.findViewById<TextView>(android.R.id.message).text =
                    SpannableString(message).apply {
                        setSpan(
                            TextAppearanceSpan(
                                context,
                                R.style.TextAppearance_Toast
                            ),
                            0,
                            this.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
            }
        }.show()
}