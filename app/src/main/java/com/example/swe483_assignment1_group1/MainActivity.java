package com.example.swe483_assignment1_group1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // SWE 483 - Assignment 1 - Group 1 - Section 60116
    //by:
    // Shahad AlHarbi (Leader), Nuha AlDawsari, Hanin AlMohaimeed, Lama AlMayouf and Rahaf AlOmar.
    
//References used:
//1- “Text Spinner,” Coding in Flow, 27-Jun-2020. [Online]. Available: https://codinginflow.com/tutorials/android/text-spinner. [Accessed: 25-Mar-2021].
//2- “Spinners &nbsp;: &nbsp; Android Developers,” Android Developers. [Online]. Available: https://developer.android.com/guide/topics/ui/controls/spinner#java. [Accessed: 25-Mar-2021].
//3- L. Android, “Insert, Delete, Update and View Data in SQLite Database Android Studio,” All Coding Tutorials, 01-Jul-2020. [Online]. Available: https://www.allcodingtutorials.com/post/insert-delete-update-and-view-data-in-sqlite-database-android-studio. [Accessed: 25-Mar-2021].
//4-  Hackstarsj, “hackstarsj/AndroidDatetime_Picker_Dialog,” GitHub. [Online]. Available: https://github.com/hackstarsj/AndroidDatetime_Picker_Dialog. [Accessed: 25-Mar-2021].
//5 - sevenssevens 2, AndomarAndomar 215k4141 gold badges344344 silver badges374374 bronze badges, WimWim 99866 silver badges99 bronze badges, greutgreut 4, netalexnetalex 38133 silver badges1313 bronze badges, and oriolowonancyoriolowonancy 24322 silver badges99 bronze badges, “How to insert a unique ID into each SQLite row?,” Stack Overflow, 01-Dec-1960. [Online]. Available: https://stackoverflow.com/questions/9342249/how-to-insert-a-unique-id-into-each-sqlite-row/17674055. [Accessed: 25-Mar-2021].
//6 - “Start an Activity from a Notification &nbsp;: &nbsp; Android Developers,” Android Developers. [Online]. Available: https://developer.android.com/training/notify-user/navigation. [Accessed: 25-Mar-2021].
    
    EditText reminderTitle;//to enter the reminder's title
    EditText reminderDate;//to enter the reminder's date
    EditText reminderTime;//to enter the reminder's time
    Spinner importance_spinner;//to display reminder's importance
    DatabaseHelper DB;
    Button setReminderButton;
    String selectedTitle;
    String selectedImportance;
    String selectedDate;
    String selectedTime;

    Date date;
    ReminderTime time;

    //------------ONLY FOR TESTING:--------------
    Button viewButton;
    //------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reminderDate= findViewById(R.id.date);
        reminderTime= findViewById(R.id.time);
        setReminderButton = findViewById(R.id.setReminderButton);
        reminderTitle = findViewById(R.id.title);

        reminderDate.setInputType(InputType.TYPE_NULL);
        reminderTime.setInputType(InputType.TYPE_NULL);

        DB = new DatabaseHelper(this);

        importance_spinner  = (Spinner) findViewById(R.id.importance_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.imprtance_array, android.R.layout.simple_spinner_item); //importance_array consists of two items: High and Low
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importance_spinner.setAdapter(adapter);

        importance_spinner.setOnItemSelectedListener(this);

        reminderDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(reminderDate);//to allow the user to select the reminder date
            }
        });

        reminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(reminderTime);//to allow the user to select the reminder time
            }
        });



         setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTitle = reminderTitle.getText().toString();
                //input fields validation to make sure nothing is empty
                if (selectedTitle.trim().equals("")|| selectedDate == null || selectedTime == null){
                    Toast.makeText(MainActivity.this, "All information is required", Toast.LENGTH_SHORT).show();
                 }
                else{
                long reminderID = DB.insertReminderDetails(selectedTitle, selectedDate, selectedTime,selectedImportance);
                if(reminderID == -1) {
                    Toast.makeText(MainActivity.this, "Reminder not Inserted", Toast.LENGTH_SHORT).show();
                }
                else{
                    scheduleAlarm(reminderID);
                    Toast.makeText(MainActivity.this, "New reminder Inserted", Toast.LENGTH_SHORT).show();
                }
            }
            }
         });





    }


    private void showTimeDialog(final EditText reminderTime) {
        final Calendar reminderClock = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                reminderClock.set(Calendar.HOUR_OF_DAY,hour);
                reminderClock.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
                reminderTime.setText(simpleDateFormat.format(reminderClock.getTime()));
                time = new ReminderTime(hour, minute, 0);
                selectedTime = reminderTime.getText().toString();
                Toast.makeText(MainActivity.this, selectedTime, Toast.LENGTH_SHORT).show();

            }
        };

        new TimePickerDialog(MainActivity.this,timeSetListener,reminderClock.get(Calendar.HOUR_OF_DAY),reminderClock.get(Calendar.MINUTE),false).show();
    }

    private void showDateDialog(final EditText reminderDate) {
        final Calendar reminderCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                reminderCalendar.set(Calendar.YEAR,year);
                reminderCalendar.set(Calendar.MONTH,month);
                reminderCalendar.set(Calendar.DAY_OF_MONTH,day);
                date = new Date(year, month, day);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd");
                reminderDate.setText(simpleDateFormat.format(reminderCalendar.getTime()));
                selectedDate = reminderDate.getText().toString();
                Toast.makeText(MainActivity.this, selectedDate, Toast.LENGTH_SHORT).show();
            }
        };

        new DatePickerDialog(MainActivity.this,dateSetListener,reminderCalendar.get(Calendar.YEAR),reminderCalendar.get(Calendar.MONTH),reminderCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedImportance = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), selectedImportance, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void scheduleAlarm(long reminderID){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(date.year, date.month, date.day, time.hour, time.minute, time.second);

        AlarmManager alarmManger = (AlarmManager) getSystemService(AlarmManager.class);
        Intent alertReceiverIntent = new Intent(this, AlertReceiver.class);
        alertReceiverIntent.putExtra("reminderID", reminderID+"");

        PendingIntent alertReceiverPendingIntent = PendingIntent.getBroadcast(this,(int)reminderID, alertReceiverIntent,0);
        alarmManger.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), alertReceiverPendingIntent);
    }



}