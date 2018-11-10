package dk.it.on.galgeleg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class win_activity extends AppCompatActivity {

    Integer number_of_mistakes;
    String hemmeligeOrd;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        //Get intent and parse values
        Intent intent = getIntent();
        number_of_mistakes = intent.getIntExtra(MainActivity.EXTRA_ATTEMPTS, 0);
        hemmeligeOrd = intent.getStringExtra(MainActivity.EXTRA_SECRETWORD);

        final TextView win_textView = (TextView)findViewById(R.id.win_textView);
        Button playagain = (Button)findViewById(R.id.playagain_button);

        win_textView.setText("Tillyke, du vandt! Det korrekte ord var: " + hemmeligeOrd + ". Du brugte " + number_of_mistakes + " fejl.");

//Add listener and code to button click
        playagain.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }
}
