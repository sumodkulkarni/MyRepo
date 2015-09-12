package com.codeitsuisse.team43.expensetracker;

import java.util.Currency;

/**
 * Created by Sumod on 12-Sep-15.
 */
public class Category {

    private String name;
    private String color;
    private double budget;
    private Currency currency;

    /** Constructors */
    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    /** Setter */
    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Getter */
    public double getBudget() {
        return budget;
    }

    public String getColor() {
        return color;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getName() {
        return name;
    }
}
