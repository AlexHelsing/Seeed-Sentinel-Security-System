package com.example.androidapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TableLayout;

public class HistoryActivity extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    TableRow tableRow;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        backButton = (Button) findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void addTableRows(TableLayout tableLayout) {
        tableLayout = findViewById(R.id.tableLayout);
        for (int i = 0; i < 10; i++) {
            tableRow = new TableRow(this);
            textView1 = new TextView(this);
            textView2 = new TextView(this);
            textView1.setText("Alarm History");
            textView2.setText("placeholder");
            tableRow.addView(textView1);
            tableRow.addView(textView2);
            tableLayout.addView(tableRow);
        }
    }
}
