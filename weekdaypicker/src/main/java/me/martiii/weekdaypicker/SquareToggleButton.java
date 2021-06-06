package me.martiii.weekdaypicker;

import android.content.Context;
import android.util.AttributeSet;

class SquareToggleButton extends androidx.appcompat.widget.AppCompatToggleButton {
    public SquareToggleButton(Context context) {
        super(context);
    }

    public SquareToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }
}
