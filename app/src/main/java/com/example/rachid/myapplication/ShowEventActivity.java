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

    private Button mButtonRegister;
    // --------------------------------------------------------------------------------------------

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
        mButtonRegister = (Button) findViewById(R.id.showEvent_button_register);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement your edit user name
                registerOnEvent();
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    // AÑADIDO: EVENT
    // --------------------------------------------------------------------------------------------
    public void registerOnEvent() {

        // TODO: Registrar a este usuario en este evento
        // ...

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
