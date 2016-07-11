package com.example.rachid.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PublishActivity extends AppCompatActivity {

    private static final String TAG = "PublishActivity";
    public static Activity activity;

    //AÑADIDO: MENU
    // -----------------------------------------------------------------------------------------
    public static MyMenu myMenu;
    // -----------------------------------------------------------------------------------------

    private Button buttonPublish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        activity = this;

        // AÑADIDO : PUBLISH
        // ----------------------------------------------------------------------------------------
        buttonPublish = (Button) findViewById(R.id.publish_button_publish);
        buttonPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Redireccionar a la pagina de publicar evento
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sozialmusfest.scalingo.io/"));
                startActivity(browserIntent);
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    //AÑADIDO: BOTON ATRAS
    // ----------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        this.finish();
    }
    //-----------------------------------------------------------------------------------------
}
