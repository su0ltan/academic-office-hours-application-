package com.sultan.lasttest.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import com.sultan.lasttest.R;

import java.util.Calendar;
import java.util.Date;

public class activity_appointment extends AppCompatActivity {

    CalendarView cv;
    TextView txtdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
       txtdata =(TextView)findViewById(R.id.txtdate);
        String x1 ="EEE yyyy/MM/dd";
        Date d = new Date();

        String curentdate = DateFormat.getDateInstance().format(d);
        txtdata.setText(curentdate);

        cv = (CalendarView)findViewById(R.id.cv);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                int x = i1 + 1;
                String Date = i + "/"+x+"/"+i2;
                txtdata.setText(Date);



                //change the textview when calender changed

            }
        });
    }

}
