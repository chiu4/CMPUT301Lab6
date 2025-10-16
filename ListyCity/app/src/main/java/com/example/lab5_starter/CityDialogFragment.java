package com.example.lab5_starter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class CityDialogFragment extends DialogFragment {
    interface CityDialogListener {
        void updateCity(City city, String title, String year);

        void addCity(City city);

        void deleteCity(City city1, int city);
    }

    private CityDialogListener listener;

    public static CityDialogFragment newInstance(City city, int position) {
        Bundle args = new Bundle();
        args.putSerializable("City", city);
        args.putInt("position", position);

        CityDialogFragment fragment = new CityDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CityDialogListener) {
            listener = (CityDialogListener) context;
        } else {
            throw new RuntimeException("Implement listener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_city_details, null);
        EditText editMovieName = view.findViewById(R.id.edit_city_name);
        EditText editMovieYear = view.findViewById(R.id.edit_province);

        String tag = getTag();
        Bundle bundle = getArguments();
        City city;
        int position;

        if (Objects.equals(tag, "City Details") && bundle != null) {
            city = (City) bundle.getSerializable("City");
            position = bundle.getInt("position", -1);

            assert city != null;
            editMovieName.setText(city.getName());
            editMovieYear.setText(city.getProvince());
        } else {
            position = -1;
            city = null;
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        if (Objects.equals(tag, "City Details")) {
            builder.setTitle("City Details")
                    .setNeutralButton("Delete", (dialog, which) -> {
                        if (city != null) listener.deleteCity(city, position);
                    })
                    .setPositiveButton("Continue", (dialog, which) -> {
                        String title = editMovieName.getText().toString();
                        String year = editMovieYear.getText().toString();
                        listener.updateCity(city, title, year);
                    });
        } else {
            builder.setTitle("Add City")
                    .setPositiveButton("Add", (dialog, which) -> {
                        String title = editMovieName.getText().toString();
                        String year = editMovieYear.getText().toString();
                        listener.addCity(new City(title, year));
                    });
        }

        builder.setNegativeButton("Cancel", null);

        return builder.create();

    }
}
