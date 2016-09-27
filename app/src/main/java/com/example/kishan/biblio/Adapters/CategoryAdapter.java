package com.example.kishan.biblio.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kishan.biblio.Getters.BooksGetter;
import com.example.kishan.biblio.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Kishan on 9/27/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.categoryHolder> {


    public ArrayList<BooksGetter> booksArray;
    Context con;
    LayoutInflater layInf;

    public CategoryAdapter(ArrayList<BooksGetter> booksArray, Context con) {
        Log.d("mk",Arrays.toString(booksArray.toArray()));
        this.booksArray = booksArray;
        this.con = con;
        layInf = LayoutInflater.from(con);
        Log.d("mnjnkk","ok");

    }

    @Override
    public categoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layInf.inflate(R.layout.category_view, parent, false);
        categoryHolder holder = new categoryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(categoryHolder holder, int position) {
        final BooksGetter thisBook = booksArray.get(position);

        Picasso.with(holder.categoryImage.getContext())
                .load(thisBook.getImageLinks().split("~")[0].trim())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.categoryImage);

        holder.categoryTitle.setText(booksArray.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        Log.d("ok",this.booksArray.size()+"");
        return this.booksArray.size();
    }

    public class categoryHolder extends RecyclerView.ViewHolder{
        TextView categoryTitle;
        ImageView categoryImage;

        public categoryHolder(View itemView) {
            super(itemView);
            categoryTitle = (TextView)itemView.findViewById(R.id.tvCategoryTitle);
            categoryImage = (ImageView)itemView.findViewById(R.id.ivCategoryImage);
        }
    }
}
