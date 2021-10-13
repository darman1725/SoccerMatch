package org.aplas.soccermatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {

    private ImageButton homeScoreBtn, homeYellowBtn, homeRedBtn, awayScoreBtn, awayYellowBtn, awayRedBtn;
    private Button startMatch, finishMatch;
    private ImageView homeLogo, awayLogo;
    private TextView timerTxt, homeTeamTxt, awayTeamTxt, homeScoreTxt, awayScoreTxt;
    private ArrayList<String> eventList = new ArrayList<>();
    private ArrayList<String> homePlayer, awayPlayer;
    final String separator = "@";
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    int Hours, Seconds, Minutes;
    Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        homeScoreBtn = (ImageButton) findViewById(R.id.addHomeScore);
        awayScoreBtn = (ImageButton) findViewById(R.id.addAwayScore);
        homeYellowBtn = (ImageButton) findViewById(R.id.addHomeYellow);
        awayYellowBtn = (ImageButton) findViewById(R.id.addAwayYellow);
        homeRedBtn = (ImageButton) findViewById(R.id.addHomeRed);
        awayRedBtn = (ImageButton) findViewById(R.id.addAwayRed);
        startMatch = (Button) findViewById(R.id.matchStartBtn);
        finishMatch = (Button) findViewById(R.id.matchFinishBtn);
        homeLogo = (ImageView) findViewById(R.id.homeLogo);
        awayLogo = (ImageView) findViewById(R.id.awayLogo);
        timerTxt = (TextView) findViewById(R.id.timerShow);
        homeTeamTxt = (TextView) findViewById(R.id.homeTeam);
        awayTeamTxt = (TextView) findViewById(R.id.awayTeam);
        homeScoreTxt = (TextView) findViewById(R.id.homeScore);
        awayScoreTxt = (TextView) findViewById(R.id.awayScore);

        handler = new Handler();


    }
}