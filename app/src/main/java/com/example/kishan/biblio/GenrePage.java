package com.example.kishan.biblio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kishan.biblio.Adapters.CategoryAdapter;
import com.example.kishan.biblio.Getters.BooksGetter;
import com.example.kishan.biblio.Tasks.BooksAsyncTask;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class GenrePage extends Fragment {

    RecyclerView recFiction;
    RecyclerView recRomance;
    CategoryAdapter adapter;
    CategoryAdapter adapter1;
    ArrayList<BooksGetter> genreBooks;
    ArrayList<BooksGetter> romanceBooks;
    SwipyRefreshLayout srl;
    SwipyRefreshLayout srlRomance;

    final String url = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genre_page, container, false);

        srl = (SwipyRefreshLayout) view.findViewById(R.id.srlFiction);
        genreBooks = new ArrayList<>();
        recFiction = (RecyclerView)view.findViewById(R.id.rvFiction);
        adapter = new CategoryAdapter(genreBooks,getContext());

        try {
            new BooksAsyncTask(getActivity(),genreBooks,adapter,recFiction).execute(url+ URLEncoder.encode("subject:\"Fiction\"","UTF-8")+"&maxResults=40&printType=books&orderBy=newest",0,srl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        recFiction.setAdapter(adapter);
        recFiction.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        //************************************************************8
        srlRomance = (SwipyRefreshLayout) view.findViewById(R.id.srlRomance);
        romanceBooks = new ArrayList<>();
        recRomance = (RecyclerView)view.findViewById(R.id.rvRomance);
        adapter1 = new CategoryAdapter(romanceBooks,getContext());

        try {
            new BooksAsyncTask(getActivity(),romanceBooks,adapter1,recRomance).execute(url+ URLEncoder.encode("subject:\"Adventure\"","UTF-8")+"&maxResults=40&printType=books&orderBy=newest",0,srl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        recRomance.setAdapter(adapter1);
        recRomance.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        return view;
    }

}
