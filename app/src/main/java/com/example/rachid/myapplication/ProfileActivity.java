package com.example.rachid.myapplication;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
// ----------------------------------------------------------------------------------------

// AÑADIDOS: GOOGLE
// ----------------------------------------------------------------------------------------
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
// ----------------------------------------------------------------------------------------

/**
 * Created by Rachid on 25/03/2016.
 */
public class ProfileActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    //AÑADIDO: PROFILE
    // -----------------------------------------------------------------------------------------
    private CircleImageView circleImageProfile;
    private TextView textEditName;
    private TextView textEditGender;
    private TextView textEditBirthday;
    private TextView textEditEmail;
    // -----------------------------------------------------------------------------------------

    //AÑADIDO: GOOGLE
    // ----------------------------------------------------------------------------------------
    private static final String TAG = "ProfileActivity";
    private GoogleApiClient mGoogleApiClient;
    // ----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //AÑADIDO: GOOGLE
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
                .enableAutoManage(this, this)
                .addApi(Plus.API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]
        // ----------------------------------------------------------------------------------------

        //AÑADIDO: BASE DE DATOS
        // ----------------------------------------------------------------------------------------
        //Abrimos la base de datos
        DBActivity mDB_Activity = new DBActivity(this, null);

        SQLiteDatabase db = mDB_Activity.getReadableDatabase();
        if (db != null) {

            // AÑADIDO: Comprobar si el usuario esta logeado
            // -----------------------------------------------------------------------------------------
            GoogleSignInAccount acct = null;
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                GoogleSignInResult result = opr.get();
                acct = result.getSignInAccount();
            }
            // -----------------------------------------------------------------------------------------

            Cursor c = db.rawQuery("SELECT * FROM Users WHERE email=\'" + acct.getEmail() + "\'", null);
            if (c.moveToFirst()) {

                circleImageProfile = (CircleImageView) findViewById(R.id.circle_image_profile);
                Picasso.with(getApplicationContext()).load(c.getString(6)).into(circleImageProfile);

                textEditName = (TextView) findViewById(R.id.text_edit_name);
                textEditName.setText(c.getString(2));

                textEditGender = (TextView) findViewById(R.id.text_edit_gender);
                textEditGender.setText(c.getString(3));

                textEditBirthday = (TextView) findViewById(R.id.text_edit_birthday);
                textEditBirthday.setText(c.getString(4));

                textEditEmail = (TextView) findViewById(R.id.text_edit_email);
                textEditEmail.setText(c.getString(0));
            }
            c.close();
            db.close();
        }
        // ----------------------------------------------------------------------------------------
    }

    //AÑADIDO: GOOGLE
    // ----------------------------------------------------------------------------------------
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
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

            if (mGoogleApiClient.isConnected()) {// Si estoy logeado con Google
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                mGoogleApiClient.disconnect();
                                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                            }
                        });
            }
            /*
            else if () { // Si estoy logeado con facebook

            }
            else {

            }
            */

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //-----------------------------------------------------------------------------------------
}
