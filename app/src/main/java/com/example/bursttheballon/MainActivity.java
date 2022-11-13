package com.example.bursttheballon;
import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.gridlayout.widget.GridLayout;

        import android.content.Intent;
        import android.media.MediaPlayer;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.os.Handler;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTime,textViewCountDown,textViewScore;
    private ImageView balloon1,balloon2,balloon3,balloon4,balloon5,balloon6,balloon7,balloon8,balloon9;
    private GridLayout gridLayout;
    int score = 0;

    Runnable runnable;
    Handler handler;

    ImageView[] balloonsArray; //Transfer all balloons in an Array of ImageView

    MediaPlayer mediaPlayer;

    boolean status = false; //Initially the sound is closed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTime = findViewById(R.id.textViewTime);
        textViewCountDown = findViewById(R.id.textViewCountDown);
        textViewScore = findViewById(R.id.textViewScore);

        balloon1 = findViewById(R.id.balloon1);
        balloon2 = findViewById(R.id.balloon2);
        balloon3 = findViewById(R.id.balloon3);
        balloon4 = findViewById(R.id.balloon4);
        balloon5 = findViewById(R.id.balloon5);
        balloon6 = findViewById(R.id.balloon6);
        balloon7 = findViewById(R.id.balloon7);
        balloon8 = findViewById(R.id.balloon8);
        balloon9 = findViewById(R.id.balloon9);
        gridLayout = findViewById(R.id.gridLayout);

        //transfer audio file to media player object
        mediaPlayer = MediaPlayer.create(this,R.raw.balloon_sound);


        balloonsArray = new ImageView[]{balloon1,balloon2,balloon3,balloon4,balloon5,balloon6,balloon7,balloon8,balloon9};

        //countDownTimer() has two parameters: total time and after how much time the total time should decrease
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Tell what needs to be done as the time decreases
                //millisUntilFinished tells us the decrease time
                //display in the center textview

                textViewCountDown.setText(String.valueOf(millisUntilFinished/1000));

            }

            @Override
            public void onFinish() {

                //What needs to be done when  time runs out
                //When time finishes the timer should disappear
                balloonsControl();

                new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                        textViewTime.setText("Remaining Time : "+millisUntilFinished/1000);

                    }

                    @Override
                    public void onFinish() {

                        //When time is up we want the Result screen to open!!
                        Intent intent = new Intent(MainActivity.this,ResultActivity.class);
                        intent.putExtra("score",score);
                        startActivity(intent);
                        finish();

                    }
                }.start();

            }
        }.start(); //Always use this start() in order for onTick() and onFinish() methods to work
    }

    public void increaseScoreByOne(View view){ //View in parameter represents an image view component
        //When balloon is clicked the score increases by 1
        score = score + 1;
        textViewScore.setText("Score :"+score);

        //if audio file is playing
        if(mediaPlayer.isPlaying() == true){
            mediaPlayer.seekTo(0);
            mediaPlayer.start(); //Start the file again if he pops tbe balloon
        }
        mediaPlayer.start();
        //We also need to show the balloon pop pic when someone taps on balloon

        //Check which balloon is tapped by grabbing its id
        for(int i=0;i<balloonsArray.length;i++){
            if(view.getId() == balloonsArray[i].getId())
            {
                balloonsArray[i].setImageResource(R.drawable.boom);
            }
        }


    }

    public void balloonsControl()
    {
        textViewCountDown.setVisibility(View.INVISIBLE);
        textViewTime.setVisibility(View.VISIBLE);
        textViewScore.setVisibility(View.VISIBLE);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                for (ImageView balloon : balloonsArray)
                {
                    balloon.setVisibility(View.INVISIBLE);
                    balloon.setImageResource(R.drawable.balloon);
                }
                gridLayout.setVisibility(View.VISIBLE);

                Random random = new Random();
                int i = random.nextInt(balloonsArray.length);
                balloonsArray[i].setVisibility(View.VISIBLE);

                if (score <= 5)
                {
                    handler.postDelayed(runnable,1000);
                }
                else if (score > 5 && score <= 10)
                {
                    handler.postDelayed(runnable,850);
                }
                else if (score > 10 && score <= 15)
                {
                    handler.postDelayed(runnable,570);
                }

                else if (score > 15)
                {
                    handler.postDelayed(runnable,450);
                }

            }
        };

        handler.post(runnable);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.volume)
        {
            //if volume button is clicked!! (volume is it's ID)
            //If sound is open close it, if it is close so open it
            if (!status)
            {
                mediaPlayer.setVolume(0,0);
                item.setIcon(R.drawable.mute_icon);
                status = true;
            }
            else
            {
                mediaPlayer.setVolume(1,1);
                item.setIcon(R.drawable.sound_on);
                status = false;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}


