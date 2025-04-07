package com.example.lab09;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_IS_SERVICE_RUNNING = "is_service_running";
    private static final String KEY_LAST_CHARACTER = "last_character";

    private TextView randomCharacterTextView;
    private TextView statusTextView;
    private Button startButton;
    private Button stopButton;

    private Animation fadeInAnimation;
    private Animation buttonAnimation;

    private Intent serviceIntent;
    private BroadcastReceiver broadcastReceiver;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean isServiceRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initAnimations();
        setupButtonAnimations();

        broadcastReceiver = new CharacterReceiver();
        serviceIntent = new Intent(this, RandomCharacterService.class);

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }

        randomCharacterTextView.setText("?");
        stopButton.setEnabled(false);
    }

    private void initViews() {
        randomCharacterTextView = findViewById(R.id.editText_randomCharacter);
        statusTextView = findViewById(R.id.textView_status);
        startButton = findViewById(R.id.button_start);
        stopButton = findViewById(R.id.button_end);
    }

}