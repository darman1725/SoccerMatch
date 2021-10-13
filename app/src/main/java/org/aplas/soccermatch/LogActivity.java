package org.aplas.soccermatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LogActivity extends AppCompatActivity {

    private RecyclerView logView;
    private Button restartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        logView = (RecyclerView) findViewById(R.id.logRcView);
        restartBtn = (Button) findViewById(R.id.restartBtn);

        logView.setLayoutManager(new LinearLayoutManager(this));
        logView.setItemAnimator(new DefaultItemAnimator());
        logView.setHasFixedSize(false);

        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
            }
        });
    }

    private void loadHeader() {
        TextView matchResultTxt = (TextView) findViewById(R.id.matchResultTxt);
        TextView matchScoreTxt = (TextView) findViewById(R.id.matchScoreTxt);
        matchResultTxt.setText("Result: " + getIntent().getStringExtra("MATCH_RESULT"));
        matchScoreTxt.setText("Score: " + getIntent().getStringExtra("MATCH_SCORE"));
    }

    private void loadLogData() {
        ArrayList<LogItem> itemList = new ArrayList<>();
        LogAdapter itemArrayAdapter = new LogAdapter(R.layout.layout_log, itemList);
        itemArrayAdapter.setHasStableIds(true);
        logView.setAdapter(itemArrayAdapter);
        ArrayList<String> eventList = getIntent().getStringArrayListExtra("MATCH_EVENT");
        for (int i = 0; i < eventList.size(); i++) {
            String[] data = eventList.get(i).split("@");
            String eventName = data[0];
            String eventTime = data[3];
            String eventPlayer = data[1] + " (" + data[2] + ")";
            itemList.add(new LogItem(eventName, eventTime, eventPlayer));
        }
        Toast.makeText(LogActivity.this, "Data loaded", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadHeader();
        loadLogData();
    }

    private void openMainActivity() {
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        main.putExtra("MESSAGE", "new");
        startActivity(main);
    }
}