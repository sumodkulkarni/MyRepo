package com.codeitsuisse.team43.expensetracker.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codeitsuisse.team43.expensetracker.Category;
import com.codeitsuisse.team43.expensetracker.DBHandler;
import com.codeitsuisse.team43.expensetracker.Expense;
import com.codeitsuisse.team43.expensetracker.R;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AddActivity extends AppCompatActivity {

    DBHandler db;

    TextView date_label, currency_textView;
    EditText enter_amount, enter_description;
    Spinner spinner, currency_spinner;
    TextView changeDate;
    ActionButton fab_done;
    CheckedTextView if_paid_view;
    LinearLayout linear_layout_principle;

    private int year;
    private int month;
    private int day;

    private static final String TAG = "AddActivity Log:";

    static final int DATE_PICKER_ID = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        db = new DBHandler(this, null, null, 1);

        linear_layout_principle = (LinearLayout) findViewById(R.id.linear_layout_principle);

        enter_amount = (EditText) findViewById(R.id.enter_amount);
        enter_amount.setText(" ");
        currency_spinner = (Spinner) findViewById(R.id.currency_spinner);
        List<String> currencyList = new ArrayList<>();
        currencyList.add("INR"); currencyList.add("USD");
        ArrayAdapter currency_adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, currencyList);
        currency_spinner.setAdapter(currency_adapter);


        enter_description = (EditText) findViewById(R.id.enter_description);
        date_label = (TextView) findViewById(R.id.date_label);
        changeDate = (TextView) findViewById(R.id.changeDate);
        spinner = (Spinner) findViewById(R.id.spinner);
        if_paid_view = (CheckedTextView) findViewById(R.id.if_paid_view);
        fab_done = (ActionButton) findViewById(R.id.fab_done);
        fab_done.setImageResource(R.drawable.ic_fab_complete);

        //Styling
        if_paid_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (if_paid_view.isChecked())
                    if_paid_view.setChecked(false);
                else
                    if_paid_view.setChecked(true);
            }
        });

        List<String> categoryList = db.getCategoryList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categoryList);
        spinner.setAdapter(adapter);

        fab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveExpense();
            }
        });

        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        // Button listener to show date picker dialog

        changeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);

            }

        });

        fab_done.playShowAnimation();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void SaveExpense(){
        Expense expense = new Expense();

        if (enter_amount.getText().toString().equals(" ")){
           ShowAlert("Invalid Input", "Please enter a valid amount", this);
        }else{
            expense.setAmount(Double.parseDouble(enter_amount.getText().toString()));
            expense.setCategory(spinner.getSelectedItem().toString());
            expense.setCurrency(currency_spinner.getSelectedItem().toString());
            expense.setDescription(enter_description.getText().toString());
            expense.setDay(day); expense.setMonth(month); expense.setYear(year);
            expense.setIf_paid(if_paid_view.isChecked());

            db.addExpense(expense);
            Toast.makeText(this, "SAVED", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            // Show selected date
            changeDate.setText(new StringBuilder().append(day)
                    .append("/").append(month+1).append("/").append(year)
                    .append(" "));

        }
    };

    public void ShowAlert(String title, String message, Context context) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
            }
        });
        alertDialog.setIcon(R.drawable.abc_dialog_material_background_dark);
        alertDialog.show();
    }
}
