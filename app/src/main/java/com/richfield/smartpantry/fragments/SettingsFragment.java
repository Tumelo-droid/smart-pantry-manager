package com.richfield.smartpantry.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.richfield.smartpantry.R;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Switch switchExpiry = view.findViewById(R.id.switchExpiryAlerts);
        Switch switchMetric = view.findViewById(R.id.switchMetricUnits);

        switchExpiry.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String msg = isChecked ? "Expiry alerts enabled" : "Expiry alerts disabled";
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });

        switchMetric.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String msg = isChecked ? "Metric units enabled" : "Metric units disabled";
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
