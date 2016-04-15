package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rachid on 14/04/2016.
 */
public class MyMenu {

    private static String TAG;
    private static Activity activity;
    private static int REQUEST_CHECK_SETTINGS;

    //AÑADIDO: PROFILE
    // -----------------------------------------------------------------------------------------
    private static CircleImageView circleImageProfile;
    private static NavigationView navHeader;
    private static View navViewHeader;
    private static TextView textUserName;
    private static TextView textUserEmail;
    private static TextView textUserLocation;
    // -----------------------------------------------------------------------------------------

    //AÑADIDO: LOCATION
    // -----------------------------------------------------------------------------------------
    private static ImageButton imageButtonDeleteLocation;
    // -----------------------------------------------------------------------------------------

    public static void onCreate(String T, Activity A, int R){

        TAG = T;
        activity = A;
        REQUEST_CHECK_SETTINGS = R;

        // AÑADIDO: VISIBLE OR INVISIBLE - NAV_HEADER_MAIN or NAV_HEADER_LOGIN
        // ----------------------------------------------------------------------------------------
        if (MyState.getLoged()) { // Si el usuario esta con sesion iniciada

            navHeader = (NavigationView) activity.findViewById(com.example.rachid.myapplication.R.id.nav_view);
            navViewHeader = navHeader.inflateHeaderView(com.example.rachid.myapplication.R.layout.nav_header_login);

            circleImageProfile = (CircleImageView) navViewHeader.findViewById(com.example.rachid.myapplication.R.id.circle_image_profile);
            Picasso.with(activity.getApplicationContext()).load(MyState.getUser().getUrlImageProfile()).into(circleImageProfile);

            textUserName = (TextView) navViewHeader.findViewById(com.example.rachid.myapplication.R.id.text_user_name);
            textUserName.setText(MyState.getUser().getName());

            textUserEmail = (TextView) navViewHeader.findViewById(com.example.rachid.myapplication.R.id.text_user_email);
            textUserEmail.setText(MyState.getUser().getEmail());

            textUserLocation = (TextView) navViewHeader.findViewById(com.example.rachid.myapplication.R.id.text_user_location);

            String location = MyState.getUser().getLocation();
            if (location != null) {
                textUserLocation.setText(MyState.getUser().getLocation());
            } else {
                textUserLocation.setText("SIN LOCALIZACIÓN");
            }

            // AÑADIDO: CLICK EVENT - CIRCLE_IMAGE_PROFILE
            // ------------------------------------------------------------------------------------
            circleImageProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.getApplicationContext().startActivity(new Intent(activity, ProfileActivity.class));
                }
            });
            //-------------------------------------------------------------------------------------

            // AÑADIDO: CLICK EVENT - IMAGE BUTTON UPDATE LOCATION
            // ------------------------------------------------------------------------------------
            imageButtonDeleteLocation = (ImageButton) navViewHeader.findViewById(com.example.rachid.myapplication.R.id.imageButton_delete_location);
            imageButtonDeleteLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyLocation.delete(TAG, activity);

                    EventsActivity.f.finish();
                    activity.startActivity(new Intent(activity, MainActivity.class));
                }
            });
            //-------------------------------------------------------------------------------------
        }
        else if (MyState.getExistsLocation()) {

            navHeader = (NavigationView) activity.findViewById(com.example.rachid.myapplication.R.id.nav_view);
            navViewHeader = navHeader.inflateHeaderView(com.example.rachid.myapplication.R.layout.nav_header_main);

            textUserLocation = (TextView) navViewHeader.findViewById(com.example.rachid.myapplication.R.id.text_user_location);
            textUserLocation.setText(MyState.getUser().getLocation());

            // AÑADIDO: CLICK EVENT - IMAGE BUTTON UPDATE LOCATION
            // ------------------------------------------------------------------------------------
            imageButtonDeleteLocation = (ImageButton) navViewHeader.findViewById(com.example.rachid.myapplication.R.id.imageButton_delete_location);
            imageButtonDeleteLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyLocation.delete(TAG, activity);

                    EventsActivity.f.finish();
                    activity.startActivity(new Intent(activity, MainActivity.class));
                }
            });
            //-------------------------------------------------------------------------------------
        }
        // ----------------------------------------------------------------------------------------
    }
}
