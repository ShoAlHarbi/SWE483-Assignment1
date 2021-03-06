package com.example.swe483_assignment1_group1;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "G01RemindersDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table ReminderDetails(reminderID INTEGER PRIMARY KEY autoincrement NOT NULL,reminderTitle TEXT NOT NULL, reminderDate TEXT NOT NULL, reminderTime TEXT NOT NULL, reminderImportance TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        //not sure if we even need it tbh
        DB.execSQL("drop Table if exists ReminderDetails");
    }

    public long insertReminderDetails(String title, String date,String time,String importance)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("reminderTitle", title);
        contentValues.put("reminderDate", date);
        contentValues.put("reminderTime", time);
        contentValues.put("reminderImportance", importance);

        long key =DB.insert("ReminderDetails", null, contentValues);
        return key;
    }

    public Cursor getAllReminders ()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from ReminderDetails", null);

        return cursor;

    }

    public Cursor getRemindersCustomQuery (String query)
    {
        //query parameter example:
        //"Select * from ReminderDetails where reminderTitle = 'hi'"
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery(query, null);

        return cursor;

    }

}
