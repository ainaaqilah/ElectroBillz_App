package com.example.myelectricbills;

import java.io.Serializable;

public class Bill implements Serializable {
    private int id;
    private String month;
    private int units;
    private int rebate;
    private double totalCharges;
    private double finalCost;

    // Constructor without ID (for insert)
    public Bill(String month, int units, int rebate, double totalCharges, double finalCost) {
        this.month = month;
        this.units = units;
        this.rebate = rebate;
        this.totalCharges = totalCharges;
        this.finalCost = finalCost;
    }

    // Constructor with ID (for fetch from DB or update)
    public Bill(int id, String month, int units, int rebate, double totalCharges, double finalCost) {
        this.id = id;
        this.month = month;
        this.units = units;
        this.rebate = rebate;
        this.totalCharges = totalCharges;
        this.finalCost = finalCost;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getMonth() {
        return month;
    }

    public int getUnits() {
        return units;
    }

    public int getRebate() {
        return rebate;
    }

    public double getTotalCharges() {
        return totalCharges;
    }

    public double getFinalCost() {
        return finalCost;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public void setRebate(int rebate) {
        this.rebate = rebate;
    }

    public void setTotalCharges(double totalCharges) {
        this.totalCharges = totalCharges;
    }

    public void setFinalCost(double finalCost) {
        this.finalCost = finalCost;
    }
}
