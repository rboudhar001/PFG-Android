package com.example.rachid.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Rachid on 06/07/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int year;
    private int month;
    private int day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String birthday = MyState.getUser().getBirthday();
        if ( (birthday != null) && (!TextUtils.isEmpty(birthday)) ) {
            // Use the old birthday as date in the picker
            String[] pieces = birthday.split("/");
            day = Integer.parseInt(pieces[0]);
            month = Integer.parseInt(pieces[1]);
            month = month - 1; // Currioso esto, el mes los da del 0 al 11 en lugar del 1 al 12 ... informaticos >.<
            year = Integer.parseInt(pieces[2]);
        }
        else {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            month = month + 1;  // Currioso esto, el mes los da del 0 al 11 en lugar del 1 al 12 ... informaticos >.<
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        month = month + 1; // Currioso esto, el mes los da del 0 al 11 en lugar del 1 al 12 ... informaticos >.<
        if ( (this.day != day) || (this.month != month) || (this.year != year) ) { // Si no se ha cambiado la fecha, no actualizamos nada

            User user = new User();
            user.setBirthday("" + day + "/" + month + "/" + year);

            // Llamamos al metodo generico para actualizar el BIRTHDAY
            ProfileActivity profile = (ProfileActivity) getActivity();
            profile.connectAndDo("birthday", user);
        }
    }
}
