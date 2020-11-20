package com.example.listviewdemo.repo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;

import com.example.listviewdemo.Updatable;
import com.example.listviewdemo.model.Note;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repo {

    private static Repo repo = new Repo(); // Kan kun køre én gang
    private Updatable activity;
    private FirebaseFirestore fireDB = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final String NOTES = "notes";
    private static final String TITLE = "title";
    private List<Note> noteList = new ArrayList<>(); // Gemmer Note objekter. Kan opdateres

    public static Repo r(){
        return repo;
    }

    public void setActivity(Updatable a){  // kaldes fra aktivitet, som skal blive opdateret
        activity = a;
        startListener();
    }

    public void addNote(Note note) {
        DocumentReference ref = fireDB.collection(NOTES).document(note.getId()); // Opretter nyt dokument i Firebase, hvor vi selv angiver document id.
        Map<String, String> map = new HashMap<>();
        map.put(TITLE, note.getTitle()); // tilføj selv flere key-value par efter behov
        ref.set(map).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                System.out.println("Error i gem: " + task.getException());
            }
        }); // gemmer hele map i aktuelt dokument

    }

    public void startListener(){
        fireDB.collection(NOTES).addSnapshotListener((values, error) -> { // Values indeholder ALLE ting fra firebase så du kan kalde de forskellige til som .getDocuments
            noteList.clear();

            for(DocumentSnapshot snap : values.getDocuments()){
                System.out.println("Test1");
                Note note = new Note(snap.get(TITLE).toString(), snap.getId());
                noteList.add(note);
            }
            activity.update(null); // kaldes efter vi har hentet data fra Firebase
        });

    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void updateNote(Note note, String newText) {
        DocumentReference ref = fireDB.collection(NOTES).document(note.getId());
        Map<String, String> map = new HashMap<>();
        //map.put(TITLE, note.setTitle(newText));
        ref.set(map);

    }

    public void downloadBitmap(String fileName, Updatable activity){
        StorageReference ref = storage.getReference(fileName);
        int max = 1024 * 1024;
        ref.getBytes(max).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
            activity.update(bitmap); // god linie!
        }).addOnFailureListener(ex -> {
            System.out.println("error in download " + ex);
        });
    }

    public void uploadBitmap(Note note, Bitmap bitmap) {
        StorageReference ref = storage.getReference(note.getId());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        ref.putBytes(baos.toByteArray()).addOnCompleteListener(snap -> {
            System.out.println("OK to upload " + snap);
        }).addOnFailureListener(exception -> {
            System.out.println("failure to upload " + exception);
        });
    }

// plus de andre CRUD metoder

    public void updateDBNote(Note note, String newText){

        fireDB.collection(NOTES).document(note.getId()).update(TITLE, newText);
    }

}
