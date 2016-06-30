package com.example.rachid.myapplication;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.SecureRandom;

import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.SubscribeListener;

// ----------------------------------------------------------------------------------------
// AÑADIDOS FACEBOOK
// ----------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------
// AÑADIDOS GOOGLE
// ----------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------

/**
 * A login screen that offers login via email/password.
 */
public class AccountActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    public static Activity activity;

    //AÑADIDO: USER
    // -----------------------------------------------------------------------------------------
    User user = new User();
    // -----------------------------------------------------------------------------------------

    //AÑADIDO: GOOGLE
    // ----------------------------------------------------------------------------------------
    private SignInButton signInButtonLoginGoogle;
    private static final int RC_GOOGLE = 9001;
    private GoogleApiClient mGoogleApiClient;
    // ----------------------------------------------------------------------------------------

    //AÑADIDO: FACEBOOK
    // ----------------------------------------------------------------------------------------
    private LoginButton loginFacebookButton;
    private CallbackManager callbackManager;
    private static final int RC_FACEBOOK = 9002;
    // ----------------------------------------------------------------------------------------

    //AÑADIDO: BOTONES
    // ----------------------------------------------------------------------------------------
    private Button buttonLoginEmail;
    private Button buttonSignupEmail;
    // ----------------------------------------------------------------------------------------

    private ProgressDialog mProgressDialog;
    private MyNetwork myNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        //AÑADIDO FACEBOOK
        // ----------------------------------------------------------------------------------------
        FacebookSdk.sdkInitialize(getApplicationContext(), 9002);
        // ----------------------------------------------------------------------------------------

        setContentView(R.layout.activity_account);

        //AÑADIDO GOOGLE
        // ----------------------------------------------------------------------------------------
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Plus.API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.

        signInButtonLoginGoogle = (SignInButton) findViewById(R.id.account_signInButton_login_google);
        signInButtonLoginGoogle.setSize(SignInButton.SIZE_STANDARD);
        signInButtonLoginGoogle.setScopes(gso.getScopeArray());
        // [END customize_button]
        //-----------------------------------------------------------------------------------------

        //AÑADIDO FACEBOOK
        // ----------------------------------------------------------------------------------------
        callbackManager = CallbackManager.Factory.create();
        loginFacebookButton = (LoginButton)findViewById(R.id.login_facebook_button);

        loginFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        // Get profile of facebook
                        //-------------------------------------------------------------------------
                        // Save the Facebook-ID
                        user.setFacebook_id(object.optString("id"));

                        // Save the ID
                        user.setID(null);
                        // Save the Email
                        if (object.has("email")) {
                            user.setEmail(object.optString("email"));
                        }
                        // Save the User_Name
                        if (object.has("name")) {
                            user.setUsername(object.optString("name"));
                        }
                        // Save the Password
                        user.setPassword(null);
                        // Save the Name
                        if (object.has("first_name")) {
                            user.setName(object.optString("first_name"));
                        }
                        // Save the Surname
                        if (object.has("last_name")) {
                            user.setSurname(object.optString("last_name"));
                        }
                        // Save the Gender
                        if (object.has("gender")) {
                            user.setGender(object.optString("gender"));
                        }
                        // Save the Gender
                        if (object.has("gender")) {
                            String g = object.optString("gender");
                            if (g.equals("male")) {
                                user.setGender(getString(R.string.text_male));
                            } else if (g.equals("female")) {
                                user.setGender(getString(R.string.text_female));
                            } else {
                                user.setGender(getString(R.string.text_other));
                            }
                        }
                        // Save the Birthday
                        if (object.has("birthday")) {
                            user.setBirthday(object.optString("birthday"));
                        }
                        // Save the Place
                        if (object.has("locale")) {
                            user.setPlace(object.optString("locale"));
                        }
                        // Save the Music Style
                        user.setMusicStyle(null);
                        // Save the Url Image Profile
                        user.setImage("https://graph.facebook.com/" + object.optString("id") + "/picture?width=400&height=400");
                        //user.setImage("https://graph.facebook.com/" + user.getID() + "/picture?width=120&height=120");
                        // Save the Location
                        user.setLocation(MyState.getUser().getLocation());


                        // TODO: Try to Connect server ...
                        showProgressDialog();
                        myNetwork = new MyNetwork(TAG, activity);
                        myNetwork.Connect();

                        // Wait 1 second to Connect
                        // ----------------------------------------------------------------------------------------
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (myNetwork.isConnected()) {

                                    Log.i(TAG, "ENTRO A Account:onCreate: SUCCESSFULLY CONNECT");
                                    //TODO: Logear o Registrar al usuario en la DB del servidor
                                    loginOrSignupUser(user);
                                    //loginUserWithFacebook(user.getFacebook_id());

                                } else {
                                    Log.i(TAG, "ENTRO A Signup:attemptSignup:Connect: COULD NOT CONNECT");
                                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                                    hideProgressDialog();
                                }

                            }
                        }, 1000);
                        // ----------------------------------------------------------------------------------------
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, email, name, first_name, last_name, gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getBaseContext(), "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getBaseContext(), "Problem connecting to Facebook", Toast.LENGTH_SHORT).show();
            }
        });
        // ----------------------------------------------------------------------------------------

        //AÑADIDO: onClick - BUTTON GOOGLE
        // ----------------------------------------------------------------------------------------
        signInButtonLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "ENTRO A Account:onClick:0");
                signIn();
                Log.i(TAG, "ENTRO A Account:onClick:1");
            }
        });
        // ----------------------------------------------------------------------------------------

        // BUTTON - LOGIN/SIGNUP EMAIL
        // ----------------------------------------------------------------------------------------
        buttonLoginEmail = (Button) findViewById(R.id.account_button_login_email);
        buttonLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
            }
        });

        buttonSignupEmail = (Button) findViewById(R.id.account_button_signup_email);
        buttonSignupEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this, SignupActivity.class));
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    // AÑADIDO: LOGIN OR SIGNUP
    // --------------------------------------------------------------------------------------------------
    //
    // --------------------------------------------------------------------------------------------
    private void loginUserWithGoogle(final String google_id) {

        // Inicializamos variable error a true
        MyError.setLoginResponse(false);

        myNetwork.loginUserWithGoogle(google_id, new ResultListener() {

            @Override
            public void onSuccess(String result) {
                MyError.setLoginResponse(true);

                Log.i(TAG, "ENTRO A Login:loginUserWithGoogle: SUCCESSFULLY LOGIN" + result);

                //TODO: Obtener los datos del usuario
                // --------------------------------------------------------------------------------
                String[] pieces = result.split("\"");
                String id = pieces[3];
                Log.i(TAG, "ENTRO A Login:loginUser:ID: " + id);

                //getUser(id);
                // --------------------------------------------------------------------------------
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setLoginResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Login:loginUser: DISCONNECT");

                if ( (error.equals("403") && (reason.equals("User not found"))) ) {
                    Toast.makeText(activity, getString(R.string.error_user_not_exists), Toast.LENGTH_LONG).show();
                } else if ( (error.equals("403") && (reason.equals("Incorrect password"))) ) {
                    Toast.makeText(activity, getString(R.string.error_password_is_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_LONG).show();
                }

                Log.i(TAG, "ENTRO A Login:loginUser: COULD NOT LOGIN: " + error + " / " + reason + " / " + details);
                hideProgressDialog();
            }

        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getLoginResponse()) {
                    Log.i(TAG, "ENTRO A Login:loginUser:getLoginResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Login:loginUser:getLoginResponse: DISCONNECT");

                    Log.i(TAG, "ENTRO A Login:loginUser:getLoginResponse: COULD NOT LOGIN");
                    Toast.makeText(activity, getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }

            }
        }, 5000);
        // ----------------------------------------------------------------------------------------
    }

    //
    // --------------------------------------------------------------------------------------------
    private void loginUserWithFacebook(final String facebook_id) {

        // Inicializamos variable error a true
        MyError.setLoginResponse(false);

        myNetwork.loginUserWithFacebook(facebook_id, new ResultListener() {

            @Override
            public void onSuccess(String result) {
                MyError.setLoginResponse(true);

                Log.i(TAG, "ENTRO A Login:loginUserWithGoogle: SUCCESSFULLY LOGIN" + result);

                //TODO: Obtener los datos del usuario
                // --------------------------------------------------------------------------------
                String[] pieces = result.split("\"");
                String id = pieces[3];
                Log.i(TAG, "ENTRO A Login:loginUser:ID: " + id);

                //getUser(id);
                // --------------------------------------------------------------------------------
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setLoginResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Login:loginUser: DISCONNECT");

                if ((error.equals("403") && (reason.equals("User not found")))) {
                    Toast.makeText(activity, getString(R.string.error_user_not_exists), Toast.LENGTH_LONG).show();
                } else if ((error.equals("403") && (reason.equals("Incorrect password")))) {
                    Toast.makeText(activity, getString(R.string.error_password_is_incorrect), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_LONG).show();
                }

                Log.i(TAG, "ENTRO A Login:loginUser: COULD NOT LOGIN: " + error + " / " + reason + " / " + details);
                hideProgressDialog();
            }

        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getLoginResponse()) {
                    Log.i(TAG, "ENTRO A Login:loginUser:getLoginResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Login:loginUser:getLoginResponse: DISCONNECT");

                    Log.i(TAG, "ENTRO A Login:loginUser:getLoginResponse: COULD NOT LOGIN");
                    Toast.makeText(activity, getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }

            }
        }, 5000);
        // ----------------------------------------------------------------------------------------
    }

    //
    private void loginOrSignupUser(final User user) {

        /*
        // CODIGO DE PRUEBA ... A ELIMINAR!!
        // ----------------------------------------------------------------------------------------
        if (user != null) { // Si el usuario existia ...

            // TODO: Registrar al usuario en la DB local
            // ----------------------------------------------------------------------------
            MyDatabase.insertUser(TAG, activity, user);
            MyState.setUser(user);
            MyState.setLoged(true);
            // ----------------------------------------------------------------------------

            myNetwork.Disconnect();
            Log.i(TAG, "ENTRO A Account:loginOrSignupUser:CODIGO_DE_PRUEBA: Usuario metido en la DB local");

            hideProgressDialog();

            if (MainActivity.activity != null) {
                MainActivity.myMenu.loadHeaderLogin();
            }
            if (EventsActivity.activity != null) {
                EventsActivity.myMenu.loadHeaderLogin();
            }

            finish();

        }
        // ----------------------------------------------------------------------------------------
        */

        // Inicializamos variable error a true
        MyError.setSubscribeResponse(false);

        String subscriptionId = myNetwork.subscribe("userData", null, new SubscribeListener() {

            @Override
            public void onSuccess() {
                MyError.setSubscribeResponse(true);

                Log.i(TAG, "ENTRO A Account:loginOrSignupUser: SUCCESSFULLY SUBSCRIBE");

                User mUser = null;
                boolean error = false;

                if ( (user.getGoogle_id() != null) && (!TextUtils.isEmpty(user.getGoogle_id())) ) {

                    // TODO: Intenta obtener de la DB del servidor a este usuario por "google_id"
                    mUser = myNetwork.getUserWithGoogle(user.getGoogle_id());

                } else if ( (user.getFacebook_id() != null) && (!TextUtils.isEmpty(user.getFacebook_id())) ) {

                    // TODO: Intenta obtener de la DB del servidor a este usuario por "facebook_id"
                    mUser = myNetwork.getUserWithFacebook(user.getFacebook_id());

                } else {
                    error = true;
                }

                final boolean fError = error;
                final User fUser = mUser;

                // Wait 1 second
                // ----------------------------------------------------------------------------------------
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (!fError) {

                            if (fUser != null) { // Si el usuario existia ...

                                // TODO: Registrar al usuario en la DB local
                                // ----------------------------------------------------------------------------
                                MyDatabase.insertUser(TAG, activity, fUser);
                                MyState.setUser(fUser);
                                MyState.setLoged(true);
                                // ----------------------------------------------------------------------------

                                myNetwork.Disconnect();
                                Log.i(TAG, "ENTRO A Account:loginOrSignupUser: DISCONNECT");

                                hideProgressDialog();

                                if (MainActivity.activity != null) {
                                    MainActivity.myMenu.loadHeaderLogin();
                                }
                                if (EventsActivity.activity != null) {
                                    EventsActivity.myMenu.loadHeaderLogin();
                                }

                                finish();

                            } else { // Si el usuario no existia, registrarlo

                                // TODO: Registrar al usuario
                                signupUser(user);
                            }

                        } else {
                            Log.i(TAG, "ENTRO A Account:loginOrSignupUser: FATAL_ERROR_NOT_SERVICE_DETECT");
                            Toast.makeText(activity, getString(R.string.error_could_not_connect_with_this_service), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1000);
                // ----------------------------------------------------------------------------------------
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setSubscribeResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Account:loginOrSignupUser: DISCONNECT");

                Log.i(TAG, "ENTRO A Account:loginOrSignupUser: COULD NOT SUBSCRIBE");
                //Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }

        });

        // Wait 6 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if ( !MyError.getSubscribeResponse() ) {
                    Log.i(TAG, "ENTRO A Account:loginOrSignupUser:getSubscribeResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Account:loginOrSignupUser:getSubscribeResponse: DISCONNECT");

                    Log.i(TAG, "ENTRO A Account:loginOrSignupUser:getSubscribeResponse: COULD NOT SUBSCRIBE");
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }

            }
        }, 6000);
        // ----------------------------------------------------------------------------------------
    }
    // -------------------------------------------------------------------------------------------------

    // AÑADIDO: SIGN_UP USER
    // --------------------------------------------------------------------------------------------
    //
    private void signupUser(final User user) {

        // Inicializamos variable error a true
        MyError.setSignupResponse(false);

        SecureRandom random = new SecureRandom();
        final String password = new BigInteger(130, random).toString(32);

        myNetwork.signupUser(user.getUsername(), user.getEmail(), password, new ResultListener() {

            @Override
            public void onSuccess(String result) {
                MyError.setSignupResponse(true);

                Log.i(TAG, "ENTRO A Account:signupUser: SUCCESSFULLY SIGN UP: " + result);

                //TODO: Registrar al usuario en la DB local y en el Sistema
                // --------------------------------------------------------------------------------
                String[] pieces = result.split("\"");
                String id = pieces[3];
                Log.i(TAG, "ENTRO A Account:signupUser:ID: " + id);

                user.setID(id);
                //mUser.setPassword(password);

                MyDatabase.insertUser(TAG, activity, user);
                MyState.setUser(user);
                MyState.setLoged(true);
                // --------------------------------------------------------------------------------

                //TODO: Actualizamos los datos del usuario en el servidor
                // --------------------------------------------------------------------------------
                myNetwork.updateUser(user);
                // --------------------------------------------------------------------------------

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Account:signupUser: DISCONNECT");

                hideProgressDialog();

                if (MainActivity.activity != null) {
                    MainActivity.myMenu.loadHeaderLogin();
                }
                if (EventsActivity.activity != null) {
                    EventsActivity.myMenu.loadHeaderLogin();
                }

                finish();
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setSignupResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Account:signupUser: DISCONNECT");

                if ((error.equals("403") && (reason.equals("Username already exists.")))) {
                    Toast.makeText(activity, getString(R.string.error_username_already_exists), Toast.LENGTH_LONG).show();
                } else if ((error.equals("403") && (reason.equals("Email already exists.")))) {
                    Toast.makeText(activity, getString(R.string.error_email_already_exists), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, getString(R.string.error_could_not_sign_up_the_user), Toast.LENGTH_LONG).show();
                }

                Log.i(TAG, "ENTRO A Account:signupUser: COULD NOT SIGN UP: " + error + " / " + reason + " / " + details);
                hideProgressDialog();
            }

        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getSignupResponse()) {
                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Account:getSignupResponse: DISCONNECT");

                    Log.i(TAG, "ENTRO A Account:getSignupResponse: COULD NOT SIGN UP");
                    Toast.makeText(activity, getString(R.string.error_could_not_sign_up_the_user), Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }

            }
        }, 5000);
        // ----------------------------------------------------------------------------------------
    }
    // --------------------------------------------------------------------------------------------

    //AÑADIDO : GOOGLE
    // --------------------------------------------------------------------------------------------
    // [START signIn]
    private void signIn() {

        Log.i(TAG, "ENTRO A Account:signIn:0");

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE);

        Log.i(TAG, "ENTRO A Account:signIn:1");

    }
    // [END signIn]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(getBaseContext(), "Error, connection failed", Toast.LENGTH_SHORT).show();
    }
    // --------------------------------------------------------------------------------------------

    //AÑADIDO : FACEBOOK
    // --------------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "ENTRO A Account:onActivityResult:0");

        super.onActivityResult(requestCode, resultCode, data);

        // GOOGLE
        if (requestCode == RC_GOOGLE) {

            Log.i(TAG, "ENTRO A Account:onActivityResult:1");

            loginGoogle(requestCode, resultCode, data);
        }

        // FACEBOOK
        if (requestCode == RC_FACEBOOK) {

            Log.i(TAG, "ENTRO A Account:onActivityResult:2");

            loginFacebook(requestCode, resultCode, data);
        }
    }
    // ----------------------------------------------------------------------------------------

    // LOGIN GOOGLE
    public void loginGoogle(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "ENTRO A Account:loginGoogle:0");

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {

            Log.i(TAG, "ENTRO A Account:loginGoogle:1");

            GoogleSignInAccount acct = result.getSignInAccount();

            // Get Profile of Google
            //-------------------------------------------------------------------------
            Person personProfile = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            Person.Name name = personProfile.getName();

            // Save the Google-ID
            user.setGoogle_id( personProfile.getId() );

            // Save the ID
            user.setID(null);
            // Save the Email
            user.setEmail(acct.getEmail());
            // Save the User Name
            user.setUsername(acct.getDisplayName());
            /*
            if (personProfile.hasNickname()) {
                user.setUsername(personProfile.getNickname());
            }
            */
            // Save the Password
            user.setPassword(null);
            // Save the Name
            if (name.hasGivenName() && name.hasMiddleName()) { // it's not guaranteed
                user.setName(name.getGivenName() + " " + name.getMiddleName());
            } else if (name.hasGivenName()) {
                user.setName(name.getGivenName());
            } else if (name.hasMiddleName()) {
                user.setName(name.getMiddleName());
            }
            // Save the Surname
            if (name.hasFamilyName()) { // it's not guaranteed
                user.setSurname(name.getFamilyName());
            }
            // Save the Gender
            if (personProfile.hasGender()) {
                int g = personProfile.getGender();
                if (g == 0) {
                    user.setGender(getString(R.string.text_male));
                } else if (g == 1) {
                    user.setGender(getString(R.string.text_female));
                } else {
                    user.setGender(getString(R.string.text_other));
                }
            }
            // Save the Birthday
            if (personProfile.hasBirthday()) { // it's not guaranteed
                user.setBirthday(personProfile.getBirthday());
            }
            // Save the Place
            if (personProfile.hasCurrentLocation()) { // it's not guaranteed
                user.setPlace(personProfile.getCurrentLocation());
            }
            // Save the Music Style
            user.setMusicStyle(null);
            // Save the Url Image Profile
            if (personProfile.hasImage()) {
                user.setImage(personProfile.getImage().getUrl());
                //user.setImage(acct.getPhotoUrl().toString());
            }
            // Save the Location
            user.setLocation(MyState.getUser().getLocation());
            // ----------------------------------------------------------------------------------------

            // LogOut Google
            // --------------------------------------------------------------------------------
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            mGoogleApiClient.disconnect();
                        }
                    });
            // --------------------------------------------------------------------------------


            // TODO: Try to Connect server ...
            showProgressDialog();
            myNetwork = new MyNetwork(TAG, activity);
            myNetwork.Connect();

            // Wait 1 second to Connect
            // ----------------------------------------------------------------------------------------
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (myNetwork.isConnected()) {

                        Log.i(TAG, "ENTRO A Account:loginGoogle: SUCCESSFULLY CONNECT");
                        //TODO: Logear o Registrar al usuario en la DB del servidor
                        loginOrSignupUser(user);
                        //loginUserWithGoogle(user.getGoogle_id());

                    } else {
                        Log.i(TAG, "ENTRO A Account:loginGoogle:Connect: COULD NOT CONNECT");
                        Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }

                }
            }, 1000);
            // ----------------------------------------------------------------------------------------
        }
    }

    // LOGIN FACEBOOK
    public void loginFacebook(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "ENTRO A Account:loginFacebook:0");

        callbackManager.onActivityResult(requestCode, resultCode, data);

        //LogOut Facebook
        LoginManager.getInstance().logOut();
    }

    //AÑADIDO: BOTON ATRAS
    // ----------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        finish();
    }
    // ----------------------------------------------------------------------------------------

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
