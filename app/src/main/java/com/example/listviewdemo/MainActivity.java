package com.example.listviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.listviewdemo.repo.Repo;

public class MainActivity extends AppCompatActivity implements Updatable {

    ImageView myImageView10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myImageView10 = findViewById(R.id.myimageView2);
        String[] countries = {"USA", "Denmark", "Norway", "Iceland"};
        ListView listView = findViewById(R.id.myListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.myrow, R.id.mytextView, countries);
        listView.setAdapter(adapter);
        // Printer i consollen
        listView.setOnItemClickListener((_listView, linearLayout, adapterPos, arrPos) -> {
            System.out.println("Klik på række " + arrPos);
        });
        Repo.r().downloadBitmap("coolcar2.jpg", this);
    }

    @Override
    public void update(Object o) {
        runOnUiThread(()->{ // ?
            if(o != null) {
                Bitmap bitmap = (Bitmap)o; // fungerer denne casting
                if(bitmap != null) {
                    myImageView10.setImageBitmap(bitmap);
                }
            }
        });
    }


}