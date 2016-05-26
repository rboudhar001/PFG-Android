package com.example.rachid.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Rachid on 23/05/2016.
 */
public class ShowEventActivity extends AppCompatActivity {

    private static final String TAG = "ShowEventActivity";
    public static Activity activity;

    private Event sEvent;

    // EVENTO
    // --------------------------------------------------------------------------------------------
    private ImageView mImageView;
    private TextView mNameView;
    private TextView mDescriptionView;
    private TextView mPlaceView;
    private TextView mFirstDayView;
    private TextView mLastDayView;
    private TextView mSalesView;
    private TextView mContactNumberView;
    private TextView mWebPageView;
    private TextView mCreatorView;
    private TextView mAssistantsView;
    private TextView mCapacityView;

    private TextView mUserNotLoggedView;
    private Button mButtonRegister;
    // --------------------------------------------------------------------------------------------

    private MyNetwork myNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        activity = this;
        sEvent = (Event) getIntent().getExtras().getSerializable("event");

        // AÑADIDO: EVENT
        // ----------------------------------------------------------------------------------------
        mImageView = (ImageView) findViewById(R.id.showEvent_image_event);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = 600; //metrics.heightPixels / 4;
        int widthPixels = metrics.widthPixels - 8;
        Picasso.with(activity).load(sEvent.getPhoto()).resize(widthPixels, heightPixels).into(mImageView);

        mNameView = (TextView) findViewById(R.id.showEvent_textEdit_name);
        mNameView.setText(sEvent.getName());

        mDescriptionView = (TextView) findViewById(R.id.showEvent_textEdit_description);
        mDescriptionView.setText(sEvent.getDescription());

        mPlaceView = (TextView) findViewById(R.id.showEvent_textEdit_place);
        mPlaceView.setText(sEvent.getPlace());

        mFirstDayView = (TextView) findViewById(R.id.showEvent_textEdit_firstDate);
        mFirstDayView.setText(sEvent.getFirstDay());

        mLastDayView = (TextView) findViewById(R.id.showEvent_textEdit_lastDate);
        mLastDayView.setText(sEvent.getLastDay());

        mSalesView = (TextView) findViewById(R.id.showEvent_textEdit_sales);
        mSalesView.setText(sEvent.getSales());

        mContactNumberView = (TextView) findViewById(R.id.showEvent_textEdit_contact_number);
        mContactNumberView.setText("" + sEvent.getContact_number());

        mWebPageView = (TextView) findViewById(R.id.showEvent_textEdit_webpage);
        mWebPageView.setText(sEvent.getWebpage());

        mCreatorView = (TextView) findViewById(R.id.showEvent_textEdit_creator);
        mCreatorView.setText(sEvent.getCreator());

        mAssistantsView = (TextView) findViewById(R.id.showEvent_textEdit_assistants);
        mAssistantsView.setText("" + sEvent.getAssistants());

        mCapacityView = (TextView) findViewById(R.id.showEvent_textEdit_capacity);
        mCapacityView.setText("" + sEvent.getCapacity());
        // ----------------------------------------------------------------------------------------

        // AÑADIDO: BUTTON LISTENER
        // ----------------------------------------------------------------------------------------
        mUserNotLoggedView = (TextView) findViewById(R.id.showEvent_text_user_not_logged);
        mButtonRegister = (Button) findViewById(R.id.showEvent_button_register);

        if (MyState.getLoged()) { // Si el usuario esta logeado

            myNetwork = new MyNetwork(TAG, activity);

            /*
            if ( !myNetwork.thisUserRegisteredInThisEvent() ) { //Si el usuario NO esta registrado en este evento

                mButtonRegister.setBackgroundColor( getResources().getColor(R.color.VERDE) );
                mButtonRegister.setText(getString(R.string.showEvent_text_register));

            } else {

                mButtonRegister.setBackgroundColor( getResources().getColor(R.color.ROJO) );
                mButtonRegister.setText(getString(R.string.showEvent_text_unregister));

            }
            */

            mUserNotLoggedView.setVisibility(View.INVISIBLE);
            mButtonRegister.setActivated(true);
            mButtonRegister.setEnabled(true);

        } else {
            mUserNotLoggedView.setVisibility(View.VISIBLE);
            mButtonRegister.setActivated(false);
            mButtonRegister.setEnabled(false);
        }

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement your edit user name
                registerOrUnregisterOnEvent();
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    // AÑADIDO: EVENT
    // --------------------------------------------------------------------------------------------
    public void registerOrUnregisterOnEvent() {

        if (MyState.getLoged()) {

            /*
            if ( !myNetwork.thisUserRegisteredInThisEvent() ) { //Si el usuario NO esta registrado en este evento

                // TODO: Registrar a este usuario en este evento
                myNetwork.
                mButtonRegister.setBackgroundColor( getResources().getColor(R.color.VERDE) );
                mButtonRegister.setText(getString(R.string.showEvent_text_register));

            } else {

                // TODO: Des-Registrar a este usuario en este evento
                mButtonRegister.setBackgroundColor( getResources().getColor(R.color.ROJO) );
                mButtonRegister.setText(getString(R.string.showEvent_text_unregister));

            }
            */

        }
    }
    // --------------------------------------------------------------------------------------------

    //AÑADIDO: BOTON ATRAS
    // --------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        this.finish();
    }
    //---------------------------------------------------------------------------------------------
}
