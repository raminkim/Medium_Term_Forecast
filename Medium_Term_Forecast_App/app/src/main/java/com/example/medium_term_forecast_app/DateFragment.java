package com.example.medium_term_forecast_app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int Y = c.get(Calendar.YEAR);
        int M = c.get(Calendar.MONTH);
        int D = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, Y, M, D);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        WeatherFragment weatherFragment = new WeatherFragment();
        weatherFragment.processDatePickerResult(year, month, dayOfMonth);
    }
}