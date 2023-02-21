package ir.torob.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.slider.RangeSlider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LabeledRangeSlider extends RangeSlider {

    public LabeledRangeSlider(@NonNull Context context) {
        super(context);
        init();
    }

    public LabeledRangeSlider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LabeledRangeSlider(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //in case this View is inside a ScrollView you can listen to OnScrollChangedListener to redraw the View
        getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        setSliderTooltipAlwaysVisible(this);
    }

    public static void setSliderTooltipAlwaysVisible(RangeSlider slider) {
        try {
            Class<?> baseSliderCls = RangeSlider.class.getSuperclass();
            if (baseSliderCls != null) {

                Method ensureLabelsAddedMethod = baseSliderCls.getDeclaredMethod("ensureLabelsAdded");
                ensureLabelsAddedMethod.setAccessible(true);
                ensureLabelsAddedMethod.invoke(slider);
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}