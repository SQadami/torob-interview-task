package ir.torob.ui.widget.snackbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import ir.torob.ui.R
import ir.torob.ui.extension.onClick

class MaterialSnackBar(
    parent: ViewGroup,
    content: MaterialSnackBarView
) : BaseTransientBottomBar<MaterialSnackBar>(parent, content, content) {

    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                android.R.color.transparent
            )
        )
        getView().setPadding(0, 0, 0, 0)
    }

    companion object {

        fun make(view: View, @StringRes messageRes: Int): MaterialSnackBar {
            return make(view, view.context.getString(messageRes))
        }

        fun make(view: View, message: String): MaterialSnackBar {
            return make(view, message, { }, 0, "")
        }

        fun make(
            view: View,
            message: String,
            listener: () -> Unit,
            icon: Int,
            actionLabel: String?
        ): MaterialSnackBar {

            // First we find a suitable parent for our custom view
            val parent = view.findSuitableParent()
                ?: throw IllegalArgumentException(
                    "No suitable parent found from the given view. Please provide a valid view."
                )

            // We inflate our custom view
            val customView = LayoutInflater.from(view.context).inflate(
                R.layout.layout_snackbar,
                parent,
                false
            ) as MaterialSnackBarView
            // We create and return our Snackbar
            customView.tvMsg.text = message
            actionLabel?.let {
                customView.actionBtn.text = actionLabel
                customView.actionBtn.onClick {
                    listener.invoke()
                }
            }
            customView.imLeft.setImageResource(icon)

            val materialSnackBar = MaterialSnackBar(parent, customView)
            materialSnackBar.duration = Snackbar.LENGTH_SHORT
            materialSnackBar.animationMode = ANIMATION_MODE_SLIDE

            return materialSnackBar
        }


        private fun View?.findSuitableParent(): ViewGroup? {
            var view = this
            var fallback: ViewGroup? = null
            do {
                if (view is CoordinatorLayout) {
                    // We've found a CoordinatorLayout, use it
                    return view
                } else if (view is FrameLayout) {
                    if (view.id == android.R.id.content) {
                        // If we've hit the decor content view, then we didn't find a CoL in the
                        // hierarchy, so use it.
                        return view
                    } else {
                        // It's not the content view but we'll use it as our fallback
                        fallback = view
                    }
                }

                if (view != null) {
                    // Else, we will loop and crawl up the view hierarchy and try to find a parent
                    val parent = view.parent
                    view = if (parent is View) parent else null
                }
            } while (view != null)

            // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
            return fallback
        }
    }
}