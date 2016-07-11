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
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    public static Activity activity;

    //AÑADIDO: FORM EDIT PROFILE
    // -----------------------------------------------------------------------------------------
    private CircleImageView circleImageProfile;
    private TextView textEditUserName;
    private TextView textEditEmail;
    private TextView textEditName;
    private TextView textEditGender;
    private TextView textEditBirthday;
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
        if ( (gender != null) && (!TextUtils.isEmpty(gender)) ) {
            textEditGender.setText(gender);
        } else {
            textEditGender.setText(getString(R.string.simbol_next));
        }

        textEditBirthday = (TextView) findViewById(R.id.profile_text_birthday);
        String birthday = MyState.getUser().getBirthday();
        if ( (birthday != null) && (!TextUtils.isEmpty(birthday)) ) {
            textEditBirthday.setText(birthday);
        } else {
            textEditBirthday.setText(getString(R.string.simbol_next));
        }

        textEditPlace = (TextView) findViewById(R.id.profile_text_place);
        String place = MyState.getUser().getPlace();
        if ( (place != null) && (!TextUtils.isEmpty(place)) ) {
            textEditPlace.setText(place);
        } else {
            textEditPlace.setText(getString(R.string.simbol_next));
        }

        textEditMusicStyle = (TextView) findViewById(R.id.profile_text_musicStyle);
        String musicStyle = MyState.getUser().getMusicStyle();
        if ( (musicStyle != null) && (!TextUtils.isEmpty(musicStyle)) ) {
            textEditMusicStyle.setText(musicStyle);
        } else {
            textEditMusicStyle.setText(getString(R.string.simbol_next));
        }
        // ----------------------------------------------------------------------------------------

        // ACTUALIZAMOS LOS VALORES DEL USUARIO
        updateWindow();

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

    // ACTUALIZAR VENTANA
    // --------------------------------------------------------------------------------------------
    public void updateWindow() {

        connectAndDo("getUser", null);

        // Wait 2.5 seconds
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // VALUES OF FORM USER
                // ----------------------------------------------------------------------------------------
                circleImageProfile = (CircleImageView) findViewById(R.id.circle_image_profile);
                if ((MyState.getUser().getImage() != null) && (!TextUtils.isEmpty(MyState.getUser().getImage()))) {
                    Picasso.with(getApplicationContext()).load(MyState.getUser().getImage()).into(circleImageProfile);
                }

                textEditUserName = (TextView) findViewById(R.id.profile_text_username);
                textEditUserName.setText(MyState.getUser().getUsername());

                textEditEmail = (TextView) findViewById(R.id.profile_text_email);
                textEditEmail.setText(MyState.getUser().getEmail());

                textEditName = (TextView) findViewById(R.id.profile_text_name);
                String name = MyState.getUser().getName();
                String surname = MyState.getUser().getSurname();
                if (((name != null) || (surname != null)) && ((!TextUtils.isEmpty(name)) || (!TextUtils.isEmpty(surname)))) {
                    textEditName.setText(name + " " + surname);
                } else {
                    textEditName.setText(getString(R.string.simbol_next));
                }

                textEditGender = (TextView) findViewById(R.id.profile_text_gender);
                String gender = MyState.getUser().getGender();
                if ((gender != null) && (!TextUtils.isEmpty(gender))) {
                    textEditGender.setText(gender);
                } else {
                    textEditGender.setText(getString(R.string.simbol_next));
                }

                textEditBirthday = (TextView) findViewById(R.id.profile_text_birthday);
                String birthday = MyState.getUser().getBirthday();
                if ((birthday != null) && (!TextUtils.isEmpty(birthday))) {
                    textEditBirthday.setText(birthday);
                } else {
                    textEditBirthday.setText(getString(R.string.simbol_next));
                }

                textEditPlace = (TextView) findViewById(R.id.profile_text_place);
                String place = MyState.getUser().getPlace();
                if ((place != null) && (!TextUtils.isEmpty(place))) {
                    textEditPlace.setText(place);
                } else {
                    textEditPlace.setText(getString(R.string.simbol_next));
                }

                textEditMusicStyle = (TextView) findViewById(R.id.profile_text_musicStyle);
                String musicStyle = MyState.getUser().getMusicStyle();
                if ((musicStyle != null) && (!TextUtils.isEmpty(musicStyle))) {
                    textEditMusicStyle.setText(musicStyle);
                } else {
                    textEditMusicStyle.setText(getString(R.string.simbol_next));
                }
                // --------------------------------------------------------------------------------

            }
        }, 2500);
        // ----------------------------------------------------------------------------------------
    }
    // --------------------------------------------------------------------------------------------

    // AÑADIDO: DIALOGS
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
            public void onClick(DialogInterface dialog, int which) {

                // get values
                String newUsername = mUserNameView.getText().toString();

                if (!TextUtils.isEmpty(newUsername)) {
                    if ((!TextUtils.equals(userName, newUsername))) { // Si se ha editado algo ...

                        User user = new User();
                        user.setUsername(newUsername);

                        // Llamamos al metodo generico para actualizar el USER_NAME
                        connectAndDo("username", user);

                    } else {
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(activity, getString(R.string.error_the_username_can_not_be_empty), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
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
            public void onClick(DialogInterface dialog, int which) {

                // get values
                String newEmail = mEmailView.getText().toString();

                if (!TextUtils.isEmpty(newEmail)) {
                    if (!(TextUtils.equals(email, newEmail))) { // Si se a editado algo ...

                        User user = new User();
                        user.setEmail(newEmail);

                        // Llamamos al metodo generico para actualizar el EMAIL
                        connectAndDo("email", user);

                    } else {
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(activity, getString(R.string.error_the_email_can_not_be_empty), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
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
            public void onClick(DialogInterface dialog, int which) {

                // get values
                String newName = mFirstNameView.getText().toString();
                String newSurname = mLastNameView.getText().toString();

                if ((TextUtils.equals(name, newName)) && (TextUtils.equals(surname, newSurname))) { // Si NO se ha editado nada ...
                    dialog.dismiss();
                } else {
                    User user = new User();
                    user.setName(newName);
                    user.setSurname(newSurname);

                    // Llamamos al metodo generico para actualizar el NAME y SURNAME
                    connectAndDo("name", user);
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
            public void onClick(DialogInterface dialog, int item) {

                if (itemDefaultSelect != item) { // Si no se ha modificado nada, no actualizamos nada

                    User user = new User();
                    user.setGender(items[item].toString());

                    // Llamamos al metodo generico para actualizar el GENDER
                    connectAndDo("gender", user);
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //
    private void showAlertDialogEditBirthday() {
        DialogFragment newFragment = new DatePickerProfileFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

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
            public void onClick(DialogInterface dialog, int which) {

                // get values
                String newPlace = mPlaceView.getText().toString();

                if ((!TextUtils.equals(place, newPlace))) { // Si se ha editado algo ...

                    User user = new User();
                    user.setPlace(newPlace);

                    // Llamamos al metodo generico para actualizar el PLACE
                    connectAndDo("place", user);
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    //
    private void showAlertDialogEditMusicStyle() {

        final CharSequence[] items = {getString(R.string.text_music_rock), getString(R.string.text_music_jazz),
                getString(R.string.text_music_rap), getString(R.string.text_music_pop), getString(R.string.text_music_classic),
                getString(R.string.text_music_alternative), getString(R.string.text_music_dance), getString(R.string.text_music_other)};

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getString(R.string.text_music_styles)).setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                User user = new User();
                user.setMusicStyle((String) items[item]);

                // Llamamos al metodo generico para actualizar el user_name
                connectAndDo("music_style", user);
            }
        });
        AlertDialog alertDialogObject = dialogBuilder.create();
        ListView listView = alertDialogObject.getListView();
        listView.setDivider(new ColorDrawable(getResources().getColor(R.color.GRIS_5))); // set color
        listView.setDividerHeight(1); // set height
        alertDialogObject.show();
        //dialogBuilder.show();
    }
    // --------------------------------------------------------------------------------------------

    //
    // --------------------------------------------------------------------------------------------
    public void connectAndDo(final String hacer, final User user) {

        if ( MyNetwork.isNetworkConnected(activity) ) {

            myNetwork = new MyNetwork(TAG, activity);

            if (myNetwork.isConnected()) {
                myNetwork.Disconnect();
            }

            myNetwork.showProgressDialog();
            myNetwork.Connect();

            // Wait 2 second
            // ------------------------------------------------------------------------------------
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (myNetwork.isConnected()) {
                        Log.i(TAG, "ENTRO A Profile:connectAndDo:Connect: SUCCESSFULLY CONNECT");

                        if (myNetwork.isLoggedIn()) {

                            // Cerramos sesión o Actualizamos al usuario !!!
                            if (hacer.equals("logout")) {
                                //logOutListener();
                                logOut();
                            } else {
                                updateUser(hacer, user); // hacer = getUser or (username, email, name ...)
                            }

                        } else {
                            myNetwork.Disconnect();
                            Log.i(TAG, "ENTRO A Profile:connectAndDo: DISCONNECT");

                            myNetwork.hideProgressDialog();
                            Toast.makeText(activity, getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "ENTRO A Profile:connectAndDo: NO_LOGGIN_IN");
                        }

                    } else {
                        myNetwork.hideProgressDialog();
                        Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "ENTRO A Profile:connectAndDo:Connect: COULD NOT CONNECT");
                    }
                }
            }, 2000);
            // ------------------------------------------------------------------------------------

        } else {
            Log.i(TAG, "ENTRO A Profile:connectAndDo:Connect: ERROR_NETWORK");
            Toast.makeText(activity, getString(R.string.error_not_network), Toast.LENGTH_SHORT).show();
        }
    }

    // LOG OUT
    // --------------------------------------------------------------------------------------------
    //
    public void logOut() {

        //TODO: Logout al usuario de Meteor
        myNetwork.Logout();
        Log.i(TAG, "ENTRO A Profile:dialogForLogout:logOut: SUCCESSFULLY LOGOUT");

        // TODO: Eliminar al usuario de la DB local y del Sistema
        // --------------------------------------------------------------------------------
        MyDatabase.deleteUser(TAG, activity, MyState.getUser());

        MyState.setUser(new User(MyState.getUser().getLocation()));
        MyState.setLoged(false);
        // --------------------------------------------------------------------------------

        myNetwork.Disconnect();
        Log.i(TAG, "ENTRO A Profile:dialogForLogout:logOut: DISCONNECT");

        myNetwork.hideProgressDialog();

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

                myNetwork.hideProgressDialog();

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
                myNetwork.hideProgressDialog();
                Toast.makeText(activity, getString(R.string.error_could_not_logout_to_server), Toast.LENGTH_LONG).show();
                Log.i(TAG, "ENTRO A Profile:dialogForLogout:logOutListener:: COULD NOT LOGOUT: " + error + " / " + reason + " / " + details);
            }
        });
    }
    //---------------------------------------------------------------------------------------------

    // UPDATE USER
    //---------------------------------------------------------------------------------------------
    //
    private void updateUser(final String hacer, final User user) {

        // Inicializamos variable error a true
        MyError.setSubscribeResponse(false);

        myNetwork.subscribe("userData", null, new SubscribeListener() {

            @Override
            public void onSuccess() {
                MyError.setSubscribeResponse(true);
                Log.i(TAG, "ENTRO A Profile:updateUser: SUCCESSFULLY SUBSCRIBE");

                //TODO: Obtener al usuario del servidor para actualizar
                final User userServer = myNetwork.getUserWithId(MyState.getUser().getID());
                Log.i(TAG, "ENTRO A Profile:updateUser: USER_SERVER_GENDER: " + userServer.getGender());
                //Log.i(TAG, "ENTRO A Profile:updateUser: USER: " + user);

                // Wait 1 seconds
                // ----------------------------------------------------------------------------------------
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (hacer.equals("getUser")) {
                            MyDatabase.updateUser(TAG, activity, userServer);
                            MyState.setUser(userServer);

                            myNetwork.hideProgressDialog();

                        } else { // hacer = username, email, name, gender, birthday, place or music_style
                            //TODO: Actualizar en el usuario obtenido del servidor, el campo a actualizar.
                            updateUserServer(hacer, userServer, user);

                            //TODO: Actualizar el usuario
                            updateUserOnNetwork(hacer, userServer);
                        }

                    }
                }, 1000);
                // ----------------------------------------------------------------------------------------
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setSubscribeResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Profile:updateUser: DISCONNECT");

                myNetwork.hideProgressDialog();
                updateWindowsProfile(hacer, null, true); //Para mostrar el error correspondiente!!!
                Log.i(TAG, "ENTRO A Profile:updateUser: COULD NOT SUBSCRIBE");
            }

        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if ( !MyError.getSubscribeResponse() ) {
                    Log.i(TAG, "ENTRO A Profile:updateUser:getSubscribeResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Profile:updateUser:getSubscribeResponse: DISCONNECT");

                    myNetwork.hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "ENTRO A Profile:updateUser:getSubscribeResponse: COULD NOT SUBSCRIBE");
                }

            }
        }, 5000);
        // ----------------------------------------------------------------------------------------
    }

    //
    public void updateUserOnNetwork(final String hacer, final User userServer) {

        // Inicializamos variable error a true
        MyError.setUpdateUserResponse(false);

        myNetwork.updateUser(userServer, new ResultListener() {

            @Override
            public void onSuccess(String result) {
                MyError.setUpdateUserResponse(true);
                Log.i(TAG, "ENTRO A Profile:updateUserOnNetwork: SUCCESSFULLY UPDATE USER");

                // Actualizar la DB local
                // Actualizar la variable del sistema
                // Actualizar la ventana de Profile Activity
                updateWindowsProfile(hacer, userServer, false);

                // Desconectar
                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Profile:updateUserOnNetwork: DISCONNECT");

                myNetwork.hideProgressDialog();
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setUpdateUserResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Profile:updateUserOnNetwork: DISCONNECT");

                myNetwork.hideProgressDialog();
                if ( (error.equals("409")) && (reason.contains("username")) ) {
                    Toast.makeText(activity, getString(R.string.error_username_already_exists), Toast.LENGTH_LONG).show();
                } else if ( (error.equals("409")) && (reason.contains("email")) ) {
                    Toast.makeText(activity, getString(R.string.error_email_already_exists), Toast.LENGTH_LONG).show();
                } else {
                    updateWindowsProfile(hacer, null, true); //Para mostrar el error correspondiente!!!
                }
                Log.i(TAG, "ENTRO A Profile:updateUserOnNetwork: COULD NOT UPDATE: " + error + " / " + reason + " / " + details);
            }
        });

        // Wait 10 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getUpdateUserResponse()) {
                    Log.i(TAG, "ENTRO A Profile:updateUser:getUpdateUserResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Profile:updateUser:getUpdateUserResponse: DISCONNECT");

                    myNetwork.hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "ENTRO A Profile:updateUser:getUpdateUserResponse: COULD NOT SUBSCRIBE");
                }
            }
        }, 10000);
        // ----------------------------------------------------------------------------------------
    }

    //
    public void updateUserServer(final String hacer, final User userServer, final User user) {

        if ( hacer.equals("username") ) {
            userServer.setUsername(user.getUsername());
            Log.i(TAG, "ENTRO A Profile:updateUserServer: USER_NAME: " + user.getUsername());
        } else if (hacer.equals("email")) {
            userServer.setEmail(user.getEmail());
            Log.i(TAG, "ENTRO A Profile:updateUserServer: EMAIL: " + user.getEmail());
        } else if (hacer.equals("name")) {
            userServer.setName(user.getName());
            userServer.setSurname(user.getSurname());
            Log.i(TAG, "ENTRO A Profile:updateUserServer: NAME and SURNAME: " + user.getName() + " " + user.getSurname());
        }  else if (hacer.equals("gender")) {
            userServer.setGender(user.getGender());
            Log.i(TAG, "ENTRO A Profile:updateUserServer: GENDER: " + user.getGender());
        } else if (hacer.equals("birthday")) {
            userServer.setBirthday(user.getBirthday());
            Log.i(TAG, "ENTRO A Profile:updateUserServer: BIRTHDAY: " + user.getBirthday());
        }  else if (hacer.equals("place")) {
            userServer.setPlace(user.getPlace());
            Log.i(TAG, "ENTRO A Profile:updateUserServer: GENDER: " + user.getPlace());
        }  else if (hacer.equals("music_style")) {
            userServer.setMusicStyle(user.getMusicStyle());
            Log.i(TAG, "ENTRO A Profile:updateUserServer: MUSIC_STYLES: " + user.getMusicStyle());
        } else {
            throw new IllegalArgumentException("Profile:updateUserServer: It is not detected method");
        }
    }

    //
    private void updateWindowsProfile(String hacer, User user, boolean error) {

        if ( error ) {

            if ( hacer.equals("username") ) {
                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_username), Toast.LENGTH_SHORT).show();
            } else if ( hacer.equals("email") ) {
                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_email), Toast.LENGTH_SHORT).show();
            } else if ( hacer.equals("name") ) {
                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_name), Toast.LENGTH_SHORT).show();
            } else if ( hacer.equals("gender") ) {
                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_gender), Toast.LENGTH_SHORT).show();
            } else if ( hacer.equals("birthday") ) {
                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_birthday), Toast.LENGTH_SHORT).show();
            } else if ( hacer.equals("place") ) {
                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_place), Toast.LENGTH_SHORT).show();
            } else if ( hacer.equals("music_style") ) {
                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_musicstyle), Toast.LENGTH_SHORT).show();
            } else {
                // ERROR ..
                Log.i(TAG, "ENTRO A Profile:updateWindowsProfile: FATAL_ERROR_VALUE: " + hacer);
                throw new IllegalArgumentException("Profile:updateUser: It is not detected method");
            }

        } else {
            // Actualizar el usuario en la DB Local
            MyDatabase.updateUser(TAG, activity, user);

            // Actualizamos el usuario del sistema
            MyState.setUser(user);

            // Actualizamos la ventana profile
            // -----------------------------------------------------------------------------------
            // USER_NAME
            textEditUserName.setText(user.getUsername());
            // EMAIL
            textEditEmail.setText(user.getEmail());
            // NAME y SURNAME
            textEditName.setText(user.getName());
            String name = user.getName();
            String surname = user.getSurname();
            if ( ((name != null) || (surname != null)) && ((!TextUtils.isEmpty(name)) || (!TextUtils.isEmpty(surname))) ) {
                textEditName.setText(name + " " + surname);
            } else {
                textEditName.setText(getString(R.string.simbol_next));
            }
            // GENDER
            textEditGender.setText(user.getGender());
            // BIRTHDAY
            String birthday = user.getBirthday();
            if ( (birthday != null) && (!TextUtils.isEmpty(birthday)) ) {
                textEditBirthday.setText(birthday);
            } else {
                textEditBirthday.setText(getString(R.string.simbol_next));
            }
            // PLACE
            String place = user.getPlace();
            if ( (place != null) && (!TextUtils.isEmpty(place)) ) {
                textEditPlace.setText(place);
            } else {
                textEditPlace.setText(getString(R.string.simbol_next));
            }
            // MUSIC_STYLE
            String musicStyle = user.getMusicStyle();
            if ( (musicStyle != null) && (!TextUtils.isEmpty(musicStyle)) ) {
                textEditMusicStyle.setText(musicStyle);
            } else {
                textEditMusicStyle.setText(getString(R.string.simbol_next));
            }
            // -----------------------------------------------------------------------------------

            // Actualizar los valores del navigation "nav_header_login"
            if ( (hacer.equals("username")) || (hacer.equals("email")) ) {
                if (MainActivity.activity != null) {
                    Log.i(TAG, "ENTRO A Profile:EditNavHeader: 0");
                    MainActivity.myMenu.updateUserName();
                    MainActivity.myMenu.updateEmail();
                }
                if (EventsActivity.activity != null) {
                    Log.i(TAG, "ENTRO A Profile:EditNavHeader: 1");
                    EventsActivity.myMenu.updateUserName();
                    EventsActivity.myMenu.updateEmail();
                }
            }
        }
    }
    //---------------------------------------------------------------------------------------------

    //AÑADIDO: OPTIONS
    // --------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_profile, menu);
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

                // Llamamos al metodo generico para cerrar sesión
                connectAndDo("logout", null);
            }
        });
        dialog.show();
    }
    // --------------------------------------------------------------------------------------------

    //AÑADIDO: BOTON ATRAS
    // -------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        this.finish();
    }
    //--------------------------------------------------------------------------------------------

    // *******************
    // CLASS: DatePicker
    // *******************
    //--------------------------------------------------------------------------------------------
    public static class DatePickerProfileFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private int year;
        private int month;
        private int day;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            String birthday = MyState.getUser().getBirthday();
            if ( (birthday != null) && (!TextUtils.isEmpty(birthday)) ) {
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

                User user = new User();
                user.setBirthday("" + day + "/" + month + "/" + year);

                // Llamamos al metodo generico para actualizar el BIRTHDAY
                ProfileActivity profile = (ProfileActivity) getActivity();
                profile.connectAndDo("birthday", user);
            }
        }
    }
    //--------------------------------------------------------------------------------------------
}
