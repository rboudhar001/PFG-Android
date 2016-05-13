package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rachid on 14/04/2016.
 */
public class MyMenu {

    // ***********
    // VARIABLES
    // ***********
    private Activity activity;

    //AÑADIDO: PROFILE
    // -----------------------------------------------------------------------------------------
    private CircleImageView circleImageProfile;
    private NavigationView navHeader;
    private View navViewHeader;
    private TextView textUserName;
    private TextView textUserEmail;
    private TextView textUserLocation;
    // -----------------------------------------------------------------------------------------

    //AÑADIDO: LOCATION
    // -----------------------------------------------------------------------------------------
    private ImageButton imageButtonUpdateLocation;
    // -----------------------------------------------------------------------------------------

    // ***********
    // CONSTRUCTOR
    // ***********
    public MyMenu(Activity A) {
        this.activity = A;
    }


    // ***********
    // FUNCIONES
    // ***********

    // IMAGE
    public void updateImage() {
        Picasso.with(activity.getApplicationContext()).load(MyState.getUser().getImage()).into(circleImageProfile);
    }

    // NAME
    public void updateUserName() {
        textUserName.setText(MyState.getUser().getUsername());
    }

    // EMAIL
    public void updateEmail() {
        textUserEmail.setText(MyState.getUser().getEmail());
    }

    // UPDATE
    public void updateLocation() {

        if (MyState.getUser().getLocation() != null) {
            textUserLocation.setText(MyState.getUser().getLocation());
        }
        else {
            textUserLocation.setText(activity.getString(R.string.text_not_exists_location));
        }
    }

    //
    public void loadHeaderLogin() {

        navHeader = (NavigationView) activity.findViewById(R.id.nav_view);
        navHeader.removeHeaderView(navViewHeader);
        navViewHeader = navHeader.inflateHeaderView(R.layout.nav_header_login);

        circleImageProfile = (CircleImageView) navViewHeader.findViewById(R.id.circle_image_profile);
        if (MyState.getUser().getImage() != null) {
            Picasso.with(activity.getApplicationContext()).load(MyState.getUser().getImage()).into(circleImageProfile);
        }

        textUserName = (TextView) navViewHeader.findViewById(R.id.text_user_name);
        textUserName.setText(MyState.getUser().getUsername());

        textUserEmail = (TextView) navViewHeader.findViewById(R.id.text_user_email);
        textUserEmail.setText(MyState.getUser().getEmail());

        textUserLocation = (TextView) navViewHeader.findViewById(R.id.text_user_location);

        Log.i("EventsActivity", "ENTRO A MyMenu:getLocation(): " + MyState.getUser().getLocation());

        if (MyState.getUser().getLocation() != null) {
            textUserLocation.setText(MyState.getUser().getLocation());
        }
        else {
            textUserLocation.setText(activity.getString(R.string.text_not_exists_location));
        }

        // AÑADIDO: CLICK EVENT - CIRCLE_IMAGE_PROFILE
        // ------------------------------------------------------------------------------------
        circleImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, ProfileActivity.class));
            }
        });
        //-------------------------------------------------------------------------------------

        //AÑADIDO: BUTTON_UPDATE_LOCATION
        // ----------------------------------------------------------------------------------------
        imageButtonUpdateLocation = (ImageButton) activity.findViewById(R.id.nav_imageButton_update_location);
        imageButtonUpdateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, LocationActivity.class));
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    //
    public void loadHeaderLocation() {

        navHeader = (NavigationView) activity.findViewById(R.id.nav_view);
        navHeader.removeHeaderView(navViewHeader);
        navViewHeader = navHeader.inflateHeaderView(R.layout.nav_header_location);

        textUserLocation = (TextView) navViewHeader.findViewById(R.id.text_user_location);
        textUserLocation.setText(MyState.getUser().getLocation());

        // AÑADIDO: CLICK EVENT - IMAGE BUTTON UPDATE LOCATION
        // ------------------------------------------------------------------------------------
        imageButtonUpdateLocation = (ImageButton) navViewHeader.findViewById(R.id.nav_imageButton_update_location);
        imageButtonUpdateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, LocationActivity.class));
            }
        });
        //-------------------------------------------------------------------------------------
    }
}
