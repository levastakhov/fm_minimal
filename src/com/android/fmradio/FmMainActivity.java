package com.android.fmradio;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioSystem;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FmMainActivity extends Activity {
    private static final String TAG = "MinimalFmRadio";

    private static final int FOR_MEDIA = 1;
    private static final int FORCE_NONE = 0;
    private static final int FORCE_SPEAKER = 1;

    private EditText mFrequencyInput;
    private Button mPowerButton;
    private Button mSpeakerButton;
    private TextView mStatusText;

    private boolean mIsPowerOn = false;
    private boolean mIsSpeakerOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("Minimal FM Tuner");
        title.setTextSize(24);
        title.setPadding(0, 0, 0, 40);
        layout.addView(title);

        mFrequencyInput = new EditText(this);
        mFrequencyInput.setHint("Частота (например 102.1)");
        mFrequencyInput.setText("102.1");
        mFrequencyInput.setTextSize(20);
        layout.addView(mFrequencyInput);

        mPowerButton = new Button(this);
        mPowerButton.setText("Включить радио");
        mPowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePower();
            }
        });
        layout.addView(mPowerButton);

        mSpeakerButton = new Button(this);
        mSpeakerButton.setText("Переключить на Динамик");
        mSpeakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSpeaker();
            }
        });
        layout.addView(mSpeakerButton);

        mStatusText = new TextView(this);
        mStatusText.setText("Статус: Выключено");
        mStatusText.setPadding(0, 30, 0, 0);
        layout.addView(mStatusText);

        setContentView(layout);
    }

    private void togglePower() {
        if (!mIsPowerOn) {
            float freqMhz;
            try {
                freqMhz = Float.parseFloat(mFrequencyInput.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Введите корректную частоту!", Toast.LENGTH_SHORT).show();
                return;
            }

            int freqKhz = (int) (freqMhz * 1000);
            Log.d(TAG, "Включение FM: " + freqKhz + " kHz");

            AudioSystem.setForceUse(FOR_MEDIA, FORCE_NONE);

            mIsPowerOn = true;
            mPowerButton.setText("Выключить радио");
            mStatusText.setText("Статус: Играет " + freqMhz + " MHz");
        } else {
            Log.d(TAG, "Выключение FM");

            AudioSystem.setForceUse(FOR_MEDIA, FORCE_NONE);

            mIsPowerOn = false;
            mPowerButton.setText("Включить радио");
            mStatusText.setText("Статус: Выключено");
        }
    }

    private void toggleSpeaker() {
        if (!mIsSpeakerOn) {
            AudioSystem.setForceUse(FOR_MEDIA, FORCE_SPEAKER);
            mIsSpeakerOn = true;
            mSpeakerButton.setText("Переключить на Наушники");
        } else {
            AudioSystem.setForceUse(FOR_MEDIA, FORCE_NONE);
            mIsSpeakerOn = false;
            mSpeakerButton.setText("Переключить на Динамик");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsPowerOn) {
            AudioSystem.setForceUse(FOR_MEDIA, FORCE_NONE);
        }
    }
}
