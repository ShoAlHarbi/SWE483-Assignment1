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

//References used:
//1- https://codinginflow.com/tutorials/android/text-spinner
//2- https://developer.android.com/guide/topics/ui/controls/spinner#java
//3- https://www.allcodingtutorials.com/post/insert-delete-update-and-view-data-in-sqlite-database-android-studio
//4- https://github.com/hackstarsj/AndroidDatetime_Picker_Dialog
// 5 - https://stackoverflow.com/questions/9342249/how-to-insert-a-unique-id-into-each-sqlite-row/17674055
// 6 - https://developer.android.com/training/notify-user/navigation
    
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

        //----------ONLY FOR TESTING:--------------
        viewButton=findViewById(R.id.viewButton);
        //----------------------------------------

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


        //-------------ONLY FOR TESTING:Start-------------------------------------------------------
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
                    buffer.append("ID :"+res.getString(0)+"\n");
                    buffer.append("Title :"+res.getString(1)+"\n");
                    buffer.append("Date :"+res.getString(2)+"\n");
                    buffer.append("Time :"+res.getString(3)+"\n");
                    buffer.append("Importance :"+res.getString(4)+"\n\n");

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("User Entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
            //-------------------ONLY FOR TESTING: END--------------------------------------------



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