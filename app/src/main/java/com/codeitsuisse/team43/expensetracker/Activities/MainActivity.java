package com.codeitsuisse.team43.expensetracker.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.codeitsuisse.team43.expensetracker.DBHandler;
import com.codeitsuisse.team43.expensetracker.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.melnykov.fab.FloatingActionButton;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;
//import com.codeitsuisse.team43.expensetracker.notimportant.DemoBase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBHandler db;
    private PieChart mChart;
    FloatingActionButton fab;

    public void gotoList(View view){
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHandler(this, null, null, 1);

        ListView listView = (ListView) findViewById(android.R.id.list);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivity(i);
            }
        });

        populatePieChart();


    }

    @Override
    protected void onResume() {
        super.onResume();
        populatePieChart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setData(int count, float range) {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        float expenses_paid = db.getTotalExpensesPaid();
        float total_liabilities = db.getTotalLiabilities();

        //ShowAlert("expenses_paid", String.valueOf(expenses_paid), this);
        //ShowAlert("total_liabilities", String.valueOf(total_liabilities), this);

        float total = expenses_paid + total_liabilities;
        float percent_expenses_paid = (expenses_paid/total) * 100;
        float percent_liabilities = (total_liabilities/total) * 100;

        yVals1.add(new Entry(percent_expenses_paid, 0));
        yVals1.add(new Entry(percent_liabilities, 1));


        ArrayList<String> xVals = new ArrayList<String>();
        String[] mParties = new String[] {"Expenses", "Liabilities"};
        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, null);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        /*
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
*/
        /*
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
*/
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

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

    private void populatePieChart(){
        mChart = (PieChart) findViewById(R.id.piechart);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");

        setData(2, 100);

        mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        mChart.setCenterText("Budget\nvs.\nLiabilities");
        mChart.setCenterTextColor(R.color.abc_primary_text_disable_only_material_dark);
        mChart.setNoDataText("Enter some Expenses and a Budget to get started!");
    }

}
