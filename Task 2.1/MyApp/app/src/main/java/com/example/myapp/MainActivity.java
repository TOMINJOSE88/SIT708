package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    Spinner categorySpinner, fromSpinner, toSpinner;
    EditText inputValue;
    Button convertButton;
    TextView resultText;

    Map<String, Double> lengthMap = new HashMap<>();
    Map<String, Double> weightMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize
        categorySpinner = findViewById(R.id.categorySpinner);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        inputValue = findViewById(R.id.inputValue);
        convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);

        // Setup conversions
        lengthMap.put("Inch", 2.54);
        lengthMap.put("Foot", 30.48);
        lengthMap.put("Yard", 91.44);
        lengthMap.put("Mile", 160934.0); // in cm
        lengthMap.put("Centimeter", 1.0);

        weightMap.put("Pound", 0.453592);
        weightMap.put("Ounce", 0.0283495);
        weightMap.put("Ton", 907.185);
        weightMap.put("Kilogram", 1.0);

        // Set categories
        String[] categories = {"Length", "Weight", "Temperature"};
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(catAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitSpinners();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        convertButton.setOnClickListener(v -> convertValue());
    }

    private void updateUnitSpinners() {
        String category = categorySpinner.getSelectedItem().toString();
        ArrayList<String> units = new ArrayList<>();

        switch (category) {
            case "Length":
                units.addAll(Arrays.asList("Inch", "Foot", "Yard", "Mile", "Centimeter"));
                break;
            case "Weight":
                units.addAll(Arrays.asList("Pound", "Ounce", "Ton", "Kilogram"));
                break;
            case "Temperature":
                units.addAll(Arrays.asList("Celsius", "Fahrenheit", "Kelvin"));
                break;
        }

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        fromSpinner.setAdapter(unitAdapter);
        toSpinner.setAdapter(unitAdapter);
    }

    private void convertValue() {
        String category = categorySpinner.getSelectedItem().toString();
        String fromUnit = fromSpinner.getSelectedItem().toString();
        String toUnit = toSpinner.getSelectedItem().toString();
        String valueStr = inputValue.getText().toString().trim();

        if (valueStr.isEmpty()) {
            Toast.makeText(this, "Please enter a value.", Toast.LENGTH_SHORT).show();
            return;
        }

        double value;
        try {
            value = Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input. Please enter a numeric value.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fromUnit.equals(toUnit)) {
            Toast.makeText(this, "Source and target units are the same. Please select different units.", Toast.LENGTH_SHORT).show();
            return;
        }

        double result = 0.0;
        switch (category) {
            case "Length":
                double fromInCm = value * lengthMap.get(fromUnit);
                result = fromInCm / lengthMap.get(toUnit);
                break;
            case "Weight":
                double fromInKg = value * weightMap.get(fromUnit);
                result = fromInKg / weightMap.get(toUnit);
                break;
            case "Temperature":
                result = convertTemperature(value, fromUnit, toUnit);
                break;
        }

        resultText.setText("Converted Value: " + result);
    }


    private double convertTemperature(double value, String fromUnit, String toUnit) {
        double tempValue = value;

        if (fromUnit.equals("Celsius") && toUnit.equals("Fahrenheit")) {
            return (tempValue * 1.8) + 32;
        } else if (fromUnit.equals("Fahrenheit") && toUnit.equals("Celsius")) {
            return (tempValue - 32) / 1.8;
        } else if (fromUnit.equals("Celsius") && toUnit.equals("Kelvin")) {
            return tempValue + 273.15;
        } else if (fromUnit.equals("Kelvin") && toUnit.equals("Celsius")) {
            return tempValue - 273.15;
        } else if (fromUnit.equals("Fahrenheit") && toUnit.equals("Kelvin")) {
            return (tempValue - 32) / 1.8 + 273.15;
        } else if (fromUnit.equals("Kelvin") && toUnit.equals("Fahrenheit")) {
            return (tempValue - 273.15) * 1.8 + 32;
        } else {
            return tempValue;  // same unit conversion
        }
    }
}