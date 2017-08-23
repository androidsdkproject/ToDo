package com.example.android1.todo;

/**
 * Created by Android1 on 8/17/2017.
 */

public class TypeTask {
    String id, date, time, taskedetails;

    TypeTask(String date, String time, String taskedetails) {
        this.date = date;
        this.time = time;
        this.taskedetails = taskedetails;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTaskedetails() {
        return taskedetails;
    }

    public void setTaskedetails(String taskedetails) {
        this.taskedetails = taskedetails;
    }
}
