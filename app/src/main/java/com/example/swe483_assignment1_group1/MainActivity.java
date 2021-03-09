package com.example.swe483_assignment1_group1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

//References:
//1- https://codinginflow.com/tutorials/android/text-spinner
//2- https://developer.android.com/guide/topics/ui/controls/spinner#java
//3- https://www.allcodingtutorials.com/post/insert-delete-update-and-view-data-in-sqlite-database-android-studio
//4- https://github.com/hackstarsj/AndroidDatetime_Picker_Dialog

    EditText title;
    EditText date_in;
    EditText time_in;
    DatabaseHelper DB;
    Button setReminderButton;
    Spinner importance_spinner;
    String selectedTitle;
    String selectedImportance;
    String selectedDate;
    String selectedTime;

    //------------ONLY FOR TESTING:--------------
    Button viewButton;
    //------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        date_in=findViewById(R.id.date_input);
        time_in=findViewById(R.id.time_input);
        setReminderButton = findViewById(R.id.setReminderButton);
        title =  findViewById(R.id.title);

        date_in.setInputType(InputType.TYPE_NULL);
        time_in.setInputType(InputType.TYPE_NULL);

        DB = new DatabaseHelper(this);

        //----------ONLY FOR TESTING:--------------
        viewButton=findViewById(R.id.viewButton);
        //----------------------------------------

        importance_spinner  = (Spinner) findViewById(R.id.importance_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.imprtance_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importance_spinner.setAdapter(adapter);

        importance_spinner.setOnItemSelectedListener(this);

        date_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(date_in);
            }
        });

        time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(time_in);
            }
        });



         setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTitle = title.getText().toString();

                Boolean isInserted = DB.insertReminderDetails(selectedTitle, selectedDate, selectedTime,selectedImportance);
                if(isInserted==true)
                    Toast.makeText(MainActivity.this, "New reminder Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "Reminder not Inserted", Toast.LENGTH_SHORT).show();
            }        });



        //-------------ONLY FOR TESTING:-------------------------------------------------------
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = DB.getAllReminders();
                if(res.getCount()==0){
                    Toast.makeText(MainActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Title :"+res.getString(0)+"\n");
                    buffer.append("Date :"+res.getString(1)+"\n");
                    buffer.append("Time :"+res.getString(2)+"\n");
                    buffer.append("Importance :"+res.getString(3)+"\n\n");

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("User Entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
            //-------------------------------------------------------------------------



    }


    private void showTimeDialog(final EditText time_in) {
        final Calendar calendar=Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                time_in.setText(simpleDateFormat.format(calendar.getTime()));

                selectedTime = time_in.getText().toString();
                Toast.makeText(MainActivity.this, selectedTime, Toast.LENGTH_SHORT).show();

            }
        };

        new TimePickerDialog(MainActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }

    private void showDateDialog(final EditText date_in) {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd");
                date_in.setText(simpleDateFormat.format(calendar.getTime()));

                selectedDate = date_in.getText().toString();
                Toast.makeText(MainActivity.this, selectedDate, Toast.LENGTH_SHORT).show();

            }
        };

        new DatePickerDialog(MainActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedImportance = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), selectedImportance, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}