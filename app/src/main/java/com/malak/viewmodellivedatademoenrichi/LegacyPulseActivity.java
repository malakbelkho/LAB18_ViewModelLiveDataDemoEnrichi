package com.malak.viewmodellivedatademoenrichi;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LegacyPulseActivity extends AppCompatActivity {

    private int localPulse = 0;

    private TextView tvPulseValue;
    private Button btnBoost;
    private Button btnLower;
    private Button btnRefresh;
    private Button btnAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPulseValue = findViewById(R.id.tvPulseValue);
        btnBoost = findViewById(R.id.btnBoost);
        btnLower = findViewById(R.id.btnLower);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnAsync = findViewById(R.id.btnAsync);

        if (savedInstanceState != null) {
            localPulse = savedInstanceState.getInt("legacy_pulse_value", 0);
        }

        displayPulse();

        btnBoost.setOnClickListener(v -> {
            localPulse++;
            displayPulse();
        });

        btnLower.setOnClickListener(v -> {
            localPulse--;
            displayPulse();
        });

        btnRefresh.setOnClickListener(v -> {
            localPulse = 0;
            displayPulse();
        });

        btnAsync.setOnClickListener(v -> {
            localPulse++;
            displayPulse();
        });
    }

    private void displayPulse() {
        tvPulseValue.setText(String.valueOf(localPulse));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("legacy_pulse_value", localPulse);
    }
}