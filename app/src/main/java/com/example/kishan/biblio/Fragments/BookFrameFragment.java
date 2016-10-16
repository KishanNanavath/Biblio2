package com.example.kishan.biblio.Fragments;


import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kishan.biblio.Database.BooksDatabase;
import com.example.kishan.biblio.Getters.BooksGetter;
import com.example.kishan.biblio.MainActivity;
import com.example.kishan.biblio.R;
import com.example.kishan.biblio.Tasks.ImageLoadTask;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFrameFragment extends Fragment implements View.OnClickListener {

    private View view;
    private BooksGetter thisBook;
    private ImageView bookImg;
    private TextView descriptionName;
    private TextView detailsName;
    private TextView title;
    private TextView authors;
    private TextView category;
    private RatingBar rating;
    private TextView description;
    private TextView tvrating;
    private LinearLayout imgBg;
    private Button addBook;
    private String type;
    private View svView;
    private CardView mainDetails;
    private CardView cdView;
    private LinearLayout imageLayout;
    Toolbar getMyBar;
    CollapsingToolbarLayout ctl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_book_frame, container, false);
        Bundle bundle = this.getArguments();
        thisBook = (BooksGetter) bundle.getSerializable("BookDetails");
        type = bundle.getString("TYPE");

        initChilds();

        if(((MainActivity)getContext()).myBar.getVisibility() == View.VISIBLE){
            ((MainActivity)getContext()).myBar.setVisibility(View.GONE);
            ((MainActivity)getContext()).setSupportActionBar(getMyBar);
            ((MainActivity)getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity)getContext()).getSupportActionBar().setSubtitle(thisBook.getTitle().replaceAll("~", ""));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        setValues();
//        setNavDrawer();

        return view;
    }

    private void setNavDrawer() {
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.ndNavDraw);
        drawerFragment.setUp(R.id.ndNavDraw, ((MainActivity)getActivity()).drawerLayout, getMyBar);
    }

    private void initChilds() {
        descriptionName = (TextView)view.findViewById(R.id.tvDescription);
        detailsName = (TextView)view.findViewById(R.id.tvDetails);
        getMyBar = (Toolbar)view.findViewById(R.id.MyToolbar);
        bookImg = (ImageView) view.findViewById(R.id.ivBookImg);
        ctl = (CollapsingToolbarLayout)view.findViewById(R.id.collapse_toolbar);
        addBook = (Button) view.findViewById(R.id.bAddBook);
        title = (TextView) view.findViewById(R.id.tvBookTitle);
        authors = (TextView) view.findViewById(R.id.tvBookAuthors);
        category = (TextView) view.findViewById(R.id.tvBookCategory);
        rating = (RatingBar) view.findViewById(R.id.rbBookRating);
        description = (TextView) view.findViewById(R.id.tvBookDescription);
        tvrating = (TextView) view.findViewById((R.id.tvRating));
    }

    private void setValues() {
        ctl.setTitle(thisBook.getTitle().replaceAll("~", ""));
        getMyBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        descriptionName.setTypeface(((MainActivity) getContext()).robotoRegular);
        detailsName.setTypeface(((MainActivity) getContext()).robotoRegular);

        title.setText(thisBook.getTitle().replaceAll("~", ""));
        title.setTypeface(((MainActivity) getContext()).robotoCondensedBold);

        authors.setText(thisBook.getAuthors().replaceAll("~", ""));
        authors.setTypeface(((MainActivity) getContext()).robotoRegular);

        category.setText(thisBook.getCategories().replaceAll("~", ""));
        category.setTypeface(((MainActivity) getContext()).robotoRegular);

        description.setText(thisBook.getDescription().replaceAll("~", ""));
        description.setTypeface(((MainActivity) getContext()).robotoCondensedRegular);

        if (thisBook.getRating().equals("")) {
            rating.setVisibility(View.GONE);
            tvrating.setText("No rating");
        } else {
            rating.setRating(Float.parseFloat(thisBook.getRating()));
        }

        addBook.setOnClickListener(this);
        bookImg.setOnClickListener(this);

        if (type.equals("online")) {
            if(thisBook.getImageLinks().split("~").length>0 && !thisBook.getImageLinks().split("~")[0].equals("")){
                Picasso.with(bookImg.getContext())
                        .load(thisBook.getImageLinks().split("~")[0].trim())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(bookImg);
            }
            else if(thisBook.getImageLinks().split("~").length>1 && !thisBook.getImageLinks().split("~")[1].equals("")){
                Picasso.with(bookImg.getContext())
                        .load(thisBook.getImageLinks().split("~")[1].trim())
                        .placeholder(R.mipmap.ic_launcher)
                        .into(bookImg);
            }
        } else {
            Bitmap bmp = BitmapFactory.decodeByteArray(thisBook.getImageByteArray(), 0, thisBook.getImageByteArray().length);
            bookImg.setImageBitmap(bmp);
        }
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
                    if(reading.isChecked() || haveRead.isChecked() || wantToRead.isChecked()){
                        String[] tableNames = tables.trim().split(",");
                        saveToDatabase(tableNames);
                    }
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
                if(thisBook.getImageLinks().split("~").length >= 0)
                    new ImageLoadTask(getActivity()).execute(thisBook.getImageLinks().split("~")[0], idi, tableNames[i]);
            } else {
                Toast.makeText(getActivity(), "Entry Already Exists", Toast.LENGTH_SHORT).show();
            }
        }
        database.close();
    }
}
