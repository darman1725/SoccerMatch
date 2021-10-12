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

        homeImage = (ImageButton) findViewById(R.id.homeImage);
        awayImage = (ImageButton) findViewById(R.id.awayImage);
        addHomePlayer = (ImageButton) findViewById(R.id.addHomePlayer);
        addAwayPlayer = (ImageButton) findViewById(R.id.addAwayPlayer);
        startBtn = (Button) findViewById(R.id.startBtn);
        homeTeam = (EditText) findViewById(R.id.homeTeam);
        awayTeam = (EditText) findViewById(R.id.awayTeam);
        homePlayerNumber = (TextView) findViewById(R.id.homePlayerNumber);
        awayPlayerNumber = (TextView) findViewById(R.id.awayPlayerNumber);
        homePlayerList = (ListView) findViewById(R.id.homePlayer);
        awayPlayerList = (ListView) findViewById(R.id.awayPlayer);

        homeDialog = getInputPlayerDialog(homePlayer, homePlayerList, homePlayerNumber);
        awayDialog = getInputPlayerDialog(awayPlayer, awayPlayerList, awayPlayerNumber);

        addHomePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeDialog.show();
            }
        });
        addAwayPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awayDialog.show();
            }
        });

        ArrayList<String> temp = new ArrayList<>();
        temp.add("add 11 players");
        loadListView(temp, homePlayerList);
        loadListView(temp, awayPlayerList);

        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_HOME_IMG);
            }
        });
        awayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_AWAY_IMG);
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormComplete()) {
                    openPlayActivity();
                }
            }
        });
    }

    private AlertDialog getInputPlayerDialog(ArrayList<String> list, ListView listView, TextView playerNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setMessage("Input Player Name:");
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(layout);
        EditText playerName = (EditText) layout.findViewById(R.id.playerName);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = playerName.getText().toString();
                list.add(name);
                loadListView(list, listView);
                playerNumber.setText(list.size() + " player(s)");
                playerName.setText("");
            }
        });
        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }

    private void loadListView(ArrayList<String> list, ListView listView) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.layout_list, R.id.listItem, list);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                if (requestCode == RESULT_HOME_IMG) {
                    homeImage.setImageBitmap(selectedImage);
                    homeImgPath = imageUri.toString();
                } else if (requestCode == RESULT_AWAY_IMG) {
                    awayImage.setImageBitmap(selectedImage);
                    awayImgPath = imageUri.toString();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isFormComplete() {
        if (homeTeam.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Home team name is still empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (awayTeam.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Away team name is still empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (homeImgPath.isEmpty()) {
            Toast.makeText(MainActivity.this, "Home team logo is still empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (awayImgPath.isEmpty()) {
            Toast.makeText(MainActivity.this, "Away team logo is still empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (homePlayer.size() < 11) {
            Toast.makeText(MainActivity.this, "Home player list is not complete", Toast.LENGTH_LONG).show();
            return false;
        }
        if (awayPlayer.size() < 11) {
            Toast.makeText(MainActivity.this, "Away player list is not complete", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void openPlayActivity() {
        Intent play = new Intent(getApplicationContext(), PlayActivity.class);
        play.putExtra("HOME_TEAM_NAME", homeTeam.getText().toString());
        play.putExtra("AWAY_TEAM_NAME", awayTeam.getText().toString());
        play.putExtra("HOME_IMG_URI", homeImgPath);
        play.putExtra("AWAY_IMG_URI", awayImgPath);
        play.putStringArrayListExtra("HOME_TEAM_PLAYER", homePlayer);
        play.putStringArrayListExtra("AWAY_TEAM_PLAYER", awayPlayer);
        startActivity(play);
    }
}