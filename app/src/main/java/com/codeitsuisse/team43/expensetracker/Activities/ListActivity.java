package com.codeitsuisse.team43.expensetracker.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.codeitsuisse.team43.expensetracker.DBHandler;
import com.codeitsuisse.team43.expensetracker.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends AppCompatActivity {

    DBHandler db;
    ListView expense_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        db = new DBHandler(this, null, null, 1);

        expense_listView = (ListView) findViewById(R.id.expense_listView);

        populateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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

    public void populateListView(){
        ArrayList<HashMap<String, String>> expenseList =  db.getExpenseList();
        ListView incidentlistview = (ListView) findViewById(R.id.expense_listView);
        ListAdapter adapter = new SimpleAdapter(this, expenseList, R.layout.expense_list_item,
                new String[] {"id","amount","currency","date"},
                new int[] {R.id.item_id, R.id.item_amount, R.id.item_currency, R.id.item_date});
        incidentlistview.setAdapter(adapter);
        incidentlistview.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView item_id = (TextView) view.findViewById(R.id.item_id);
                        String expenseId = item_id.getText().toString();
                        Intent objIntent = new Intent(getApplicationContext(),DetailActivity.class);
                        objIntent.putExtra("expense_id", Integer.parseInt(expenseId));
                        startActivity(objIntent);
                    }
                }
        );
    }
}
