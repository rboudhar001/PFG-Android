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

import java.io.InputStream;
import java.io.OutputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Profile;
import com.facebook.AccessToken;

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
        NavigationView.OnNavigationItemSelectedListener,
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
    boolean account_exist = false;
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

                String accessToken = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        // Get facebook data from login
                        state.getUser().setID(object.optString("id"));
                        state.getUser().setUrlImageProfile("https://graph.facebook.com/" + state.getUser().getID() + "/picture?width=120&height=120");

                        if ( object.has("first_name") && object.has("last_name") ) {
                            state.getUser().setName(object.optString("first_name") + " " + object.optString("last_name"));
                        }

                        if (object.has("email")) {
                            state.getUser().setEmail(object.optString("email"));
                            Log.i(TAG, "ESTADO Face-Email-1: " + object.optString("email"));
                        }

                        Log.i(TAG, "ESTADO Face-Email-2: " + state.getUser().getEmail());

                        if (object.has("gender")) {
                            state.getUser().setGender(object.optString("gender"));
                        }

                        if (object.has("birthday")) {
                            state.getUser().setBirthday(object.optString("birthday"));
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();

                //AÑADIDO: BASE DE DATOS
                // ----------------------------------------------------------------------------------------
                //Abrimos la base de datos
                DBActivity mDB_Activity = new DBActivity(getApplicationContext(), null);

                SQLiteDatabase db = mDB_Activity.getReadableDatabase();
                if (db != null) {
                    Cursor c = db.rawQuery("SELECT email FROM Users WHERE email=\'" + state.getUser().getEmail() + "\'", null);
                    if (c.moveToFirst()) { //Comprobar si existe la cuenta
                        account_exist = true;
                    }
                    c.close();
                    db.close();
                }

                db = mDB_Activity.getWritableDatabase();
                if (!account_exist) { //Si la cuenta no existe, la creamos
                    if (db != null) {

                        // Get Profile of Facebook
                        //-------------------------------------------------------------------------
                        //Save the Email
                        String email = state.getUser().getEmail();
                        // Save the Name
                        String name = state.getUser().getName();
                        // Save the Gender
                        String gender = state.getUser().getGender();
                        // Save the Birthday
                        String birthday = state.getUser().getBirthday();
                        // Save the circle image
                        String url_image_profile = state.getUser().getUrlImageProfile();

                        Log.i(TAG, "ESTADO Face-Email-3: " + email);

                        state.setState(true);

                        //Insertamos la nueva cuenta
                        db.execSQL("INSERT INTO Users (email, password, name, gender, birthdate, location, image) VALUES (\'"
                                + email + "\', " + null + ", \'"+ name + "\', \'" + gender + "\', \'" + birthday + "\', " + null + ", \'" + url_image_profile + "\')");
                        db.close();
                    }
                }

                // Re-direct to Main_Page
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                // ----------------------------------------------------------------------------------------
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        // ----------------------------------------------------------------------------------------

        //AÑADIDO MENU
        // ----------------------------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //-----------------------------------------------------------------------------------------

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
    }

    //AÑADIDO GOOGLE
    // ----------------------------------------------------------------------------------------
    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "ENTRO A 0.1");

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {

            Log.i(TAG, "ENTRO A 0.2");

            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();

            if (result.isSuccess()) {
                Log.i(TAG, "ENTRO A 0.3");
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        } else {

            Log.i(TAG, "ENTRO A 0.4");

            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();

                    if (googleSignInResult.isSuccess()) {

                        Log.i(TAG, "ENTRO A 0.5");

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                }
            });
        }
    }

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

        Log.i(TAG, "ENTRO A 3.1");

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE);
    }
    // [END signIn]

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
    // ----------------------------------------------------------------------------------------

    //AÑADIDO FACEBOOK
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

    /*
    @Override
    public void onConnected() {

        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            String personName = currentPerson.getDisplayName();
            String personPhoto = currentPerson.getImage();
            String personGooglePlusProfile = currentPerson.getUrl();
        }

    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "ENTRO A 1");

        // GOOGLE
        if (requestCode == RC_GOOGLE) {

            Log.i(TAG, "ENTRO A 1.1");

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {

                GoogleSignInAccount acct = result.getSignInAccount();

                //AÑADIDO: BASE DE DATOS
                // ----------------------------------------------------------------------------------------
                //Abrimos la base de datos
                DBActivity mDB_Activity = new DBActivity(this, null);

                Log.i(TAG, "ENTRO A 1.2");

                SQLiteDatabase db = mDB_Activity.getReadableDatabase();
                if (db != null) {
                    Log.i(TAG, "ENTRO A 1.3");
                    Cursor c = db.rawQuery("SELECT email FROM Users WHERE email=\'" + acct.getEmail() + "\'", null);
                    if (c.moveToFirst()) { //Comprobar si existe la cuenta
                        Log.i(TAG, "ENTRO A 1.4");
                        account_exist = true;
                    }
                    c.close();
                    db.close();
                }

                db = mDB_Activity.getWritableDatabase();
                if (!account_exist) { //Si la cuenta no existe, la creamos
                    Log.i(TAG, "ENTRO A 1.5");
                    if (db != null) {

                        // Get Profile of Google
                        //-------------------------------------------------------------------------
                        Person personProfile = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

                        //Save the Email
                        String email = acct.getEmail();
                        // Save the Name
                        String name = acct.getDisplayName();
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
                        // Save the birthday
                        String birthday = "";
                        if (personProfile.hasBirthday()) { // it's not guaranteed
                            birthday = personProfile.getBirthday();
                        }
                        // Save the circle image
                        String url_image_profile = "";
                        if (personProfile.hasImage()) {
                            url_image_profile = personProfile.getImage().getUrl();
                        }

                        Log.i(TAG, "ENTRO A 1.6");

                        User user = new User(null, email, name, gender, birthday, url_image_profile);
                        state.setUser(user);

                        Log.i(TAG, "ESTADO Login: " + state.getUser().getEmail());

                        //Insertamos la nueva cuenta
                        db.execSQL("INSERT INTO Users (email, password, name, gender, birthdate, location, image) VALUES (\'"
                                + acct.getEmail() + "\', " + null + ", \'"+ acct.getDisplayName() + "\', \'" + gender + "\', \'" + birthday + "\', " + null + ", \'" + url_image_profile + "\')");
                        db.close();
                    }
                }

                state.setState(true);
                // ----------------------------------------------------------------------------------------

                // Close Google login
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

        // FACEBOOK
        if (requestCode == RC_FACEBOOK) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    // ----------------------------------------------------------------------------------------

    //AÑADIDO MENU
    // ----------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.main_page) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else if (id == R.id.sign_up) {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        } else if (id == R.id.publish) {
            startActivity(new Intent(LoginActivity.this, PublishActivity.class));
        } else if (id == R.id.search) {
            startActivity(new Intent(LoginActivity.this, SearchActivity.class));
        } else if (id == R.id.info) {
            startActivity(new Intent(LoginActivity.this, InfoActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //-----------------------------------------------------------------------------------------

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

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

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


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
}
