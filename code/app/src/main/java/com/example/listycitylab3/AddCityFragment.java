package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class AddCityFragment extends DialogFragment {

    interface AddCityDialogListener {
        void addCity(City city);
    }

    interface EditCityDialogListener {
        void editCity(City city, Integer position);
    }

    private AddCityDialogListener listenerA;
    private EditCityDialogListener listenerE;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener && context instanceof EditCityDialogListener) {
            listenerA = (AddCityDialogListener) context;
            listenerE = (EditCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener and EditCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view =
                LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        City city;
        Integer position;

        if (getArguments() != null) {
            city = (City) getArguments().getSerializable("city");
            position = getArguments().getInt("position");
            editCityName.setText(city.getName());
            editProvinceName.setText(city.getProvince());
        } else {
            city = null;
            position = null;
        }

        return builder
                .setView(view)
                .setTitle(getArguments() == null ? "Add a city" : "Edit a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (getArguments() == null) {
                        String cityName = editCityName.getText().toString();
                        String provinceName = editProvinceName.getText().toString();
                        listenerA.addCity(new City(cityName, provinceName));
                    } else {
                        city.setName(editCityName.getText().toString());
                        city.setProvince(editProvinceName.getText().toString());
                        listenerE.editCity(city, position);
                    }
                })
                .create();
    }

    static AddCityFragment newInstance(City city, Integer position) {
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        args.putInt("position", position);

        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
