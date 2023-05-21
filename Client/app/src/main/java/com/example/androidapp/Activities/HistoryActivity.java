package com.example.androidapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.ViewModels.AlarmViewModel;
import com.example.androidapp.R;
import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import com.example.androidapp.dbHandler;

import org.bson.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

                tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

                for (Document timestamp : timestamps) {
                    String location = timestamp.get("location").toString();
                    Date date = timestamp.getDate("date");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("CET"));
                    String formattedDate = sdf.format(date);

                    tableRow = new TableRow(this);
                    TextView textView1 = new TextView(this);
                    textView1.setText(location);
                    textView1.setTextColor(Color.WHITE);
                    textView1.setTextSize(15);
                    textView1.setGravity(Gravity.CENTER);


                    TextView textView2 = new TextView(this);
                    textView2.setText(formattedDate);
                    textView2.setTextColor(Color.WHITE);
                    textView2.setTextSize(15);
                    textView2.setGravity(Gravity.CENTER);

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
