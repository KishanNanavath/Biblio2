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
    CategoryAdapter adapterFiction;
    ArrayList<BooksGetter> genreBooks;
    SwipyRefreshLayout srl;

    RecyclerView recRomance;
    CategoryAdapter adapterRomance;
    ArrayList<BooksGetter> romanceBooks;
    SwipyRefreshLayout srlRomance;

    RecyclerView recAdventure;
    CategoryAdapter adapterAdventure;
    ArrayList<BooksGetter> AdventureBooks;
    SwipyRefreshLayout srlAdventure;

    RecyclerView recBiography;
    CategoryAdapter adapterBiography;
    ArrayList<BooksGetter> BiographyBooks;
    SwipyRefreshLayout srlBiography;

    final String url = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genre_page, container, false);

        srl = (SwipyRefreshLayout) view.findViewById(R.id.srlFiction);
        genreBooks = new ArrayList<>();
        recFiction = (RecyclerView)view.findViewById(R.id.rvFiction);
        adapterFiction = new CategoryAdapter(genreBooks,getContext());

        try {
            new BooksAsyncTask(getActivity(),genreBooks,adapterFiction,recFiction).execute(url+ URLEncoder.encode("subject:\"Fiction\"","UTF-8")+"&maxResults=40&printType=books&orderBy=newest",0,srl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        recFiction.setAdapter(adapterFiction);
        recFiction.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        //************************************************************8
        srlRomance = (SwipyRefreshLayout) view.findViewById(R.id.srlRomance);
        romanceBooks = new ArrayList<>();
        recRomance = (RecyclerView)view.findViewById(R.id.rvRomance);
        adapterRomance = new CategoryAdapter(romanceBooks,getContext());

        try {
            new BooksAsyncTask(getActivity(),romanceBooks,adapterRomance,recRomance).execute(url+ URLEncoder.encode("subject:\"Romance\"","UTF-8")+"&maxResults=40&printType=books&orderBy=newest",0,srl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        recRomance.setAdapter(adapterRomance);
        recRomance.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        //************************************************************8
        srlAdventure = (SwipyRefreshLayout) view.findViewById(R.id.srlAdventure);
        AdventureBooks = new ArrayList<>();
        recAdventure = (RecyclerView)view.findViewById(R.id.rvAdventure);
        adapterAdventure = new CategoryAdapter(AdventureBooks,getContext());

        try {
            new BooksAsyncTask(getActivity(),AdventureBooks,adapterAdventure,recAdventure).execute(url+ URLEncoder.encode("subject:\"Adventure\"","UTF-8")+"&maxResults=40&printType=books&orderBy=newest",0,srl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        recAdventure.setAdapter(adapterAdventure);
        recAdventure.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        //************************************************************8
        srlBiography = (SwipyRefreshLayout) view.findViewById(R.id.srlBiography);
        BiographyBooks = new ArrayList<>();
        recBiography = (RecyclerView)view.findViewById(R.id.rvBiography);
        adapterBiography = new CategoryAdapter(BiographyBooks,getContext());

        try {
            new BooksAsyncTask(getActivity(),BiographyBooks,adapterBiography,recBiography).execute(url+ URLEncoder.encode("subject:\"Biography\"","UTF-8")+"&maxResults=40&printType=books&orderBy=newest",0,srl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        recBiography.setAdapter(adapterBiography);
        recBiography.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        return view;
    }

}
