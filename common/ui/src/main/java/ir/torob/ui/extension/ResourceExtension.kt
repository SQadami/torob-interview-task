package ir.torob.ui.extension

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Int.stringRes(
    context: Context,
    vararg objects: Any?
): String {
    return context.resources.getString(this, *objects)
}

fun Int.stringRes(
    context: Context
): String {
    return context.resources.getString(this)
}

fun Int.colorRes(
    context: Context
): Int {
    return ContextCompat.getColor(context, this)
}

fun Int.drawableRes(
    context: Context
): Drawable {
    return ContextCompat.getDrawable(context, this)!!
}

fun Int.dimenRes(
    context: Context
): Int {
    return context.resources.getDimensionPixelOffset(this)
}

fun Int.toPx(
    context: Context
): Int {
    return ((this * context.resources.displayMetrics.density + 0.5f).toInt())
}

@ColorInt
fun Context.colorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true,
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun Date.simpleFormat():String{
    val dateFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
    return dateFormat.format(this)
}