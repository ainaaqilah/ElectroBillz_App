package com.example.myelectricbills;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ElectricBills.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "bills";
    public static final String COL_ID = "id";
    public static final String COL_MONTH = "month";
    public static final String COL_UNITS = "units";
    public static final String COL_REBATE = "rebate";
    public static final String COL_TOTAL_CHARGES = "totalCharges";
    public static final String COL_FINAL_COST = "finalCost";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MONTH + " TEXT, " +
                COL_UNITS + " INTEGER, " +
                COL_REBATE + " INTEGER, " +
                COL_TOTAL_CHARGES + " REAL, " +
                COL_FINAL_COST + " REAL)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // To drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert a new bill record into the database
    public boolean insertBill(String month, int units, int rebate, double totalCharges, double finalCost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MONTH, month);
        values.put(COL_UNITS, units);
        values.put(COL_REBATE, rebate);
        values.put(COL_TOTAL_CHARGES, totalCharges);
        values.put(COL_FINAL_COST, finalCost);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1; // return true if insert successful
    }

    // Overloaded method to insert a Bill object
    public boolean insertBill(Bill bill) {
        return insertBill(
                bill.getMonth(),
                bill.getUnits(),
                bill.getRebate(),
                bill.getTotalCharges(),
                bill.getFinalCost()
        );
    }

    // To fetch all bills from the database
    public ArrayList<Bill> getAllBills() {
        ArrayList<Bill> bills = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String month = cursor.getString(cursor.getColumnIndexOrThrow(COL_MONTH));
                int units = cursor.getInt(cursor.getColumnIndexOrThrow(COL_UNITS));
                int rebate = cursor.getInt(cursor.getColumnIndexOrThrow(COL_REBATE));
                double totalCharges = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_TOTAL_CHARGES));
                double finalCost = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_FINAL_COST));

                bills.add(new Bill(id, month, units, rebate, totalCharges, finalCost));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bills;
    }

    public boolean updateBill(Bill bill) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MONTH, bill.getMonth());
        values.put(COL_UNITS, bill.getUnits());
        values.put(COL_REBATE, bill.getRebate());
        values.put(COL_TOTAL_CHARGES, bill.getTotalCharges());
        values.put(COL_FINAL_COST, bill.getFinalCost());


        int result = db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(bill.getId())});
        return result > 0;
    }

    public void deleteBill(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

}
