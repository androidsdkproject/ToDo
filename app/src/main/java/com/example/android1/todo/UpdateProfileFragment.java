package com.example.android1.todo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class UpdateProfileFragment extends Fragment {


    TypeUser typeUser;
    DatabaseHelper databaseHelper;
    String TAG = "ShowProfileFragment";
    EditText editTextfragmentemail, editTextfragmentname, editTextfragmentphone, editTextfragmentpassword;
    Button UpdateProfilebuttonfragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        final String id = getArguments().getString("id");
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


        editTextfragmentemail = (EditText) view.findViewById(R.id.editTextfragmentemail);
        editTextfragmentname = (EditText) view.findViewById(R.id.editTextfragmentname);
        editTextfragmentphone = (EditText) view.findViewById(R.id.editTextfragmentphone);
        editTextfragmentpassword = (EditText) view.findViewById(R.id.editTextfragmentpassword);
        UpdateProfilebuttonfragment = (Button) view.findViewById(R.id.UpdateProfilebuttonfragment);

        editTextfragmentname.setText(typeUser.getName());
        editTextfragmentemail.setText(typeUser.getEmail());
        editTextfragmentphone.setText(typeUser.getPhone());
        editTextfragmentpassword.setText(typeUser.getPassword());
        UpdateProfilebuttonfragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String update_email = editTextfragmentemail.getText().toString();
                String update_name = editTextfragmentname.getText().toString();
                String update_phone = editTextfragmentphone.getText().toString();
                String update_password = editTextfragmentpassword.getText().toString();
                if (databaseHelper.updateUser(Integer.parseInt(id), update_name, update_phone, update_email, update_password)) {
                    Snackbar.make(view, "update Data Successfully ", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(view, "update Data Failed ", Snackbar.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }


}
