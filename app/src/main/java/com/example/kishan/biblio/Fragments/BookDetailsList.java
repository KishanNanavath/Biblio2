package com.example.kishan.biblio.Fragments;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.example.kishan.biblio.Adapters.BooksDetailsAdapter;
import com.example.kishan.biblio.Getters.BooksGetter;
import com.example.kishan.biblio.R;
import com.example.kishan.biblio.Tasks.BooksAsyncTask;
import com.example.kishan.biblio.Tasks.ReadDatabaseTask;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.software.shell.fab.FloatingActionButton;

import java.util.ArrayList;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

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

    String url;
    String type;
    int startIndex = 0;
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view != null){
            if(Build.VERSION.SDK_INT<=11)
                ((ViewGroup)view.getParent()).removeView(view);
        }else{
            view = inflater.inflate(R.layout.rec_view_layout, container, false);

            /*
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.cross);

            FloatingActionButton actionButton = new FloatingActionButton.Builder(getActivity())
                    .setContentView(imageView)
                    .build();

            SubActionButton.Builder builder = new SubActionButton.Builder(getActivity());

            ImageView imageView1 = new ImageView(getActivity());
            imageView1.setImageResource(R.drawable.cross);
            SubActionButton button1 = builder.setContentView(imageView1).build();

            ImageView imageView2 = new ImageView(getActivity());
            imageView2.setImageResource(R.drawable.cross);
            SubActionButton button2 = builder.setContentView(imageView2).build();

            ImageView imageView3 = new ImageView(getActivity());
            imageView3.setImageResource(R.drawable.cross);
            SubActionButton button3 = builder.setContentView(imageView3).build();

            ImageView imageView4 = new ImageView(getActivity());
            imageView4.setImageResource(R.drawable.cross);
            SubActionButton button4 = builder.setContentView(imageView4).build();

            FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                    .addSubActionView(button1)
                    .addSubActionView(button2)
                    .addSubActionView(button3)
                    .addSubActionView(button4)
                    .attachTo(actionButton)
                    .build();
*/

/*
>>>>>>> 9b016e0ea1509c9c10bc1d160aecff7c396857d3
            fab = (FloatingActionButton)view.findViewById(R.id.fbFab);
            fab.setVisibility(View.VISIBLE);


<<<<<<< HEAD
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams)fab.getLayoutParams();
            p.setBehavior(new ShrinkBehavior());
            fab.setLayoutParams(p);
*/

//            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams)fab.getLayoutParams();
//            p.setBehavior(new ShrinkBehavior());
//            fab.setLayoutParams(p);


            fab = (FloatingActionButton) view.findViewById(R.id.fbFab);
            fab.setVisibility(View.VISIBLE);

            final View myView = view.findViewById(R.id.awesome_card);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get the center for the clipping circle
                    int cx = (myView.getLeft() + myView.getRight());
                    int cy = (myView.getBottom());

                    // get the final radius for the clipping circle
                    int dx = Math.max(cx, myView.getWidth() - cx);
                    int dy = Math.max(cy, myView.getHeight() - cy);
                    float finalRadius = (float) Math.hypot(dx, dy);

                    Toast.makeText(getContext(),(myView.getVisibility() == View.GONE)+"",Toast.LENGTH_SHORT).show();

                    int dur = 800;
                    if(myView.getVisibility() == View.GONE){
                        myView.setVisibility(View.VISIBLE);
                        // Android native animator
                        SupportAnimator animator =
                                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
                        animator.setInterpolator(new FastOutSlowInInterpolator());
                        animator.setDuration(dur);
                        animator.start();
                    }
                    else{
                        // Android native animator
                        SupportAnimator animator =
                                ViewAnimationUtils.createCircularReveal(myView, cx, cy, finalRadius,0);
                        animator.setInterpolator(new FastOutSlowInInterpolator());
                        animator.setDuration(dur);
                        animator.addListener(new SupportAnimator.AnimatorListener() {
                            @Override
                            public void onAnimationStart() {

                            }

                            @Override
                            public void onAnimationEnd() {
                                myView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel() {

                            }

                            @Override
                            public void onAnimationRepeat() {

                            }
                        });
                        animator.start();
                    }
                }
            });

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
                view.setBackgroundColor(Color.parseColor("#dddddd"));
//                view.setBackgroundColor(Color.parseColor("#4FC3F7"));
                new ReadDatabaseTask(getActivity(),booksArray,adapter,BooksList).execute("BookDetailsHaveRead");
                break;
            case "Reading":
                //Toast.makeText(getActivity(),"reading",Toast.LENGTH_SHORT).show();
                view.setBackgroundColor(Color.parseColor("#dddddd"));
//                view.setBackgroundColor(Color.parseColor("#FFD54F"));
                new ReadDatabaseTask(getActivity(),booksArray,adapter,BooksList).execute("BookDetailsReading");
                break;
            case "WantToRead":
                //Toast.makeText(getActivity(),"wanttoread",Toast.LENGTH_SHORT).show();
                view.setBackgroundColor(Color.parseColor("#dddddd"));
//                view.setBackgroundColor(Color.parseColor("#E57373"));
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
