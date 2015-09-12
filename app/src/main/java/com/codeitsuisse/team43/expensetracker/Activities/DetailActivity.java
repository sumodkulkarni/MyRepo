package com.codeitsuisse.team43.expensetracker.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codeitsuisse.team43.expensetracker.DBHandler;
import com.codeitsuisse.team43.expensetracker.Expense;
import com.codeitsuisse.team43.expensetracker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    DBHandler db;
    Button button_edit_expense, button_delete_expense;
    EditText detail_amount,detail_description;
    TextView detail_changeDate;
    Spinner detail_currency_spinner, detail_category_spinner;
    CheckedTextView detail_if_paid_view;

    private int year;
    private int month;
    private int day;
    static final int DATE_PICKER_ID = 1111;


    public void buttonEditExpense(View view){
        String text = String.valueOf(button_edit_expense.getText());

        switch (text){
            case "EDIT":
                enableAllViews(true);
                button_edit_expense.setText("SAVE");

                break;

            case "SAVE":
                enableAllViews(false);

                Expense expense = new Expense();

                Intent intent = getIntent();
                int expense_id = intent.getIntExtra("expense_id", 0);

                expense.set_id(expense_id);
                expense.set(Double.valueOf(String.valueOf(inc_lat_view.getText())));
                expense.setLongitude(Double.valueOf(String.valueOf(inc_long_view.getText())));
                expense.setCategory(spinner_inc_details.getSelectedItem().toString());

                expense.setImage(bitmap);
                expense.setPin_code(IncidentDetails.this);
                db.updateExpense(expense);
                db.close();
                Toast.makeText(this, "UPDATED", Toast.LENGTH_LONG).show();
                button_edit_expense.setText("EDIT");

                break;
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = new DBHandler(this, null, null, 1);

        detail_amount = (EditText) findViewById(R.id.detail_amount);
        detail_description = (EditText) findViewById(R.id.detail_description);
        button_delete_expense = (Button) findViewById(R.id.button_delete_expense);
        button_edit_expense = (Button) findViewById(R.id.button_edit_expense);

        detail_currency_spinner = (Spinner) findViewById(R.id.detail_currency_spinner);
        List<String> currencyList = new ArrayList<>();
        currencyList.add("INR"); currencyList.add("USD");
        ArrayAdapter currency_adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, currencyList);
        detail_currency_spinner.setAdapter(currency_adapter);

        detail_category_spinner = (Spinner) findViewById(R.id.detail_category_spinner);
        List<String> categoryList = db.getCategoryList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, categoryList);
        detail_category_spinner.setAdapter(adapter);

        detail_changeDate = (TextView) findViewById(R.id.detail_changeDate);
        // Get current date by calender
        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        // Button listener to show date picker dialog
        detail_changeDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);

            }

        });

        detail_if_paid_view = (CheckedTextView) findViewById(R.id.detail_if_paid_view);
        detail_if_paid_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detail_if_paid_view.isChecked())
                    detail_if_paid_view.setChecked(false);
                else
                    detail_if_paid_view.setChecked(true);
            }
        });

        enableAllViews(false);
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
            detail_changeDate.setText(new StringBuilder().append(day)
                    .append("/").append(month+1).append("/").append(year)
                    .append(" "));

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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

    private void enableAllViews(boolean bool){
        if (bool){
            detail_amount.setEnabled(true);
            detail_category_spinner.setEnabled(true);
            detail_category_spinner.setEnabled(true);
            detail_description.setEnabled(true);
            detail_changeDate.setClickable(true);
            detail_if_paid_view.setClickable(true);
        }
        else{
            detail_amount.setEnabled(false);
            detail_category_spinner.setEnabled(false);
            detail_category_spinner.setEnabled(false);
            detail_description.setEnabled(false);
            detail_changeDate.setClickable(false);
            detail_if_paid_view.setClickable(false);
        }
    }
}
