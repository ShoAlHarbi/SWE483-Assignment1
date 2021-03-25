package com.example.swe483_assignment1_group1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class infoActivity extends AppCompatActivity implements View.OnClickListener {

    String title;
    String date;
    String time;
    String importance;
    TextView titleView;
    TextView dateView;
    TextView timeView;
    TextView importanceView;
    Button button;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_view);
        titleView = findViewById(R.id.TitleTextView);
        dateView = findViewById(R.id.DateTextView);
        timeView = findViewById(R.id.TimeTextView);
        importanceView = findViewById(R.id.ImportanceTextView);
        button = findViewById(R.id.addReminderButton);
        button.setOnClickListener(this);

        Intent intent = getIntent();

        title = intent.getStringExtra("reminderTitle");
        date = intent.getStringExtra("reminderDate");
        time = intent.getStringExtra("reminderTime");
        importance = intent.getStringExtra("reminderImportance");

        titleView.setText(title);
        dateView.setText(date);
        timeView.setText(time);
        importanceView.setText(importance);

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.addReminderButton){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }
}
