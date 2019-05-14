package com.example.yous.pt14_josep_ortiz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.RequiresApi;
import android.text.format.Time;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by yous on 14/12/17.
 */

public class Bloc {

    private GregorianCalendar hora;
    private String sHora;

    private String horaSortidaSol;
    private GregorianCalendar gcHoraSortidaSol;

    private String horaAmagadaSol;
    private GregorianCalendar gcHoraAmagadaSol;

    private double temperatura;

    // "no" -- "rain" -- "snow"
    private String precipitacio;

    private boolean clouds;

    // true = hace calor -- false frio // canvi als 15 graus
    private boolean calor;

    public Bloc (String dataIHora, double temperatura, String precipitacio, String clouds,
                 String horaSortidaSol, String horaAmagadaSol) {

        this.precipitacio = precipitacio;
        this.sHora = dataIHora;
        this.hora = parseDataHora(dataIHora);
        this.temperatura = temperatura;

        this.horaSortidaSol = horaSortidaSol;
        this.horaAmagadaSol = horaAmagadaSol;

        this.gcHoraSortidaSol = parseDataHora(horaSortidaSol);
        this.gcHoraAmagadaSol = parseDataHora(horaAmagadaSol);

        this.clouds = cloudsOno(clouds);

        if (temperatura < 15) {
            calor = false;
        } else {
            calor = true;
        }
    }

    private boolean cloudsOno(String clouds) {
        // "overcast clouds" -- "broken clouds" -- "few clouds" -- "clear sky"
        if (clouds.equalsIgnoreCase("overcast clouds") || clouds.equalsIgnoreCase("broken clouds")
                || clouds.equalsIgnoreCase("few clouds")) {
            return true;
        }
        return false;
    }

    private GregorianCalendar parseDataHora (String dataIHora) {

        // 2017-12-14T15:00:00
        // [0] = 2017-12-14 // [1] = 15:00:00
        String[] dataIHoraPartida = dataIHora.split("T");

        // [0] = 2017 // [1] = 12 (mes) // [2] = 14 /(dia)
        String[] data = dataIHoraPartida[0].split("-");

        // [0] = 15 (hora) // [1] = 00 (mins) // [2] = 00 (segons)
        String[] hora = dataIHoraPartida[1].split(":");

        // (int any, int mes, int dia, int hora, int mins, int segons)
        return new GregorianCalendar(   Integer.parseInt(data[0]),  // any
                Integer.parseInt(data[1])-1,  // mes
                Integer.parseInt(data[2]),  // dia del mes
                Integer.parseInt(hora[0]),  // hora
                Integer.parseInt(hora[1]),  // minuts
                Integer.parseInt(hora[2])); // segons

    }

    public Bitmap getIconBloc(Context context) {

        Bitmap icon = null;

        String solLluna = solLluna( hora.get(Calendar.HOUR_OF_DAY),
                                    gcHoraSortidaSol.get(Calendar.HOUR_OF_DAY),
                                    gcHoraAmagadaSol.get(Calendar.HOUR_OF_DAY));

        if (solLluna.equalsIgnoreCase("sol")) {

            // Iconos de sol

            if (clouds) {

                // Hay nubes
                if (precipitacio.equalsIgnoreCase("snow")) {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathersnowscatteredday);
                } else if (precipitacio.equalsIgnoreCase("rain")) {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathershowersday);
                } else {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weatherfewclouds);
                }

            } else {

                if (precipitacio.equalsIgnoreCase("snow")) {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathersnowscatteredday);
                } else if (precipitacio.equalsIgnoreCase("rain")) {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathershowersday);
                } else {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weatherclear);
                }
            }

        } else {

            // Iconos de luna
            if (clouds) {

                if (precipitacio.equalsIgnoreCase("snow")) {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathersnowscatterednight);
                } else if (precipitacio.equalsIgnoreCase("rain")) {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathershowersnight);
                } else {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathercloudsnight);
                }

            } else {

                if (precipitacio.equalsIgnoreCase("snow")) {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathersnowscatterednight);
                } else if (precipitacio.equalsIgnoreCase("rain")) {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathershowersnight);
                } else {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weatherclearnight);
                }
            }
        }



        /*
        if (solLluna.equalsIgnoreCase("sol") && clouds.equalsIgnoreCase("no")) {

            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weatherclear);

        } else if (precipitacio.equalsIgnoreCase("no") && solLluna.equalsIgnoreCase("lluna")) {

            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weatherclearnight);

        } else if (solLluna.equalsIgnoreCase("sol") && clouds.equalsIgnoreCase("yes")) {

            if (precipitacio.equalsIgnoreCase("snow")) {
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathersnowscatteredday);
            } else if (precipitacio.equalsIgnoreCase("rain")) {
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathershowersday);
            } else {
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weatherfewclouds);
            }

        } else if (solLluna.equalsIgnoreCase("lluna") && clouds.equalsIgnoreCase("yes")) {

            if (precipitacio.equalsIgnoreCase("snow")) {
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathersnowscatterednight);
            } else if (precipitacio.equalsIgnoreCase("rain")) {
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathershowersnight);
            } else {
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathercloudsnight);
            }
        } else {
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.weathernoneavailable);
        }
*/

        return icon;
    }

    private String solLluna(int horaActual, int horaSortida, int horaAmagada){

        if (horaActual > horaSortida && horaActual < horaAmagada){
            return "sol";
        }

        return "lluna";
    }

    public String getsHora() {
        return sHora;
    }

    public boolean isClouds() {
        return clouds;
    }

    public String getPrecipitacio() {
        return precipitacio;
    }

    public GregorianCalendar getHora(){
        return hora;
    }

    public double getTemperatura(){
        return temperatura;
    }

    public boolean isCalor(){
        return calor;
    }
}
