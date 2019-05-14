package com.example.yous.pt14_josep_ortiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

/**
 * Created by yous on 8/01/18.
 */

public class TemperaturesHelper extends SQLiteOpenHelper {

    // Instancies per accedir a la BBDD
    private SQLiteDatabase dbW;
    private SQLiteDatabase dbR;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TemperaturesCiutats";

    // Table CIUTAT
    private final String TABLE_CIUTATS = "CIUTATS";
    private final String COLUMN_TABLE_CIUTATS_ID = "ID";
    private final String COLUMN_TABLE_CIUTATS_NOMCIUTAT = "CIUTAT";

    // Table TEMPERATURES
    private final String TABLE_TEMPERATURES = "TEMPERATURES";
    private final String COLUMN_TABLE_TEMPERATURES_ID = "ID";
    private final String COLUMN_TABLE_TEMPERATURES_ID_CIUTAT = "ID_CIUTAT";
    private final String COLUMN_TABLE_TEMPERATURES_TEMPERATURA = "TEMPERATURA";
    private final String COLUMN_TABLE_TEMPERATURES_HORA = "HORA";
    private final String COLUMN_TABLE_TEMPERATURES_HORA_SORTIDA_SOL = "HORA_SORTIDA_SOL";
    private final String COLUMN_TABLE_TEMPERATURES_HORA_AMAGADA_SOL = "HORA_AMGADA_SOL";
    private final String COLUMN_TABLE_TEMPERATURES_PRECIPITACIO = "PRECIPITACIO";
    private final String COLUMN_TABLE_TEMPERATURES_CLOUDS = "CLOUDS";

    public TemperaturesHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbW = this.getWritableDatabase();
        dbR = this.getReadableDatabase();
    }

    public boolean estaCiutatDescarregada(String nomCiutat){

        String[] projection = {
                COLUMN_TABLE_CIUTATS_ID,
                COLUMN_TABLE_CIUTATS_NOMCIUTAT
        };

        String[] valorsInterrogants = {nomCiutat};

        Cursor c = dbR.query(
                TABLE_CIUTATS,                                          // Taula a la que fem la consulta
                projection,                                             // Les columnes que vull que em retorni
                COLUMN_TABLE_CIUTATS_NOMCIUTAT + " = ?",        // Where
                valorsInterrogants,                                     // Valors dels interrogants
                null,                                           // Group by
                null,                                             // having
                null);

        int count = c.getCount();

        if ( count > 0 ) {

            return true;
        } else {
            return false;
        }
    }

    public void guarda(String nomCiutat, List<Bloc> blocs) {

        int maxIdActualCiutats = getMaxId(TABLE_CIUTATS);
        int maxIdActualTemperatures = getMaxId(TABLE_TEMPERATURES);

        ContentValues values = new ContentValues();
        values.put(COLUMN_TABLE_CIUTATS_ID, (maxIdActualCiutats+1));
        values.put(COLUMN_TABLE_CIUTATS_NOMCIUTAT, nomCiutat);
        dbW.insert(TABLE_CIUTATS, null, values);

        for (Bloc b : blocs) {

            ContentValues valuesBlocs = new ContentValues();
            valuesBlocs.put(COLUMN_TABLE_TEMPERATURES_ID, (maxIdActualTemperatures+1));
            valuesBlocs.put(COLUMN_TABLE_TEMPERATURES_ID_CIUTAT, (maxIdActualCiutats+1));
            valuesBlocs.put(COLUMN_TABLE_TEMPERATURES_HORA, b.getsHora());
            valuesBlocs.put(COLUMN_TABLE_TEMPERATURES_TEMPERATURA, b.getTemperatura()+"");
            dbW.insert(TABLE_TEMPERATURES, null, valuesBlocs);
        }


    }

    public int getMaxId(String table) {
        Cursor c = dbR.rawQuery("SELECT MAX(ID) FROM " + table, null);
        return c.getColumnIndex("MAX(ID)");
    }

    public List<Bloc> llegeix(String nomCiutat) {
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String createTableCiutats = "CREATE TABLE IF NOT EXISTS " + TABLE_CIUTATS +
                " ( " + COLUMN_TABLE_CIUTATS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TABLE_CIUTATS_NOMCIUTAT + " TEXT )";

        String createTableTemps= "CREATE TABLE IF NOT EXISTS " + TABLE_TEMPERATURES +
                " ( " + COLUMN_TABLE_TEMPERATURES_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TABLE_TEMPERATURES_ID_CIUTAT + " INTEGER, " +
                COLUMN_TABLE_TEMPERATURES_TEMPERATURA + " REAL, " +
                COLUMN_TABLE_TEMPERATURES_HORA + " TEXT, " +
                COLUMN_TABLE_TEMPERATURES_HORA_SORTIDA_SOL + " TEXT, " +
                COLUMN_TABLE_TEMPERATURES_HORA_AMAGADA_SOL + " TEXT, " +
                COLUMN_TABLE_TEMPERATURES_PRECIPITACIO + " TEXT, " +
                COLUMN_TABLE_TEMPERATURES_CLOUDS + " INTEGER )";

        sqLiteDatabase.execSQL(createTableCiutats);
        sqLiteDatabase.execSQL(createTableTemps);

        /*
        dbW.execSQL("INSERT INTO " + TABLE_CIUTATS + " VALUES (1, 'testCity')");
        dbW.execSQL("INSERT INTO " + TABLE_TEMPERATURES + " VALUES (1, 1, 0, '2018-01-12T12:49:03')");*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CIUTATS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPERATURES);

        onCreate(sqLiteDatabase);
    }
}
