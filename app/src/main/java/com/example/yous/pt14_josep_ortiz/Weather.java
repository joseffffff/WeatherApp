package com.example.yous.pt14_josep_ortiz;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Weather extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String nomCiutat = getIntent().getStringExtra("nomCiutat");

        BBDDConnex db = new BBDDConnex(nomCiutat);
        db.execute(1);

        while (!db.isFinished()) {
            Log.d("alo", db.isFinished()+"");
        }

        if (db.isLaCiutatEstaDescarregada()) {

        } else {

        }

        Descarregador descarregador = new Descarregador();
        descarregador.execute(nomCiutat.replace('+', ' '));

        List<Bloc> blocs = null;

        while(!descarregador.isFinished()){
            Log.d("status ", ""+descarregador.isFinished());
            if (descarregador.isError404()) break;
        }

        String xmlString = descarregador.getXmlString();

        TextView tvTempMitja = (TextView) findViewById(R.id.tvTempMitja);
        TextView tvCiutat = (TextView) findViewById(R.id.tvCiutat);
        TextView tvTempActual = (TextView) findViewById(R.id.tvTempActual);

        ImageView ivTempActual = (ImageView) findViewById(R.id.ivTempActual);

        if (xmlString != null) try {

            blocs = Parsejador.parseja(xmlString);

            tvTempMitja.setText(getResources().getString(R.string.media) + " "
                    + String.format("%.2f ºC", getMitjaTemperatura(blocs)));

            tvCiutat.setText(nomCiutat.replace('+', ' ').toUpperCase());

            Bloc blocActual = getBlocActual(blocs);
            tvTempActual.setText(String.format("%.2f ºC", blocActual.getTemperatura()));

            if (blocActual.isCalor()) {
                tvTempActual.setTextColor(Color.rgb(255, 204, 102));
            } else {
                tvTempActual.setTextColor(Color.rgb(153, 204, 255));
            }

            ivTempActual.setImageBitmap(
                    Bitmap.createScaledBitmap(
                            blocActual.getIconBloc(
                                    getApplicationContext()), 300, 300, true));

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);

        if (blocs == null) {

            rv.setVisibility(View.GONE);
            tvCiutat.setVisibility(View.GONE);
            tvTempMitja.setVisibility(View.GONE);
            ivTempActual.setVisibility(View.GONE);
            tvTempActual.setVisibility(View.GONE);

            TextView tvFail = (TextView) findViewById(R.id.tvError);
            tvFail.setVisibility(View.VISIBLE);

        } else {

            rv.setHasFixedSize(true);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(new CiutatAdapter(blocs, getApplicationContext()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public double getMitjaTemperatura(List<Bloc> blocs) {

        double temperaturaTotalSumada = 0;

        for (Bloc b : blocs) {
            temperaturaTotalSumada += b.getTemperatura();
        }

        return temperaturaTotalSumada / blocs.size();
    }

    private Bloc getBlocActual(List<Bloc> blocs){

        GregorianCalendar gc = new GregorianCalendar();

        for (Bloc b : blocs) {

            if (b.getHora().get(Calendar.YEAR) == gc.get(Calendar.YEAR)) {
                if (b.getHora().get(Calendar.MONTH) == gc.get(Calendar.MONTH)) {
                    if (b.getHora().get(Calendar.DAY_OF_MONTH) == gc.get(Calendar.DAY_OF_MONTH)) {

                        if (b.getHora().get(Calendar.HOUR_OF_DAY) >= gc.get(Calendar.HOUR_OF_DAY) &&
                                b.getHora().get(Calendar.HOUR_OF_DAY) < gc.get(Calendar.HOUR_OF_DAY)+3){
                            return b;
                        }

                    }
                }
            }

        }

        return null;
    }

    class BBDDConnex extends AsyncTask<Integer, Void, Void> {

        private boolean finished;
        private String nomCiutat;
        private boolean laCiutatEstaDescarregada;

        private TemperaturesHelper db;

        public BBDDConnex (String nomCiutat) {
            this.nomCiutat = nomCiutat;
            finished = false;
            db = new TemperaturesHelper(getApplicationContext());
        }

        /**
         *
         * @param mode quina accio volem fer a la BBDD
         *             1: saber si la ciutat pasada en el constructor ja està a la BBDD
         *             2: Retornar la llista de ciutats (si el anterior ha donat true)
         * @return
         */

        @Override
        protected Void doInBackground(Integer[] mode) {

            finished = false;

            switch (mode[0]) {

                case 1:
                    laCiutatEstaDescarregada = db.estaCiutatDescarregada(nomCiutat);
                    break;

                case 2:

                    break;

            }



            finished = true;
            return null;
        }

        public boolean isFinished() {
            return finished;
        }

        public boolean isLaCiutatEstaDescarregada() {
            return laCiutatEstaDescarregada;
        }
    }
}
