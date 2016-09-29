package com.example.kishan.biblio.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.example.kishan.biblio.Database.BooksDatabase;
import com.example.kishan.biblio.Fragments.BookFrameFragment;
import com.example.kishan.biblio.Getters.BooksGetter;
import com.example.kishan.biblio.MainActivity;
import com.example.kishan.biblio.R;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Kishan on 2/17/2016.
 */
public class BooksDetailsAdapter extends RecyclerView.Adapter<BooksDetailsAdapter.MyBDHolder> {

    public ArrayList<BooksGetter> booksArray;
    Context con;
    LayoutInflater layInf;
    FragmentManager fm;
    String type;
    int prevPos=0;

    int fragInAni = 0;
    int fragOutANi = 0;
    int fragPopIn = 0;
    int fragPopOut = 0;

    public BooksDetailsAdapter(ArrayList<BooksGetter> booksArray, Context con, FragmentManager fm, String type) {
        this.booksArray = booksArray;
        this.con = con;
        layInf = LayoutInflater.from(con);
        this.fm = fm;
        this.type = type;

        fragInAni = ((MainActivity)con).fragInAni;
        fragOutANi = ((MainActivity)con).fragOutANi;
        fragPopIn = ((MainActivity)con).fragPopIn;
        fragPopOut = ((MainActivity)con).fragPopOut;
    }

    @Override
    public MyBDHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layInf.inflate(R.layout.row_cardview, parent, false);
        MyBDHolder holder = new MyBDHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyBDHolder holder, int position) {
        final BooksGetter thisBook = booksArray.get(position);
        holder.title.setText(thisBook.getTitle());
        holder.authors.setText(thisBook.getAuthors().replaceAll("~", "\n"));
        holder.category.setText(thisBook.getCategories().replaceAll("~", "\n"));
        holder.rating.setText(thisBook.getRating());



        Log.d("TYPE In Adapter :", type);
        if (type.equals("online")) {
            if (!thisBook.getImageLinks().split("~")[0].equals("")) {
                Picasso.with(holder.bookImage.getContext())
                        .load(thisBook.getImageLinks().split("~")[0].trim())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(holder.bookImage);
            }
        } else {
            // if(thisBook.getImageByteArray().length>0){
            Bitmap bmp = BitmapFactory.decodeByteArray(thisBook.getImageByteArray(), 0, thisBook.getImageByteArray().length);
            holder.bookImage.setImageBitmap(bmp);
            //}
        }

        final View imageView = holder.bookImage;

        holder.ripContainer.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("BookDetails", thisBook);
                bundle.putString("TYPE", type);

                BookFrameFragment bff = new BookFrameFragment();
                bff.setArguments(bundle);

                FragmentTransaction ft = fm.beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                ft.replace(R.id.fInnerContainers, bff);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

//        holder.bookImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("BookDetails", thisBook);
//                bundle.putString("TYPE", type);
//
//                BookFrameFragment bff = new BookFrameFragment();
//                bff.setArguments(bundle);
//
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//
//                ft.replace(R.id.fInnerContainers, bff);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//        });

        boolean up = false;
        up = prevPos>=position;
        rowAni(holder,up,position);
        prevPos = position;
    }

    public void rowAni(MyBDHolder holder,boolean up,int pos){
        int dur = 500;

        ObjectAnimator skew;
        ObjectAnimator transX;
        if(pos%2 == 0){
            skew = ObjectAnimator.ofFloat(holder.itemView,"rotationY",45,0);
            skew.setDuration(dur*2);

            transX = ObjectAnimator.ofFloat(holder.itemView,"translationX",-90,0);
            transX.setDuration(dur);

        }
        else{
            skew = ObjectAnimator.ofFloat(holder.itemView,"rotationY",-45,0);
            skew.setDuration(dur*2);

            transX = ObjectAnimator.ofFloat(holder.itemView,"translationX",90,0);
            transX.setDuration(dur);

        }

        skew.setInterpolator(new AnticipateOvershootInterpolator());
        skew.start();


        //Log.d("Position :",pos+" : "+holder.itemView.getY()+"");
        AnimatorSet animatorSet = new AnimatorSet();


        ObjectAnimator transY = ObjectAnimator.ofFloat(holder.itemView,"translationY",up==true?-300:300,0);
        transY.setDuration(dur);

        animatorSet.playTogether(
                transX,
                transY);
        animatorSet.setInterpolator(new FastOutSlowInInterpolator());
        animatorSet.start();
    }

    @Override
    public int getItemCount() {
        return booksArray.size();
    }

    public class MyBDHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView bookImage;
        TextView title;
        TextView authors;
        TextView category;
        TextView rating;
        ImageView close;
        RippleView ripContainer;

        public MyBDHolder(View itemView) {
            super(itemView);
            bookImage = (ImageView) itemView.findViewById(R.id.ivBookImg);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            authors = (TextView) itemView.findViewById(R.id.tvAuthors);
            category = (TextView) itemView.findViewById(R.id.tvCategory);
            rating = (TextView) itemView.findViewById(R.id.tvRating);
            close = (ImageView) itemView.findViewById(R.id.ibClose);
            ripContainer = (RippleView) itemView.findViewById(R.id.ripContainer);
            close.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.ibClose:
                    AlertDialog.Builder alert = new AlertDialog.Builder(con);
                    alert.setTitle("Alert!!");
                    alert.setMessage("Are you sure to delete record");

                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do your work here
                            dialog.dismiss();
                            delete(getPosition());
                            Log.d("DELETE : ", getPosition() + "");
                        }
                    });

                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();
                    break;
            }
//            if (v.getId() == R.id.ibClose) {
//                AlertDialog.Builder alert = new AlertDialog.Builder(con);
//                alert.setTitle("Alert!!");
//                alert.setMessage("Are you sure to delete record");
//
//                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //do your work here
//                        dialog.dismiss();
//                        delete(getPosition());
//                        Log.d("DELETE : ", getPosition() + "");
//                    }
//                });
//
//                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                alert.show();
//            }
        }

        private void delete(int position) {
            Log.d("POSITION :", position + "");

            BooksDatabase database = new BooksDatabase(con);
            database.open();
            switch (type) {
                case "HaveRead":
                    database.deleteRow(booksArray.get(position).getRowId(), database.DATABASE_TABLE_HAVE_READ);
                    break;
                case "WantToRead":
                    database.deleteRow(booksArray.get(position).getRowId(), database.DATABASE_TABLE_WANT_TO_READ);
                    break;
                case "Reading":
                    database.deleteRow(booksArray.get(position).getRowId(), database.DATABASE_TABLE_READING);
                    break;
            }
            database.close();

            booksArray.remove(position);
            notifyItemRemoved(position);
        }
    }
}
