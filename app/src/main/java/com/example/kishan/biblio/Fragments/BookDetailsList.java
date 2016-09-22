package com.example.kishan.biblio.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.kishan.biblio.Adapters.BooksDetailsAdapter;
import com.example.kishan.biblio.Getters.BooksGetter;
import com.example.kishan.biblio.MainActivity;
import com.example.kishan.biblio.R;
import com.example.kishan.biblio.Tasks.BooksAsyncTask;
import com.example.kishan.biblio.Tasks.ReadDatabaseTask;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookDetailsList extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SwipyRefreshLayout.OnRefreshListener {

    RecyclerView BooksList;
    ArrayList<BooksGetter> booksArray;
    View view;
    SwipyRefreshLayout srl;
//    SwipeRefreshLayout srl;
    BooksDetailsAdapter adapter;
    FloatingActionButton fab;

    String url;
    String type;
    int startIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view != null){
            if(Build.VERSION.SDK_INT<=11)
                ((ViewGroup)view.getParent()).removeView(view);
        }else{
            view = inflater.inflate(R.layout.rec_view_layout, container, false);

            fab = (FloatingActionButton)view.findViewById(R.id.fbFab);
            fab.setVisibility(View.VISIBLE);

            srl = (SwipyRefreshLayout) view.findViewById(R.id.srlGetMoreData);

            srl.setOnRefreshListener(this);

            BooksList = (RecyclerView)view.findViewById(R.id.rvRecView);
            BooksList.setOnScrollListener(scrollListener);
            booksArray = new ArrayList<>();

            Bundle bundle = this.getArguments();
            url = bundle.getString("URL");
            type = bundle.getString("TYPE");

            FragmentManager fm = getActivity().getSupportFragmentManager();
            adapter = new BooksDetailsAdapter(booksArray,getActivity(),fm,type);

            Log.d("TYPE :",type);

            callReqTask();

            BooksList.setAdapter(adapter);
            BooksList.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(type);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(null);

        return view;
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager manager = ((LinearLayoutManager)recyclerView.getLayoutManager());
            boolean enabled =manager.findFirstCompletelyVisibleItemPosition() == 0;
            srl.setEnabled(enabled);
        }
    };

    private void callReqTask() {
        switch (type){
            case "HaveRead":
                //Toast.makeText(getActivity(),"have read",Toast.LENGTH_SHORT).show();
                view.setBackgroundColor(Color.parseColor("#4FC3F7"));
                new ReadDatabaseTask(getActivity(),booksArray,adapter,BooksList).execute("BookDetailsHaveRead");
                break;
            case "Reading":
                //Toast.makeText(getActivity(),"reading",Toast.LENGTH_SHORT).show();
                view.setBackgroundColor(Color.parseColor("#FFD54F"));
                new ReadDatabaseTask(getActivity(),booksArray,adapter,BooksList).execute("BookDetailsReading");
                break;
            case "WantToRead":
                //Toast.makeText(getActivity(),"wanttoread",Toast.LENGTH_SHORT).show();
                view.setBackgroundColor(Color.parseColor("#E57373"));
                new ReadDatabaseTask(getActivity(),booksArray,adapter,BooksList).execute("BookDetailsWantToRead");
                break;
            case "online":
                view.setBackgroundColor(Color.parseColor("#dddddd"));
                new BooksAsyncTask(getActivity(),booksArray,adapter,BooksList).execute(url,startIndex,srl);
        }
    }

    @Override
    public void onRefresh() {
        if(type.equals("online")){
            startIndex = booksArray.size();
            Log.d("StartIndex :",startIndex+"");
            new BooksAsyncTask(getActivity(),booksArray,adapter,BooksList).execute(url+"&startIndex="+startIndex,startIndex,srl);
        }else{
            srl.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        Log.d("MainActivity", "Refresh triggered at "
                + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
        if(type.equals("online")){
            startIndex = booksArray.size();
            Log.d("StartIndex :",startIndex+"");
            new BooksAsyncTask(getActivity(),booksArray,adapter,BooksList).execute(url+"&startIndex="+startIndex,startIndex,srl);
        }else{
            srl.setRefreshing(false);
        }
    }
}
