package com.codeitsuisse.team43.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sumod on 11-Sep-15.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "DBHandler.java";

    //Database Version and Name:
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "EXPENSE_MANAGER";

    //Table names:
    public static final String TABLE_EXPENSES = "EXPENSES";

    //column names:
    public static final String KEY_ID = "ID";
    public static final String KEY_CATEGORY = "CATEGORY";
    public static final String KEY_AMOUNT = "AMOUNT";
    public static final String KEY_CURRENCY = "CURRENCY"; //ISO 4217 currency code
    public static final String KEY_DESCRIPTION = "DESCRIPTION";
    public static final String KEY_DAY = "DAY";
    public static final String KEY_MONTH = "MONTH";
    public static final String KEY_YEAR = "YEAR";
    public static final String KEY_IF_PAID = "IF_PAID";

    //Table name:
    public static final String TABLE_CATEGORIES = "CATEGORIES";

    //column names:
    public static final String KEY_NAME = "NAME";
    public static final String KEY_COLOUR = "COLOUR";
    public static final String KEY_BUDGET = "BUDGET";

    //Table name:
    public static final String TABLE_USER_PREFERENCES = "USER_PREFERENCES";

    //Column names:
    public static final String KEY_USER_CURRENCY = "USER_CURRENCY";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_EXPENSES = "CREATE TABLE " + TABLE_EXPENSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + KEY_CATEGORY + " VARCHAR(255) , "
                + KEY_AMOUNT + " VARCHAR(255), "
                + KEY_CURRENCY + " VARCHAR(255), "
                + KEY_DESCRIPTION + " VARCHAR(255), "
                + KEY_DAY + " INTEGER, "
                + KEY_MONTH + " INTEGER, "
                + KEY_YEAR + " INTEGER, "
                + KEY_IF_PAID + " INTEGER DEFAULT 0 " + ")";
        db.execSQL(CREATE_TABLE_EXPENSES);

        String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + " ("
                + KEY_NAME + " VARCHAR(255) PRIMARY KEY, "
                + KEY_COLOUR + " VARCHAR(255), "
                + KEY_BUDGET + " VARCHAR(255) " + ")";
        db.execSQL(CREATE_TABLE_CATEGORIES);

        String CREATE_TABLE_USER_PREFERENCES = "CREATE TABLE " + TABLE_USER_PREFERENCES + " ("
                + KEY_USER_CURRENCY + " VARCHAR(255) DEFAULT 'INR'" + ")";
        db.execSQL(CREATE_TABLE_USER_PREFERENCES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    //Delete all tables:
    public void resetDatabase(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PREFERENCES);
        onCreate(db);

    }

    /** CRUD OPERATIONS */
    public void addExpense(Expense expense){
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_AMOUNT, expense.getAmount());
            values.put(KEY_CURRENCY, expense.getCurrency());
            values.put(KEY_CATEGORY, expense.getCategory());
            values.put(KEY_DESCRIPTION, expense.getDescription());
            values.put(KEY_DAY, expense.getDay());
            values.put(KEY_MONTH, expense.getMonth());
            values.put(KEY_YEAR, expense.getYear());
            values.put(KEY_IF_PAID, IntFromBoolean(expense.isIf_paid()));

            db.insertOrThrow(TABLE_EXPENSES, null, values);
            db.close();
        }catch (SQLException e){
            Log.e(TAG, e.getMessage());
        }
    }

    public Expense getExpense(int _id){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EXPENSES + "WHERE " + KEY_ID + " =?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(_id)});

        Expense expense = new Expense();
        expense.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        expense.setAmount(Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_AMOUNT))));
        expense.setCurrency(cursor.getString(cursor.getColumnIndex(KEY_CURRENCY)));
        expense.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
        expense.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
        expense.setDay(cursor.getInt(cursor.getColumnIndex(KEY_DAY)));
        expense.setMonth(cursor.getInt(cursor.getColumnIndex(KEY_MONTH)));
        expense.setYear(cursor.getInt(cursor.getColumnIndex(KEY_YEAR)));
        expense.setIf_paid(BooleanFromInt(cursor.getInt(cursor.getColumnIndex(KEY_IF_PAID))));

        cursor.close();
        return expense;
    }

    public void updateExpense(Expense expense){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, expense.getAmount());
        values.put(KEY_CURRENCY, expense.getCurrency());
        values.put(KEY_CATEGORY, expense.getCategory());
        values.put(KEY_DESCRIPTION, expense.getDescription());
        values.put(KEY_DAY, expense.getDay());
        values.put(KEY_MONTH, expense.getMonth());
        values.put(KEY_YEAR, expense.getYear());
        values.put(KEY_IF_PAID, IntFromBoolean(expense.isIf_paid()));

        db.update(TABLE_EXPENSES, values, KEY_ID + "=?", new String[]{String.valueOf(expense.get_id())});
        db.close();
    }

    public void expensePaid(int _id){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IF_PAID, 1);
        db.update(TABLE_EXPENSES, values, KEY_ID + "=?", new String[]{String.valueOf(_id)});
        db.close();
    }

    public void deleteExpense(int _id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_EXPENSES, KEY_ID + "=?", new String[]{String.valueOf(_id)});
        db.close();
    }

    public Cursor getAllExpenses(){
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT " + KEY_ID + " as _id, "
                + KEY_AMOUNT + ", " + KEY_CURRENCY + ", "
                + KEY_DAY + ", " + KEY_MONTH + ", "
                + KEY_YEAR + " FROM " + TABLE_EXPENSES;

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }


    public Long addCategory(Category category){
        long k = -2;
        try{
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_NAME, category.getName());
            values.put(KEY_COLOUR, category.getColor());
            values.put(KEY_BUDGET, category.getBudget());

            k = db.insertOrThrow(TABLE_CATEGORIES, null, values);
            db.close();
        }catch (SQLException e){
            Log.e(TAG, e.getMessage());
        }

        return k;
    }

    public void updateCategory(Category category){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());
        values.put(KEY_COLOUR, category.getColor());
        values.put(KEY_BUDGET, category.getBudget());

        db.update(TABLE_CATEGORIES, values, KEY_NAME + "=?", new String[]{category.getName()});
        db.close();
    }

    public Category getCategory(String name){
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT " +
                KEY_NAME + ", " +
                KEY_COLOUR + ", " +
                KEY_BUDGET + ", " +
                "FROM " + TABLE_CATEGORIES + " WHERE " + KEY_NAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{name});
        cursor.moveToFirst();

        Category category = new Category();
        category.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
        category.setColor(cursor.getString(cursor.getColumnIndex(KEY_COLOUR)));
        category.setBudget(Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_BUDGET))));
        cursor.close();

        return category;
    }

    public void deleteCategory(String name){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_CATEGORIES, KEY_NAME + "=?", new String[]{name});
        db.close();
    }

    public List<String> getCategoryList(){
        List<String> categoryList = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT " + KEY_NAME + " FROM " + TABLE_CATEGORIES, null);

            if(cursor.moveToFirst()){
                do{
                    categoryList.add(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                }
                while(cursor.moveToNext());
            }
            cursor.close();
            return categoryList;
        }catch (SQLException e){
            Log.e(TAG, e.getMessage());
        }
        return categoryList;
    }

    public void setCurrency(String currency){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PREFERENCES);
            String CREATE_TABLE_USER_PREFERENCES = "CREATE TABLE " + TABLE_USER_PREFERENCES + " ("
                    + KEY_USER_CURRENCY + " VARCHAR(255) " + ")";
            db.execSQL(CREATE_TABLE_USER_PREFERENCES);
            ContentValues values = new ContentValues();
            values.put(KEY_USER_CURRENCY, currency);
            db.insertOrThrow(TABLE_USER_PREFERENCES, null, values);
        }catch (SQLException e){
            Log.e(TAG, e.getMessage());
        }
    }

    public String getCurrency(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + KEY_USER_CURRENCY + " FROM " + TABLE_USER_PREFERENCES, null);
        cursor.moveToFirst();

        String currency = cursor.getString(cursor.getColumnIndex(KEY_USER_CURRENCY));
        cursor.close();
        return currency;
    }


    public ArrayList<HashMap<String, String>> getExpenseList() {
        //Open connection to read only
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT  " +
                KEY_ID + "," +
                KEY_AMOUNT + "," +
                KEY_CURRENCY + "," +
                KEY_CATEGORY + "," +
                KEY_DAY + "," +
                KEY_MONTH + "," +
                KEY_YEAR +
                " FROM " + TABLE_EXPENSES;

        ArrayList<HashMap<String, String>> incidentList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> expense = new HashMap<String, String>();
                expense.put("id", cursor.getString(cursor.getColumnIndex(KEY_ID)));//new
                expense.put("amount", cursor.getString(cursor.getColumnIndex(KEY_AMOUNT)));
                expense.put("currency", cursor.getString(cursor.getColumnIndex(KEY_CURRENCY)));
                expense.put("category", cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
                expense.put("date", cursor.getString(cursor.getColumnIndex(KEY_DAY)) + "/" +
                                    cursor.getString(cursor.getColumnIndex(KEY_MONTH)) + "/" +
                                    cursor.getString(cursor.getColumnIndex(KEY_YEAR)));
                incidentList.add(expense);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return incidentList;

    }




    /** Utility methods */
    public int IntFromBoolean(boolean bool){
        if (bool)
            return  1;
        else
            return 0;
    }

    public boolean BooleanFromInt(int i){
        return i == 1;
    }
}
