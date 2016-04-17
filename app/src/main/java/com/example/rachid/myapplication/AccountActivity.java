package com.example.rachid.myapplication;

import static android.Manifest.permission.READ_CONTACTS;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

// ----------------------------------------------------------------------------------------

// AÑADIDOS JAVA
// ----------------------------------------------------------------------------------------
import java.util.ArrayList;
import java.util.List;
// ----------------------------------------------------------------------------------------

// AÑADIDOS FACEBOOK
// ----------------------------------------------------------------------------------------
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
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
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private final Activity activity = this;

    //AÑADIDO: USER
    // -----------------------------------------------------------------------------------------
    User user = new User();
    // -----------------------------------------------------------------------------------------

    //AÑADIDO: GOOGLE
    // ----------------------------------------------------------------------------------------
    private static final int RC_GOOGLE = 9001;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
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

        //AÑADIDO FACEBOOK
        // ----------------------------------------------------------------------------------------
        FacebookSdk.sdkInitialize(getApplicationContext(),9002);
        // ----------------------------------------------------------------------------------------

        setContentView(R.layout.activity_account);

        //AÑADIDO GOOGLE
        // ----------------------------------------------------------------------------------------
        // Button listeners
        findViewById(R.id.login_google_button).setOnClickListener(this);

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
        SignInButton signInButton = (SignInButton) findViewById(R.id.login_google_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
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
                        user.setID(object.optString("id"));
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
                        user.setUrlImageProfile("https://graph.facebook.com/" + user.getID() + "/picture?width=400&height=400");
                        //user.setUrlImageProfile("https://graph.facebook.com/" + user.getID() + "/picture?width=120&height=120");
                        // Save the Location
                        user.setLocation(MyState.getUser().getLocation());

                        //Insert or Update DataBase
                        MyDatabase.insertUser(TAG, activity, user);

                        MyState.setUser(user);
                        MyState.setLoged(true);

                        if (MainActivity.f != null) {
                            MainActivity.f.finish();
                        }
                        if (EventsActivity.f != null) {
                            EventsActivity.f.finish();
                        }
                        if (MyState.getExistsLocation()) {
                            startActivity(new Intent(AccountActivity.this, EventsActivity.class));
                        } else {
                            startActivity(new Intent(AccountActivity.this, MainActivity.class));
                        }
                        finish();
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

        // BOTONOES - LOGIN/SIGNUP EMAIL
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_google_button:
                signIn();
                break;
        }
    }

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE);
    }
    // [END signIn]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
    // ----------------------------------------------------------------------------------------

    //AÑADIDO : FACEBOOK
    // ----------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();

        // Facebook
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // GOOGLE
        if (requestCode == RC_GOOGLE) {
            loginGoogle(requestCode, resultCode, data);
        }

        // FACEBOOK
        if (requestCode == RC_FACEBOOK) {
            loginFacebook(requestCode, resultCode, data);
        }
    }
    // ----------------------------------------------------------------------------------------

    // LOGIN GOOGLE
    public void loginGoogle(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "ENTRO A G.1");

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();

            // Get Profile of Google
            //-------------------------------------------------------------------------
            Person personProfile = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            // Save the ID
            user.setID(acct.getId());
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
                }
                else if (g == 1) {
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

            //Insert or Update DataBase
            MyDatabase.insertUser(TAG, activity, user);

            MyState.setUser(user);
            MyState.setLoged(true);

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

            // Re-direct to Main_Page
            if (MainActivity.f != null) {
                MainActivity.f.finish();
            }
            if (EventsActivity.f != null) {
                EventsActivity.f.finish();
            }
            if (MyState.getExistsLocation()) {
                startActivity(new Intent(AccountActivity.this, EventsActivity.class));
            } else {
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
            }
            finish();
        }
    }

    // LOGIN FACEBOOK
    public void loginFacebook(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //LogOut Facebook
        LoginManager.getInstance().logOut();
    }
}
