package com.example.lab09;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class RandomCharacterService extends Service {

    public static final String BROADCAST_ACTION = "my.custom.action.tag.lab9";
    public static final String EXTRA_CHAR = "randomCharacter";

    private static final String TAG = "RandomCharacterService";

    private final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private boolean isRandomGeneratorOn;
    private Thread workerThread;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Сервис создан");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Сервис запущен...");
        Toast.makeText(getApplicationContext(), "Сервис запущен", Toast.LENGTH_SHORT).show();

        isRandomGeneratorOn = true;

        workerThread = new Thread(this::startRandomGenerator);
        workerThread.start();

        Log.i(TAG, "ID потока в onStartCommand: " + Thread.currentThread().getId());
        return START_STICKY;
    }

    private void startRandomGenerator() {
        Log.d(TAG, "Генератор случайных букв запущен");

        Random random = new Random();

        while (isRandomGeneratorOn) {
            try {
                Thread.sleep(1000);

                if (!isRandomGeneratorOn) break;

                int randomIndex = random.nextInt(alphabet.length);
                char randomChar = alphabet[randomIndex];

                Log.i(TAG, "ID потока: " + Thread.currentThread().getId() + ", Случайная буква: " + randomChar);

                mainHandler.post(() -> sendCharacterBroadcast(randomChar));

            } catch (InterruptedException e) {
                Log.w(TAG, "Поток прерван", e);
                Thread.currentThread().interrupt(); // корректное завершение
                break;
            } catch (Exception e) {
                Log.e(TAG, "Ошибка при генерации случайной буквы", e);
            }
        }

        Log.d(TAG, "Генератор случайных букв остановлен");
    }
}