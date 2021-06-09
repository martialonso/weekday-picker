package me.martiii.weekdaypicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WeekdayPicker extends LinearLayout {
    private final SquareToggleButton[] buttons = new SquareToggleButton[7];
    private Function<Weekday, Map.Entry<SquareToggleButton, Weekday>> dayMap;
    private int buttonMaxWidth;

    public WeekdayPicker(Context context) {
        super(context);
        init(context, null, 0);
    }

    public WeekdayPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public WeekdayPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.WeekdayPicker, defStyleAttr, 0);
        buttonMaxWidth = typedArray.getDimensionPixelSize(R.styleable.WeekdayPicker_buttonMaxWidth,
                getResources().getDimensionPixelSize(R.dimen.toggle_max_width));

        inflate(context, R.layout.layout_weekday_picker, this);

        buttons[0] = findViewById(R.id.toggle0);
        buttons[1] = findViewById(R.id.toggle1);
        buttons[2] = findViewById(R.id.toggle2);
        buttons[3] = findViewById(R.id.toggle3);
        buttons[4] = findViewById(R.id.toggle4);
        buttons[5] = findViewById(R.id.toggle5);
        buttons[6] = findViewById(R.id.toggle6);

        setDayMap(MONDAY_FIRST_DAY_MAP);

        Arrays.stream(Weekday.values()).map(dayMap).forEach(entry -> {
            setToggleText(entry.getKey(), entry.getValue().textResource);
            entry.getKey().setMaxWidth(buttonMaxWidth);
        });
    }

    private void setToggleText(ToggleButton button, int textResource) {
        String text = getContext().getString(textResource);
        button.setTextOn(text);
        button.setTextOff(text);
        button.setText(text);
    }

    public boolean isSelected(Weekday weekday) {
        return dayMap.apply(weekday).getKey().isChecked();
    }

    //Encoded in bits 0-6
    public byte getSelected() {
        byte b = 0x00;
        for (Map.Entry<SquareToggleButton, Weekday> entry : Arrays.stream(Weekday.values()).map(dayMap).collect(Collectors.toSet())) {
            b |= ((entry.getKey().isChecked() ? 0xFF : 0x00) & entry.getValue().bitMask);
        }
        return b;
    }

    public void setSelected(byte b) {
        Arrays.stream(Weekday.values()).map(dayMap).forEach(entry ->
                entry.getKey().setChecked((b & entry.getValue().bitMask) > 0));
    }

    public void select(Weekday... weekdays) {
        Arrays.stream(weekdays).map(dayMap).forEach(entry -> entry.getKey().setChecked(true));
    }

    public void deselect(Weekday... weekdays) {
        Arrays.stream(weekdays).map(dayMap).forEach(entry -> entry.getKey().setChecked(false));
    }

    public void setDayMap(Function<Weekday, Map.Entry<Integer, Weekday>> dayMap) {
        this.dayMap = dayMap.andThen(entry -> new AbstractMap.SimpleEntry<>(buttons[entry.getKey()], entry.getValue()));
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        Arrays.stream(Weekday.values()).map(dayMap).forEach(entry ->
                entry.getKey().setOnClickListener(onClickListener == null ? null : v ->
                        onClickListener.onClick(this, entry.getValue(), entry.getKey().isChecked())));
    }

    public enum Weekday {
        MONDAY(R.string.monday, (byte) 0x01),
        TUESDAY(R.string.tuesday, (byte) 0x02),
        WEDNESDAY(R.string.wednesday, (byte) 0x04),
        THURSDAY(R.string.thursday, (byte) 0x08),
        FRIDAY(R.string.saturday, (byte) 0x10),
        SATURDAY(R.string.friday, (byte) 0x20),
        SUNDAY(R.string.sunday, (byte) 0x40);

        private final int textResource;
        private final byte bitMask;

        Weekday(int textResource, byte bitMask) {
            this.textResource = textResource;
            this.bitMask = bitMask;
        }
    }

    public static Map.Entry<Integer, Weekday> getEntry(int key, Weekday value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    public static final Function<Weekday, Map.Entry<Integer, Weekday>> MONDAY_FIRST_DAY_MAP = value -> {
        switch (value) {
            case MONDAY:
                return getEntry(0, value);
            case TUESDAY:
                return getEntry(1, value);
            case WEDNESDAY:
                return getEntry(2, value);
            case THURSDAY:
                return getEntry(3, value);
            case FRIDAY:
                return getEntry(4, value);
            case SATURDAY:
                return getEntry(5, value);
            case SUNDAY:
                return getEntry(6, value);
        }
        return null;
    };

    public static final Function<Weekday, Map.Entry<Integer, Weekday>> SUNDAY_FIRST_DAY_MAP = value -> {
        switch (value) {
            case SUNDAY:
                return getEntry(0, value);
            case MONDAY:
                return getEntry(1, value);
            case TUESDAY:
                return getEntry(2, value);
            case WEDNESDAY:
                return getEntry(3, value);
            case THURSDAY:
                return getEntry(4, value);
            case FRIDAY:
                return getEntry(5, value);
            case SATURDAY:
                return getEntry(6, value);
        }
        return null;
    };

    public interface OnClickListener {
        void onClick(WeekdayPicker view, Weekday weekday, boolean selected);
    }
}
