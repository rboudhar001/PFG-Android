package com.example.rachid.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import im.delight.android.ddp.ResultListener;

/**
 * Created by Rachid on 17/04/2016.
 */
public class SignupActivity extends AppCompatActivity { //implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    //private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUserNameView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private static final String TAG = "SignupActivity";
    private final Activity activity = this;

    private static ProgressDialog mProgressDialog;

    private MyNetwork myNetwork;

    //AÑADIDO: BOTONES
    // ----------------------------------------------------------------------------------------
    private Button buttonHaveAccount;
    // ----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // AÑADIDO : LOGIN EMAIL
        // ----------------------------------------------------------------------------------------
        // Set up the login form.
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.signup_user_name);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.signup_email);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.signup_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.signup_button_keyboard_signup || id == EditorInfo.IME_NULL) {

                    Log.i(TAG, "ENTRO A Signup:onEditorAction:0");
                    attemptSignup();
                    Log.i(TAG, "ENTRO A Signup:onEditorAction:1");

                    return true;
                }
                return false;
            }
        });

        Button mLoginEmailButton = (Button) findViewById(R.id.signup_button_signup);
        mLoginEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "ENTRO A Signup:onClick:0");
                attemptSignup();
                Log.i(TAG, "ENTRO A Signup:onClick:1");
            }
        });
        // ----------------------------------------------------------------------------------------

        // AÑADIDO: BOTONOES
        // ----------------------------------------------------------------------------------------
        buttonHaveAccount = (Button) findViewById(R.id.signup_button_have_account);
        buttonHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    //AÑADIDO : LOGIN EMAIL
    // ----------------------------------------------------------------------------------------
    /*
    private void populateAutoComplete() {

        Log.i(TAG, "ENTRO A Signup:populateAutoComplete:0");

        if (!mayRequestContacts()) {

            Log.i(TAG, "ENTRO A Signup:populateAutoComplete:1");
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }
    */

    /*
    //
    private boolean mayRequestContacts() {

        Log.i(TAG, "ENTRO A Signup:mayRequestContacts:0");

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
    */

    /**
     * Callback received when a permissions request has been completed.
     */
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.i(TAG, "ENTRO A Signup:onRequestPermissionsResult:0");

        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }
    */


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignup() {

        Log.i(TAG, "ENTRO A Signup:attemptSignup:0");

        /*
        if (mAuthTask != null) { // Si ya esta logeado
            Log.i(TAG, "ENTRO A Signup:attemptSignup:1");
            return;
        }
        */
        if (MyState.getLoged()) {
            Log.i(TAG, "ENTRO A Login:attemptLogin:1");
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String userName = mUserNameView.getText().toString();
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // CHECK FORM
        if (TextUtils.isEmpty(userName)) { // Check for user name
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) { // Check for a valid email address.
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) { // Check for a valid password
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error.
            Log.i(TAG, "ENTRO A Signup:attemptSignup:2");

            focusView.requestFocus();
        } else {

            Log.i(TAG, "ENTRO A Signup:attemptSignup:3");

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgressDialog();
            //mAuthTask = new UserLoginTask(userName, email, password);
            //mAuthTask.execute((Void) null);

            // SIGN UP USER
            // ------------------------------------------------------------------------------------
            myNetwork = new MyNetwork(TAG, activity);
            myNetwork.Connect();

            // Wait 1 second
            // ----------------------------------------------------------------------------------------
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (myNetwork.isConnected()) {

                        Log.i(TAG, "ENTRO A Signup:attemptSignup:Connect: SUCCESSFULLY CONNECT");

                        //TODO: Registrar al usuario en la DB del servidor
                        signupUser(userName, email, password);

                    } else {
                        Log.i(TAG, "ENTRO A Signup:attemptSignup:Connect: COULD NOT CONNECT");
                        Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }

                }
            }, 1000);
            // ----------------------------------------------------------------------------------------

            /*
            myNetwork.Connect(new ResultListener() {

                @Override
                public void onSuccess(String result) {
                    Log.i(TAG, "ENTRO A MyMeteor:Connect: SUCCESSFULLY CONNECT: " + result);

                    //TODO: Registrar al usuario en la DB del servidor
                    signupUser(userName, email, password);
                }

                @Override
                public void onError(String error, String reason, String details) {
                    Log.i(TAG, "ENTRO A MyMeteor:signupUser: COULD NOT CONNECT: " + error + " / " + reason + " / " + details);
                    Toast.makeText(activity, getString(R.string.error_could_not_connect_to_server), Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }

            });
            */
            // ------------------------------------------------------------------------------------
        }
    }

    //
    private void signupUser(final String userName, final String email, final String password) {

        // Inicializamos variable error a true
        MyError.setSignupResponse(false);

        myNetwork.signupUser(userName, email, password, new ResultListener() {

            @Override
            public void onSuccess(String result) {
                MyError.setSignupResponse(true);

                Log.i(TAG, "ENTRO A Signup:signupUser: SUCCESSFULLY SIGN UP: " + result);

                //TODO: Registrar al usuario en la DB local
                // --------------------------------------------------------------------------------
                String[] pieces = result.split("\"");
                String id = pieces[3];
                Log.i(TAG, "ENTRO A Signup:signupUser:ID: " + id);

                User mUser = new User();
                mUser.setID(id);
                mUser.setUsername(userName);
                mUser.setEmail(email);
                mUser.setPassword(null); //mUser.setPassword(password);

                MyDatabase.insertUser(TAG, activity, mUser);
                MyState.setUser(mUser);
                MyState.setLoged(true);
                // --------------------------------------------------------------------------------

                myNetwork.Disconnect();
                Log.i(TAG, "ENTRO A SignUp:signupUser: DISCONNECT");

                hideProgressDialog();

                AccountActivity.activity.finish();

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
                Log.i(TAG, "ENTRO A Signup:signupUser: DISCONNECT");

                if ( (error.equals("403") && (reason.equals("Username already exists."))) ) {
                    Toast.makeText(activity, getString(R.string.error_username_already_exists), Toast.LENGTH_LONG).show();
                } else if ( (error.equals("403") && (reason.equals("Email already exists."))) ) {
                    Toast.makeText(activity, getString(R.string.error_email_already_exists), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, getString(R.string.error_could_not_sign_up_the_user), Toast.LENGTH_LONG).show();
                }

                Log.i(TAG, "ENTRO A Signup:signupUser: COULD NOT SIGN UP: " + error + " / " + reason + " / " + details);
                hideProgressDialog();
            }

        });

        // Wait 5 seconds, si no responde en este tiempo, cerrar.
        // ----------------------------------------------------------------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!MyError.getSignupResponse()) {
                    Log.i(TAG, "ENTRO A Signup:signupUser:getSignupResponse: TIMES_EXPIRED");

                    myNetwork.Disconnect();
                    Log.i(TAG, "ENTRO A Signup:signupUser:getSignupResponse: DISCONNECT");

                    Log.i(TAG, "ENTRO A Signup:signupUser:getSignupResponse: COULD NOT SIGN UP");
                    Toast.makeText(activity, getString(R.string.error_could_not_sign_up_the_user), Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }

            }
        }, 5000);
        // ----------------------------------------------------------------------------------------
    }

    //
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    //
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return (password.length() > 4);
    }

    /*
    //---------------------------------------------------------------------------------------------
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Log.i(TAG, "ENTRO A Signup:onCreateLoader:0");

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

        Log.i(TAG, "ENTRO A Signup:onLoadFinished:0");

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
        Log.i(TAG, "ENTRO A Signup:onLoaderReset:0");
    }
    //---------------------------------------------------------------------------------------------

    // **********
    // INTERFACE
    // **********
    // -------------------------------------------------------------------------------------------
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
    // -------------------------------------------------------------------------------------------
    */

    /*
    //
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {

        Log.i(TAG, "ENTRO A Signup:addEmailsToAutoComplete:0");

        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignupActivity.this, android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }
    */

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    /*
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserName;
        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String userName, String email, String password) {

            Log.i(TAG, "ENTRO A Signup:UserLoginTask:0");

            mUserName = userName;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            Log.i(TAG, "ENTRO A Signup:UserLoginTask:doInBackground:0");

            User user = MyState.getUser();

            //user.setID(MyState.getUser().getID());
            user.setUsername(mUserName);
            user.setEmail(mEmail);
            user.setPassword(mPassword);

            MyNetwork myNetwork = new MyNetwork(TAG, activity);
            user  = myNetwork.signupUser(user); //Insert MyNetwork
            if (user != null) { // Guarda en la DB del servidor al usuario mandado como parametro y devuelve el id del usuario o null si no se ha registrado

                Log.i(TAG, "ENTRO A Signup:UserLoginTask:doInBackground:mUserName: " + mUserName);
                Log.i(TAG, "ENTRO A Signup:UserLoginTask:doInBackground:mEmail: " + mEmail);
                Log.i(TAG, "ENTRO A Signup:UserLoginTask:doInBackground:mPassword: " + mPassword);

                //user.setID(id);
                MyDatabase.insertUser(TAG, activity, user);

                MyState.setUser(user);
                MyState.setLoged(true);

                return true; // Account signup, return true
            }
            else {
                Toast.makeText(getBaseContext(), "Error, no se ha podido registrar al usuario", Toast.LENGTH_SHORT).show();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            Log.i(TAG, "ENTRO A Signup:UserLoginTask:onPostExecute:0");

            mAuthTask = null;
            hideProgressDialog();

            if (success) {

                Log.i(TAG, "ENTRO A Signup:UserLoginTask:onPostExecute:1");

                AccountActivity.activity.finish();

                if (MainActivity.activity != null) {
                    MainActivity.myMenu.loadHeaderLogin();
                }
                if (EventsActivity.activity != null) {
                    EventsActivity.myMenu.loadHeaderLogin();
                }

                finish();
            }
        }

        @Override
        protected void onCancelled() {

            Log.i(TAG, "ENTRO A Signup:UserLoginTask:onCancelled:0");

            mAuthTask = null;
            hideProgressDialog();
        }
    }
    */

    //AÑADIDO: BOTON ATRAS
    // ----------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        this.finish();
    }
    //-----------------------------------------------------------------------------------------

    // **********
    // FUNTIONS
    // **********
    // ----------------------------------------------------------------------------------------
    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.setCancelable(false);

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    // ----------------------------------------------------------------------------------------
}
