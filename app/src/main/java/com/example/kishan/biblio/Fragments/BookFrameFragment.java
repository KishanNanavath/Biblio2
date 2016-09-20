package com.example.kishan.biblio.Fragments;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kishan.biblio.Database.BooksDatabase;
import com.example.kishan.biblio.Getters.BooksGetter;
import com.example.kishan.biblio.R;
import com.example.kishan.biblio.Tasks.ImageLoadTask;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFrameFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    View view;
    BooksGetter thisBook;
    ImageView bookImg;
    TextView title;
    TextView authors;
    TextView category;
    RatingBar rating;
    TextView description;
    TextView tvrating;
    LinearLayout imgBg;
    Button addBook;
    String type;
    View svView;
    CardView mainDetails;
    CardView cdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_book_frame, container, false);

        svView = view.findViewById(R.id.svInnerView);

        mainDetails = (CardView)view.findViewById(R.id.cvMainDetails);
        cdView = (CardView)view.findViewById(R.id.cdView);

        Bundle bundle = this.getArguments();
        thisBook = (BooksGetter) bundle.getSerializable("BookDetails");
        type = bundle.getString("TYPE");

        addBook = (Button) view.findViewById(R.id.bAddBook);
        addBook.setOnClickListener(this);

        imgBg = (LinearLayout) view.findViewById(R.id.llImgBg);
        bookImg = (ImageView) view.findViewById(R.id.ivBookImg);
        bookImg.setOnClickListener(this);

        if (type.equals("online") && !thisBook.getImageLinks().split("~")[0].equals("")) {
            Picasso.with(bookImg.getContext())
                    .load(thisBook.getImageLinks().split("~")[0].trim())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(bookImg);
        } else {
            Bitmap bmp = BitmapFactory.decodeByteArray(thisBook.getImageByteArray(), 0, thisBook.getImageByteArray().length);
            bookImg.setImageBitmap(bmp);
        }

        title = (TextView) view.findViewById(R.id.tvBookTitle);
        authors = (TextView) view.findViewById(R.id.tvBookAuthors);
        category = (TextView) view.findViewById(R.id.tvBookCategory);
        rating = (RatingBar) view.findViewById(R.id.rbBookRating);
        description = (TextView) view.findViewById(R.id.tvBookDescription);
        tvrating = (TextView) view.findViewById((R.id.tvRating));

        ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(thisBook.getTitle().replaceAll("~", ""));
        title.setText(thisBook.getTitle().replaceAll("~", ""));
        authors.setText(thisBook.getAuthors().replaceAll("~", ""));
        category.setText(thisBook.getCategories().replaceAll("~", ""));
        description.setText(thisBook.getDescription().replaceAll("~", ""));

        if (thisBook.getRating().equals("")) {
            rating.setVisibility(View.GONE);
            tvrating.setText("No rating");
        } else {
            rating.setRating(Float.parseFloat(thisBook.getRating()));
        }

        imgBg.setOnTouchListener(this);
        cdView.setOnTouchListener(this);
        mainDetails.setOnTouchListener(this);

        return view;
    }

    public void imgAni(View v){
        ObjectAnimator flipOnY = ObjectAnimator.ofFloat(v,"rotationY",0,180);
        flipOnY.setDuration(1000).setStartDelay(500);
        flipOnY.start();
    }

    public void childAni(View v){
        AnimatorSet rotSet = new AnimatorSet();
        ObjectAnimator rotX = ObjectAnimator.ofFloat(v,"rotationX",-20,10,-5,0);
        ObjectAnimator rotY = ObjectAnimator.ofFloat(v,"rotationY",20,-10,5,0);

        rotSet.playTogether(rotX, rotY);
        rotSet.setDuration(1000);
        rotSet.setInterpolator(new BounceInterpolator());
        rotSet.start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivBookImg){
            imgAni(v);
        }
        if (v.getId() == R.id.bAddBook) {
            final Dialog popup = new Dialog(getActivity());
            popup.setContentView(R.layout.dialog_pop_up);
            popup.setTitle("Pick the Shelf");
            popup.show();

            final CheckBox haveRead = (CheckBox) popup.findViewById(R.id.cbHaveRead);
            final CheckBox wantToRead = (CheckBox) popup.findViewById(R.id.cbWantToRead);
            final CheckBox reading = (CheckBox) popup.findViewById(R.id.cbReading);

            switch (type) {
                case "HaveRead":
                    haveRead.setVisibility(View.GONE);
                    break;
                case "WantToRead":
                    wantToRead.setVisibility(View.GONE);
                    break;
                case "Reading":
                    reading.setVisibility(View.GONE);
                    break;
            }

            Button submit = (Button) popup.findViewById(R.id.bSubmit);
            Button cancel = (Button) popup.findViewById(R.id.bCancel);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tables = "";
                    if (reading.isChecked()) {
                        tables += "BookDetailsReading,";
                    }
                    if (haveRead.isChecked()) {
                        tables += "BookDetailsHaveRead,";
                    }
                    if (wantToRead.isChecked()) {
                        tables += "BookDetailsWantToRead";
                    }
                    popup.dismiss();
                    String[] tableNames = tables.trim().split(",");
                    saveToDatabase(tableNames);
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.dismiss();
                }
            });
        }
    }

    public void saveToDatabase(String[] tableNames) {
        BooksDatabase database = new BooksDatabase(getActivity());
        database.open();
        for (int i = 0; i < tableNames.length; i++) {
            if (!database.isEntryByTitleExists(tableNames[i], thisBook.getTitle())) {
                Long id = database.createEntry(thisBook, tableNames[i]);
                int idi = Integer.parseInt(id + "");
                new ImageLoadTask(getActivity()).execute(thisBook.getImageLinks().split("~")[0], idi, tableNames[i]);
            } else {
                Toast.makeText(getActivity(), "Entry Already Exists", Toast.LENGTH_SHORT).show();
            }
        }
        database.close();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
//            childAni(v);
        }
        return true;
    }
}
