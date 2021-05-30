package org.tensorflow.lite.examples.detection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLLiteHelper extends SQLiteOpenHelper {

    private static final int DB_VER = 1;

    private static final String DB_NAME = "Detections";

    private static final String DETECTIONS = "Detection";

    private static final String ID = "id";
    private static final String TITLE = "title";


    public SQLLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + DETECTIONS + "("
                + ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT"
                + ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DETECTIONS);

        onCreate(db);
    }

    public void addDetection(FoundObject object){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE,object.getTitle());

        db.insert(DETECTIONS,null,contentValues);

        db.close();
    }

    public int getObjectCount(){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DETECTIONS;
        Cursor cursor = db.rawQuery(sql, null);

        int count = cursor.getCount();

        db.close();
        return count;
    }

    public FoundObject getObject(int id){
        SQLiteDatabase db = getReadableDatabase();
        FoundObject object = null;

        Cursor cursor = db.query(DETECTIONS,new String[]{ID,TITLE},
                ID + "=?", new String[]{String.valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            object = new FoundObject(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2));
        }
        db.close();
        return object;
    }

    public List<FoundObject> getAllNotes(){
        List<FoundObject> noteList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DETECTIONS;
        Cursor cursor = db.rawQuery(sql,null);

        FoundObject object = null;
        if(cursor.moveToFirst()){
            do{
                object = new FoundObject();
                object.setId(cursor.getInt(0));
                object.setTitle(cursor.getString(1));
                object.setContent(cursor.getString(2));
                noteList.add(object);
            }
            while (cursor.moveToNext());
        }

        db.close();
        return noteList;
    }

    public void updateDetection(FoundObject object){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, object.getTitle());


        db.update(DETECTIONS,contentValues,ID + " =?", new String[]{String.valueOf(object.getId())});

        db.close();
    }

    public void deleteDetection(FoundObject object){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(DETECTIONS, ID + " =?", new String[]{String.valueOf(object.getId())});

        db.close();
    }

    public void deleteAllDetections(){
        SQLiteDatabase db = getWritableDatabase();

        String sql = " DELETE FROM " + DETECTIONS;

        db.execSQL(sql);

        db.close();
    }

    }

