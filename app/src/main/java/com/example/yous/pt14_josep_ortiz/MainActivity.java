package com.example.yous.pt14_josep_ortiz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView ivActivityMain = (ImageView) findViewById(R.id.ivActivityMain);

        Bitmap iconWorld = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.worldicon);
        ivActivityMain.setImageBitmap(Bitmap.createScaledBitmap(iconWorld, 400,400, true));

    }

    protected void buscaTemperatura(View view){
        EditText et = (EditText) findViewById(R.id.nomCiutat);

        String etString = et.getText().toString();

        if (etString.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.errorEmptyCity),
                    Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, Weather.class);
            intent.putExtra("nomCiutat", deleteSpaces(etString));
            startActivity(intent);
        }
    }

    private String deleteSpaces(String word){
        word = word.toLowerCase();
        return word.replace(' ', '+');
    }

}
