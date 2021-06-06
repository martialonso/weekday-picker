package me.martiii.weekdaypickerapp;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import me.martiii.weekdaypicker.WeekdayPicker;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeekdayPicker weekdayPicker = findViewById(R.id.weekday_picker);
        weekdayPicker.setOnClickListener((view, weekday, selected) -> {
            Log.d("WeekdayPickerApp TAG", "Click: " + weekday.name() + " , selected: " + selected +
                    " , overall selected: " + weekdayPicker.getSelected());
        });
    }
}