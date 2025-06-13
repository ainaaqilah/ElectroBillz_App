package com.example.myelectricbills;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class ListDetailActivity extends AppCompatActivity {

    TextView txtMonthDetail, txtUnitsDetail, txtRebateDetail, txtTotalChargesDetail, txtFinalCostDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

        // Initialize views
        txtMonthDetail = findViewById(R.id.txtMonthDetail);
        txtUnitsDetail = findViewById(R.id.txtUnitsDetail);
        txtRebateDetail = findViewById(R.id.txtRebateDetail);
        txtTotalChargesDetail = findViewById(R.id.txtTotalChargesDetail);
        txtFinalCostDetail = findViewById(R.id.txtFinalCostDetail);

        // Get the Bill object from the intent
        Bill bill = (Bill) getIntent().getSerializableExtra("bill");

        if (bill != null) {
            // Display bill details
            txtMonthDetail.setText(bill.getMonth());
            txtUnitsDetail.setText("Units Used: " + bill.getUnits() + " kWh");
            txtRebateDetail.setText("Rebate: " + bill.getRebate() + "%");
            txtTotalChargesDetail.setText(String.format(Locale.getDefault(), "Total Charges: RM %.2f", bill.getTotalCharges()));
            txtFinalCostDetail.setText(String.format(Locale.getDefault(), "Final Cost After Rebate: \nRM %.2f", bill.getFinalCost()));
        } else {
            txtMonthDetail.setText("No data available.");
            txtUnitsDetail.setText("");
            txtRebateDetail.setText("");
            txtTotalChargesDetail.setText("");
            txtFinalCostDetail.setText("");
        }
    }
}
