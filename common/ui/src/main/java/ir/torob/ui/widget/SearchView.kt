package ir.torob.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.text.Editable
import android.util.AttributeSet
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.internal.TextWatcherAdapter
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import ir.torob.ui.R
import ir.torob.ui.extension.*

class SearchView : LinearLayoutCompat {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    val input: EditText
    val btnClose: ImageView
    val btnSearch: ImageView

    var hint: String
        get() = input.hint.toString()
        set(value) {
            input.hint = value
        }

    init {
        inflate(context, R.layout.view_search, this)

        background = createBackground()
        descendantFocusability = FOCUS_BEFORE_DESCENDANTS
        isFocusable = true
        isFocusableInTouchMode = true

        input = findViewById(R.id.input)
        btnClose = findViewById(R.id.btn_close)
        btnSearch = findViewById(R.id.btn_search)

        btnSearch.expandTouch()
        btnSearch.onClick {
            input.requestFocus()
            input.showKeyboard()
        }

        btnClose.expandTouch()
        btnClose.onClick {
            input.clearComposingText()
            input.setText("")

            input.hideKeyboard()
        }

        input.addTextChangedListener(
            @SuppressLint("RestrictedApi")
            object : TextWatcherAdapter() {
                override fun afterTextChanged(s: Editable) {
                    s.isBlank().let {
                        btnSearch.visibleIf(it)
                        btnClose.visibleIf(!it)
                    }
                }
            })
        input.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                v.clearFocus()
            }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        input.isEnabled = enabled
    }

    private fun createBackground(): MaterialShapeDrawable {
        val shapeAppearanceModel = ShapeAppearanceModel.builder(
            this.context,
            0,
            R.style.ShapeAppearanceSmallComponent
        ).build()
        return MaterialShapeDrawable(shapeAppearanceModel).apply {
            setTint(context.colorFromAttr(R.attr.colorBackgroundSearchBar))
            paintStyle = Paint.Style.FILL
        }
    }
}