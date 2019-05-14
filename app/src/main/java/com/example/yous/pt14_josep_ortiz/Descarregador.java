package com.example.yous.pt14_josep_ortiz;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yous on 4/12/17.
 *
 * API key: 97ba95dc5cc02310406ff5e81ba291c7
 *
 */

public class Descarregador extends AsyncTask<String, Integer, Void> {

    private static final String API_KEY = "97ba95dc5cc02310406ff5e81ba291c7";

    private String xmlString;
    private boolean finished;
    private boolean error404;

    @Override
    protected Void doInBackground(String... strings) {

        finished = false;
        error404 = false;

        try {

            BufferedReader in = getDataFromAPI(strings[0]);

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            xmlString = response.toString();
            finished = true;

        } catch (IOException e) {
            e.printStackTrace();
            error404 = true;
        }

        return null;
    }

    public BufferedReader getDataFromAPI(String city) throws IOException {

        URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q="
                + city + "&mode=xml&appid=" + API_KEY);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setReadTimeout(5000);
        con.setDoOutput(true);
        con.setRequestMethod("GET");
        con.disconnect();

        return new BufferedReader(new InputStreamReader(con.getInputStream()));
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isError404() {
        return error404;
    }

    public String getXmlString(){
        return xmlString;
    }
}
