package dk.it.on.galgeleg;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HighScore extends AppCompatActivity {

    LinearLayout scroller;
    int id = 1;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        scroller = findViewById(R.id.HighScoreScroller);

        for(int i = 0; i<20; i++)
            createNew("Mathias", "Java",4.2, 250., R.drawable.forkert1);
    }

    public void createNew(String name, String job, double rank, Double pay, int Drawable){
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
        IVProfilePic.setPadding(0,0,350,0);
        IVProfilePic.getWidth();
        IVProfilePic.requestLayout();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
        //IVProfilePic.getLayoutParams().height = 20;
        cl.addView(IVProfilePic);
        //Add Name and Job
        TextView TVName = new TextView(this);
        TVName.setId(id++);
        TVName.setText(name+"\n"+job);
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



        scroller.addView(cv);
    }
}
