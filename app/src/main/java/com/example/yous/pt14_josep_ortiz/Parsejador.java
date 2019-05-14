package com.example.yous.pt14_josep_ortiz;

import android.text.format.Time;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by yous on 11/12/17.
 */

public class Parsejador {

    public static List<Bloc> parseja(String xml) throws XmlPullParserException, IOException {

        List<Bloc> blocs = new ArrayList<Bloc>();

        XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
        xmlPullParser.setInput(new StringReader(xml));

        int eventType = xmlPullParser.getEventType();

        double temperatura = 0;
        String sortidaSol = null;
        String amagadaSol = null;
        String time = null;
        String precipitacio = null;
        String clouds = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {

            String tagname = xmlPullParser.getName();

            switch (eventType) {

                case XmlPullParser.START_TAG:

                    if (tagname.equalsIgnoreCase("time")) {

                        // 2017-12-14T15:00:00
                        time = xmlPullParser.getAttributeValue(0);

                    } else if (tagname.equalsIgnoreCase("temperature")) {

                        String tempString = xmlPullParser.getAttributeValue(1);
                        temperatura = (Double.parseDouble(tempString) - 273.15);

                    } else if (tagname.equalsIgnoreCase("precipitation")){

                        try {
                            precipitacio = xmlPullParser.getAttributeValue(2);
                        } catch (Exception e) {
                            precipitacio = "no";
                        }

                    } else if(tagname.equalsIgnoreCase("clouds")) {
                            clouds = xmlPullParser.getAttributeValue(0);
                    } else if(tagname.equalsIgnoreCase("sun")) {
                        sortidaSol = xmlPullParser.getAttributeValue(0);
                        amagadaSol = xmlPullParser.getAttributeValue(1);
                    }

                    break;

                    /*
                case XmlPullParser.TEXT:
                    break;
                case XmlPullParser.END_TAG:
                    break;*/
            }

            if (temperatura != 0 && time != null
                    && precipitacio != null && clouds != null
                        && sortidaSol != null && amagadaSol != null) {

                Bloc bloc = new Bloc(time, temperatura, precipitacio, clouds, sortidaSol, amagadaSol);
                blocs.add(bloc);

                temperatura = 0;
                time = null;
                precipitacio = null;
                clouds = null;
            }

            eventType = xmlPullParser.next();
        }

        return blocs;
    }
}
