package com.example.yous.pt14_josep_ortiz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by yous on 11/12/17.
 */

public class CiutatAdapter extends RecyclerView.Adapter<CiutatAdapter.BlocViewHolder>{

    private Context context;
    private List<Bloc> blocs;

    public CiutatAdapter(List<Bloc> blocs, Context context){

        this.context = context;
        this.blocs = blocs;
    }

    @Override
    public BlocViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.temperatures, parent, false);

        BlocViewHolder vh = new BlocViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(BlocViewHolder holder, int position) {

        GregorianCalendar gcAux = blocs.get(position).getHora();
        double tempPerMostrar = blocs.get(position).getTemperatura();

        Bitmap icon = blocs.get(position).getIconBloc(context);
        holder.ivIcon.setImageBitmap(Bitmap.createScaledBitmap(icon, 300, 300, true));

        String space = getSpaces(tempPerMostrar);
        String diaSetmana = getDiaSetmana(gcAux.get(Calendar.DAY_OF_WEEK));
        String mes = getMes(gcAux.get(Calendar.MONTH));

        holder.tvTemp.setText(String.format(space + "%.2f ºC", tempPerMostrar));
        holder.tvHora.setText(  String.format("%02d", gcAux.get(Calendar.HOUR_OF_DAY)) + ":" +
                                String.format("%02d", gcAux.get(Calendar.MINUTE)) + " - " +
                                String.format("%02d", gcAux.get(Calendar.HOUR_OF_DAY) + 3) + ":" +
                                String.format("%02d", gcAux.get(Calendar.MINUTE)));
        holder.tvData.setText(diaSetmana + ", " + gcAux.get(Calendar.DAY_OF_MONTH) + " "
                                + mes + " " + gcAux.get(Calendar.YEAR));

        if (blocs.get(position).isCalor()) {
            holder.tvTemp.setTextColor(Color.rgb(255, 204, 102));
        } else {
            holder.tvTemp.setTextColor(Color.rgb(153, 204, 255));
        }
    }

    private String getMes(int i) {

        String mes = "";

        switch (i) {

            case 0:
                mes = context.getResources().getString(R.string.gener);
                break;
            case 1:
                mes = context.getResources().getString(R.string.febrer);
                break;
            case 2:
                mes = context.getResources().getString(R.string.març);
                break;
            case 3:
                mes = context.getResources().getString(R.string.abril);
                break;
            case 4:
                mes = context.getResources().getString(R.string.maig);
                break;
            case 5:
                mes = context.getResources().getString(R.string.juny);
                break;
            case 6:
                mes = context.getResources().getString(R.string.juliol);
                break;
            case 7:
                mes = context.getResources().getString(R.string.agost);
                break;
            case 8:
                mes = context.getResources().getString(R.string.setembre);
                break;
            case 9:
                mes = context.getResources().getString(R.string.octubre);
                break;
            case 10:
                mes = context.getResources().getString(R.string.novembre);
                break;
            case 11:
                mes = context.getResources().getString(R.string.desembre);
                break;
        }

        return mes;

    }

    private String getDiaSetmana(int i) {

        String diaSetmana = "";

        switch (i) {

            case 1:
                diaSetmana = context.getResources().getString(R.string.diumenge);
                break;
            case 2:
                diaSetmana = context.getResources().getString(R.string.dilluns);
                break;
            case 3:
                diaSetmana = context.getResources().getString(R.string.dimarts);
                break;
            case 4:
                diaSetmana = context.getResources().getString(R.string.dimecres);
                break;
            case 5:
                diaSetmana = context.getResources().getString(R.string.dijous);
                break;
            case 6:
                diaSetmana = context.getResources().getString(R.string.divendres);
                break;
            case 7:
                diaSetmana = context.getResources().getString(R.string.dissabte);
                break;
        }

        return diaSetmana;
    }

    private String getSpaces(double tempPerMostrar) {

        String space;

        if (tempPerMostrar >= 0 && tempPerMostrar < 10) {
            space = "   ";
        } else if (tempPerMostrar < 0 && tempPerMostrar > -10) {
            space = "  ";
        } else if (tempPerMostrar >= 10){
            space = " ";
        } else {
            space = "";
        }

        return space;
    }

    @Override
    public int getItemCount() {
        return blocs.size();
    }

    public class BlocViewHolder extends RecyclerView.ViewHolder{

        public TextView tvHora;
        public TextView tvData;
        public TextView tvTemp;
        public ImageView ivIcon;

        public BlocViewHolder(View itemView) {
            super(itemView);

            tvHora = (TextView) itemView.findViewById(R.id.tvHora);
            tvData = (TextView) itemView.findViewById(R.id.tvData);
            tvTemp = (TextView) itemView.findViewById(R.id.tvTemp);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
        }
    }
}
