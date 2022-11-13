package com.example.bursttheballon;
import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private TextView textViewInfo,textViewMyScore,textViewHighestScore;
    private Button buttonPlayAgain,buttonQuitGame;


    int myScore; //Grab the score from the game

    SharedPreferences sharedPreferences; //record our score using Shared Preferences class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textViewHighestScore = findViewById(R.id.textViewHighest);
        textViewMyScore = findViewById(R.id.textViewMyScore);
        textViewInfo = findViewById(R.id.textViewInfo);
        buttonPlayAgain = findViewById(R.id.buttonPlayAgain);
        buttonQuitGame = findViewById(R.id.buttonQuitGame);

        myScore = getIntent().getIntExtra("score",0);  //default value of score is 0
        //Also print score on the screen!!
        textViewMyScore.setText("Your Score : "+myScore);

        sharedPreferences = this.getSharedPreferences("Score", Context.MODE_PRIVATE);
        int highestScore = sharedPreferences.getInt("highestScore",0); //Since game is played first time so highest score is 0
        //HighestScore is the name we have given to whatever is being saved in the SharedPreferences

        if (myScore >= highestScore)
        {
            sharedPreferences.edit().putInt("highestScore",myScore).apply(); //saves new Highscore in database
            textViewHighestScore.setText("Highest Score : "+myScore);
            textViewInfo.setText("Congratulations. The new high score. Do you want to get better scores?");
        }
        else
        {
            textViewHighestScore.setText("Highest Score : "+highestScore);
            if ((highestScore - myScore) > 10)
            {
                textViewInfo.setText("You must get a little faster!");
            }
            if ((highestScore - myScore) > 3 && (highestScore - myScore) <= 10)
            {
                textViewInfo.setText("Good. How about getting a little faster?");
            }
            if ((highestScore - myScore) <= 3)
            {
                textViewInfo.setText("Excellent. If you get a little faster, you can reach the high score.");
            }
        }

        buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ResultActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        buttonQuitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //To exit the game
                //The following is the standard code to exit the game
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);

            }
        });
    }
}
