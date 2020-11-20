package com.example.listviewdemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.listviewdemo.global.Global;
import com.example.listviewdemo.model.Note;
import com.example.listviewdemo.repo.Repo;

import java.io.InputStream;

public class DetailActivity extends AppCompatActivity {

    private Note currentNote;
    Bitmap currentBitmap;
    ImageView myDetailImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView myDetailTextView = findViewById(R.id.myDetailTextView);
        currentNote = (Note) Global.map.get(Global.NOTE_KEY);
        myDetailTextView.setText(currentNote.getTitle());

    }

    public void addUpdatedNote(View view){
        TextView myDetailTextView = findViewById(R.id.myDetailTextView);
        EditText newDetailText = findViewById(R.id.newText);
        currentNote = (Note) Global.map.get(Global.NOTE_KEY);
        Editable newText = newDetailText.getText();
        myDetailTextView.setText(newText);
        System.out.println("Text has been updated to: " + newText);
        Repo.r().updateDBNote(currentNote, newText.toString()); // Kalder update + gemmer i Firebase
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


    private void backFromGallery(@Nullable Intent data) {
        Uri uri = data.getData();
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            currentBitmap = BitmapFactory.decodeStream(is);
            myDetailImageView.setImageBitmap(currentBitmap);

        }catch (Exception e){

        }
    }

    private void backFromCamera(@Nullable Intent data) {
        try {
            currentBitmap = (Bitmap)data.getExtras().get("data");
            myDetailImageView.setImageBitmap(currentBitmap);
        }catch (Exception e){

        }
    }

    public void cameraBtnPressed(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

}