package dk.it.on.galgeleg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;

public class win_activity extends AppCompatActivity {

    Integer number_of_mistakes;
    String hemmeligeOrd;
    TextView win_textView;
    Button playagain;
    Button HighScores;

    String filename = "game_history.txt";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        //Get intent and parse values
        Intent intent = getIntent();
        number_of_mistakes = intent.getIntExtra(MainActivity.EXTRA_ATTEMPTS, 0);
        hemmeligeOrd = intent.getStringExtra(MainActivity.EXTRA_SECRETWORD);

        win_textView = (TextView)findViewById(R.id.win_textView);
        playagain = (Button)findViewById(R.id.playagain_button);
        HighScores = (Button)findViewById(R.id.highScores2);

        win_textView.setText("Tillyke, du vandt! Det korrekte ord var: \"" + hemmeligeOrd + "\". Du brugte " + number_of_mistakes + " fejl.");

        //Save history to local storage
        try {
            SaveGameHistory();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Add listener and code to button click
        playagain.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        //Add listener and code to button click
        HighScores.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                go_to_HighScore_activity();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void SaveGameHistory() throws IOException {
        FileOutputStream fOut = openFileOutput(filename, MODE_APPEND);
        OutputStreamWriter osw = new OutputStreamWriter(fOut);

        // Write the string to the file
        osw.append("Dato: " + java.time.LocalDate.now()+ " Antal fejl: "+number_of_mistakes+" Ordlængde: "+hemmeligeOrd.length()+"\n");

        /* ensure that everything is
         * really written out and close */
        osw.flush();
        osw.close();
    }

    public void go_to_HighScore_activity(){
        Intent intent = new Intent(this, HighScore.class);
        startActivity(intent);
    }
}
