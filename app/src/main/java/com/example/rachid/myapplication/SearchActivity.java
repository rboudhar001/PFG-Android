package com.example.rachid.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Rachid on 25/03/2016.
 */
public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";
    public static Activity activity;

    private AutoCompleteTextView mPlaceView;
    private RelativeLayout relativeDate;
    private TextView textEditDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        activity = this;

        // AÑADIDO : SEARCH
        // ----------------------------------------------------------------------------------------
        mPlaceView = (AutoCompleteTextView) findViewById(R.id.search_place);

        relativeDate = (RelativeLayout) findViewById(R.id.search_relative_date);
        relativeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement your edit date
                DialogFragment newFragment = new DatePickerSearchFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        textEditDate = (TextView) findViewById(R.id.search_text_date);

        /*
        relativeDate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                Log.i(TAG, "ENTRO A Search:onEditorAction:0");

                if (id == R.id.search_button_keyboard_search || id == EditorInfo.IME_NULL) {

                    Log.i(TAG, "ENTRO A Search:onEditorAction:1");

                    attemptSearch();

                    return true;
                }
                return false;
            }
        });
        */

        Button mSearchButton = (Button) findViewById(R.id.search_button_search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "ENTRO A Login:onClick:0");
                attemptSearch();
                Log.i(TAG, "ENTRO A Login:onClick:1");
            }
        });
        // ----------------------------------------------------------------------------------------
    }

    //
    public void editTextDate(String date) {
        textEditDate.setText(date);
    }

    //AÑADIDO : SEARCH
    // ----------------------------------------------------------------------------------------
    /**
     * Attempts to search the events specified by the search form
     * If there are form errors (invalid place, day, etc.), the
     * errors are presented and no actual search attempt is made.
     */
    private void attemptSearch() {

        Log.i(TAG, "ENTRO A Login:attemptLogin:0");

        // Reset errors.
        mPlaceView.setError(null);

        // Store values at the time of the search attempt.
        String place = mPlaceView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid place, if the place entered one.
        if (TextUtils.isEmpty(place)) {
            mPlaceView.setError(getString(R.string.error_field_required));
            focusView = mPlaceView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt search and focus the first form field with an error.

            Log.i(TAG, "ENTRO A Search:attemptSearch:2");

            focusView.requestFocus();
        } else {

            Log.i(TAG, "ENTRO A Search:attemptSearch:3");

            Intent intent = new Intent(SearchActivity.this, SearchResultsActivity.class);
            intent.putExtra("place", place);

            String date = textEditDate.getText().toString();
            if ( !TextUtils.equals(date, getString(R.string.prompt_date)) ) {
                intent.putExtra("date", date);
            } else {
                intent.putExtra("date", (String)null);
            }

            startActivity(intent);
        }
    }
    // ----------------------------------------------------------------------------------------

    //AÑADIDO: BOTON ATRAS
    // ----------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        this.finish();
    }
    //-----------------------------------------------------------------------------------------

    // *******************
    // CLASS: DatePicker
    // *******************
    //--------------------------------------------------------------------------------------------
    public static class DatePickerSearchFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            month = month + 1;  // Currioso esto, el mes los da del 0 al 11 en lugar del 1 al 12 ... informaticos >.<
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            
            // Editamos el cammpo texto de la fecha
            SearchActivity search = (SearchActivity) getActivity();
            String date = "" + day + "/" + month + "/" + year;
            search.editTextDate( date );
        }
    }
    //--------------------------------------------------------------------------------------------
}
