package com.example.buoi_8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Bai3Activity extends AppCompatActivity {
    ListView lv_list;
    List<MusicList> data = new ArrayList<>();

    TextView startTimeField, endTimeField, tv_title;
    MediaPlayer mediaPlayer;
    double startTime = 0;
    double finaltTime = 0;
    Handler myHandler = new Handler();
    int forwardTime = 5000;
    int backwardTime = 5000;
    SeekBar seekBar;
    ImageButton playBtn, pauseBtn, stopBtn, rePlayBtn, tuaBtn, backBtn;
    public static int onTimeOnLy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai3);

        lv_list = findViewById(R.id.lv_list);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Xin quyền truy cập ảnh trong máy
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    999);
        }
        get_music();
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.show_music, null);
                    show_music(
                            layout,
                            Uri.parse(String.valueOf(data.get(position).getFile())) ,
                            data.get(position).getTitle() );
                }catch (Exception e){
                    Log.e("----------------------" , e.toString());
                }
            }
        });
    }

    public void get_music() {

        String[] projection = {
                MediaStore.MediaColumns.DISPLAY_NAME,
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.DATA
        };
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            Uri mp3 = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            data.add(new MusicList(title , mp3 ));

            cursor.moveToNext();
        }
        cursor.close();
        ShowAdapter adapter = new ShowAdapter(Bai3Activity.this , data);
        lv_list.setAdapter(adapter);

    }

    public void show_music(View layout , Uri mp3, String title) {
        tv_title = layout.findViewById(R.id.tv_title_dl);
        startTimeField = layout.findViewById(R.id.tv_start_field_dl);
        endTimeField = layout.findViewById(R.id.tv_end_field_dl);
        seekBar = layout.findViewById(R.id.seekBar2_dl);
        playBtn = layout.findViewById(R.id.imageButton_play_dl);
        pauseBtn = layout.findViewById(R.id.imageButton_pause_dl);
        stopBtn = layout.findViewById(R.id.imageButton_stop_dl);
        rePlayBtn = layout.findViewById(R.id.imageButton_replay_dl);
        tuaBtn = layout.findViewById(R.id.imageButton2_tua_dl);
        backBtn = layout.findViewById(R.id.imageButton4_back_dl);
        seekBar.setClickable(false);
        pauseBtn.setEnabled(false);
        mediaPlayer = MediaPlayer.create(Bai3Activity.this, mp3);
        tv_title.setText(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(Bai3Activity.this);
        builder.setTitle("Play Music");
        builder.setView(layout);
//        playBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    play();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        pauseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pause();
//            }
//        });
//        tuaBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tua();
//            }
//        });
//        stopBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stop();
//            }
//        });
//        rePlayBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rePlay();
//            }
//        });
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                back();
//            }
//        });
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.stop();
                mediaPlayer.release();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        change_seekbar();
    }

    public void play_dl(View view) throws IOException {
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

    public void pause_dl(View view) {
        mediaPlayer.pause();
        playBtn.setEnabled(true);
        pauseBtn.setEnabled(false);
    }

    public void tua_dl(View view) {
        int temp = (int) startTime;
        if ((temp + forwardTime) <= finaltTime) {
            startTime = startTime + forwardTime;
            mediaPlayer.seekTo((int) startTime);
        } else {
            Toast.makeText(this, "Cannot jump forward  5 seconds",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public void stop_dl(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông Báo").setMessage("Chức năng chưa xây dựng");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void rePlay_dl(View view) {
        finaltTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        if (startTime == finaltTime){
            mediaPlayer.pause();
            mediaPlayer.start();
        }
    }

    public void back_dl(View view) {
        backBtn.setEnabled(false);
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        finish();
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