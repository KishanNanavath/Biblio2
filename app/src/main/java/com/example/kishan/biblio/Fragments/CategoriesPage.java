package com.example.kishan.biblio.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.kishan.biblio.Adapters.CategoryAdapter;
import com.example.kishan.biblio.Getters.BooksGetter;
import com.example.kishan.biblio.MainActivity;
import com.example.kishan.biblio.R;
import com.example.kishan.biblio.Tasks.BooksAsyncTask;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.net.URLEncoder;
import java.util.ArrayList;

public class CategoriesPage extends Fragment {

    final String url = "https://www.googleapis.com/books/v1/volumes?q=";

    RecyclerView fictionView;
    ArrayList<BooksGetter> fictionBooksGetterArrayList;

    SwipyRefreshLayout srl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.primaryColorDark));
        }

        if(((MainActivity)getContext()).myBar.getVisibility() == View.GONE)
            ((MainActivity)getContext()).myBar.setVisibility(View.VISIBLE);

        View view = inflater.inflate(R.layout.fragment_categories_page, container, false);

        String fictionQuery = "subject:'Fiction'";

        fictionBooksGetterArrayList = new ArrayList<>();
        CategoryAdapter categoryAdapter = new CategoryAdapter(fictionBooksGetterArrayList,getContext());
        fictionView = (RecyclerView)view.findViewById(R.id.rvFiction);

        srl = (SwipyRefreshLayout)view.findViewById(R.id.srlFiction);
        String fullLink = url+ URLEncoder.encode(fictionQuery);
        Log.d("Link : ",fullLink);
        new BooksAsyncTask(getContext(),fictionBooksGetterArrayList,categoryAdapter,fictionView).execute(fullLink,0,srl);

        fictionView.setAdapter(categoryAdapter);
        fictionView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        return view;
    }
}
