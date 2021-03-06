
package com.example.candor365;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.candor365.Database.readClassDb;


public class CalendarActivity extends AppCompatActivity {
    private Button backButton;
    private Button addEventButton;
    private CalendarView eventCalendar;
    private TextView classView;
    private static String date="";
    static String TAG = "Calendar Activity";

    private View.OnClickListener backButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backButtonClicked();
        }
    };
    private View.OnClickListener addEventListener = new View.OnClickListener(){
        @Override
        public void onClick(View e){
            addEventClicked();
        }
    };

    private void addEventClicked(){
        startActivity(new Intent(CalendarActivity.this, addCalenderEvent.class));
    }

    private void backButtonClicked(){
        startActivity(new Intent(CalendarActivity.this, SignedInActivity.class));
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        eventCalendar = (CalendarView) findViewById(R.id.calendar_view);

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(backButtonListener);
        addEventButton = (Button) findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(addEventListener);



        eventCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                classView = (TextView) findViewById(R.id.classEvent);
                date = year + ""+ (month+1) + dayOfMonth;
                classView.setText("");
                String[] class_times= getResources().getStringArray(R.array.class_time_array);
                for (String time : class_times) {
                    readClassDb(date, time, new readCallBack() {
                        @Override
                        public void onCallBack(Map dataMap) {
                            if (dataMap != null)
                                classView.append(dataMap.toString() + "\n");

                        }
                    });
                }
            }
        });
    }

    public static String getDate(){
        return date;
    }
}