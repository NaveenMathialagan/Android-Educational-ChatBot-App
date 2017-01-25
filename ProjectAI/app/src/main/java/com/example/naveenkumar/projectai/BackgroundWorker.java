package com.example.naveenkumar.projectai;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

/**
 * Created by Naveen kumar on 17-08-2016.
 */
public class BackgroundWorker extends AsyncTask<String, Void, String> {
TextToSpeech t;
TextView res;
    public BackgroundWorker(TextToSpeech t, TextView res){

        this.t=t;
        this.res=res;
    }
    @Override
    protected String doInBackground(String... p) {
        //File fileExt = new File(getExternalFilesDir(null).getAbsolutePath() + "/bots");

       // final String path = getExternalFilesDir(null).getAbsolutePath();

        Bot bot = new Bot("subjects", p[1]);
        Chat chatSession = new Chat(bot);
        //String request =
        //String request = "What is your name?";
        String response = chatSession.multisentenceRespond(p[0]);

        Log.v("nhh", "response = " + response);
        return response;
    }



    @Override
    protected void onPostExecute(String response) {

        super.onPostExecute(response);
        res.setText(response.toString());
        t.speak(response, TextToSpeech.QUEUE_FLUSH, null);
    }
    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }
}
