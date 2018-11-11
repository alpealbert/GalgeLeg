package dk.it.on.galgeleg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class lose_activity extends AppCompatActivity {

    String hemmeligeOrd;
    String filename = "game_history.txt";
    Button HighScores;
    Button playagain;
    TextView lose_textView;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose);

        //Get intent and parse values
        Intent intent = getIntent();
        hemmeligeOrd = intent.getStringExtra(MainActivity.EXTRA_SECRETWORD);

        playagain = (Button)findViewById(R.id.playagain_button);
        lose_textView = (TextView)findViewById(R.id.lose_textView);
        HighScores = (Button)findViewById(R.id.highScores3);

        lose_textView.setText("Beklager, du tabte! Det hemmelige ord var: \"" + hemmeligeOrd + "\".");

        //Save stats to local game history
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
        osw.append("Dato: " + java.time.LocalDate.now()+ " Antal fejl: "+6+" Ordlængde: "+hemmeligeOrd.length()+"\n");

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
