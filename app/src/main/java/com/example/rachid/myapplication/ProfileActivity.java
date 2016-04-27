package com.example.rachid.myapplication;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
// ----------------------------------------------------------------------------------------

// AÑADIDOS: GOOGLE
// ----------------------------------------------------------------------------------------
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
// ----------------------------------------------------------------------------------------

/**
 * Created by Rachid on 25/03/2016.
 */
public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static Activity activity;

    //AÑADIDO: FORM EDIT PROFILE
    // -----------------------------------------------------------------------------------------
    private CircleImageView circleImageProfile;
    private TextView textEditName;
    private TextView textEditGender;
    private static TextView textEditBirthday;
    private TextView textEditEmail;

    private RelativeLayout relativeName;
    private RelativeLayout relativeGender;
    private RelativeLayout relativeBirthday;
    private RelativeLayout relativeEmail;
    // -----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        activity = this;

        // VALUES OF FORM USER
        // ----------------------------------------------------------------------------------------
        circleImageProfile = (CircleImageView) findViewById(R.id.circle_image_profile);
        Picasso.with(getApplicationContext()).load(MyState.getUser().getUrlImageProfile()).into(circleImageProfile);

        textEditName = (TextView) findViewById(R.id.profile_text_name);
        textEditName.setText(MyState.getUser().getName());

        textEditGender = (TextView) findViewById(R.id.profile_text_gender);
        textEditGender.setText(MyState.getUser().getGender());

        textEditBirthday = (TextView) findViewById(R.id.profile_text_birthday);
        textEditBirthday.setText(MyState.getUser().getBirthday());

        textEditEmail = (TextView) findViewById(R.id.profile_text_email);
        textEditEmail.setText(MyState.getUser().getEmail());
        // ----------------------------------------------------------------------------------------

        // EVENTS CLICK
        // ----------------------------------------------------------------------------------------
        relativeName = (RelativeLayout) findViewById(R.id.profile_relative_name);
        relativeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement your edit name
                showAlertDialogEditName();
            }
        });

        relativeGender = (RelativeLayout) findViewById(R.id.profile_relative_gender);
        relativeGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement your edit gender
                showAlertDialogEditGender();
            }
        });

        relativeBirthday = (RelativeLayout) findViewById(R.id.profile_relative_birthday);
        relativeBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement your edit birthday
                showAlertDialogEditBirthday(); // update birthday
            }
        });

        relativeEmail = (RelativeLayout) findViewById(R.id.profile_relative_email);
        relativeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement your edit email
                showAlertDialogEditEmail();
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    // ----------------------------------------------------------------------------------------
    //
    private void showAlertDialogEditName() {

        final String firstName;
        final String lastName;

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.text_name));

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_name, null);
        dialog.setView(dialogView);

        final AutoCompleteTextView mFirstNameView = (AutoCompleteTextView) dialogView.findViewById(R.id.dialog_first_name);
        final AutoCompleteTextView mLastNameView = (AutoCompleteTextView) dialogView.findViewById(R.id.dialog_last_name);

        String name = MyState.getUser().getName();
        if (name != null) {
            String[] pieces = name.split(" ");
            firstName = pieces[0];
            lastName = pieces[1];

            mFirstNameView.setText(firstName);
            mLastNameView.setText(lastName);
        }
        else {
            firstName = null;
            lastName = null;
        }

        dialog.setNegativeButton(getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(getString(R.string.text_done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String newFirstName = mFirstNameView.getText().toString();
                String newLastName = mLastNameView.getText().toString();

                if ((((firstName == null) & (newFirstName != null))
                        | ((firstName != null) & (newFirstName != null) & (!firstName.equals(newFirstName))))
                        |
                        (((lastName == null) & (newLastName != null))
                                | ((lastName != null) & (newLastName != null) & (!lastName.equals(newFirstName))))) {

                    User user = MyState.getUser();
                    user.setName(newFirstName + " " + newLastName);

                    if (MyNetwork.updateUser(user)) { // devuelve true si se logro actualizar con exito
                        MyDatabase.updateUser(TAG, activity, user);
                        MyState.setUser(user);

                        textEditName.setText(user.getName());

                        // --------------------------------------------------------------------------------
                        if (MainActivity.activity != null) {
                            Log.i(TAG, "ENTRO A Profile:EditName:0");
                            MainActivity.myMenu.updateName();
                        }
                        if (EventsActivity.activity != null){
                            Log.i(TAG, "ENTRO A Profile:EditName:1");
                            EventsActivity.myMenu.updateName();
                        }
                        // --------------------------------------------------------------------------------

                    } else {
                        Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_name), Toast.LENGTH_SHORT).show();
                    }
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //
    private void showAlertDialogEditGender() {

        String male = getString(R.string.text_male);
        String female = getString(R.string.text_female);
        String other = getString(R.string.text_other);

        final CharSequence[] items = {male, female, other};
        final int itemDefaultSelect;

        String gender = MyState.getUser().getGender();
        if (gender.equals(male)) {
            itemDefaultSelect = 0;
        }
        else if (gender.equals(female)) {
            itemDefaultSelect = 1;
        }
        else if (gender.equals(other)) {
            itemDefaultSelect = 2;
        }
        else {
            itemDefaultSelect = -1; // no tiene ninguna opcion seleccionada
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.text_gender));
        dialog.setSingleChoiceItems(items, itemDefaultSelect, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                if (itemDefaultSelect != item) { // Si no se ha modificado nada, no actualizamos nada

                    User user = MyState.getUser();
                    user.setGender(items[item].toString());

                    if (MyNetwork.updateUser(user)) { // devuelve true si se logro actualizar con exito
                        MyDatabase.updateUser(TAG, activity, user);
                        MyState.setUser(user);

                        textEditGender.setText(user.getGender());
                    } else {
                        Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_gender), Toast.LENGTH_SHORT).show();
                    }
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //
    private void showAlertDialogEditBirthday() {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    // CLASS
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private int year;
        private int month;
        private int day;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            String birthday = MyState.getUser().getBirthday();
            if (birthday != null) {
                // Use the old birthday as date in the picker
                String[] pieces = birthday.split("/");
                day = Integer.parseInt(pieces[0]);
                month = Integer.parseInt(pieces[1]);
                month = month - 1; // Currioso esto, el mes los da del 0 al 11 en lugar del 1 al 12 ... informaticos >.<
                year = Integer.parseInt(pieces[2]);
            }
            else {
                // Use the current date as the default date in the picker
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                month = month + 1;  // Currioso esto, el mes los da del 0 al 11 en lugar del 1 al 12 ... informaticos >.<
                day = c.get(Calendar.DAY_OF_MONTH);
            }

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            month = month + 1; // Currioso esto, el mes los da del 0 al 11 en lugar del 1 al 12 ... informaticos >.<
            if ( (this.day != day) || (this.month != month) || (this.year != year) ) { // Si no se ha cambiado la fecha, no actualizamos nada

                User user = MyState.getUser();
                user.setBirthday("" + day + "/" + month + "/" + year);

                if (MyNetwork.updateUser(user)) { // devuelve true si se logro actualizar con exito
                    MyDatabase.updateUser(TAG, activity, user);
                    MyState.setUser(user);

                    textEditBirthday.setText(user.getBirthday());
                } else {
                    Toast.makeText(activity.getBaseContext(), getString(R.string.error_could_not_update_birthday), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //
    private void showAlertDialogEditEmail() {

        final String email;

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.text_email));

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_email, null);
        dialog.setView(dialogView);

        final AutoCompleteTextView mEmailView = (AutoCompleteTextView) dialogView.findViewById(R.id.dialog_email);

        email = MyState.getUser().getEmail();
        if (email != null) {
            mEmailView.setText(email);
        }

        dialog.setNegativeButton(getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(getString(R.string.text_done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String newEmail = mEmailView.getText().toString();

                if ( ((email == null) & (newEmail != null))
                        | (email != null) & (newEmail != null) & (!email.equals(newEmail)) ){ // Si se editado el email actualizamos, sino nada.

                    User user = MyState.getUser();
                    user.setEmail(newEmail);

                    if (MyNetwork.updateUser(user)) { // devuelve true si se logro actualizar con exito
                        MyDatabase.updateUser(TAG, activity, user);
                        MyState.setUser(user);

                        textEditEmail.setText(user.getEmail());

                        // --------------------------------------------------------------------------------
                        if (MainActivity.activity != null) {
                            Log.i(TAG, "ENTRO A Profile:EditEmail:0");
                            MainActivity.myMenu.updateEmail();
                        }
                        if (EventsActivity.activity != null){
                            Log.i(TAG, "ENTRO A Profile:EditEmail:1");
                            EventsActivity.myMenu.updateEmail();
                        }
                        // --------------------------------------------------------------------------------

                    } else {
                        Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_email), Toast.LENGTH_SHORT).show();
                    }
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }
    // ----------------------------------------------------------------------------------------

    //AÑADIDO: OPTIONS
    // ----------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            return true;
        }
        else if (id == R.id.action_log_out) {

            showAlertDialogForLogOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //
    private void showAlertDialogForLogOut() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.text_logout));
        dialog.setMessage(getString(R.string.text_are_you_sure_you_want_to_logout));
        dialog.setNegativeButton(getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(getString(R.string.text_log_out), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MyDatabase.deleteUser(TAG, activity, MyState.getUser());

                MyState.setUser(new User(MyState.getUser().getLocation()));
                MyState.setLoged(false);

                if (MainActivity.activity != null) {
                    MainActivity.activity.finish();
                }
                if (EventsActivity.activity != null) {
                    EventsActivity.activity.finish();
                }

                dialog.dismiss();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
        dialog.show();
    }
    //-----------------------------------------------------------------------------------------
}
