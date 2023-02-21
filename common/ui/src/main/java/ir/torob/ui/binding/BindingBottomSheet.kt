package ir.torob.ui.binding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import ir.torob.ui.BaseBottomSheetFragment

abstract class BindingBottomSheet<B : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int
) : BaseBottomSheetFragment() {

    private var _binding: B? = null

    /**
     * This can be accessed by the child fragments
     * Only valid between onCreateView and onDestroyView
     */
    val binding: B get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        onCreateView(
            DataBindingUtil
                .inflate<B>(
                    inflater,
                    layoutRes,
                    container,
                    false
                ).apply {
                    _binding = this
                    initialize()
                }.root
        )

    /**
     * Removing the binding reference when not needed is recommended as it avoids memory leak
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun B.initialize() {}
}