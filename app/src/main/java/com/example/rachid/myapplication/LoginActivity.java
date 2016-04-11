package com.example.rachid.myapplication;

import static android.Manifest.permission.READ_CONTACTS;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------
import android.content.ContentValues;
import android.content.IntentSender.SendIntentException;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.database.sqlite.SQLiteDatabase;
// ----------------------------------------------------------------------------------------

// AÑADIDOS JAVA
// ----------------------------------------------------------------------------------------
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
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
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.Plus;
// ----------------------------------------------------------------------------------------

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements
        LoaderCallbacks<Cursor>,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mScrollLoginFormView;

    private static final String TAG = "LoginActivity";

    //AÑADIDO: STATE
    // -----------------------------------------------------------------------------------------
    State state = new State();
    User user = new User();
    boolean location = false;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //AÑADIDO FACEBOOK
        // ----------------------------------------------------------------------------------------
        FacebookSdk.sdkInitialize(getApplicationContext(),9002);
        // ----------------------------------------------------------------------------------------

        setContentView(R.layout.activity_login);

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
                        user.setLocation(null);

                        //Insert or Update DataBase
                        dataBase(user);

                        state.setUser(user);
                        state.setState(true);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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

        // AÑADIDO : LOGIN EMAIL
        // ----------------------------------------------------------------------------------------
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mLoginEmailButton = (Button) findViewById(R.id.login_email_button);
        mLoginEmailButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mScrollLoginFormView = findViewById(R.id.scroll_login_form);
        mProgressView = findViewById(R.id.login_progress);
        // ----------------------------------------------------------------------------------------
    }

    // AÑADIDO : DATA BASE
    // ----------------------------------------------------------------------------------------
    public void dataBase(User user) {
        //Abrimos la base de datos
        DBActivity mDB_Activity = new DBActivity(getApplicationContext(), null);

        SQLiteDatabase db = mDB_Activity.getReadableDatabase();
        if (db != null) {
            Cursor c = db.rawQuery("SELECT * FROM Users", null);
            if (c.moveToFirst()) { // Comprobar que existe la localizacion del usuario
                location = true;
                user.setLocation(c.getString(7));
            }
            c.close();
            db.close();
        }

        db = mDB_Activity.getWritableDatabase();
        if (location) { //Existe la localizacion
            if (db != null) {
                //Actualizamos la cuenta
                db.execSQL("UPDATE Users SET id=\'" + user.getID() + "\', email=\'" + user.getEmail() + "\', password=\'"
                        + user.getPassword() + "\', name=\'" + user.getName() + "\', gender=\'" + user.getGender() + "\', birthday=\'"
                        + user.getBirthday() + "\', image=\'" + user.getUrlImageProfile() + "\' WHERE location=\'" + user.getLocation() + "\'");
                db.close();
            }
        }
        else { // No existe la localizacion
            if (db != null) {
                //Insertamos la nueva cuenta
                db.execSQL("INSERT INTO Users (id, email, password, name, gender, birthday, image, location) VALUES (\'"
                        + user.getID() + "\', \'" +  user.getEmail() + "\', \'" + user.getPassword()  + "\', \'" + user.getName()
                        + "\', \'" + user.getGender() + "\', \'" + user.getBirthday() + "\', \'" + user.getUrlImageProfile()
                        + "\', \'" + user.getLocation() + "\')");
                db.close();
            }
        }
    }
    // ----------------------------------------------------------------------------------------

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

        Log.i(TAG, "ENTRO A 0");

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
            user.setLocation(null);
            // ----------------------------------------------------------------------------------------

            //Insert or Update DataBase
            dataBase(user);

            state.setUser(user);
            state.setState(true);

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
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    // LOGIN FACEBOOK
    public void loginFacebook(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //LogOut Facebook
        LoginManager.getInstance().logOut();
    }

    //AÑADIDO : LOGIN EMAIL
    // ----------------------------------------------------------------------------------------
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    //
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    //
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    //
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mScrollLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mScrollLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mScrollLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mScrollLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    //
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    //
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
    // ----------------------------------------------------------------------------------------
}
