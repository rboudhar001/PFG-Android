package com.example.rachid.myapplication;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONObject;

import im.delight.android.ddp.ResultListener;
import im.delight.android.ddp.SubscribeListener;
// ----------------------------------------------------------------------------------------

/**
 * A login screen that offers login via email/password.
 */
public class AccountActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    public static Activity activity;

    //AÑADIDO: USER
    // -----------------------------------------------------------------------------------------
    User user;
    // -----------------------------------------------------------------------------------------

    //AÑADIDO: GOOGLE
    // ----------------------------------------------------------------------------------------
    private Button mButtonLoginGoogle;
    //private SignInButton signInButtonLoginGoogle;
    private static final int RC_GOOGLE = 9001;
    private GoogleApiClient mGoogleApiClient;
    // ----------------------------------------------------------------------------------------

    //AÑADIDO: FACEBOOK
    // ----------------------------------------------------------------------------------------
    private Button mButtonLoginFacebook;
    private LoginButton loginButtonFacebook;
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
        AppEventsLogger.activateApp(this);
        // ----------------------------------------------------------------------------------------

        setContentView(R.layout.activity_account);

        //AÑADIDO: onClick - BUTTON GOOGLE
        // ----------------------------------------------------------------------------------------
        mButtonLoginGoogle = (Button) findViewById(R.id.account_button_google);
        mButtonLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "ENTRO A Account:onClick:0");
                signIn();
                Log.i(TAG, "ENTRO A Account:onClick:1");
            }
        });
        // ----------------------------------------------------------------------------------------

        //AÑADIDO: onClick - BUTTON FACEBOOK
        // ----------------------------------------------------------------------------------------
        mButtonLoginFacebook = (Button) findViewById(R.id.account_button_facebook);
        mButtonLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButtonFacebook.performClick();
            }
        });
        // ----------------------------------------------------------------------------------------

        callbackManager = CallbackManager.Factory.create();
        loginButtonFacebook = (LoginButton) findViewById(R.id.account_loginButton_facebook);
        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        //TODO: Obtain data of User
                        obtainUserOfFacebook(object, response);

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

        // BUTTON - LOGIN/SIGNUP EMAIL
        // ----------------------------------------------------------------------------------------
        buttonLoginEmail = (Button) findViewById(R.id.account_button_loginEmail);
        buttonLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
            }
        });

        buttonSignupEmail = (Button) findViewById(R.id.account_button_signupEmail);
        buttonSignupEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this, SignupActivity.class));
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    // *****************
    // AÑADIDO : GOOGLE
    // *****************
    // ********************************************************************************************
    // [START signIn]
    private void signIn() {

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

        //mButtonLoginGoogle = (Button) findViewById(R.id.account_button_google);
        //signInButtonLoginGoogle.setSize(SignInButton.SIZE_STANDARD);
        //signInButtonLoginGoogle.setScopes(gso.getScopeArray());
        // [END customize_button]
        //-----------------------------------------------------------------------------------------

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

    // LOGIN GOOGLE
    public void loginGoogle(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "ENTRO A Account:loginGoogle:0");

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {

            Log.i(TAG, "ENTRO A Account:loginGoogle:1");
            GoogleSignInAccount acct = result.getSignInAccount();

            //TODO: Obtain data of User
            obtainUserOfGoogle(acct);
        }
    }
    // ********************************************************************************************

    // *******************
    // AÑADIDO : FACEBOOK
    // *******************
    // ********************************************************************************************
    //
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

    //
    public void loginFacebook(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "ENTRO A Account:loginFacebook:0");

        callbackManager.onActivityResult(requestCode, resultCode, data);

        //LogOut Facebook
        LoginManager.getInstance().logOut();
    }
    // ********************************************************************************************

    // *************
    // OBTAIN USERS
    // *************
    // ********************************************************************************************
    //
    public void obtainUserOfGoogle(final GoogleSignInAccount acct) {

        user = new User();

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
        if ( personProfile.hasNickname() ) {
            user.setUsername(personProfile.getNickname());
        } else {
            user.setUsername( acct.getDisplayName() );
        }
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
        // ------------------------------------------------------------------------------------

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

        //TODO: Login or Signup with Facebook
        connectAndDo("Google", user);
    }

    //
    // --------------------------------------------------------------------------------------------
    public void obtainUserOfFacebook(final JSONObject object, final GraphResponse response) {

        user = new User();

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

        //TODO: Login or Signup with Facebook
        connectAndDo("Facebook", user);
    }
    // ********************************************************************************************

    // *************
    // LOGIN USER
    // *************
    // ********************************************************************************************
    //
    public void connectAndDo(final String hacer, final User user) {

        if ( MyNetwork.isNetworkConnected(activity) ) {

            myNetwork = new MyNetwork(TAG, activity);
            myNetwork.showProgressDialog();
            myNetwork.Connect();

            // Wait 1 second to Connect
            // ------------------------------------------------------------------------------------
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (myNetwork.isConnected()) {
                        Log.i(TAG, "ENTRO A Account:connectAndDo: SUCCESSFULLY CONNECT");

                        //TODO: Logear al usuario (si no se puede registra al usuario)
                        loginWithService(hacer, user);

                    } else {
                        myNetwork.hideProgressDialog();
                        Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "ENTRO A Account:connectAndDo:Connect: COULD NOT CONNECT");
                    }

                }
            }, 1000);
            // ------------------------------------------------------------------------------------

        } else {
            Toast.makeText(activity, getString(R.string.error_not_network), Toast.LENGTH_SHORT).show();
            Log.i(TAG, "ENTRO A Account:connectAndDo:Connect: ERROR_NETWORK");
        }
    }
    // --------------------------------------------------------------------------------------------

    //
    public void loginWithService(final String hacer, final User user) {

        // Inicializamos variable error a true
        MyError.setLoginResponse(false);

        String id;
        if ( hacer.equals("Google") ) {
            id = user.getGoogle_id();
        } else if ( hacer.equals("Facebook") ) {
            id = user.getFacebook_id();
        } else {
            throw new IllegalArgumentException("Account:loginWithService: Error, login service not known");
        }

        myNetwork.loginUserWithService(user.getEmail(), id, new ResultListener() {

            @Override
            public void onSuccess(String result) {
                MyError.setLoginResponse(true);
                Log.i(TAG, "ENTRO A Account:loginWithService: SUCCESSFULLY LOGIN: " + result);

                //TODO: Obtener los datos del usuario
                // --------------------------------------------------------------------------------
                String[] pieces = result.split("\"");
                String id = pieces[3];
                Log.i(TAG, "ENTRO A Account:loginWithService: ID: " + id);

                getUserWithID(id);
                // --------------------------------------------------------------------------------
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setLoginResponse(true);

                if ((error.equals("403") && (reason.equals("User not found")))) {
                    //Toast.makeText(activity, getString(R.string.error_user_not_exists), Toast.LENGTH_LONG).show();
                    Log.i(TAG, "ENTRO A Account:loginWithService: USER_NOT_EXIST");
                    Log.i(TAG, "ENTRO A Account:loginWithService: COULD NOT LOGIN: " + error + " / " + reason + " / " + details);

                    //TODO: Registrar al usuario
                    signupUser(hacer, user);

                } else {
                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Account:loginWithService: DISCONNECT");

                    myNetwork.hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_LONG).show();
                    Log.i(TAG, "ENTRO A Account:loginWithService: COULD NOT LOGIN: " + error + " / " + reason + " / " + details);
                }
            }

        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getLoginResponse()) {
                    Log.i(TAG, "ENTRO A Account:loginWithService:getLoginResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Account:loginWithService:getLoginResponse: DISCONNECT");

                    myNetwork.hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_could_not_logged_to_server), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "ENTRO A Account:loginWithService:getLoginResponse: COULD NOT LOGIN");
                }

            }
        }, 5000);
        // ----------------------------------------------------------------------------------------
    }

    //
    private void getUserWithID(final String id) {

        // Inicializamos variable error a true
        MyError.setSubscribeResponse(false);

        String subscriptionId = myNetwork.subscribe("userData", null, new SubscribeListener() {

            @Override
            public void onSuccess() {
                MyError.setSubscribeResponse(true);
                Log.i(TAG, "ENTRO A Account:getUserWithID: SUCCESSFULLY SUBSCRIBE");

                //TODO: Obtener al usuario de la DB del servidor
                // --------------------------------------------------------------------------------
                final User user = myNetwork.getUserWithId(id);
                // --------------------------------------------------------------------------------

                // Wait 1 second
                // ----------------------------------------------------------------------------------------
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // TODO: Registrar al usuario en la DB local
                        // --------------------------------------------------------------------------------
                        MyDatabase.insertUser(TAG, activity, user);
                        MyState.setUser(user);
                        MyState.setLoged(true);
                        // --------------------------------------------------------------------------------

                        myNetwork.Disconnect();
                        Log.i(TAG, "ENTRO A Account:getUserWithID: DISCONNECT");

                        myNetwork.hideProgressDialog();

                        AccountActivity.activity.finish();

                        if (MainActivity.activity != null) {
                            MainActivity.myMenu.loadHeaderLogin();
                        }
                        if (EventsActivity.activity != null) {
                            EventsActivity.myMenu.loadHeaderLogin();
                        }

                        finish();

                    }
                }, 1000);
                // ----------------------------------------------------------------------------------------
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setSubscribeResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Account:getUserWithID: DISCONNECT");

                myNetwork.hideProgressDialog();
                Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "ENTRO A Account:getUserWithID: COULD NOT SUBSCRIBE");
            }
        });

        // Wait 6 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getSubscribeResponse()) {
                    Log.i(TAG, "ENTRO A Account:getUserWithID:getSubscribeResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Account:getUserWithID:getSubscribeResponse: DISCONNECT");

                    myNetwork.hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "ENTRO A Account:getUserWithID:getSubscribeResponse: COULD NOT SUBSCRIBE");
                }

            }
        }, 6000);
        // ----------------------------------------------------------------------------------------
    }
    // ********************************************************************************************

    // *************
    // SIGN-UP USER
    // *************
    // ********************************************************************************************
    //
    private void signupUser(final String hacer, final User user) {

        // Inicializamos variable error a true
        MyError.setSignupResponse(false);

        //SecureRandom random = new SecureRandom();
        //final String password = new BigInteger(130, random).toString(32);

        String id;
        if ( hacer.equals("Google") ) {
            id = user.getGoogle_id();
        } else if ( hacer.equals("Facebook") ) {
            id = user.getFacebook_id();
        } else {
            throw new IllegalArgumentException("Account:signupUser: Error, login service not known");
        }

        myNetwork.signupUser(user.getUsername(), user.getEmail(), id, new ResultListener() {

            @Override
            public void onSuccess(String result) {
                MyError.setSignupResponse(true);
                Log.i(TAG, "ENTRO A Account:signupUser: SUCCESSFULLY SIGN UP: " + result);

                //TODO: Actualizamos el id del usuario
                // --------------------------------------------------------------------------------
                String[] pieces = result.split("\"");
                String id = pieces[3];
                Log.i(TAG, "ENTRO A Account:signupUser: ID: " + id);

                user.setID(id);
                //mUser.setPassword(password);
                // --------------------------------------------------------------------------------

                //TODO: Introducimos al usuario en la DB local y en el Sistema
                MyDatabase.insertUser(TAG, activity, user);
                MyState.setUser(user);
                MyState.setLoged(true);

                //TODO: Actualizar el usuario en la DB servidor
                updateUserOnNetwork(hacer, user);
            }

            @Override
            public void onError(String error, String reason, String details) {
                MyError.setSignupResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Account:signupUser: DISCONNECT");

                myNetwork.hideProgressDialog();
                if ((error.equals("403") && (reason.equals("Username already exists.")))) {
                    Toast.makeText(activity, getString(R.string.error_username_already_exists), Toast.LENGTH_LONG).show();
                } else if ((error.equals("403") && (reason.equals("Email already exists.")))) {
                    Toast.makeText(activity, getString(R.string.error_email_already_exists), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, getString(R.string.error_could_not_sign_up_the_user), Toast.LENGTH_LONG).show();
                }
                Log.i(TAG, "ENTRO A Account:signupUser: COULD NOT SIGN UP: " + error + " / " + reason + " / " + details);
            }

        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getSignupResponse()) {
                    Log.i(TAG, "ENTRO A Account:signupUser:getLoginResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Account:signupUser:getSignupResponse: DISCONNECT");

                    myNetwork.hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_could_not_sign_up_the_user), Toast.LENGTH_LONG).show();
                    Log.i(TAG, "ENTRO A Account:signupUser:getSignupResponse: COULD NOT SIGN UP");
                }

            }
        }, 5000);
        // ----------------------------------------------------------------------------------------
    }

    //
    public void updateUserOnNetwork(final String hacer, final User user) {

        // Inicializamos variable error a true
        MyError.setUpdateUserResponse(false);

        myNetwork.updateUser(user, new ResultListener() {

            @Override
            public void onSuccess(String result) {
                MyError.setUpdateUserResponse(true);
                Log.i(TAG, "ENTRO A Account:updateUserOnNetwork: SUCCESSFULLY UPDATE USER");

                //TODO: Actualizamos el usuario en la DB local y en el Sistema
                MyDatabase.insertUser(TAG, activity, user);
                MyState.setUser(user);
                //MyState.setLoged(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Account:updateUserOnNetwork: DISCONNECT");

                myNetwork.hideProgressDialog();

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
                MyError.setUpdateUserResponse(true);

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A Account:updateUserOnNetwork: DISCONNECT");

                myNetwork.hideProgressDialog();
                if ((error.equals("409")) && (reason.contains("username"))) {
                    Toast.makeText(activity, getString(R.string.error_username_already_exists), Toast.LENGTH_LONG).show();
                } else if ((error.equals("409")) && (reason.contains("email"))) {
                    Toast.makeText(activity, getString(R.string.error_email_already_exists), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, getString(R.string.error_could_not_update_user), Toast.LENGTH_LONG).show();
                }
                Log.i(TAG, "ENTRO A Account:updateUserOnNetwork: COULD NOT UPDATE: " + error + " / " + reason + " / " + details);
            }
        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getUpdateUserResponse()) {
                    Log.i(TAG, "ENTRO A Account:updateUserOnNetwork:getUpdateUserResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Account:updateUserOnNetwork:getUpdateUserResponse: DISCONNECT");

                    myNetwork.hideProgressDialog();
                    Toast.makeText(activity, getString(R.string.error_could_not_update_user), Toast.LENGTH_LONG).show();
                    //Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "ENTRO A Account:updateUserOnNetwork:getUpdateUserResponse: COULD NOT SUBSCRIBE");
                }
            }
        }, 5000);
        // ----------------------------------------------------------------------------------------
    }
    // ********************************************************************************************

    // *********************
    // AÑADIDO: BOTON ATRAS
    // *********************
    // ----------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        finish();
    }
    // ----------------------------------------------------------------------------------------
}
