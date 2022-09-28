package com.linex.mediate;

import static com.linex.mediate.R.*;
import static com.linex.mediate.R.layout.activity_main;
import static com.linex.mediate.R.raw.boul_song;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    EditText etSetTime;
    TextView tvShowTime;
    ImageView imgView;
    Button btnStartAction, btnStopAction;
    MediaPlayer mpNight1;
    CountDownTimer countDownTimer;
    int setTime ;

    int defTimeOut = 0;






    @SuppressLint({"MissingInflatedId", "ResourceType", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle(" Meditation Mode");

        etSetTime = (EditText) findViewById(id.editTextTime2);
        tvShowTime = (TextView) findViewById(id.textViewTime);
        imgView = (ImageView) findViewById(id.imageView);
        btnStartAction = (Button) findViewById(id.btnStartActiont);
        btnStopAction = (Button) findViewById(id.btnStopAction);


        mpNight1 = MediaPlayer.create(this, boul_song);


        setDefTimesOut();

        btnStartAction.setOnClickListener(this::onStartAction);
        btnStopAction.setOnClickListener(this::onStopAction);




    }


    @SuppressLint({"ResourceType", "SetTextI18n"})
    public void onStartAction(View view) {

        try {
           setTime = Integer.parseInt(etSetTime.getText().toString());
            countDownTimer =  new CountDownTimer(setTime * 60000L, 1000) {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTick(long l) {
                    NumberFormat f = new DecimalFormat("00");
                    long hour = (l / 3600000) % 24;
                    long min = (l / 60000) % 60;
                    long sec = (l / 1000) % 60;
                    tvShowTime.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                    etSetTime.setText( setTime  + " Min");
                    mpNight1.start();
                    Glide.with(getApplicationContext()).load(raw.bowl).into(imgView);
                }

                @SuppressLint({"SetTextI18n", "ResourceType"})
                @Override
                public void onFinish() {
                    tvShowTime.setText("00:00:00");
                    etSetTime.setText("");
                    Glide.with(getApplicationContext()).load(R.layout.activity_main).into(imgView).onStop();
                    mpNight1.stop();
                    Toast.makeText(MainActivity.this, "Finish", Toast.LENGTH_SHORT).show();
                }
            };
            countDownTimer.start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Set Time", Toast.LENGTH_SHORT).show();
        }



}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == id.exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected  void  setDefTimesOut(){
        try{
            defTimeOut = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, setTime);
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, setTime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, defTimeOut);
    }


    @SuppressLint({"ResourceType", "SetTextI18n"})
    public void onStopAction(View view) {
        countDownTimer.cancel();
        tvShowTime.setText("00:00:00");
        etSetTime.setText("");
        mpNight1.stop();
        Glide.with(getApplicationContext()).load(R.layout.activity_main).into(imgView).onStop();
        Toast.makeText(MainActivity.this, "Finish", Toast.LENGTH_SHORT).show();
    }
}

