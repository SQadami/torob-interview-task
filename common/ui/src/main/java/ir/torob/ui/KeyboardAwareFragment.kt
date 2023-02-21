@file:Suppress("DEPRECATION")

package ir.torob.ui

import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

fun Fragment.resizeOnKeyboardVisibility() {

    lifecycle.addObserver(object : DefaultLifecycleObserver {

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

        override fun onStop(owner: LifecycleOwner) {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            super.onStop(owner)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            lifecycle.removeObserver(this)
            super.onDestroy(owner)
        }
    })
}