package com.malak.viewmodellivedatademoenrichi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private PulseCounterViewModel pulseViewModel;

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

        pulseViewModel = new ViewModelProvider(this).get(PulseCounterViewModel.class);

        pulseViewModel.getPulseState().observe(this, updatedPulse -> {
            tvPulseValue.setText(String.valueOf(updatedPulse));
        });

        btnBoost.setOnClickListener(v -> pulseViewModel.boostPulse());

        btnLower.setOnClickListener(v -> pulseViewModel.lowerPulse());

        btnRefresh.setOnClickListener(v -> pulseViewModel.resetPulse());

        btnAsync.setOnClickListener(v -> pulseViewModel.boostFromWorkerThread());
    }
}