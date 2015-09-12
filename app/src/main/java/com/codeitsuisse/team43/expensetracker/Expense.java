package com.codeitsuisse.team43.expensetracker;

import java.security.Timestamp;
import java.util.Currency;

/**
 * Created by Sumod on 11-Sep-15.
 */
public class Expense {

    private int _id;
    private String category;
    private double amount;
    private String currency;
    private String description;
    private int day;
    private int month;
    private int year;
    private boolean if_paid = false;


    /** Constructors */
    public Expense() {
    }

    /** Setters */
    public void set_id(int _id) {
        this._id = _id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIf_paid(boolean if_paid) {
        this.if_paid = if_paid;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }


    /** Getters */
    public int get_id() {
        return _id;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getCurrency() {
        return currency;
    }

    public int getDay() {
        return day;
    }

    public String getDescription() {
        return description;
    }

    public boolean isIf_paid() {
        return if_paid;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
