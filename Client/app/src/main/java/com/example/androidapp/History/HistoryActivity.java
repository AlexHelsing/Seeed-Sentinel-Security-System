package com.example.androidapp.History;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TableLayout;
import com.example.androidapp.MainActivity;
import com.example.androidapp.R;
import com.example.androidapp.ViewModels.UserViewModel;
import com.example.androidapp.ViewModels.UserViewModelFactory;
import com.example.androidapp.dbHandler;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    TableRow tableRow;
    Button backButton;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        dbHandler db = new dbHandler(getApplicationContext());
        userViewModel = new UserViewModelFactory(db).create(UserViewModel.class);

        // gets all the breakins, might be suboptimal to put them in userModel but it works  :)
        userViewModel.getUser().observe(this, user -> {
            if (user != null) {
                List<Document> timestamps = user.getBreakins();
                for (Document timestamp : timestamps) {
                    String location = timestamp.get("location").toString();
                    String date = timestamp.get("date").toString();

                    // TODO FOR MR STEFAAN - FIX THIS SO IT LOOKS LIKE THE REST :)
                    tableRow = new TableRow(this);
                    textView1 = new TextView(this);
                    textView2 = new TextView(this);
                    textView1.setText(location);
                    textView2.setText(date);
                    tableRow.addView(textView1);
                    tableRow.addView(textView2);
                    tableLayout.addView(tableRow);



                }
            }
        });



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
        SensorTimestamps sensorTimestamps = new SensorTimestamps();
        LinkedList<String> timestamps = sensorTimestamps.getTimestamps();
        for (int i = 0; i < 10; i++) {
            tableRow = new TableRow(this);
            textView1 = new TextView(this);
            textView2 = new TextView(this);
            textView1.setText("Alarm History");
            textView2.setText(TextUtils.join("\n", timestamps));
            tableRow.addView(textView1);
            tableRow.addView(textView2);
            tableLayout.addView(tableRow);
        }
    }
}
