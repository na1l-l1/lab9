package com.example.lab09;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_IS_SERVICE_RUNNING = "is_service_running";
    private static final String KEY_LAST_CHARACTER = "last_character";

    private TextView randomCharacterTextView;
    private TextView statusTextView;
    private Button startButton;
    private Button stopButton;
}