package com.example.kishan.biblio.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kishan.biblio.Getters.BooksGetter;

import java.util.ArrayList;

/**
 * Created by Kishan on 2/16/2016.
 */
public class BooksDatabase {
    public static final String KEY_ROWID = "_id";
    //public static final String KEY_TYPE = "_type";
    public static final String KEY_TITLE = "_title";
    public static final String KEY_AUTHORS = "_authors";
    public static final String KEY_RATING = "_rating";
    public static final String KEY_PUBLISHDATE = "_publish_date";
    public static final String KEY_CATEGORIES = "_categories";
    public static final String KEY_IMAGELINKS = "_image_links";
    public static final String KEY_LANGUAGE = "_language";
    public static final String KEY_DESCRIPTION = "_description";
    public static final String KEY_PAGE_NUM = "_page_num";
    public static final String KEY_IMAGE_BLOB = "_image";

    private static final String DATABASE_NAME = "BooksDB";
    //private static final String DATABASE_TABLE = "BookDetails";

    public static final String DATABASE_TABLE_READING = "BookDetailsReading";
    public static final String DATABASE_TABLE_HAVE_READ = "BookDetailsHaveRead";
    public static final String DATABASE_TABLE_WANT_TO_READ = "BookDetailsWantToRead";

    private static final int DATABASE_VERSION = 1;

    private DBHelper dbHelper;
    private final Context context;
    private SQLiteDatabase sqLiteDatabase;

    String[] columns = new String[]{
            KEY_ROWID,
            //KEY_TYPE,
            KEY_TITLE,
            KEY_AUTHORS,
            KEY_RATING,
            KEY_PUBLISHDATE,
            KEY_CATEGORIES,
            KEY_IMAGELINKS,
            KEY_LANGUAGE,
            KEY_DESCRIPTION,
            KEY_IMAGE_BLOB
    };

    public BooksDatabase(Context context) {
        this.context = context;
    }

    public BooksDatabase open() throws SQLException {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public ContentValues getContentValues(BooksGetter book){
        ContentValues contentValues = new ContentValues();
        //contentValues.put(KEY_TYPE,book.getType());
        contentValues.put(KEY_TITLE, book.getTitle());
        contentValues.put(KEY_AUTHORS, book.getAuthors());
        contentValues.put(KEY_RATING, book.getRating());
        contentValues.put(KEY_PUBLISHDATE, book.getPublishDate());
        contentValues.put(KEY_CATEGORIES, book.getCategories());
        contentValues.put(KEY_IMAGELINKS, book.getImageLinks());
        contentValues.put(KEY_LANGUAGE, book.getLanguage());
        contentValues.put(KEY_DESCRIPTION, book.getDescription());
        contentValues.put(KEY_IMAGE_BLOB, book.getImageByteArray());
        return contentValues;
    }

    public ArrayList<BooksGetter> getEntry(Cursor c){
        ArrayList<BooksGetter> results = new ArrayList<>();

        int iRow = c.getColumnIndex(KEY_ROWID);
        //int iType = c.getColumnIndex(KEY_TYPE);
        int iTitle = c.getColumnIndex(KEY_TITLE);
        int iAuthors = c.getColumnIndex(KEY_AUTHORS);
        int iRating = c.getColumnIndex(KEY_RATING);
        int iPublishdate = c.getColumnIndex(KEY_PUBLISHDATE);
        int iCategory = c.getColumnIndex(KEY_CATEGORIES);
        int iImageLinks = c.getColumnIndex(KEY_IMAGELINKS);
        int iLanguage = c.getColumnIndex(KEY_LANGUAGE);
        int iDescription = c.getColumnIndex(KEY_DESCRIPTION);
        int iImageArrayColumn = c.getColumnIndex(KEY_IMAGE_BLOB);

        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            int rowId = (int) c.getLong(iRow);
            //String type = c.getString(iType);
            String title = c.getString(iTitle);
            String authors = c.getString(iAuthors);
            String rating = c.getString(iRating);
            String publish = c.getString(iPublishdate);
            String category = c.getString(iCategory);
            String imageLinks = c.getString(iImageLinks);
            String language = c.getString(iLanguage);
            String description = c.getString(iDescription);
            byte[] imageArray = c.getBlob(iImageArrayColumn);

            results.add(new BooksGetter(rowId,title,authors,rating,publish,category,imageLinks,language,description,imageArray));
        }
        return results;
    }

    public Long createEntry(BooksGetter book,String TableName) {
        return sqLiteDatabase.insert(TableName, null, getContentValues(book));
    }

    public ArrayList<BooksGetter> readEntries(String TableName){
        Cursor c = sqLiteDatabase.query(TableName, columns, null, null, null, null, null);
        return getEntry(c);
    }

    public ArrayList<BooksGetter> getEntryByRowId(int rowId,String TableName){
        Cursor c = sqLiteDatabase.query(TableName,columns,KEY_ROWID +" = "+rowId,null,null,null,null);
        return getEntry(c);
    }

    public int deleteRow(int rowId,String TableName){
        return sqLiteDatabase.delete(TableName, KEY_ROWID + " = " + rowId, null);
    }

    public int updateRowById(int rowId, BooksGetter book,String TableName){
        return sqLiteDatabase.update(TableName, getContentValues(book), KEY_ROWID + " = " + book, null);
    }

    public int getNumRows(String TableName){
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase,TableName);
    }

    public boolean isEntryByTitleExists(String TableName,String Title){
        final String DATABASE_COMPARE = "select count(*) from "+TableName+" where "+KEY_TITLE+" = '"+Title+"'";
        return (int) DatabaseUtils.longForQuery(sqLiteDatabase,DATABASE_COMPARE,null)>0;
    }

    public int setBlob(String TableName, int rowId, byte[] imageArray){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_IMAGE_BLOB,imageArray);
        return sqLiteDatabase.update(TableName,contentValues,KEY_ROWID +" = "+rowId,null);
    }

    public int setPaheNum(String TableName, int rowId, int page_num){
        if(!TableName.equals(DATABASE_TABLE_READING))
            return -1;
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PAGE_NUM,page_num);
        return sqLiteDatabase.update(TableName,contentValues,KEY_ROWID +" = "+rowId,null);
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            /*
            db.execSQL(
                    "CREATE TABLE " +
                            DATABASE_TABLE + " ( " +
                            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_TITLE + " TEXT, " +
                            KEY_AUTHORS + " TEXT, " +
                            KEY_RATING + " TEXT, " +
                            KEY_PUBLISHDATE + " TEXT, " +
                            KEY_CATEGORIES + " TEXT, " +
                            KEY_IMAGELINKS + " TEXT, " +
                            KEY_LANGUAGE + " TEXT, " +
                            KEY_DESCRIPTION + " TEXT, " +
                            KEY_IMAGE_BLOB + " BLOB " + " );"
            );
            */

            db.execSQL(
                    "CREATE TABLE " +
                            DATABASE_TABLE_HAVE_READ + " ( " +
                            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_TITLE + " TEXT, " +
                            KEY_AUTHORS + " TEXT, " +
                            KEY_RATING + " TEXT, " +
                            KEY_PUBLISHDATE + " TEXT, " +
                            KEY_CATEGORIES + " TEXT, " +
                            KEY_IMAGELINKS + " TEXT, " +
                            KEY_LANGUAGE + " TEXT, " +
                            KEY_DESCRIPTION + " TEXT, " +
                            KEY_IMAGE_BLOB + " BLOB " + " );"
            );

            db.execSQL(
                    "CREATE TABLE " +
                            DATABASE_TABLE_READING + " ( " +
                            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_TITLE + " TEXT, " +
                            KEY_AUTHORS + " TEXT, " +
                            KEY_RATING + " TEXT, " +
                            KEY_PUBLISHDATE + " TEXT, " +
                            KEY_CATEGORIES + " TEXT, " +
                            KEY_IMAGELINKS + " TEXT, " +
                            KEY_LANGUAGE + " TEXT, " +
                            KEY_DESCRIPTION + " TEXT, " +
                            KEY_PAGE_NUM + " TEXT, " +
                            KEY_IMAGE_BLOB + " BLOB " + " );"
            );

            db.execSQL(
                    "CREATE TABLE " +
                            DATABASE_TABLE_WANT_TO_READ + " ( " +
                            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_TITLE + " TEXT, " +
                            KEY_AUTHORS + " TEXT, " +
                            KEY_RATING + " TEXT, " +
                            KEY_PUBLISHDATE + " TEXT, " +
                            KEY_CATEGORIES + " TEXT, " +
                            KEY_IMAGELINKS + " TEXT, " +
                            KEY_LANGUAGE + " TEXT, " +
                            KEY_DESCRIPTION + " TEXT, " +
                            KEY_IMAGE_BLOB + " BLOB " + " );"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_READING);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_WANT_TO_READ);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_HAVE_READ);

            onCreate(db);
        }
    }

}
