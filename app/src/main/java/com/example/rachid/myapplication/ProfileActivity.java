package com.example.rachid.myapplication;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
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
import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.SubscribeListener;
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
    private TextView textEditUserName;
    private TextView textEditEmail;
    private TextView textEditName;
    private TextView textEditGender;
    private static TextView textEditBirthday;
    private TextView textEditPlace;
    private TextView textEditMusicStyle;

    private RelativeLayout relativeUserName;
    private RelativeLayout relativeEmail;
    private RelativeLayout relativeName;
    private RelativeLayout relativeGender;
    private RelativeLayout relativeBirthday;
    private RelativeLayout relativePlace;
    private RelativeLayout relativeMusicStyle;
    // -----------------------------------------------------------------------------------------

    private ProgressDialog mProgressDialog;
    private MyNetwork myNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        activity = this;

        // VALUES OF FORM USER
        // ----------------------------------------------------------------------------------------
        circleImageProfile = (CircleImageView) findViewById(R.id.circle_image_profile);
        if ( (MyState.getUser().getImage() != null) && (!TextUtils.isEmpty(MyState.getUser().getImage())) ) {
            Picasso.with(getApplicationContext()).load(MyState.getUser().getImage()).into(circleImageProfile);
        }

        textEditUserName = (TextView) findViewById(R.id.profile_text_username);
        textEditUserName.setText(MyState.getUser().getUsername());

        textEditEmail = (TextView) findViewById(R.id.profile_text_email);
        textEditEmail.setText(MyState.getUser().getEmail());

        textEditName = (TextView) findViewById(R.id.profile_text_name);
        String name = MyState.getUser().getName();
        String surname = MyState.getUser().getSurname();
        if ( ((name != null) || (surname != null)) && ((!TextUtils.isEmpty(name)) || (!TextUtils.isEmpty(surname))) ) {
            textEditName.setText(name + " " + surname);
        } else {
            textEditName.setText(getString(R.string.simbol_next));
        }

        textEditGender = (TextView) findViewById(R.id.profile_text_gender);
        String gender = MyState.getUser().getGender();
        if (gender != null) {
            textEditGender.setText(gender);
        } else {
            textEditGender.setText(getString(R.string.simbol_next));
        }

        textEditBirthday = (TextView) findViewById(R.id.profile_text_birthday);
        String birthday = MyState.getUser().getBirthday();
        if (birthday != null) {
            textEditBirthday.setText(birthday);
        } else {
            textEditBirthday.setText(getString(R.string.simbol_next));
        }

        textEditPlace = (TextView) findViewById(R.id.profile_text_place);
        String place = MyState.getUser().getPlace();
        if (place != null) {
            textEditPlace.setText(place);
        } else {
            textEditPlace.setText(getString(R.string.simbol_next));
        }

        textEditMusicStyle = (TextView) findViewById(R.id.profile_text_musicStyle);
        String musicStyle = MyState.getUser().getMusicStyle();
        if (musicStyle != null) {
            textEditMusicStyle.setText(musicStyle);
        } else {
            textEditMusicStyle.setText(getString(R.string.simbol_next));
        }
        // ----------------------------------------------------------------------------------------

        // EVENTS CLICK
        // ----------------------------------------------------------------------------------------
        relativeUserName = (RelativeLayout) findViewById(R.id.profile_relative_username);
        relativeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement your edit user name
                showAlertDialogEditUserName();
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

        relativePlace = (RelativeLayout) findViewById(R.id.profile_relative_place);
        relativePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement your edit name
                showAlertDialogEditPlace();
            }
        });

        relativeMusicStyle = (RelativeLayout) findViewById(R.id.profile_relative_musicStyle);
        relativeMusicStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement your edit name
                showAlertDialogEditMusicStyle();
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    // ----------------------------------------------------------------------------------------
    //
    private void showAlertDialogEditUserName() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.profile_text_username));

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_username, null);
        dialog.setView(dialogView);

        final AutoCompleteTextView mUserNameView = (AutoCompleteTextView) dialogView.findViewById(R.id.dialog_username);

        final String userName = MyState.getUser().getUsername();
        if (userName != null) {
            mUserNameView.setText(userName);
        }

        dialog.setNegativeButton(getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(getString(R.string.text_done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                // Reset errors.
                mUserNameView.setError(null);

                // get values
                String newUserName = mUserNameView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Check for a valid values
                if (TextUtils.isEmpty(newUserName)) {
                    mUserNameView.setError(getString(R.string.error_field_required));
                    focusView = mUserNameView;
                    cancel = true;
                }

                if (cancel) {
                    Log.i(TAG, "ENTRO A Profile:DialogEditUserName: ERROR_PARAMETERS");

                    // Show the errors
                    focusView.requestFocus();
                } else {

                    if (!userName.equals(newUserName)) {

                        final User mUser = MyState.getUser();
                        mUser.setUsername(newUserName);

                        // MyNetwork : Update User
                        // ----------------------------------------------------------------------------
                        showProgressDialog();
                        myNetwork = new MyNetwork(TAG, activity);
                        myNetwork.Connect();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (myNetwork.isConnected()) {

                                    Log.i(TAG, "ENTRO A Profile:updateUser: SUCCESSFULLY CONNECT");
                                    //TODO: Actualizar los datos del usuario
                                    updateUser(dialog, mUser, "userName");

                                } else {
                                    Log.i(TAG, "ENTRO A Profile:updateUser: COULD NOT CONNECT");

                                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                                    hideProgressDialog();
                                    dialog.dismiss();
                                }

                            }
                        }, 2000);
                        // ----------------------------------------------------------------------------

                    } else {
                        dialog.dismiss();
                    }
                }
            }
        });
        dialog.show();
    }

    //
    private void showAlertDialogEditEmail() {

        final String email;

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.profile_text_email));

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
            public void onClick(final DialogInterface dialog, int which) {

                // Reset errors.
                mEmailView.setError(null);

                // get values
                String newEmail = mEmailView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Check for a valid values
                if ( TextUtils.isEmpty(newEmail) ) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    focusView = mEmailView;
                    cancel = true;
                }

                if (cancel) {
                    Log.i(TAG, "ENTRO A Profile:DialogEditEmail: ERROR_PARAMETERS");

                    // Show the errors
                    focusView.requestFocus();
                } else {

                    if ( !email.equals(newEmail) ) { // Si se a editado el email actualizamos, sino nada.

                        final User mUser = MyState.getUser();
                        mUser.setEmail(newEmail);

                        // MyNetwork : Update User
                        // ----------------------------------------------------------------------------
                        showProgressDialog();
                        myNetwork = new MyNetwork(TAG, activity);
                        myNetwork.Connect();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (myNetwork.isConnected()) {

                                    Log.i(TAG, "ENTRO A Profile:updateUser: SUCCESSFULLY CONNECT");
                                    //TODO: Actualizar los datos del usuario
                                    updateUser(dialog, mUser, "email");

                                } else {
                                    Log.i(TAG, "ENTRO A Profile:updateUser: COULD NOT CONNECT");
                                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                                    hideProgressDialog();
                                    dialog.dismiss();
                                }

                            }
                        }, 2000);
                        // ----------------------------------------------------------------------------

                    } else {
                        dialog.dismiss();
                    }

                }
            }
        });
        dialog.show();
    }

    //
    private void showAlertDialogEditName() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.profile_text_complete_name));

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_name, null);
        dialog.setView(dialogView);

        final AutoCompleteTextView mFirstNameView = (AutoCompleteTextView) dialogView.findViewById(R.id.dialog_first_name);
        final AutoCompleteTextView mLastNameView = (AutoCompleteTextView) dialogView.findViewById(R.id.dialog_last_name);

        final String name = MyState.getUser().getName();
        final String surname = MyState.getUser().getSurname();
        if (name != null) {
            mFirstNameView.setText(name);
        }
        if (surname != null) {
            mLastNameView.setText(surname);
        }

        dialog.setNegativeButton(getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(getString(R.string.text_done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                // Reset errors.
                mFirstNameView.setError(null);
                mLastNameView.setError(null);

                // get values
                String newName = mFirstNameView.getText().toString();
                String newSurname = mLastNameView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Check for a valid values
                if (TextUtils.isEmpty(newName)) {
                    mFirstNameView.setError(getString(R.string.error_field_required));
                    focusView = mFirstNameView;
                    cancel = true;
                }
                if (TextUtils.isEmpty(newSurname)) {
                    mLastNameView.setError(getString(R.string.error_field_required));
                    focusView = mLastNameView;
                    cancel = true;
                }

                if (cancel) {
                    Log.i(TAG, "ENTRO A Profile:DialogEditName: ERROR_PARAMETERS");

                    // Show the errors
                    focusView.requestFocus();
                } else {

                    if (name.equals(newName) && surname.equals(newSurname)) { // Si NO se ha editado nada ...

                        dialog.dismiss();

                    } else {

                        final User mUser = MyState.getUser();
                        mUser.setName(newName);
                        mUser.setSurname(newSurname);

                        // MyNetwork : Update User
                        // ----------------------------------------------------------------------------
                        showProgressDialog();
                        myNetwork = new MyNetwork(TAG, activity);
                        myNetwork.Connect();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (myNetwork.isConnected()) {

                                    Log.i(TAG, "ENTRO A Profile:updateUser: SUCCESSFULLY CONNECT");
                                    //TODO: Actualizar los datos del usuario
                                    updateUser(dialog, mUser, "name");

                                } else {
                                    Log.i(TAG, "ENTRO A Profile:updateUser: COULD NOT CONNECT");
                                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                                    hideProgressDialog();
                                    dialog.dismiss();
                                }

                            }
                        }, 2000);
                        // ----------------------------------------------------------------------------

                    }

                }
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
        if (gender != null) {
            if (gender.equals(male)) {
                itemDefaultSelect = 0;
            } else if (gender.equals(female)) {
                itemDefaultSelect = 1;
            } else if (gender.equals(other)) {
                itemDefaultSelect = 2;
            } else {
                itemDefaultSelect = -1; // no tiene ninguna opcion seleccionada
            }
        }
        else {
            itemDefaultSelect = -1; // no tiene ninguna opcion seleccionada
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.profile_text_gender));
        dialog.setSingleChoiceItems(items, itemDefaultSelect, new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int item) {

                if (itemDefaultSelect != item) { // Si no se ha modificado nada, no actualizamos nada

                    final User mUser = MyState.getUser();
                    mUser.setGender(items[item].toString());

                    // MyNetwork : Update User
                    // ----------------------------------------------------------------------------
                    showProgressDialog();
                    myNetwork = new MyNetwork(TAG, activity);
                    myNetwork.Connect();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (myNetwork.isConnected()) {

                                Log.i(TAG, "ENTRO A Profile:updateUser: SUCCESSFULLY CONNECT");
                                //TODO: Actualizar los datos del usuario
                                updateUser(dialog, mUser, "gender");

                            } else {
                                Log.i(TAG, "ENTRO A Profile:updateUser: COULD NOT CONNECT");
                                Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                                hideProgressDialog();
                                dialog.dismiss();
                            }

                        }
                    }, 2000);
                    // ----------------------------------------------------------------------------
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //
    private void showAlertDialogEditBirthday() {
        /*
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
        */
    }

    /*
    // CLASS
    public class DatePickerFragment extends DialogFragment
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

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            month = month + 1; // Currioso esto, el mes los da del 0 al 11 en lugar del 1 al 12 ... informaticos >.<
            if ( (this.day != day) || (this.month != month) || (this.year != year) ) { // Si no se ha cambiado la fecha, no actualizamos nada

                final User mUser = MyState.getUser();
                mUser.setBirthday("" + day + "/" + month + "/" + year);

                // MyNetwork : Update User
                // ----------------------------------------------------------------------------
                showProgressDialog();
                myNetwork = new MyNetwork(TAG, activity);
                myNetwork.Connect();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (myNetwork.isConnected()) {

                            Log.i(TAG, "ENTRO A Profile:updateUser: SUCCESSFULLY CONNECT");
                            //TODO: Actualizar los datos del usuario
                            updateUser(mUser, "birthday");

                        } else {
                            Log.i(TAG, "ENTRO A Profile:updateUser: COULD NOT CONNECT");
                            Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                        }

                    }
                }, 2000);
                // ----------------------------------------------------------------------------
            }
        }
    }
    */

    //
    private void showAlertDialogEditPlace() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.profile_text_place));

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_place, null);
        dialog.setView(dialogView);

        final AutoCompleteTextView mPlaceView = (AutoCompleteTextView) dialogView.findViewById(R.id.dialog_place);

        final String place = MyState.getUser().getPlace();
        if (place != null) {
            mPlaceView.setText(place);
        }

        dialog.setNegativeButton(getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(getString(R.string.text_done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                // Reset errors.
                mPlaceView.setError(null);

                // get values
                String newPlace = mPlaceView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Check for a valid values
                if ( TextUtils.isEmpty(newPlace) ) {
                    mPlaceView.setError(getString(R.string.error_field_required));
                    focusView = mPlaceView;
                    cancel = true;
                }

                if (cancel) {
                    Log.i(TAG, "ENTRO A Profile:DialogEditPlace: ERROR_PARAMETERS");

                    // Show the errors
                    focusView.requestFocus();
                } else {

                    if ( !place.equals(newPlace) ) {

                        final User mUser = MyState.getUser();
                        mUser.setPlace(newPlace);

                        // MyNetwork : Update User
                        // ----------------------------------------------------------------------------
                        showProgressDialog();
                        myNetwork = new MyNetwork(TAG, activity);
                        myNetwork.Connect();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (myNetwork.isConnected()) {

                                    Log.i(TAG, "ENTRO A Profile:updateUser: SUCCESSFULLY CONNECT");
                                    //TODO: Actualizar los datos del usuario
                                    updateUser(dialog, mUser, "place");

                                } else {
                                    Log.i(TAG, "ENTRO A Profile:updateUser: COULD NOT CONNECT");
                                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                                    hideProgressDialog();
                                    dialog.dismiss();
                                }

                            }
                        }, 2000);
                        // ----------------------------------------------------------------------------
                    } else {

                        dialog.dismiss();
                    }

                }
            }
        });
        dialog.show();
    }

    //
    private void showAlertDialogEditMusicStyle() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.profile_text_musicstyle));

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_profile_musicstyle, null);
        dialog.setView(dialogView);

        final AutoCompleteTextView mMusicStyleView = (AutoCompleteTextView) dialogView.findViewById(R.id.dialog_musicstyle);

        final String musicStyle = MyState.getUser().getMusicStyle();
        if (musicStyle != null) {
            mMusicStyleView.setText(musicStyle);
        }

        dialog.setNegativeButton(getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(getString(R.string.text_done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                // Reset errors.
                mMusicStyleView.setError(null);

                // get values
                String newMusicStyle = mMusicStyleView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Check for a valid values
                if ( TextUtils.isEmpty(newMusicStyle) ) {
                    mMusicStyleView.setError(getString(R.string.error_field_required));
                    focusView = mMusicStyleView;
                    cancel = true;
                }

                if (cancel) {
                    Log.i(TAG, "ENTRO A Profile:DialogEditMusicStyle: ERROR_PARAMETERS");

                    // Show the errors
                    focusView.requestFocus();
                } else {

                    if ( !musicStyle.equals(newMusicStyle) ) {

                        final User mUser = MyState.getUser();
                        mUser.setMusicStyle(newMusicStyle);

                        // MyNetwork : Update User
                        // ----------------------------------------------------------------------------
                        showProgressDialog();
                        myNetwork = new MyNetwork(TAG, activity);
                        myNetwork.Connect();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (myNetwork.isConnected()) {

                                    Log.i(TAG, "ENTRO A Profile:updateUser: SUCCESSFULLY CONNECT");
                                    //TODO: Actualizar los datos del usuario
                                    updateUser(dialog, mUser, "music_style");

                                } else {
                                    Log.i(TAG, "ENTRO A Profile:updateUser: COULD NOT CONNECT");
                                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                                    hideProgressDialog();
                                    dialog.dismiss();
                                }

                            }
                        }, 2000);
                        // ----------------------------------------------------------------------------
                    } else {

                        dialog.dismiss();
                    }
                }
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

                // TODO: Deslogear al usuario de Meteor
                // --------------------------------------------------------------------------------
                showProgressDialog();
                myNetwork = new MyNetwork(TAG, activity);
                myNetwork.Connect();

                // Wait 2 second
                // --------------------------------------------------------------------------------
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (myNetwork.isConnected()) {
                            Log.i(TAG, "ENTRO A Profile:dialogForLogout:Connect: SUCCESSFULLY CONNECT");

                            if (myNetwork.isLoggedIn()) {

                                // TODO: Des-logear al usuario
                                //logOutListener();
                                logOut();

                            } else {
                                myNetwork.Disconnect();
                                Log.i(TAG, "ENTRO A Profile:dialogForLogout: DISCONNECT");

                                Log.i(TAG, "ENTRO A Profile:dialogForLogout: NO_LOGGIN_IN");
                                Toast.makeText(activity, getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_SHORT).show();
                                hideProgressDialog();
                            }

                        } else {
                            Log.i(TAG, "ENTRO A Profile:dialogForLogout:Connect: COULD NOT CONNECT");
                            Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                        }

                    }
                }, 2000);
                // --------------------------------------------------------------------------------

            }
        });
        dialog.show();
    }

    //
    private void updateUser(final DialogInterface dialog, final User user, final String value) {

        String subscriptionId = myNetwork.subscribe("userData", null, new SubscribeListener() {

            @Override
            public void onSuccess() {
                Log.i(TAG, "ENTRO A Profile:updateUser: SUCCESSFULLY SUBSCRIBE");

                //TODO: Actualizar los datos del usuario
                myNetwork.updateUser(user);
                Log.i(TAG, "ENTRO A Profile:updateUser: SUCCESSFULLY UPDATE");

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Profile:updateUser: DISCONNECT");

                // Actualizar la ventana de Profile Activity y Actualizar la DB local
                updateWindowsProfile(user, value, false);

                hideProgressDialog();
                dialog.dismiss();
            }

            @Override
            public void onError(String error, String reason, String details) {
                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Profile:updateUser: DISCONNECT");

                updateWindowsProfile(user, value, true);

                hideProgressDialog();

                Log.i(TAG, "ENTRO A Profile:updateUser: COULD NOT SUBSCRIBE");
            }

        });
    }

    //
    private void updateWindowsProfile(User user, String value, boolean error) {

        if ( value.equals("userName") ) {

            if (error) {

                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_username), Toast.LENGTH_SHORT).show();

            } else {

                // Actualizar el dato en la DB local
                MyState.getUser().setUsername(user.getUsername());

                // Actualizar el texto del userName de la ventana "ProfileActivity"
                textEditUserName.setText(user.getUsername());

                // Actualizar el texto del userName del navigation "nav_header_login"
                // --------------------------------------------------------------------------------
                if (MainActivity.activity != null) {
                    Log.i(TAG, "ENTRO A Profile:EditNavHeader: 0");
                    MainActivity.myMenu.updateUserName();
                }
                if (EventsActivity.activity != null) {
                    Log.i(TAG, "ENTRO A Profile:EditNavHeader: 1");
                    EventsActivity.myMenu.updateUserName();
                }
                // --------------------------------------------------------------------------------

            }

        } else if ( value.equals("email") ) {

            if (error) {

                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_email), Toast.LENGTH_SHORT).show();

            } else {
                // Actualizar el dato en la DB local
                MyState.getUser().setEmail(user.getEmail());

                // Actualizar el texto del email de la ventana "ProfileActivity"
                textEditEmail.setText(user.getEmail());

                // Actualizar el texto del email del navigation "nav_header_login"
                // --------------------------------------------------------------------------------
                if (MainActivity.activity != null) {
                    Log.i(TAG, "ENTRO A Profile:EditNavHeader: 0");
                    MainActivity.myMenu.updateEmail();
                }
                if (EventsActivity.activity != null) {
                    Log.i(TAG, "ENTRO A Profile:EditNavHeader: 1");
                    EventsActivity.myMenu.updateEmail();
                }
                // --------------------------------------------------------------------------------

            }

        } else if ( value.equals("name") ) {

            if (error) {
                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_name), Toast.LENGTH_SHORT).show();
            } else {
                // Actualizar el dato en la DB local
                MyState.getUser().setName(user.getName());
                MyState.getUser().setSurname(user.getSurname());

                // Actualizar el texto del nombre de la ventana "ProfileActivity"
                textEditName.setText(user.getName());

                String name = user.getName();
                String surname = user.getSurname();
                if ( ((name != null) || (surname != null)) && ((!TextUtils.isEmpty(name)) || (!TextUtils.isEmpty(surname))) ) {
                    textEditName.setText(name + " " + surname);
                } else {
                    textEditName.setText(getString(R.string.simbol_next));
                }
            }

        } else if ( value.equals("gender") ) {

            if (error) {
                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_gender), Toast.LENGTH_SHORT).show();
            } else {
                // Actualizar el dato en la DB local
                MyState.getUser().setGender(user.getGender());

                // Actualizar el texto del nombre de la ventana "ProfileActivity"
                textEditGender.setText(user.getGender());
            }

        } else if ( value.equals("birthday") ) {

            if (error) {
                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_birthday), Toast.LENGTH_SHORT).show();
            } else {
                // Actualizar el dato en la DB local
                MyState.getUser().setBirthday(user.getBirthday());

                // Actualizar el texto del nombre de la ventana "ProfileActivity"
                textEditBirthday.setText(user.getBirthday());
            }

        } else if ( value.equals("place") ) {

            if (error) {
                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_place), Toast.LENGTH_SHORT).show();
            } else {
                // Actualizar el dato en la DB local
                MyState.getUser().setPlace(user.getPlace());

                // Actualizar el texto del nombre de la ventana "ProfileActivity"
                textEditPlace.setText(user.getPlace());
            }

        } else if ( value.equals("music_style") ) {

            if (error) {
                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_musicstyle), Toast.LENGTH_SHORT).show();
            } else {
                // Actualizar el dato en la DB local
                MyState.getUser().setMusicStyle(user.getMusicStyle());

                // Actualizar el texto del nombre de la ventana "ProfileActivity"
                textEditMusicStyle.setText(user.getMusicStyle());
            }

        } else {
            // ERROR ..
            Log.i(TAG, "ENTRO A Profile:updateWindowsProfile: FATAL_ERROR_VALUE: " + value);
        }
    }

    //
    public void logOut() {

        //TODO: Logout al usuario de Meteor
        myNetwork.Logout();
        Log.i(TAG, "ENTRO A Profile:dialogForLogout:logOut: SUCCESSFULLY LOGOUT");

        // TODO: Eliminar al usuario de la DB local
        // --------------------------------------------------------------------------------
        MyDatabase.deleteUser(TAG, activity, MyState.getUser());

        MyState.setUser(new User(MyState.getUser().getLocation()));
        MyState.setLoged(false);
        // --------------------------------------------------------------------------------

        myNetwork.Disconnect();
        Log.i(TAG, "ENTRO A Profile:dialogForLogout:logOut: DISCONNECT");

        hideProgressDialog();

        if (MainActivity.activity != null) {
            Log.i(TAG, "ENTRO A Profile:dialogForLogout:logOut: CLOSE MAIN_ACTIVITY");
            MainActivity.activity.finish();
        }
        if (EventsActivity.activity != null) {
            Log.i(TAG, "ENTRO A Profile:dialogForLogout:logOut: CLOSE EVENTS_ACTIVITY");
            EventsActivity.activity.finish();
        }

        Log.i(TAG, "ENTRO A Profile:dialogForLogout:logOut: OPEN INICIALIZATE.CLASS");
        Inicializate.activity.finish();
        startActivity(new Intent(ProfileActivity.this, Inicializate.class));

        finish();
    }

    //
    public void logOutListener() {

        //TODO: Logout al usuario de Meteor
        myNetwork.Logout(new ResultListener() {

            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "ENTRO A Profile:dialogForLogout:logOutListener: SUCCESSFULLY LOGOUT: " + result);

                // TODO: Eliminar al usuario de la DB local
                // --------------------------------------------------------------------------------
                MyDatabase.deleteUser(TAG, activity, MyState.getUser());

                MyState.setUser(new User(MyState.getUser().getLocation()));
                MyState.setLoged(false);
                // --------------------------------------------------------------------------------

                hideProgressDialog();

                if (MainActivity.activity != null) {
                    Log.i(TAG, "ENTRO A Profile:dialogForLogout:logOutListener: CLOSE MAIN_ACTIVITY");
                    MainActivity.activity.finish();
                }
                if (EventsActivity.activity != null) {
                    Log.i(TAG, "ENTRO A Profile:dialogForLogout:logOutListener: CLOSE EVENTS_ACTIVITY");
                    EventsActivity.activity.finish();
                }

                Log.i(TAG, "ENTRO A Profile:dialogForLogout:logOutListener: OPEN INICIALIZATE.CLASS");
                Inicializate.activity.finish();
                startActivity(new Intent(ProfileActivity.this, Inicializate.class));

                finish();
            }

            @Override
            public void onError(String error, String reason, String details) {
                Log.i(TAG, "ENTRO A Profile:dialogForLogout:logOutListener:: COULD NOT LOGOUT: " + error + " / " + reason + " / " + details);
                Toast.makeText(activity, getString(R.string.error_could_not_logout_to_server), Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }

        });

    }
    //-----------------------------------------------------------------------------------------

    // **********
    // FUNTIONS
    // **********
    // ----------------------------------------------------------------------------------------
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
        }
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    // ----------------------------------------------------------------------------------------
}
