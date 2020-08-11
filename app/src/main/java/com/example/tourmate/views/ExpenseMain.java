package com.example.tourmate.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import com.example.tourmate.R;
import com.example.tourmate.adapter.EntryAdapter;
import com.example.tourmate.adapter.EntryDetails;
import com.example.tourmate.data.Databases;
import com.example.tourmate.dialogBox.editBudgetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ExpenseMain extends AppCompatActivity {

    Databases mDatabases;
    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_main);


        // Checks for theme settings option
        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        switch (prefs.getInt("theme", 0)) {
            case 1:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case 2:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

        mDatabases = new Databases(this);

        this.mViewHolder.txtBudgetInt = findViewById(R.id.txt_budget_integers);
        this.mViewHolder.txtBudgetDec = findViewById(R.id.txt_budget_decimal);
        this.mViewHolder.btnBudgetEdit = findViewById(R.id.btn_budget_edit);
        this.mViewHolder.lstTransactions = findViewById(R.id.list_transactions);
        this.mViewHolder.txtCurrency = findViewById(R.id.txt_budget_currency);

        this.mViewHolder.fab = findViewById(R.id.fab);


        // Click events

        this.mViewHolder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpenseMain.this, NewTransactionActivity.class);
                startActivity(intent);
            }
        });

        this.mViewHolder.btnBudgetEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open dialog box to edit budget
                DialogFragment newFragment = new editBudgetDialog();
                newFragment.show(getSupportFragmentManager(), "add budget");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        this.mViewHolder.txtCurrency.setText(prefs.getString("currency", getResources().getString(R.string.currency)));

        updateBudgetDisplay();

        setAdapter();
    }

    public void setAdapter() {
        String[][] entries = mDatabases.getEntries();

        // Construct the data source
        ArrayList<EntryDetails> entryList = new ArrayList<EntryDetails>();

        for (int i = 0; i < entries.length; i++) {
            EntryDetails details = new EntryDetails(entries[i][0], entries[i][1], entries[i][2], entries[i][3]);
            entryList.add(details);
        }

        // Create the adapter to convert the array to views
        EntryAdapter adapter = new EntryAdapter(this, entryList);
        // Attach the adapter to a ListView
        this.mViewHolder.lstTransactions.setAdapter(adapter);
    }

    /**
     * This method is used to update the budget text
     * every time a new value is assigned to the total budget
     *
     * @return Nothing.
     */
    public void updateBudgetDisplay() {
        try {
            float money = mDatabases.getTotalBudget();
            String strMoney = String.valueOf(money);

            // Separates Integer part and Decimal part
            // Example: 14.50 turns into X = 14 and Y = .50
            int indexOfDecimal = strMoney.indexOf(".");
            String moneyInt = strMoney.substring(0, indexOfDecimal);
            String moneyDec = strMoney.substring(indexOfDecimal);

            // Handles case of missing decimal at the end
            if (moneyDec.length() == 2) { moneyDec = moneyDec+"0"; }

            // Sets text to view
            this.mViewHolder.txtBudgetInt.setText(moneyInt);
            this.mViewHolder.txtBudgetDec.setText(moneyDec);

            // Sets correct color
            if (money > 0) {
                this.mViewHolder.txtBudgetInt.setTextColor(getResources().getColor(R.color.moneyPositive));
                this.mViewHolder.txtBudgetDec.setTextColor(getResources().getColor(R.color.moneyPositive));
            } else {
                this.mViewHolder.txtBudgetInt.setTextColor(getResources().getColor(R.color.moneyNegative));
                this.mViewHolder.txtBudgetDec.setTextColor(getResources().getColor(R.color.moneyNegative));
            }

        } catch (Exception e) {
            // This will be executed if there is no budget in Database
            this.mViewHolder.txtBudgetInt.setText("0");
        }
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
            Intent intent = new Intent(ExpenseMain.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private static class ViewHolder {
        FloatingActionButton fab;
        TextView txtBudgetInt;
        TextView txtBudgetDec;
        Button btnBudgetEdit;
        ListView lstTransactions;
        TextView txtCurrency;
    }
}
