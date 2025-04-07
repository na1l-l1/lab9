package com.example.lab09;

import android.content.BroadcastReceiver;
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

    private void initAnimations() {
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_press);
    }

    private void setupButtonAnimations() {
        buttonAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainHandler.postDelayed(() -> {
                    startButton.clearAnimation();
                    stopButton.clearAnimation();
                }, 50);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Do nothing
            }
        });
    }

    private void restoreState(Bundle savedInstanceState) {
        isServiceRunning = savedInstanceState.getBoolean(KEY_IS_SERVICE_RUNNING, false);
        String lastChar = savedInstanceState.getString(KEY_LAST_CHARACTER, "");

        if (!lastChar.isEmpty()) {
            randomCharacterTextView.setText(lastChar);
        }

        if (isServiceRunning) {
            startService(serviceIntent);
            updateStatusView(true);
        }
    }

    private void updateStatusView(boolean isRunning) {
        mainHandler.post(() -> {
            statusTextView.setText(isRunning ? "Статус: работает" : "Статус: остановлен");
            statusTextView.setTextColor(Color.parseColor(isRunning ? "#4CAF50" : "#F44336"));
            startButton.setEnabled(!isRunning);
            stopButton.setEnabled(isRunning);
        });
    }

    private void updateCharacterWithAnimation(String character) {
        Log.d(TAG, "Обновление символа: " + character);
        mainHandler.post(() -> {
            randomCharacterTextView.setText(character);
            randomCharacterTextView.startAnimation(fadeInAnimation);
            Log.d(TAG, "Текст после обновления: " + randomCharacterTextView.getText());
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_SERVICE_RUNNING, isServiceRunning);
        outState.putString(KEY_LAST_CHARACTER, randomCharacterTextView.getText().toString());
    }

    public void onClick(View view) {
        view.startAnimation(buttonAnimation);

        int viewId = view.getId();
        if (viewId == R.id.button_start) {
            Log.d(TAG, "Нажата кнопка СТАРТ");
            startService(serviceIntent);
            isServiceRunning = true;
            updateStatusView(true);
        } else if (viewId == R.id.button_end) {
            Log.d(TAG, "Нажата кнопка СТОП");
            stopService(serviceIntent);
            randomCharacterTextView.setText("?");
            isServiceRunning = false;
            updateStatusView(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(RandomCharacterService.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
        updateStatusView(isServiceRunning);
        Log.d(TAG, "BroadcastReceiver зарегистрирован");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        Log.d(TAG, "BroadcastReceiver отменен");
    }
}