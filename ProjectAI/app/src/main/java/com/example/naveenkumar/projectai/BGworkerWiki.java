package com.example.naveenkumar.projectai;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Naveen kumar on 05-11-2016.
 */

public class BGworkerWiki extends AsyncTask<String, Void, String> {
    static TextToSpeech t;
    static TextView res;
    static String keyword="";
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;
    public String out = "";
    int event;
    String text="";
    boolean fnds=false;
    public BGworkerWiki(){

    }
    public BGworkerWiki(TextToSpeech t, TextView res){

        this.t=t;
        this.res=res;
    }
    @Override
    protected String doInBackground(String... p) {
        try {
            URL url = new URL(p[0]);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            InputStream stream = conn.getInputStream();
            xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(stream, null);
            try {
                event = myparser.getEventType();

                while (event != XmlPullParser.END_DOCUMENT) {
                    String name=myparser.getName();

                    switch (event){
                        case XmlPullParser.START_TAG:
                            if(name.equals("page")){

                                Log.d("nav","title found");
                                String key=myparser.getAttributeValue(null,"title");
                                Log.d("nav",key);
                                keyword=key;
                                //Log.d("nav1",text);

                            }
                            break;

                        case XmlPullParser.TEXT:
                            text = myparser.getText();
                            break;

                        case XmlPullParser.END_TAG:
                            if(name.equals("rev")){
                                out=new stringfunc().alter(text.toString(),keyword);
                                Log.d("nav",out.toString());
                                //    tv.setText(out.toString());
                                fnds=true;
                            }

                            break;
                    }
                    if (fnds==true){
                        break;
                    }else{
                        event = myparser.next();
                    }
                }
                parsingComplete = false;
            }
            catch (Exception e){
                Log.d("nav","Exception 2\t"+e);
            }
            stream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }
    @Override
    protected void onPostExecute(String response)
    {
        super.onPostExecute(response);
        if (response.isEmpty()){
            res.setText("Incorrect topic");
            t.speak("Incorrect topic", TextToSpeech.QUEUE_FLUSH, null);
        }else{
        if (response.contains("#REDIRECT")){
            Log.d("nav","Redirected");
            response=response.replace("#REDIRECT","");
            response=response.trim();
            keyword=response;
            new MainActivity().call(response);
        }else if( response.contains("#redirect")) {

            Log.d("nav","Redirected");
            response=response.replace("#redirect","");
            response=response.trim();
            keyword=response;
            new MainActivity().call(response);
        } else {
            //  Log.d("nav","reponse before setText\t"+response);

            String spch="";
            response = response.trim();
            response=response.replace("#"," ");
            int i = 0;
            String[] strary = response.split("\n");
            while (i< strary.length){
                if (spch.length()<800){
                    spch=spch+strary[i]+"\n";
                    i++;
                }else{
                    break;
                }

            }
            res.setText(spch.toString());
            t.speak(spch, TextToSpeech.QUEUE_FLUSH, null);
            Log.d("nav", spch.length()+"\t"+i);
        }

       }
    }
    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }
}


