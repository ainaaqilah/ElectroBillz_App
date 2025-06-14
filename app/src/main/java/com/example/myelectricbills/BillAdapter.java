package com.example.myelectricbills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class BillAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Bill> billList;

    public BillAdapter(Context context, ArrayList<Bill> billList) {
        this.context = context;
        this.billList = billList;
    }

    @Override
    public int getCount() {
        return billList.size();
    }

    @Override
    public Object getItem(int position) {
        return billList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // This method is to connect each item to row_bill.xml
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            row = LayoutInflater.from(context).inflate(R.layout.row_bill, parent, false);
        }

        TextView textMonth = row.findViewById(R.id.textMonth);
        TextView textDetails = row.findViewById(R.id.textDetails);

        Bill bill = billList.get(position);

        textMonth.setText(bill.getMonth());
        String detail = String.format(Locale.getDefault(), "Final Cost: RM %.2f", bill.getFinalCost());
        textDetails.setText(detail);


        return row;
    }
}

