package com.example.android1.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ///////for shared Preferences
    public static final String LoginPREFERENCES = "LoginPrefs";
    public static final String idKey = "id";
    public static final String emailKey = "emailKey";
    public static final String passwordKey = "urlKey";


    EditText editTextdate = null;
    EditText editTextTime = null, editTextTaskDetails;
    Button buttonok, buttoncancel;
    Calendar myCalendar = Calendar.getInstance();
    final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            try {
                editTextdate.setText(sdf.format(myCalendar.getTime()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    };
    String user_id;
    String TAG = "HomeActivity";
    TypeUser typeUser;
    DatabaseHelper databaseHelper;
    ArrayList<TypeTask> ParticularUserTask;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sharedpreferences = getSharedPreferences(LoginPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.clear().apply();


        ParticularUserTask = new ArrayList<>();
        databaseHelper = new DatabaseHelper(HomeActivity.this);

        user_id = getIntent().getStringExtra("id");
        Log.d(TAG, "id " + user_id);
        Cursor cursorUser = databaseHelper.getUser(Integer.parseInt(user_id));
        if (cursorUser != null) {
            while (cursorUser.moveToNext()) {
                Log.d(TAG, cursorUser.getString(1));
                Log.d(TAG, cursorUser.getString(2));
                Log.d(TAG, cursorUser.getString(3));
                Log.d(TAG, cursorUser.getString(4));
                typeUser = new TypeUser(user_id, cursorUser.getString(1), cursorUser.getString(2), cursorUser.getString(3), cursorUser.getString(4));
            }
        }


        Cursor cursorUserTask = databaseHelper.getTask(Integer.parseInt(user_id));
        if (cursorUserTask != null) {
            while (cursorUserTask.moveToNext()) {
                Log.d(TAG, cursorUserTask.getString(1));
                Log.d(TAG, cursorUserTask.getString(2));
                Log.d(TAG, cursorUserTask.getString(3));
                ParticularUserTask.add(new TypeTask(cursorUserTask.getString(1), cursorUserTask.getString(2), cursorUserTask.getString(3)));
            }
        }


        /////to Commit data in shared Preferences
        editor.putString(passwordKey, typeUser.getPassword());
        editor.putString(emailKey, typeUser.getEmail());
        editor.putString(idKey, user_id);
        editor.apply();


        setTitle("To Do");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);

        TextView textviewname = (TextView) view.findViewById(R.id.textviewname);
        TextView textviewemail = (TextView) view.findViewById(R.id.textviewemail);
        textviewname.setText(typeUser.getName());
        textviewemail.setText(typeUser.getEmail());


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = new Bundle();
        bundle.putString("id", user_id);

        Fragment fragment = new TaskListFragment();
        fragment.setArguments(bundle);
        if (null != fragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        alert();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;

        Bundle bundle = new Bundle();
        bundle.putString("id", user_id);

        if (id == R.id.ShowProfile) {
            fragment = new ShowProfileFragment();
            fragment.setArguments(bundle);

        } else if (id == R.id.UpdateProfile) {
            fragment = new UpdateProfileFragment();
            fragment.setArguments(bundle);

        } else if (id == R.id.SeeTasksList) {
            fragment = new TaskListFragment();
            fragment.setArguments(bundle);

        } else if (id == R.id.AddTask) {
            showAddTaskDialog();

        } else if (id == R.id.Logout) {
            editor.clear().apply();
            Intent in = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(in);
            finish();

        } else if (id == R.id.send) {
            String task_string = "";
            for (int i = 0; i < ParticularUserTask.size(); i++) {
                task_string += ParticularUserTask.get(i).getDate() + "\n" +
                        ParticularUserTask.get(i).getTime() + "\n" +
                        ParticularUserTask.get(i).getTaskedetails() + "\n\n";
            }
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, task_string);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }


        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    void alert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("Do you want to exit from ToDo ?")
                .setCancelable(false)
                .setTitle("To Do")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void showAddTaskDialog() {


        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.custom_add_task_dialog, null);
        dialogBuilder.setView(dialogView);

        editTextdate = (EditText) dialogView.findViewById(R.id.edittextDate);
        editTextTime = (EditText) dialogView.findViewById(R.id.edittextTime);
        editTextTaskDetails = (EditText) dialogView.findViewById(R.id.edittextTask);
        buttonok = (Button) dialogView.findViewById(R.id.buttonok);
        buttoncancel = (Button) dialogView.findViewById(R.id.buttoncancel);
        final AlertDialog b = dialogBuilder.create();

        editTextdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(HomeActivity.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        editTextTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(HomeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String newHour, newMinute;
                        String am_pm = null;

                        if ((selectedHour >= 1) && (selectedHour <= 11.59)) {
                            am_pm = getString(R.string.am);
                        } else if (selectedHour >= 12) {
                            selectedHour = selectedHour - 12;
                            am_pm = getString(R.string.pm);
                        } else if (selectedHour == 0) {
                            selectedHour = 12;
                            am_pm = "AM";
                        }

                        newHour = String.valueOf(selectedHour);
                        newMinute = String.valueOf(selectedMinute);
                        if (selectedHour < 10) newHour = "0" + selectedHour;
                        if (selectedMinute < 10) newMinute = "0" + selectedMinute;


                        editTextTime.setText(newHour + ":" + newMinute + ":" + am_pm);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = editTextdate.getText().toString();
                String time = editTextTime.getText().toString();
                String taskdetails = editTextTaskDetails.getText().toString();
                Log.d("Dialog ", date);
                Log.d("Dialog ", time);
                Log.d("Dialog ", taskdetails);
                int flag = 1;
                if (date.isEmpty()) {
                    flag = 0;
                    editTextdate.setError("Select Date");
                }
                if (time.isEmpty()) {
                    flag = 0;
                    editTextTime.setError("Select Time");
                }
                if (taskdetails.isEmpty()) {
                    flag = 0;
                    editTextTaskDetails.setError("Enter Task");
                }

                if (flag == 1) {
                    if (databaseHelper.insertTASK(user_id, date, time, taskdetails)) {
                        Toast.makeText(HomeActivity.this, "Successfully Add new Task", LENGTH_LONG).show();
                        b.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", user_id);

                        Fragment fragment = new TaskListFragment();
                        fragment.setArguments(bundle);
                        if (fragment != null) {
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.container, fragment);
                            ft.commit();
                        }
                    } else {
                        Toast.makeText(HomeActivity.this, "Failed Add new Task", LENGTH_LONG).show();
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
