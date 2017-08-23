package com.example.android1.todo;

/**
 * Created by Android1 on 8/17/2017.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter {


    ArrayList<TypeTask> TypeTaskList = new ArrayList<>();

    public TaskAdapter(ArrayList<TypeTask> AttendanceItemList) {
        this.TypeTaskList = AttendanceItemList;
    }


    @Override
    public int getCount() {
        return TypeTaskList.size();
    }

    @Override
    public Object getItem(int i) {
        return TypeTaskList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tasklistttem, viewGroup, false);

        TextView task_details = (TextView) view.findViewById(R.id.textviewTask);
        TextView date_text = (TextView) view.findViewById(R.id.textviewdate);
        TextView Time_text = (TextView) view.findViewById(R.id.textviewTime);

        task_details.setText(TypeTaskList.get(i).getTaskedetails());
        date_text.setText(TypeTaskList.get(i).getDate());
        Time_text.setText(TypeTaskList.get(i).getTime());


        return view;
    }
}
