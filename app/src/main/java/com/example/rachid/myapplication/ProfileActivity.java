package com.example.rachid.myapplication;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
// ----------------------------------------------------------------------------------------

// AÑADIDOS: GOOGLE
// ----------------------------------------------------------------------------------------
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
// ----------------------------------------------------------------------------------------

/**
 * Created by Rachid on 25/03/2016.
 */
public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private final Activity activity = this;

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

        //AÑADIDO: BASE DE DATOS
        // ----------------------------------------------------------------------------------------
        //Abrimos la base de datos
        DBActivity mDB_Activity = new DBActivity(this, null);

        SQLiteDatabase db = mDB_Activity.getReadableDatabase();
        if (db != null) {

            Cursor c = db.rawQuery("SELECT * FROM Users", null);
            if (c.moveToFirst()) {
                circleImageProfile = (CircleImageView) findViewById(R.id.circle_image_profile);
                Picasso.with(getApplicationContext()).load(c.getString(6)).into(circleImageProfile);

                textEditName = (TextView) findViewById(R.id.text_edit_name);
                textEditName.setText(c.getString(3));

                textEditGender = (TextView) findViewById(R.id.text_edit_gender);
                textEditGender.setText(c.getString(4));

                textEditBirthday = (TextView) findViewById(R.id.text_edit_birthday);
                textEditBirthday.setText(c.getString(5));

                textEditEmail = (TextView) findViewById(R.id.text_edit_email);
                textEditEmail.setText(c.getString(1));
            }
            c.close();
            db.close();
        }
        // ----------------------------------------------------------------------------------------
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

            MyDatabase.deleteUser(TAG, activity, state.getUser());

            state.setUser(new User(state.getUser().getLocation()));
            state.setLoged(false);


            if (MainActivity.f != null) {
                MainActivity.f.finish();
            }
            if (EventsActivity.f != null) {
                EventsActivity.f.finish();
            }
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //-----------------------------------------------------------------------------------------
}
