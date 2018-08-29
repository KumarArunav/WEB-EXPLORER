package com.example.hp.exploretheweb;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Browser1 extends AppCompatActivity implements View.OnClickListener {
    WebView wv;
    Button go,fwd,back,rel,clear,mic;
    EditText et;
    final int REQ_CODE_SPEECH_OUTPUT=143;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser1);
        wv = (WebView) findViewById(R.id.web1);
        go = (Button) findViewById(R.id.buttonGo);
        fwd = (Button) findViewById(R.id.button);
        back = (Button) findViewById(R.id.button1);
        rel = (Button) findViewById(R.id.button2);
        clear = (Button) findViewById(R.id.button3);
        et = (EditText) findViewById(R.id.edittext);
        mic = (Button) findViewById(R.id.buttonMic);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btntoopen();
            }
        });

        go.setOnClickListener(this);
        fwd.setOnClickListener(this);
        back.setOnClickListener(this);
        rel.setOnClickListener(this);
        clear.setOnClickListener(this);
        wv.setWebViewClient(new viewclient()); //for opening the url in the same website
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true); //for youtube access
    }
    private void btntoopen(){
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.EXTRA_LANGUAGE_MODEL);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hii speak now.....");
        try{
            startActivityForResult(intent,REQ_CODE_SPEECH_OUTPUT);
        }
        catch (ActivityNotFoundException tim){
            Toast.makeText(this,"Mic Not Opened",Toast.LENGTH_SHORT).show();
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_CODE_SPEECH_OUTPUT:{
                if(resultCode==RESULT_OK&& null!=data){
                    ArrayList<String> voiceInText =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    et.setText(voiceInText.get(0));
                }
                break;
            }

        }
    }




    @Override
    public void onClick(View view) {
        if(view==go){
            String editextvalue=et.getText().toString();
            if(!editextvalue.startsWith("http://"))
                editextvalue= "http://" + "www."+ editextvalue;
            String url=editextvalue;
            wv.loadUrl(url);
            //hide keyboard after go button pressed
            InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et.getWindowToken(),0);

        }
        if(view==fwd){
            if(wv.canGoForward())
                wv.goForward();
        }
        if(view==back){
            if(wv.canGoBack())
                wv.goBack();
        }
        if(view==rel){
            wv.reload();

        }
        if(view==clear)
            wv.clearHistory();



    }
}
