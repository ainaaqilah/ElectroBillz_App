package com.example.myelectricbills;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class AddBillActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText editTextUnits;
    RadioGroup radioGroupRebate;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_bill);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        editTextUnits = findViewById(R.id.editTextUnits);
        radioGroupRebate = findViewById(R.id.radioGroupRebate);
        btnSave = findViewById(R.id.btnSave);
        TextView textViewTotalCharges = findViewById(R.id.textViewTotalCharges);
        TextView textViewFinalCost = findViewById(R.id.textViewFinalCost);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.months_array, // Make sure this is the correct array in strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);


        DatabaseHelper dbHelper = new DatabaseHelper(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Step 1: Get user inputs
                String selectedMonth = spinnerMonth.getSelectedItem().toString();
                String unitsStr = editTextUnits.getText().toString().trim();
                if (unitsStr.isEmpty()) {
                    editTextUnits.setError("Please enter electricity units");
                    editTextUnits.requestFocus();
                    return;
                }

                int units = Integer.parseInt(unitsStr);

                // Step 2: Get rebate percentage
                int selectedRebateId = radioGroupRebate.getCheckedRadioButtonId();
                if (selectedRebateId == -1) {
                    Toast.makeText(AddBillActivity.this, "Please select a rebate percentage", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedRadioButton = findViewById(selectedRebateId);
                String rebateText = selectedRadioButton.getText().toString(); // e.g. "2%"
                int rebatePercent = Integer.parseInt(rebateText.replace("%", ""));

                // Step 3: Calculate total charges based on blocks
                double totalCharges;
                int remainingUnits = units;
                totalCharges = 0;

                // First 200 units
                if (remainingUnits > 0) {
                    int block = Math.min(remainingUnits, 200);
                    totalCharges += block * 0.218;
                    remainingUnits -= block;
                }

                // Next 100 units (201–300)
                if (remainingUnits > 0) {
                    int block = Math.min(remainingUnits, 100);
                    totalCharges += block * 0.334;
                    remainingUnits -= block;
                }

                // Above 300 units
                if (remainingUnits > 0) {
                    totalCharges += remainingUnits * 0.516;
                }

                // Step 4: Calculate final cost after rebate
                double finalCost = totalCharges - (totalCharges * rebatePercent / 100.0);

                // Step 5: Show result on screen
                textViewTotalCharges.setText(String.format(Locale.getDefault(), "Total Charges: RM %.2f", totalCharges));
                textViewFinalCost.setText(String.format(Locale.getDefault(), "Final Cost After Rebate: RM %.2f", finalCost));

                // Step 6: Save to SQLite
                Bill bill = new Bill(selectedMonth, units, rebatePercent, totalCharges, finalCost);
                boolean success = dbHelper.insertBill(bill);

                if (success) {
                    Toast.makeText(AddBillActivity.this, "Bill has calculated and saved successfully", Toast.LENGTH_SHORT).show();
                    editTextUnits.setText("");
                    radioGroupRebate.clearCheck();
                    spinnerMonth.setSelection(0);
                } else {
                    Toast.makeText(AddBillActivity.this, "Failed to save bill.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}