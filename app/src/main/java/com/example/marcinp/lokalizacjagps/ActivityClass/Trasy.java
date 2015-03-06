package com.example.marcinp.lokalizacjagps.ActivityClass;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import com.example.marcinp.lokalizacjagps.DBAdapter;
import com.example.marcinp.lokalizacjagps.R;

/**
 * Created by MarcinP on 2015-03-04.
 */
public class Trasy extends Activity {
    TextView tvDane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp);
        tvDane = (TextView) findViewById(R.id.tvDane);
        DBAdapter sql = new DBAdapter(getApplicationContext());

        Cursor c = sql.getAllWspolrzedne();

        tvDane.setText("Liczba rekordow:"+c.getCount());
        while ( c.moveToNext() )
        {
            String wiersz = c.getInt(0)+"| "+c.getInt(1)+"| "+c.getDouble(2)+"||"+c.getDouble(3)+"\n";
            tvDane.setText(tvDane.getText().toString()+wiersz);
        }
    }
}
