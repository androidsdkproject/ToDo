package com.example.android1.todo;

/**
 * Created by Android1 on 8/11/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ToDo.db";


    /////for registrationtable
    public static final String USERS_TABLE_NAME = "RegistrationMember";
    public static final String USERS_COLUMN_NAME = "name";
    public static final String USERS_COLUMN_EMAIL = "email";
    public static final String USERS_COLUMN_PASSWORD = "password";
    public static final String USERS_COLUMN_PHONE = "phone";


    /////for task table
    public static final String TASK_TABLE_NAME = "TaskTable";
    public static final String TASK_COLUMN_ID = "id";
    public static final String TASK_COLUMN_DATE = "date";
    public static final String TASK_COLUMN_TIME = "time";
    public static final String TASK_COLUMN_TASK_DETAILS = "task_details";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table  " + USERS_TABLE_NAME
                        +
                        "(id integer primary key, name text,phone text,email text, password text)"
        );

        db.execSQL(
                "create table  " + TASK_TABLE_NAME
                        +
                        "(id integer, date date,time date, task_details text)"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUser(String name, String phone, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_NAME, name);
        contentValues.put(USERS_COLUMN_PHONE, phone);
        contentValues.put(USERS_COLUMN_EMAIL, email);
        contentValues.put(USERS_COLUMN_PASSWORD, password);
        db.insert(USERS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean insertTASK(String id, String date, String time, String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_COLUMN_ID, id);
        contentValues.put(TASK_COLUMN_DATE, date);
        contentValues.put(TASK_COLUMN_TIME, time);
        contentValues.put(TASK_COLUMN_TASK_DETAILS, task);
        db.insert(TASK_TABLE_NAME, null, contentValues);
        return true;
    }


    public boolean updateTask(String id, String date, String time, String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_COLUMN_ID, id);
        contentValues.put(TASK_COLUMN_DATE, date);
        contentValues.put(TASK_COLUMN_TIME, time);
        contentValues.put(TASK_COLUMN_TASK_DETAILS, task);
        db.update(TASK_TABLE_NAME, contentValues, "id = ? and date = ? and time = ? ", new String[]{id, date, time});
        return true;
    }


    public Cursor getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TASK_TABLE_NAME + " where id=" + id + "" + " order by date DESC", null);
        return res;
    }


    public Cursor getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + USERS_TABLE_NAME + " where id=" + id + "", null);
        return res;
    }


    public int numberOfUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, USERS_TABLE_NAME);
        return numRows;
    }

    public boolean updateUser(Integer id, String name, String phone, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_NAME, name);
        contentValues.put(USERS_COLUMN_PHONE, phone);
        contentValues.put(USERS_COLUMN_EMAIL, email);
        contentValues.put(USERS_COLUMN_PASSWORD, password);
        db.update(USERS_TABLE_NAME, contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteUser(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USERS_TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public Integer deleteTask(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TASK_TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public Cursor getAllUser() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + USERS_TABLE_NAME, null);
        return res;
    }
}