package dk.it.on.galgeleg;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_ATTEMPTS = "dk.it.on.myapplication2.EXTRA_ATTEMPTS"; //God practice når der skal sendes data mellem to acitivities vha. intent. Navnet som sendt data kan hentes med.
    public static final String EXTRA_SECRETWORD = "dk.it.on.myapplication2.EXTRA_SECRETWORD";


    //Visual components
    TextView secret_word_textView;
    EditText guess_editText;
    Button guess_button;
    ImageView galge_imageView;
    Button HighScores;

    //Logical components
    List<String> ordliste = new ArrayList<String>();
    List<Integer> guessed_letters = new ArrayList<Integer>();
    Random ordvælger = new Random();
    String hemmeligeOrd;
    Integer letters_left;
    Integer number_of_mistakes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Add words to list of words
        ordliste.add("giraf");
        ordliste.add("mobiltelefon");
        ordliste.add("barbermaskine");
        ordliste.add("vingummibamser");
        ordliste.add("hjemmeside");

        //Select random word from list of words.
        hemmeligeOrd = ordliste.get(ordvælger.nextInt(ordliste.size()));
        letters_left = hemmeligeOrd.length();

        //Initialize access to visual components
        final EditText guess = (EditText)findViewById(R.id.guess_editText);
        final TextView secret_word = (TextView)findViewById(R.id.secret_word_textView);
        guess_button = (Button)findViewById(R.id.guess_button);
        HighScores = (Button)findViewById(R.id.HighScores);


        //Display number of letters
        String word_to_display = "";
        for(int i = 0; i<hemmeligeOrd.length(); i++){
            word_to_display += "_   ";
        }
        secret_word.setText(word_to_display);


        //Add listener and code to button click
        guess_button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                String input = guess.getText().toString().toLowerCase();

                //If no input
                if(input.length() == 0) return;

                //If input of one letter
                else if(input.length() == 1) {
                    guess.setText("");

                    //If correct letter guess
                    if (hemmeligeOrd.contains(input)) {

                        //Find occurrences
                        int index = hemmeligeOrd.indexOf(input);
                        while (index >= 0) {
                            guessed_letters.add(index);
                            index = hemmeligeOrd.indexOf(input, index + 1);
                        }

                        //Display partly secret word
                        String new_word_to_display = "";
                        for (int i = 0; i < hemmeligeOrd.length(); i++) {
                            if (guessed_letters.contains(i))
                                new_word_to_display += hemmeligeOrd.substring(i,i+1) + "   ";
                            else
                                new_word_to_display += "_   ";
                        }
                        secret_word.setText(new_word_to_display);

                        //Check for winning guess
                        if(guessed_letters.size() == hemmeligeOrd.length()){
                            go_to_win_activity();
                        }
                    }
                    //If incorrect letter guess
                    else {
                       update_number_of_attempts();
                    }
                }
                //If input of entire word
                else if(input.length() > 1){
                    if(input.equals(hemmeligeOrd)) go_to_win_activity();
                    else update_number_of_attempts();
                }

            }
        });

        //Add listener and code to button click
        HighScores.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                go_to_HighScore_activity();
            }
        });


    }

    public void update_number_of_attempts(){
        number_of_mistakes++;
        ImageView galge = (ImageView)findViewById(R.id.galge_imageView);
        if(number_of_mistakes.equals(1)){
            galge.setImageResource(R.drawable.forkert1);
        }else if(number_of_mistakes.equals(2)){
            galge.setImageResource(R.drawable.forkert2);
        }else if(number_of_mistakes.equals(3)){
            galge.setImageResource(R.drawable.forkert3);
        }else if(number_of_mistakes.equals(4)){
            galge.setImageResource(R.drawable.forkert4);
        }else if(number_of_mistakes.equals(5)){
            galge.setImageResource(R.drawable.forkert5);
        }else{
            Intent intent = new Intent(this, lose_activity.class);
            intent.putExtra(EXTRA_SECRETWORD, hemmeligeOrd);
            startActivity(intent);
        }

    }
    public void go_to_win_activity(){
        Intent intent = new Intent(this, win_activity.class);
        intent.putExtra(EXTRA_ATTEMPTS, number_of_mistakes);
        intent.putExtra(EXTRA_SECRETWORD, hemmeligeOrd);
        startActivity(intent);
    }

    public void go_to_HighScore_activity(){
        Intent intent = new Intent(this, HighScore.class);
        startActivity(intent);
    }
}
