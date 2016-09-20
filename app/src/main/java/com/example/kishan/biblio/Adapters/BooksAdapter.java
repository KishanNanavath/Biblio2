package com.example.kishan.biblio.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kishan.biblio.Getters.BooksGetter;
import com.example.kishan.biblio.R;

import java.util.ArrayList;

/**
 * Created by Kishan on 2/6/2016.
 */
public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    ArrayList<BooksGetter> booksArray;
    Context c;
    LayoutInflater layInf;

    public BooksAdapter(ArrayList<BooksGetter> booksArray, Context con, String type, Activity act) {
        this.booksArray = booksArray;
        this.c = con;
        layInf = LayoutInflater.from(c);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layInf.inflate(R.layout.row_cardview, parent, false);
        MyViewHolder BooksHolder = new MyViewHolder(view);
        return BooksHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final BooksGetter curBook = booksArray.get(position);
        holder.title.setText(curBook.getTitle());
        holder.authors.setText(curBook.getAuthors().replaceAll("~", "\n"));
        holder.category.setText(curBook.getCategories().replaceAll("~", "\n"));
        holder.rating.setText(curBook.getRating());
    }

    @Override
    public int getItemCount() {
        return booksArray.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView bookImage;
        TextView title;
        TextView authors;
        TextView category;
        TextView rating;

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
