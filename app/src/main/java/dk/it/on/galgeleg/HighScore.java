package dk.it.on.galgeleg;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HighScore extends AppCompatActivity {

    LinearLayout scroller;
    int id = 1;
    String filename = "game_history.txt";
    Button play_again;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        play_again = (Button)findViewById(R.id.play_again);
        scroller = findViewById(R.id.HighScoreScroller);
        String history = null;
        try {
            history = ReadGameHistory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] historyList = null;
        historyList = history.split("\n");
        for(int i = 0; i<historyList.length-1; i++) {
            int mistakes = Integer.parseInt(historyList[i].substring(29,30));
            System.out.println("HERE! "+historyList[i].substring(29,30)+ "   " + mistakes + "     " + historyList[i]);
            if(mistakes == 0)
                    createNew(historyList[i].substring(0, 17), historyList[i].substring(17), R.drawable.galge);
            if(mistakes == 1)
                    createNew(historyList[i].substring(0, 17), historyList[i].substring(17), R.drawable.forkert1);
            if(mistakes == 2)
                    createNew(historyList[i].substring(0, 17), historyList[i].substring(17), R.drawable.forkert2);
            if(mistakes == 3)
                    createNew(historyList[i].substring(0, 17), historyList[i].substring(17), R.drawable.forkert3);
            if(mistakes == 4)
                    createNew(historyList[i].substring(0, 17), historyList[i].substring(17), R.drawable.forkert4);
            if(mistakes == 5)
                    createNew(historyList[i].substring(0, 17), historyList[i].substring(17), R.drawable.forkert5);
            if(mistakes == 6)
                    createNew(historyList[i].substring(0, 17), historyList[i].substring(17), R.drawable.forkert6);

        }


        //Add listener and code to button click
        play_again.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });



    }

    public void createNew(String dato, String stats, int Drawable){
        CardView cv = new CardView(getApplicationContext());
        cv.setId(id++);
        LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,225);
        size.setMargins(0, 5, 0, 5);
        cv.setLayoutParams(size);
        cv.setRadius(15);
        ConstraintLayout cl = new ConstraintLayout(this);
        cv.addView(cl);
        //Add pic
        ImageView IVProfilePic = new ImageView(this);
        IVProfilePic.setId(id++);
        IVProfilePic.setImageResource(Drawable);
        //IVProfilePic.setAdjustViewBounds(true);
        IVProfilePic.setPadding(0,0,-50,0);
        IVProfilePic.getWidth();
        IVProfilePic.requestLayout();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
        //IVProfilePic.getLayoutParams().height = 20;
        cl.addView(IVProfilePic);
        //Add date and stats
        TextView TVName = new TextView(this);
        TVName.setId(id++);
        TVName.setText(dato+"\n"+stats);
        TVName.setTextSize(18);

        cl.addView(TVName);
        //Add distance
        cv.setPadding(0,100,0,100);
        //ReadMore

        ConstraintSet CS = new ConstraintSet();
        CS.clone(cl);
        //Pic
        CS.connect(IVProfilePic.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT,0);
        CS.connect(IVProfilePic.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
        //Name and Job
        CS.connect(TVName.getId(), ConstraintSet.LEFT, IVProfilePic.getId(), ConstraintSet.RIGHT,8);
        CS.connect(TVName.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
        CS.connect(TVName.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);



        CS.applyTo(cl);



        scroller.addView(cv, 0);
    }

    public String ReadGameHistory() throws IOException {
        FileInputStream fIn = openFileInput(filename);
        InputStreamReader isr = new InputStreamReader(fIn);

        /* Prepare a char-Array that will
         * hold the chars we read back in. */
        char[] inputBuffer = new char[1024];

        // Fill the Buffer with data from the file
        isr.read(inputBuffer);

        // Transform the chars to a String
        String readString = new String(inputBuffer);
        return readString;
    }
}
