package com.example.android1.todo;

/**
 * Created by Android1 on 8/18/2017.
 */


import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SelectTaskAdapter extends ArrayAdapter<TypeTask> {
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<TypeTask> UserTaskList;
    private SparseBooleanArray mSelectedItemsIds;

    public SelectTaskAdapter(Context context, int resourceId,
                           ArrayList<TypeTask> UserTaskList) {
        super(context, resourceId, UserTaskList);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.UserTaskList = UserTaskList;
        inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.tasklistttem, null);

            holder.dateTextView = (TextView) view.findViewById(R.id.textviewdate);
            holder.timeTextView = (TextView) view.findViewById(R.id.textviewTime);
            holder.taskTextView = (TextView) view.findViewById(R.id.textviewTask);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.dateTextView.setText(UserTaskList.get(position).getDate());
        holder.timeTextView.setText(UserTaskList.get(position).getTime());
        holder.taskTextView.setText(UserTaskList.get(position)
                .getTaskedetails());

        return view;
    }

    @Override
    public void remove(TypeTask object) {
        UserTaskList.remove(object);
        notifyDataSetChanged();
    }

    public List<TypeTask> getWorldPopulation() {
        return UserTaskList;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    private class ViewHolder {
        TextView dateTextView;
        TextView timeTextView;
        TextView taskTextView;

    }
}
