package dk.it.on.galgeleg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class lose_activity extends AppCompatActivity {

    String hemmeligeOrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose);

        //Get intent and parse values
        Intent intent = getIntent();
        hemmeligeOrd = intent.getStringExtra(MainActivity.EXTRA_SECRETWORD);

        Button playagain = (Button)findViewById(R.id.playagain_button);
        final TextView lose_textView = (TextView)findViewById(R.id.lose_textView);

        lose_textView.setText("Beklager, du tabte! Det hemmelige ord var " + hemmeligeOrd + ".");
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
