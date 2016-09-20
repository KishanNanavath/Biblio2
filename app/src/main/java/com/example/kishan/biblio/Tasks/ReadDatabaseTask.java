package com.example.kishan.biblio.Tasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.kishan.biblio.Adapters.BooksDetailsAdapter;
import com.example.kishan.biblio.Database.BooksDatabase;
import com.example.kishan.biblio.Getters.BooksGetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Kishan on 2/23/2016.
 */
public class ReadDatabaseTask extends AsyncTask {

    Context con;
    ArrayList<BooksGetter> booksArray;
    BooksDetailsAdapter adapter;
    RecyclerView booksList;

    public ReadDatabaseTask(Context con, ArrayList<BooksGetter> booksArray, BooksDetailsAdapter adapter, RecyclerView booksList) {
        this.con = con;
        this.booksArray = booksArray;
        this.adapter = adapter;
        this.booksList = booksList;
    }

    ProgressDialog bookDbLoad;
    @Override
    protected void onPreExecute() {
        /*
        bookDbLoad = new ProgressDialog(con);
        bookDbLoad.setIndeterminate(true);
        bookDbLoad.setTitle("Reading Database");
        bookDbLoad.setMessage("Reading...");
        bookDbLoad.show();
        */
    }

    ArrayList<BooksGetter> books;
    @Override
    protected Object doInBackground(Object[] params) {
        String tableName = params[0].toString();
        BooksDatabase database = new BooksDatabase(con);

        database.open();
        books = database.readEntries(tableName);
        database.close();

        Log.d("Size :",books.size()+"");
        //Collections.copy(booksArray,books);

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        Collections.sort(books, new Comparator<BooksGetter>() {
            @Override
            public int compare(BooksGetter a, BooksGetter b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        adapter.booksArray = books;
        //bookDbLoad.dismiss();
        adapter.notifyDataSetChanged();
        booksList.invalidate();
    }
}
