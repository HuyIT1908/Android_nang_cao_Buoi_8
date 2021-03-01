package com.example.buoi_8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Bai2Activity extends AppCompatActivity {
    TextView startTimeField, endTimeField, tv_title;
    MediaPlayer mediaPlayer;
    double startTime = 0;
    double finaltTime = 0;
    Handler myHandler = new Handler();
    int forwardTime = 5000;
    int backwardTime = 5000;
    SeekBar seekBar;
    ImageButton playBtn, pauseBtn, stopBtn, rePlayBtn;
    public static int onTimeOnLy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai2);

        chay(R.raw.bhdn, "Bông hoa đẹp nhất");
        change_seekbar();
    }

    public void chay(Integer mp3, String title) {
        tv_title = findViewById(R.id.tv_title);
        startTimeField = findViewById(R.id.tv_start_field);
        endTimeField = findViewById(R.id.tv_end_field);
        seekBar = findViewById(R.id.seekBar2);
        playBtn = findViewById(R.id.imageButton_play);
        pauseBtn = findViewById(R.id.imageButton_pause);
        stopBtn = findViewById(R.id.imageButton_stop);
        rePlayBtn = findViewById(R.id.imageButton_replay);
        seekBar.setClickable(false);
        pauseBtn.setEnabled(false);
        mediaPlayer = MediaPlayer.create(Bai2Activity.this, mp3);
        tv_title.setText(title);
    }

    public void play(View view) throws IOException {
        Toast.makeText(this, "playing sound", Toast.LENGTH_SHORT).show();
        mediaPlayer.start();
        finaltTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        if (onTimeOnLy == 0) {
            seekBar.setMax((int) finaltTime);
            onTimeOnLy = 1;
        }

        endTimeField.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finaltTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finaltTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finaltTime))
        ));

        startTimeField.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
        ));
        seekBar.setProgress((int) startTime);
        myHandler.postDelayed(UpdateSongTime, 10);
        pauseBtn.setEnabled(true);
        playBtn.setEnabled(false);
    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeField.setText(String.format(
                    "%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
            ));
            seekBar.setProgress((int) startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    public void pause(View view) {
        mediaPlayer.pause();
        playBtn.setEnabled(true);
        pauseBtn.setEnabled(false);
    }

    public void tua(View view) {
        int temp = (int) startTime;
        if ((temp + forwardTime) <= finaltTime) {
            startTime = startTime + forwardTime;
            mediaPlayer.seekTo((int) startTime);
        } else {
            Toast.makeText(this, "Cannot jump forward  5 seconds",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(Settings.ACTION_SOUND_SETTINGS));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void stop(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông Báo").setMessage("Chức năng chưa xây dựng");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void rePlay(View view) {
        mediaPlayer.pause();
        mediaPlayer.start();
    }

    public void back(View view) {
        mediaPlayer.stop();
        mediaPlayer.release();
        finish();
    }

    private void change_seekbar(){
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }
}