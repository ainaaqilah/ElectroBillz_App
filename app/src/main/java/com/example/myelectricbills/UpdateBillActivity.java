package com.example.myelectricbills;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateBillActivity extends AppCompatActivity {

    private Spinner spinnerMonth;
    private EditText editTextUnits;
    private RadioGroup radioGroupRebate;
    private Button buttonUpdate;

    private Bill currentBill;
    private DatabaseHelper dbHelper;

    private String selectedMonth = "";
    private int selectedRebate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bill);

        spinnerMonth = findViewById(R.id.spinnerUpdateMonth);
        editTextUnits = findViewById(R.id.editTextUpdateUnits);
        radioGroupRebate = findViewById(R.id.radioGroupUpdateRebate);
        buttonUpdate = findViewById(R.id.buttonSave);

        dbHelper = new DatabaseHelper(this);

        // Get Bill object from intent
        Intent intent = getIntent();
        currentBill = (Bill) intent.getSerializableExtra("bill");

        if (currentBill == null) {
            Toast.makeText(this, "Bill not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set values to the form
        setupSpinner();
        spinnerMonth.setSelection(getMonthIndex(currentBill.getMonth()));
        editTextUnits.setText(String.valueOf(currentBill.getUnits()));
        setRebateRadio(currentBill.getRebate());

        // Handle update button click
        buttonUpdate.setText("Update Bill");
        buttonUpdate.setOnClickListener(v -> {
            selectedMonth = spinnerMonth.getSelectedItem().toString();
            String unitsStr = editTextUnits.getText().toString().trim();

            if (unitsStr.isEmpty()) {
                Toast.makeText(this, "Please enter units", Toast.LENGTH_SHORT).show();
                return;
            }

            int units = Integer.parseInt(unitsStr);
            int rebate = getSelectedRebate();

            // Recalculate
            double totalCharges = calculateTotalCharges(units);
            double finalCost = totalCharges * (1 - rebate / 100.0);

            // Update Bill Details object
            currentBill.setMonth(selectedMonth);
            currentBill.setUnits(units);
            currentBill.setRebate(rebate);
            currentBill.setTotalCharges(totalCharges);
            currentBill.setFinalCost(finalCost);

            // Update in the database
            boolean success = dbHelper.updateBill(currentBill);

            if (success) {
                Toast.makeText(this, "Bill updated successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK); // ✅ To refresh the list
                finish(); // ✅ To finish the activity
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);
    }

    private int getMonthIndex(String month) {
        String[] months = getResources().getStringArray(R.array.months_array);
        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(month)) {
                return i;
            }
        }
        return 0;
    }

    private void setRebateRadio(int rebatePercent) {
        switch (rebatePercent) {
            case 0:
                radioGroupRebate.check(R.id.radioUpdate0);
                break;
            case 1:
                radioGroupRebate.check(R.id.radioUpdate1);
                break;
            case 2:
                radioGroupRebate.check(R.id.radioUpdate2);
                break;
            case 3:
                radioGroupRebate.check(R.id.radioUpdate3);
                break;
            case 4:
                radioGroupRebate.check(R.id.radioUpdate4);
                break;
            case 5:
                radioGroupRebate.check(R.id.radioUpdate5);
                break;
        }
    }

    private int getSelectedRebate() {
        int selectedId = radioGroupRebate.getCheckedRadioButtonId();
        RadioButton selectedRadio = findViewById(selectedId);
        if (selectedRadio != null) {
            String text = selectedRadio.getText().toString().replace("%", "").trim();
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private double calculateTotalCharges(int units) {
        double rate;

        if (units <= 200) {
            rate = 0.218;
        } else if (units <= 300) {
            rate = 0.334;
        } else if (units <= 600) {
            rate = 0.516;
        } else {
            rate = 0.546;
        }

        return units * rate;
    }
}

