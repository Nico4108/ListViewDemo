package com.example.listviewdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.listviewdemo.global.Global;
import com.example.listviewdemo.model.Note;
import com.example.listviewdemo.repo.Repo;

import java.io.InputStream;

public class CustomListActivity extends AppCompatActivity implements Updatable{

    private MyAdapter myAdapter;
    ImageView myImageView10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        myImageView10 = findViewById(R.id.myImageView10);

        String[] countries = {"USA", "Denmark", "Norway", "Iceland"};
        // vi skal have et array med billeder:
        int[] pictures = {R.drawable.usa, R.drawable.denmark1, R.drawable.norway1, R.drawable.iceland1};
        myAdapter = new MyAdapter(this, Repo.r().getNoteList(), pictures);
        ListView listView = findViewById(R.id.myListView2);

        listView.setAdapter(myAdapter);
        // Printer i consollen
        listView.setOnItemClickListener((_listView, linearLayout, adapterPos, arrPos) -> {
            System.out.println("Klik på række " + arrPos);
            // Intent er en hjælper fra Androids side
            Intent intent = new Intent(this, DetailActivity.class);
            Global.map.put(Global.NOTE_KEY, Repo.r().getNoteList().get((int)arrPos));
             // gemmer text i untent objektet og kan hentes på et andet view
            startActivity(intent);
        });

        Repo.r().setActivity(this);
        Repo.r().downloadBitmap("coolcar2.jpg", this);

    }

    public void galleryBtnPressed(View view){
        Intent intent = new Intent(Intent.ACTION_PICK); // make an implicit intent, which will allow
        // the user to choose among different services to accomplish this task.
        intent.setType("image/*"); // we need to set the type of content to pick
        startActivityForResult(intent, 1); // start the activity, and in this case
        // expect an answer
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("back from image picking ");
        if(requestCode == 1) { // gallery
            backFromGallery(data);
        }
        if(requestCode == 2) { // camera
            backFromCamera(data);
        }
    }

    private Bitmap currentBitmap;

    private void backFromGallery(@Nullable Intent data) {
        Uri uri = data.getData();
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            currentBitmap = BitmapFactory.decodeStream(is);
            myImageView10.setImageBitmap(currentBitmap);

        }catch (Exception e){

        }
    }

    private void backFromCamera(@Nullable Intent data) {
        try {
            Bitmap currentBitmap = (Bitmap)data.getExtras().get("data");
            myImageView10.setImageBitmap(currentBitmap);
            // skaf en id til dit billede. F.eks. id fra noten
        }catch (Exception e){

        }
    }

    public void cameraBtnPressed(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

    @Override
    public void update(Object o) {
        System.out.println("Update() kaldet!!!");
        // kald på adapters notidyDatasetChange()
        runOnUiThread(() -> {
            myAdapter.notifyDataSetChanged();
            if(o != null) {
                Bitmap bitmap = (Bitmap)o; // fungerer denne casting
                if(bitmap != null) {
                    myImageView10.setImageBitmap(bitmap);
                }
            }
        });
    }

    public void addNote(View view){
        Note note = new Note("Skriv nu");
        Repo.r().addNote(note); // Opretter ny Nye + gemmer i Firebase
    }

    public void uploadImage(View view) {
        Note note = new Note("My friday note: Chill!");
        Repo.r().uploadBitmap(note, currentBitmap);
    }


}