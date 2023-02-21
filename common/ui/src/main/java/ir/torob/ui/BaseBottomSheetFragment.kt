package ir.torob.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    protected var peekHeight = -1
    protected var initialState = BottomSheetBehavior.STATE_EXPANDED
    protected var skipCollapsed = false
    protected var isAlreadyHidden = false
    protected var draggable = true

    protected var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private var backgroundMaterialShapeDrawable: MaterialShapeDrawable? = null
    private val bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback =
        object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    isAlreadyHidden = true
                    onHidden()
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    backgroundMaterialShapeDrawable?.let { adjustBackground(bottomSheet) }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset.toDouble() == -1.0) {
                    isAlreadyHidden = true
                    onDismissedByScrolling()
                }
            }
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnKeyListener { _: DialogInterface?,
                                  keyCode: Int,
                                  _: KeyEvent? ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                isAlreadyHidden = true
                onDismissedByScrolling()
            }
            false
        }
        return dialog
    }

    override fun onDetach() {
        if (!isAlreadyHidden) {
            onDismissedByScrolling()
        }
        super.onDetach()
    }

    protected open fun onCreateView(toCreateView: View): View {
        toCreateView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                toCreateView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val parent =
                    dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

                backgroundMaterialShapeDrawable =
                    if (parent.background is MaterialShapeDrawable)
                        parent.background as MaterialShapeDrawable
                    else null

                bottomSheetBehavior = BottomSheetBehavior.from(parent)
                bottomSheetBehavior?.let {
                    it.addBottomSheetCallback(bottomSheetCallback)
                    it.skipCollapsed = skipCollapsed
                    it.state = initialState
                    it.isDraggable = draggable
                    if (peekHeight > 0) {
                        it.peekHeight = peekHeight
                    }
                }
                addBottomLayout()
            }
        })
        return toCreateView
    }

    protected open fun bottomLayout(): View? = null

    protected open fun onDismissedByScrolling() = onHidden()

    protected open fun onHidden() = try {
        dismiss()
    } catch (ignored: IllegalStateException) {
    }

    private fun adjustBackground(bottomSheet: View) {
        val parent = bottomSheet.parent as View
        if (parent.height > bottomSheet.height) {
            val newMaterialShapeDrawable = createMaterialShapeDrawable()
            ViewCompat.setBackground(bottomSheet, newMaterialShapeDrawable)
        } else {
            ViewCompat.setBackground(bottomSheet, backgroundMaterialShapeDrawable)
        }
    }

    private fun createMaterialShapeDrawable(): MaterialShapeDrawable {
        val shapeAppearanceModel = ShapeAppearanceModel.builder(
            context,
            0,
            R.style.ShapeAppearanceModal
        ).build()

        //Create a new MaterialShapeDrawable (can't use the original MaterialShapeDrawable in the BottomSheet)
        val newMaterialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        //Copy the attributes in the new MaterialShapeDrawable
        newMaterialShapeDrawable.initializeElevationOverlay(requireContext())
        newMaterialShapeDrawable.fillColor = backgroundMaterialShapeDrawable!!.fillColor
        newMaterialShapeDrawable.tintList = backgroundMaterialShapeDrawable!!.tintList
        newMaterialShapeDrawable.elevation = backgroundMaterialShapeDrawable!!.elevation
        newMaterialShapeDrawable.strokeWidth = backgroundMaterialShapeDrawable!!.strokeWidth
        newMaterialShapeDrawable.strokeColor = backgroundMaterialShapeDrawable!!.strokeColor
        return newMaterialShapeDrawable
    }

    private fun addBottomLayout() = bottomLayout()?.let {
        val frameLayout =
            dialog!!.findViewById<FrameLayout>(com.google.android.material.R.id.container)
        val lp = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.gravity = Gravity.BOTTOM
        frameLayout.addView(it, lp)
    }
}