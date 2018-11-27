package com.example.dam.ttspspaad;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private static final String TAG = "TextToSpeechDemo";
    private TextToSpeech mTts;
    private Button mAgainButton, btRecord;
    private EditText etTexto;
    private static final int CTE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mTts = new TextToSpeech(this, this);
        mAgainButton = (Button) findViewById(R.id.btSpeech);
        etTexto = findViewById(R.id.editText);
        mAgainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sayHello();
            }
        });
        btRecord = findViewById(R.id.btRecord);
        btRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-ES");
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora");
                i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
                startActivityForResult(i, CTE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CTE && resultCode == RESULT_OK){
            ArrayList<String> textos = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.v(TAG, textos.size() + "textos");
            if(textos.size()>0){
                etTexto.setText(textos.get(0));
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            //int result = mTts.setLanguage(Locale.JAPAN);
            Locale spanish = new Locale ("spa", "SPA");
            int result = mTts.setLanguage(spanish);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language is not available.");
            } else {
                mAgainButton.setEnabled(true);
                sayHello();
            }
        } else {
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }

    /*private static final Random RANDOM = new Random();
    private static final String[] HELLOS = {
            "Hola",
            "Saludos",
            "Adiós",
            "OOh, mukatte kuru no ka? Kono DIO ni chikazuite kuru no ka?",
            "Eso explica el pestazo"
    };*/

    private void sayHello() {
        /*int helloLength = HELLOS.length;
        String hello = HELLOS[RANDOM.nextInt(helloLength)];
        mTts.speak(hello, TextToSpeech.QUEUE_FLUSH,null);*/
        sayHello(etTexto.getText().toString());
    }

    private void sayHello(String s){
        mTts.speak(s,TextToSpeech.QUEUE_ADD,null);
    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }
}
