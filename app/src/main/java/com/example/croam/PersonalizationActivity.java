package com.example.croam;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class PersonalizationActivity extends AppCompatActivity implements
        VoiceView.OnRecordListener {

    private int count = 0;
    Runnable r;
    String TAG = "personalization";
    ProgressBar pb1, pb2, pb3, pb4, pb5;
    ImageView im1, im2, im3, im4, im5;
    //    private TextView mTextView;
    private VoiceView mVoiceView;
    private MediaRecorder mMediaRecorder;
    private Handler mHandler;
    private boolean mIsRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalization);

//        mTextView = (TextView) findViewById(R.id.text);
        mVoiceView = (VoiceView) findViewById(R.id.voiceview);
        mVoiceView.setOnRecordListener(this);
        pb1 = findViewById(R.id.progress_circular);
        pb2 = findViewById(R.id.progress_circular2);
        pb3 = findViewById(R.id.progress_circular3);
        pb4 = findViewById(R.id.progress_circular4);
        pb5 = findViewById(R.id.progress_circular5);
        im1 = findViewById(R.id.tick1);
        im2 = findViewById(R.id.tick2);
        im3 = findViewById(R.id.tick3);
        im4 = findViewById(R.id.tick4);
        im5 = findViewById(R.id.tick5);
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onRecordStart() {
        Log.d(TAG, "onRecordStart");
        try {
            ++count;
            if (count == 1) {
                pb1.setVisibility(View.VISIBLE);
                im1.setVisibility(View.GONE);
            }
            if (count == 2) {
                pb2.setVisibility(View.VISIBLE);
                im2.setVisibility(View.GONE);
            }
            if (count == 3) {
                pb3.setVisibility(View.VISIBLE);
                im3.setVisibility(View.GONE);
            }
            if (count == 4) {
                pb4.setVisibility(View.VISIBLE);
                im4.setVisibility(View.GONE);
            }
            if (count == 5) {
                pb5.setVisibility(View.VISIBLE);
                im5.setVisibility(View.GONE);
            }
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setOutputFile(new File(Environment.getExternalStorageDirectory(),
                    "audio" + count + ".amr").getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            mIsRecording = true;
            r = new Runnable() {
                @Override
                public void run() {
                    float radius = (float) Math.log10(
                            Math.max(1, mMediaRecorder.getMaxAmplitude() - 500))
                            * ScreenUtils.dp2px(PersonalizationActivity.this, 20);
//                    mTextView.setText(String.valueOf(radius));
                    mVoiceView.animateRadius(radius);
                    if (mIsRecording) {
                        mHandler.postDelayed(this, 50);
                    }
                }
            };
            mHandler.post(r);
        } catch (IOException e) {
            Toast.makeText(this, "MediaRecorder prepare failed!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRecordFinish() {
        Log.d(TAG, "onRecordFinish");
        mIsRecording = false;
        mMediaRecorder.stop();
        if (count == 1) {
            pb1.setVisibility(View.GONE);
            im1.setVisibility(View.VISIBLE);
            im1.setColorFilter(R.color.switch_on);
        }
        if (count == 2) {
            pb2.setVisibility(View.GONE);
            im2.setVisibility(View.VISIBLE);
            im2.setColorFilter(R.color.switch_on);
        }
        if (count == 3) {
            pb3.setVisibility(View.GONE);
            im3.setVisibility(View.VISIBLE);
            im3.setColorFilter(R.color.switch_on);
        }
        if (count == 4) {
            pb4.setVisibility(View.GONE);
            im4.setVisibility(View.VISIBLE);
            im4.setColorFilter(R.color.switch_on);
        }
        if (count == 5) {
            pb5.setVisibility(View.GONE);
            im5.setVisibility(View.VISIBLE);
            im5.setColorFilter(R.color.switch_on);
        }
        if (count == 5) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onDestroy() {
        if (mIsRecording) {
            mMediaRecorder.stop();
            mIsRecording = false;
        }
        mMediaRecorder.release();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mHandler.removeCallbacks(r);
        super.onBackPressed();
    }
}