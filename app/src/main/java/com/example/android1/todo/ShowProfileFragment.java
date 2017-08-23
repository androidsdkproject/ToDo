package com.example.android1.todo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ShowProfileFragment extends Fragment {
    TypeUser typeUser;
    DatabaseHelper databaseHelper;
    String TAG = "ShowProfileFragment";
    TextView textviewfragmentemail, textviewfragmentname, textviewfragmentphone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_profile, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        String id = getArguments().getString("id");

        Log.d(TAG, "id " + id);
        Cursor cursorUser = databaseHelper.getUser(Integer.parseInt(id));
        if (cursorUser != null) {
            while (cursorUser.moveToNext()) {
                Log.d(TAG, cursorUser.getString(1));
                Log.d(TAG, cursorUser.getString(2));
                Log.d(TAG, cursorUser.getString(3));
                Log.d(TAG, cursorUser.getString(4));
                typeUser = new TypeUser(id, cursorUser.getString(1), cursorUser.getString(2), cursorUser.getString(3), cursorUser.getString(4));
            }
        }

        textviewfragmentemail = (TextView) view.findViewById(R.id.textviewfragmentemail);
        textviewfragmentname = (TextView) view.findViewById(R.id.textviewfragmentname);
        textviewfragmentphone = (TextView) view.findViewById(R.id.textviewfragmentphone);


        textviewfragmentname.setText("Name :        " + typeUser.getName());
        textviewfragmentemail.setText("Email :      " + typeUser.getEmail());
        textviewfragmentphone.setText("Phone :      " + typeUser.getPhone());


        return view;
    }

}
