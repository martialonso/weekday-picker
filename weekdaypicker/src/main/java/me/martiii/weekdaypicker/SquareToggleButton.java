package me.martiii.weekdaypicker;

import android.content.Context;
import android.util.AttributeSet;

class SquareToggleButton extends androidx.appcompat.widget.AppCompatToggleButton {
    private int maxWidth;

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
        width = Math.min(maxWidth, width);
        setMeasuredDimension(width, width);
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }
}
