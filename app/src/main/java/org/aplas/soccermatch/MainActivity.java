package org.aplas.soccermatch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private final int RESULT_HOME_IMG = 1;
    private final int RESULT_AWAY_IMG = 2;
    private final int EDITTEXT_ID = 900;
    private AlertDialog homeDialog, awayDialog;
    private ImageButton homeImage, awayImage, addHomePlayer, addAwayPlayer;
    private Button startBtn;
    private EditText homeTeam, awayTeam;
    private TextView homePlayerNumber, awayPlayerNumber;
    private ListView homePlayerList, awayPlayerList;
    private String homeImgPath = "";
    private String awayImgPath = "";
    private ArrayList<String> homePlayer = new ArrayList<>();
    private ArrayList<String> awayPlayer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}