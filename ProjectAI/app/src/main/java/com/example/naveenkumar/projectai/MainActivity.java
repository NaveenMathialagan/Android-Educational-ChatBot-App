package com.example.naveenkumar.projectai;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
public class MainActivity extends AppCompatActivity implements RecognitionListener {
    TextToSpeech t1;
    private TextView txt;
    private ToggleButton toggleButton,lib;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    MediaPlayer mediaPlayer;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private String spk="";
    TextView res;
    static int instruct=0;
    String url=null;
    private AssetFileDescriptor afd;
    boolean WIKI=true;
    ImageView imgres;
    public static String fURL = "https://en.wikipedia.org/w/api.php?format=xml&action=query&titles=";
    public static String lURL = "&prop=revisions&rvprop=content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File fileExt = new File(getExternalFilesDir(null).getAbsolutePath() + "/bots");


        if (!fileExt.exists()) {
            ZipFileExtraction extract = new ZipFileExtraction();

            try {
                extract.unZipIt(getAssets().open("bots.zip"), getExternalFilesDir(null).getAbsolutePath() + "/");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        res=(TextView)findViewById(R.id.response);

       // imgres=(ImageView)findViewById(R.id.imgres);
       // imgres.setImageResource(R.drawable.nothing);
        txt = (TextView) findViewById(R.id.txt);
        mediaPlayer=new MediaPlayer();
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton1);
        lib = (ToggleButton) findViewById(R.id.toggleButton2);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    Log.d("nav","On Iniit");
                    t1.setLanguage(Locale.US);
                    //t1.speak("Press button at top left corner to give input",TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        try {
            if (instruct==0){
                instruct=1;
            afd=getAssets().openFd("sample.mp3");
            mediaPlayer.reset();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            }
        }
        catch (Exception io){

        }
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    if (t1.isSpeaking()){
                        t1.stop();
                    }
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                    Thread t=Thread.currentThread();
                    t.interrupt();
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                    speech.startListening(recognizerIntent);
                } else {
                    progressBar.setIndeterminate(false);
                    progressBar.setVisibility(View.INVISIBLE);
                    speech.stopListening();
                }
            }
        });
        lib.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    WIKI=false;
                    Log.d("nav","Wiki false");
                    t1.stop();
                    t1.speak("A.I.M.L",TextToSpeech.QUEUE_FLUSH, null);
                } else {
                  WIKI=true;
                    Log.d("nav","Wiki true");
                    t1.stop();
                    t1.speak("Wikipedia",TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

       // new wiki(t1,res).fetchXML("https://en.wikipedia.org/w/api.php?format=xml&action=query&titles=data%20mining&prop=revisions&rvprop=content");
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (speech != null) {
            speech.destroy();
            //   Log.i(LOG_TAG, "destroy");
        }
        if (t1.isSpeaking()){
            t1.stop();
        }
    }
    @Override
    public void onBeginningOfSpeech() {
        // Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }
    @Override
    public void onBufferReceived(byte[] buffer) {

        // Log.i(LOG_TAG, "onBufferRece;ived: " + buffer);
    }
    @Override
    public void onEndOfSpeech() {
        //  Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
        toggleButton.setChecked(false);
    }
    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        //  Log.d(LOG_TAG, "FAILED " + errorMessage);
        txt.setText(errorMessage);
        t1.speak(errorMessage, TextToSpeech.QUEUE_FLUSH, null);
        toggleButton.setChecked(false);
    }
    @Override
    public void onEvent(int arg0, Bundle arg1) {
        // Log.i(LOG_TAG, "onEvent");
    }
    @Override
    public void onPartialResults(Bundle arg0) {
        //Log.i(LOG_TAG, "onPartialResults");
    }
    @Override
    public void onReadyForSpeech(Bundle arg0) {
        //Log.i(LOG_TAG, "onReadyForSpeech");
    }
    @Override
    public void onResults(Bundle results) {
        // Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        txt.setText(matches.get(0).toString());
        spk=matches.get(0).toString();
       Log.d("nav",spk);

        if (WIKI){
            spk= spk.replaceAll(" ", "%20");
            Log.d("nav",spk);
            url = fURL + spk + lURL;
            Log.d("nav",url);
            BGworkerWiki bw=new BGworkerWiki(t1,res);
            bw.execute(url);
        }else{
            if (spk.contains("+")){
                Log.d("nav","+ contains");
                try{
                    spk=spk.replace("+","");
                    spk=spk.trim();
                    String[] num=spk.split(",");
                    int ans=Integer.parseInt(num[0].trim())+Integer.parseInt(num[1].trim());
                    res.setText(ans+"");
                    t1.speak(ans+"",TextToSpeech.QUEUE_FLUSH, null);
                }

                catch (Exception e){
                    Log.d("nav",e+"");
                 }
            }else if (spk.contains("-")){
                Log.d("nav","- contains");
                try{
                    spk=spk.replace("-","");
                    spk=spk.trim();
                    String[] num=spk.split(",");
                    int ans=Integer.parseInt(num[0].trim())-Integer.parseInt(num[1].trim());
                    res.setText(ans+"");
                    t1.speak(ans+"",TextToSpeech.QUEUE_FLUSH, null);
                }
                catch (Exception e){
                    Log.d("nav",e+"");
                }

            }else if (spk.contains("multiply")){
                Log.d("nav","multiply contains");
                try{
                    spk=spk.replace("multiply","");
                    spk=spk.trim();
                    String[] num=spk.split("with");
                    int ans=Integer.parseInt(num[0].trim())*Integer.parseInt(num[1].trim());
                    res.setText(ans+"");
                    t1.speak(ans+"",TextToSpeech.QUEUE_FLUSH, null);
                }
                catch (Exception e){
                    Log.d("nav",e+"");
                }

            }else if (spk.contains("divide"))
            {
                Log.d("nav","divide contains");
                try{
                    spk=spk.replace("divide","");
                    spk=spk.trim();
                    String[] num=spk.split("by");
                    int ans=Integer.parseInt(num[0].trim())/Integer.parseInt(num[1].trim());
                    res.setText(ans+"");
                    t1.speak(ans+"",TextToSpeech.QUEUE_FLUSH, null);
                }
                catch (Exception e){
                    Log.d("nav",e+"");
                }
            }else if (spk.contains("power")){
             try{
                String[] num=spk.split("power");
                int ans= (int) Math.pow((double) Integer.parseInt(num[0].trim()),(double) Integer.parseInt(num[1].trim()));
                res.setText(ans+"");
                t1.speak(ans+"",TextToSpeech.QUEUE_FLUSH, null);
            }
            catch (Exception e){
                Log.d("nav",e+"");
            }
            }
            else {
                final String path = getExternalFilesDir(null).getAbsolutePath();
                BackgroundWorker bw = new BackgroundWorker(t1, res);
                bw.execute(spk, path);
            }
        }
    //    new wiki(t1,res).fetchXML(url);

        Log.d("nav","after wiki in main activity");
        //wiki w=new wiki();


    }
    @Override
    public void onRmsChanged(float rmsdB) {
        // Log.d(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }


    public void call(String qry)
    {
        qry=qry.replaceAll(" ", "%20");
        url = fURL + qry + lURL;
        Log.d("nav","call function called");
        Log.d("nav",qry);
        BGworkerWiki bw=new BGworkerWiki();
        bw.execute(url);
    }
 /*@Override
 protected void onStop(){
     super.onStop();
     if (t1.isSpeaking()){
         t1.stop();
     }
 }*/

}
