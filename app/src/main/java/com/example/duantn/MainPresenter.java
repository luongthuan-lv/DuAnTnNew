package com.example.duantn;

import android.util.Log;


import com.example.duantn.googlecloudtts.AudioConfig;
import com.example.duantn.googlecloudtts.EAudioEncoding;
import com.example.duantn.googlecloudtts.GoogleCloudAPIConfig;
import com.example.duantn.googlecloudtts.GoogleCloudVoice;
import com.example.duantn.googlecloudtts.VoiceCollection;
import com.example.duantn.googlecloudtts.VoiceList;
import com.example.duantn.modol.AndroidTTSAdapter;
import com.example.duantn.modol.GoogleCloudTTSAdapter;
import com.example.duantn.modol.SpeechManager;
import com.example.duantn.modol.android.AndroidVoice;

import java.util.Locale;

/**
 * Author: Changemyminds.
 * Date: 2019/6/23.
 * Description:
 * Reference:
 */
public class MainPresenter implements MainContract.IPresenter, VoiceList.IVoiceListener {
    private static final String TAG = "MainPresenter";

    private MainContract.IView mView;

    private GoogleCloudAPIConfig mApiConfig = new GoogleCloudAPIConfig("AIzaSyDri0QAoSoG-j8YBHMqAR-8rg4P3EuoU0I");

    private VoiceCollection mVoiceCollection;

    private SpeechManager mSpeechManager;
    private GoogleCloudTTSAdapter mGoogleCloudTTSAdapter;
    private String languageCode;
    private String voiceName;

    public MainPresenter(MainContract.IView view,String languageCode,String voiceName) {
        // ioc
        mView = view;
        mView.setPresenter(this);
        this.languageCode=languageCode;
        this.voiceName=voiceName;
    }


    @Override
    public void onCreate() {
        mSpeechManager = new SpeechManager();

        mGoogleCloudTTSAdapter = new GoogleCloudTTSAdapter(mApiConfig);
        mGoogleCloudTTSAdapter.addVoiceListener(this);
        loadGoogleCloudTTS();

        // set ConcreteDecorator
        mSpeechManager.setSpeech(mGoogleCloudTTSAdapter);
    }

    @Override
    public void onDestroy() {
        disposeSpeak();
    }

    private void initGoogleCloudTTSVoice() {
        float pitch = ((float) (mView.getProgressPitch() - 2000) / 100);
        float speakRate = ((float) (mView.getProgressSpeakRate() + 25) / 100);

        mGoogleCloudTTSAdapter.setGoogleCloudVoice(new GoogleCloudVoice(languageCode, voiceName))
                .setAudioConfig(new AudioConfig.Builder()
                        .addAudioEncoding(EAudioEncoding.MP3)
                        .addSpeakingRate(speakRate)
                        .addPitch(pitch)
                        .build()
                );
    }

    @Override
    public void onResponse(String jsonText) {
        Log.d(TAG, "Load voice json string " + jsonText);
    }

    @Override
    public void onReceive(VoiceCollection collection) {
        mVoiceCollection = collection;
        mView.invoke(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @Override
    public void onFailure(String error) {

    }


    public void startSpeak(String text) {
        mSpeechManager.stopSpeak();
        if (mVoiceCollection == null || mVoiceCollection.size() == 0) {
            return;
        }

        initGoogleCloudTTSVoice();
        mSpeechManager.startSpeak(text);
    }

    public void stopSpeak() {
        mSpeechManager.stopSpeak();
    }

    public void resumeSpeak() {
        mSpeechManager.resume();
    }

    public void pauseSpeak() {
        mSpeechManager.pause();
    }

    public void disposeSpeak() {
        mSpeechManager.dispose();
        mSpeechManager = null;
    }

    @Override
    public void loadGoogleCloudTTS() {
        mGoogleCloudTTSAdapter.loadVoiceList();
    }

    @Override
    public void initAndroidTTS() {
        AndroidTTSAdapter androidTTSAdapter = new AndroidTTSAdapter(mView.getContext());
        AndroidVoice androidVoice = new AndroidVoice.Builder()
                .addLanguage(Locale.ENGLISH)
                .addPitch(1.0f)
                .addSpeakingRate(1.0f)
                .build();
        androidTTSAdapter.setAndroidVoice(androidVoice);

        // set the next handler
        SpeechManager androidTTSManager = new SpeechManager();
        androidTTSManager.setSpeech(androidTTSAdapter);
        mSpeechManager.setSupervisor(androidTTSManager);
    }
}



