package com.example.rachid.myapplication;

// AÑADIDOS: ANDROID
// ----------------------------------------------------------------------------------------
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

        circleImageProfile = (CircleImageView) findViewById(R.id.circle_image_profile);
        Picasso.with(getApplicationContext()).load(MyState.getUser().getUrlImageProfile()).into(circleImageProfile);

        textEditName = (TextView) findViewById(R.id.text_edit_name);
        textEditName.setText(MyState.getUser().getName());

        textEditGender = (TextView) findViewById(R.id.text_edit_gender);
        textEditGender.setText(MyState.getUser().getGender());

        textEditBirthday = (TextView) findViewById(R.id.text_edit_birthday);
        textEditBirthday.setText(MyState.getUser().getBirthday());

        textEditEmail = (TextView) findViewById(R.id.text_edit_email);
        textEditEmail.setText(MyState.getUser().getEmail());
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

            showAlertDialogForLogOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialogForLogOut() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Logout");
        dialog.setMessage("Are you sure you want to logout?");
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MyDatabase.deleteUser(TAG, activity, MyState.getUser());

                MyState.setUser(new User(MyState.getUser().getLocation()));
                MyState.setLoged(false);

                if (MainActivity.f != null) {
                    MainActivity.f.finish();
                }
                if (EventsActivity.f != null) {
                    EventsActivity.f.finish();
                }

                dialog.dismiss();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
        dialog.show();
    }
    //-----------------------------------------------------------------------------------------
}
