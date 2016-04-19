package com.example.rachid.myapplication;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
// ----------------------------------------------------------------------------------------

// AÑADIDOS FACEBOOK
// ----------------------------------------------------------------------------------------
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
// ----------------------------------------------------------------------------------------

// AÑADIDOS GOOGLE
// ----------------------------------------------------------------------------------------
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.Plus;
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
                        // Save the ID
                        user.setID(null);
                        // Save the Email
                        if (object.has("email")) {
                            user.setEmail(object.optString("email"));
                        }
                        // Save the Password
                        user.setPassword(null);
                        // Save the Name
                        if (object.has("first_name") && object.has("last_name")) {
                            user.setName(object.optString("first_name") + " " + object.optString("last_name"));
                        }
                        // Save the Gender
                        if (object.has("gender")) {
                            user.setGender(object.optString("gender"));
                        }
                        // Save the Birthday
                        if (object.has("birthday")) {
                            user.setBirthday(object.optString("birthday"));
                        }
                        // Save the Url Image Profile
                        user.setUrlImageProfile("https://graph.facebook.com/" + object.optString("id") + "/picture?width=400&height=400");
                        //user.setUrlImageProfile("https://graph.facebook.com/" + user.getID() + "/picture?width=120&height=120");
                        // Save the Location
                        user.setLocation(MyState.getUser().getLocation());

                        String id = MyNetwork.signupUser(user); //Insert MyNetwork
                        if (id != null) {

                            Log.i(TAG, "ENTRO A Account:loginGoogle:2");
                            user.setID(id);

                            //Insert or Update DataBase
                            MyDatabase.insertUser(TAG, activity, user);

                            //Update local data
                            MyState.setUser(user);
                            MyState.setLoged(true);

                            AccountActivity.activity.finish();

                            if (MainActivity.activity != null) {
                                MainActivity.myMenu.loadHeaderLogin();
                            }
                            if (EventsActivity.activity != null) {
                                EventsActivity.myMenu.loadHeaderLogin();
                            }

                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), "Error, no se ha podido registrar al usuario", Toast.LENGTH_SHORT).show();
                        }
                        // ----------------------------------------------------------------------------------------
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday"); // Parámetros que pedimos a facebook
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

    //AÑADIDO : GOOGLE
    // ----------------------------------------------------------------------------------------
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
    // ----------------------------------------------------------------------------------------

    //AÑADIDO : FACEBOOK
    // ----------------------------------------------------------------------------------------
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

            // Save the ID
            user.setID(null);
            // Save the Email
            user.setEmail(acct.getEmail());
            // Save the Name
            user.setPassword(null);
            // Save the Name
            user.setName(acct.getDisplayName());
            // Save the Gender
            String gender = "Otro";
            if (personProfile.hasGender()) { // it's not guaranteed
                int g = personProfile.getGender();
                if (g == 0) {
                    gender = "Hombre";
                } else if (g == 1) {
                    gender = "Mujer";
                }
            }
            user.setGender(gender);
            // Save the birthday
            String birthday = "";
            if (personProfile.hasBirthday()) { // it's not guaranteed
                birthday = personProfile.getBirthday();
            }
            user.setBirthday(birthday);
            // Save the Url Image Profile
            String url_image_profile = "";
            if (personProfile.hasImage()) {
                url_image_profile = personProfile.getImage().getUrl();
            }
            user.setUrlImageProfile(url_image_profile);
            // Save the location
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

            //Insert MyNetwork
            String id = MyNetwork.signupUser(user);
            if (id != null) {

                Log.i(TAG, "ENTRO A Account:loginGoogle:2");
                user.setID(id);

                //Insert or Update DataBase
                MyDatabase.insertUser(TAG, activity, user);

                //Update local data
                MyState.setUser(user);
                MyState.setLoged(true);

                if (MainActivity.activity != null) {
                    MainActivity.myMenu.loadHeaderLogin();
                }
                if (EventsActivity.activity != null) {
                    EventsActivity.myMenu.loadHeaderLogin();
                }

                finish();
            } else {
                Toast.makeText(getBaseContext(), "Error, no se ha podido registrar al usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // LOGIN FACEBOOK
    public void loginFacebook(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "ENTRO A Account:loginFacebook:0");

        callbackManager.onActivityResult(requestCode, resultCode, data);

        //LogOut Facebook
        LoginManager.getInstance().logOut();
    }
}
