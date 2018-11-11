package dk.it.on.galgeleg;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_ATTEMPTS = "dk.it.on.myapplication2.EXTRA_ATTEMPTS"; //God practice når der skal sendes data mellem to acitivities vha. intent. Navnet som sendt data kan hentes med.
    public static final String EXTRA_SECRETWORD = "dk.it.on.myapplication2.EXTRA_SECRETWORD";


    //Visual components
    TextView secret_word_textView;
    EditText guess_editText;
    Button guess_button;
    ImageView galge_imageView;
    Button HighScores;

    //Initialize access to visual components
    EditText guess;
    TextView secret_word;

    //Logical components
    List<Integer> guessed_letters = new ArrayList<Integer>();
    String hemmeligeOrd = "";
    Integer letters_left;
    Integer number_of_mistakes = 0;

    //Network variables
    private RequestQueue myRequestQueue;
    private StringRequest myStringRequest;
    private JsonObjectRequest myObjectRequest;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * Jeg er godt klar over at AsyncTask ikke giver den helt store mening her, men jeg har lidt svært ved at få brugt det ordenligt. Grunden til dette er
        * at det "Volley"-bibliotek, som jeg bruger, også har noget Asynkron kode indeljret, f.eks. onResponse(), som fungerer lidt ligesom onPostExecute(). Dette betyder at der faktisk
        * bliver returneret fra sendGetRequestAndGetSecretWord() FØR "hemmeligeord" er opdateret i onResponse (linje 239). Derfor vil "onPostExecute" faktisk køre INDEN
        * sendGetRequestAndGetSecretWord() er færdig. Jeg har derfor svært ved at "blande" de to funktioner, da postExecute jo er nyttesløs i dette tilfælde, men jeg ville
        * gerne vise, at jeg kan finde ud af at bruge AsyncTask...
        * */
        //Test async task
        new AsyncTask<Void, Void, String>(){

            //Do something in background
            @Override
            protected String doInBackground(Void... voids) {
                sendGetRequestAndGetSecretWord("https://da.wikipedia.org/wiki/Skib");
                return hemmeligeOrd;
            }

            //Do something when background process closes (Gets return from 'doInBackground')
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                display_secret_word(hemmeligeOrd);
            }
        }.execute();




        //Initialize access to visual components
        guess = (EditText)findViewById(R.id.guess_editText);
        secret_word = (TextView)findViewById(R.id.secret_word_textView);
        guess_button = (Button)findViewById(R.id.guess_button);
        HighScores = (Button)findViewById(R.id.HighScores);


        //Add listener and code to button click
        guess_button.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                if(hemmeligeOrd.equals("")){
                    secret_word.setText("Henter ord. Vent venligst");
                    return;
                }
                String input = guess.getText().toString().toLowerCase();

                //If no input
                if(input.length() == 0) return;

                //If input of one letter
                else if(input.length() == 1) {
                    guess.setText("");

                    //If correct letter guess and has not been guessed before
                    if (hemmeligeOrd.contains(input)) {
                        if(guessed_letters.contains(input)) return;

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

    public void display_secret_word(String ord){
        if(hemmeligeOrd.equals("")){
            secret_word.setText("Henter ord. Vent venligst");
            return;
        }
        System.out.println("Denne først");
        hemmeligeOrd = ord;
        //Display number of letters
        String word_to_display = "";
        for(int i = 0; i<hemmeligeOrd.length(); i++){
            word_to_display += "_   ";
        }
        secret_word.setText(word_to_display);
        System.out.println("Hemmelige ord: " + hemmeligeOrd);
        letters_left = hemmeligeOrd.length();
    }


    /*
    *
    * Jeg ved godt det er en meget "rodet" måde at få et "tilfældigt ord på. Planen var at bruge et offentligt "Random word generator" REST-API. Jeg har bestilt en gratis
    * access-key, som man bruger til authroization af sit kald, men den tager op til 7 dage at få tilsendt, og jeg venter stadig på den. Jeg håber på at have modtaget
    * nøglen inden endelige version af projektet afleveres.
    *
    * */
    private void sendGetRequestAndGetSecretWord(String url) {
        myRequestQueue = Volley.newRequestQueue(this);
        //Method, URL, successListener, errorListener
            myStringRequest = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("HERE");
                            String unsorted_word_list = response;
                            String[] word_list = unsorted_word_list.split(" ");
                            ArrayList<String> sorted_word_list = new ArrayList<>();
                            for (int i = 0; i < word_list.length; i++) {
                                if (Pattern.matches("[a-zA-Z]+", word_list[i]) && word_list[i].length() > 5) {
                                    sorted_word_list.add(word_list[i]);
                                }
                            }
                            Random rand = new Random();
                            int n = rand.nextInt(sorted_word_list.size());
                            hemmeligeOrd = sorted_word_list.get(n);
                            display_secret_word(hemmeligeOrd);
                        }
                    },
                    new Response.ErrorListener() {
                        //If call is not successful
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error");
                            List<String> ordliste = new ArrayList<String>();
                            Random ordvælger = new Random();
                            //Add words to list of words
                             ordliste.add("giraf");
                             ordliste.add("mobiltelefon");
                             ordliste.add("barbermaskine");
                             ordliste.add("vingummibamser");
                             ordliste.add("hjemmeside");

                            //Select random word from list of words.
                            hemmeligeOrd = ordliste.get(ordvælger.nextInt(ordliste.size()));
                            display_secret_word(hemmeligeOrd);
                        }
                    }
            ); //End of 'new StringRequest' arguments

        myRequestQueue.add(myStringRequest);
    }
}
