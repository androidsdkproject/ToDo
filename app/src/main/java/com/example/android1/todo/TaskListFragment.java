package com.example.android1.todo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;


public class TaskListFragment extends Fragment {
    DatabaseHelper databaseHelper;
    String TAG = "TaskListFragment";
    ArrayList<TypeTask> UserAllTask;
    String user_id;
    ListView listView;
    TextView textView;
    SelectTaskAdapter selectTaskAdapter;
    EditText editTextTaskDetails;
    Button buttonok, buttoncancel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        listView = (ListView) view.findViewById(R.id.listview);
        textView = (TextView) view.findViewById(R.id.no_task_found);
        user_id = getArguments().getString("id");
        UserAllTask = new ArrayList<>();
        Log.d(TAG, "id " + user_id);
        Cursor cursorUserTask = databaseHelper.getTask(Integer.parseInt(user_id));
        if (cursorUserTask != null) {
            while (cursorUserTask.moveToNext()) {
                Log.d(TAG, cursorUserTask.getString(1));
                Log.d(TAG, cursorUserTask.getString(2));
                Log.d(TAG, cursorUserTask.getString(3));
                UserAllTask.add(new TypeTask(cursorUserTask.getString(1), cursorUserTask.getString(2), cursorUserTask.getString(3)));
            }
        }

        selectTaskAdapter = new SelectTaskAdapter(getActivity(), R.layout.tasklistttem, UserAllTask);
        listView.setAdapter(selectTaskAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                editAddTaskDialog(UserAllTask.get(i).getDate(), UserAllTask.get(i).getTime(), UserAllTask.get(i).getTaskedetails());
            }
        });


        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {

                final int checkedCount = listView.getCheckedItemCount();


                mode.setTitle(checkedCount + " Selected");

                selectTaskAdapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        SparseBooleanArray selected = selectTaskAdapter
                                .getSelectedIds();
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                TypeTask selecteditem = selectTaskAdapter
                                        .getItem(selected.keyAt(i));
                                selectTaskAdapter.remove(selecteditem);
                            }
                        }
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.deletemenu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                selectTaskAdapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });


        if (UserAllTask.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);

        } else {
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }

        return view;
    }


    void editAddTaskDialog(final String select_task_date, final String select_task_time, String select_task_details) {


        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.custom_update_task_dialog, null);
        dialogBuilder.setView(dialogView);


        editTextTaskDetails = (EditText) dialogView.findViewById(R.id.edittextTask);

        editTextTaskDetails.setText(select_task_details);


        buttonok = (Button) dialogView.findViewById(R.id.buttonok);
        buttoncancel = (Button) dialogView.findViewById(R.id.buttoncancel);
        final AlertDialog b = dialogBuilder.create();


        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String taskdetails = editTextTaskDetails.getText().toString();
                Log.d("Dialog ", taskdetails);
                int flag = 1;
                if (taskdetails.isEmpty()) {
                    flag = 0;
                    editTextTaskDetails.setError("Enter Task");
                }

                if (flag == 1) {
                    if (databaseHelper.updateTask(user_id, select_task_date, select_task_time, taskdetails)) {
                        Toast.makeText(getActivity(), "Successfully Update Task", LENGTH_LONG).show();
                        b.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", user_id);

                        Fragment fragment = new TaskListFragment();
                        fragment.setArguments(bundle);
                        if (fragment != null) {
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container, fragment);
                            ft.commit();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Failed Add new Task", LENGTH_LONG).show();
                    }
                }


            }
        });

        buttoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });


        b.show();


    }

}
