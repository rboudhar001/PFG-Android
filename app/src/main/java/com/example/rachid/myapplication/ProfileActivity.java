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
public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    //AÑADIDO: STATE
    // -----------------------------------------------------------------------------------------
    State state = new State();
    // -----------------------------------------------------------------------------------------

    //AÑADIDO: PROFILE
    // -----------------------------------------------------------------------------------------
    private CircleImageView circleImageProfile;
    private TextView textEditName;
    private TextView textEditGender;
    private TextView textEditBirthday;
    private TextView textEditEmail;
    // -----------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*
        //AÑADIDO: BASE DE DATOS
        // ----------------------------------------------------------------------------------------
        //Abrimos la base de datos
        DBActivity mDB_Activity = new DBActivity(this, null);

        SQLiteDatabase db = mDB_Activity.getReadableDatabase();
        if (db != null) {

            Cursor c = db.rawQuery("SELECT * FROM Users WHERE email=\'" + state.getUser().getEmail() + "\'", null);
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
        */
    }

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

            state.setUser(null);
            state.setState(false);

            /*
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
            */

            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //-----------------------------------------------------------------------------------------
}
