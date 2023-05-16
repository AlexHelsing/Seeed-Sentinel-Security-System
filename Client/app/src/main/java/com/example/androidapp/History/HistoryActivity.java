package com.example.androidapp.History;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.AlarmViewModel;
import com.example.androidapp.MainActivity;
import com.example.androidapp.R;
import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import com.example.androidapp.dbHandler;

import org.bson.Document;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    TableRow tableRow;
    ImageView backButton;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        AlarmViewModel alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        dbHandler db = new dbHandler(getApplicationContext());
        userViewModel = new UserViewModelFactory(db).create(UserViewModel.class);

        userViewModel.getUser().observe(this, user -> {
            if (user != null) {
                List<Document> timestamps = user.getBreakins();

                for (Document timestamp : timestamps) {
                    String location = timestamp.get("location").toString();
                    String date = timestamp.get("date").toString();

                    tableRow = new TableRow(this);
                    TextView textView1 = new TextView(this);
                    textView1.setText(location);
                    textView1.setTextColor(Color.WHITE);

                    TextView textView2 = new TextView(this);
                    textView2.setText(date);
                    textView2.setTextColor(Color.WHITE);
                    tableRow.addView(textView1);
                    tableRow.addView(textView2);
                    tableLayout.addView(tableRow);

                    if (tableLayout.getChildCount() > 10) {
                        tableLayout.removeViewAt(0); // Remove the first row
                    }

                }
            }
        });




        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
