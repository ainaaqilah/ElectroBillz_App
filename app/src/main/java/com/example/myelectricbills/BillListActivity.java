package com.example.myelectricbills;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BillListActivity extends AppCompatActivity {


    ListView listView;
    ArrayList<Bill> billList;
    BillAdapter adapter;
    DatabaseHelper dbHelper;
    ActivityResultLauncher<Intent> updateBillLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);

        listView = findViewById(R.id.listViewBills);
        dbHelper = new DatabaseHelper(this);

        // Fetch data from database
        billList = dbHelper.getAllBills();

        updateBillLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        billList.clear();
                        billList.addAll(dbHelper.getAllBills());
                        adapter.notifyDataSetChanged();
                    }
                });


        // Set up adapter
        adapter = new BillAdapter(this, billList);
        listView.setAdapter(adapter);

        // ✅ Set OnItemClickListener with dialog popup (only once)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Bill selectedBill = billList.get(position);

                final CharSequence[] options = {"View Bill Details", "Update Bill Details", "Delete Bill Details"};
                AlertDialog.Builder builder = new AlertDialog.Builder(BillListActivity.this);
                builder.setTitle("Select Action");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // View
                                Intent viewIntent = new Intent(BillListActivity.this, ListDetailActivity.class);
                                viewIntent.putExtra("bill", selectedBill);
                                startActivity(viewIntent);
                                break;

                            case 1: // Update
                                Intent updateIntent = new Intent(BillListActivity.this, UpdateBillActivity.class);
                                updateIntent.putExtra("bill", selectedBill);
                                updateBillLauncher.launch(updateIntent);
                                break;

                            case 2: // Delete
                                dbHelper.deleteBill(selectedBill.getId());
                                Toast.makeText(BillListActivity.this, "Bill is deleted", Toast.LENGTH_SHORT).show();

                                // Refresh list
                                billList.clear();
                                billList.addAll(dbHelper.getAllBills());
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }
}
