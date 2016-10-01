package com.example.kishan.biblio.Fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.example.kishan.biblio.Adapters.BooksDetailsAdapter;
import com.example.kishan.biblio.Getters.BooksGetter;
import com.example.kishan.biblio.MainActivity;
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
    private Paint p = new Paint();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view != null){
            if(Build.VERSION.SDK_INT<=11)
                ((ViewGroup)view.getParent()).removeView(view);
        }else{
            view = inflater.inflate(R.layout.rec_view_layout, container, false);

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
                        // Android native animator
                        SupportAnimator animator =
                                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
                        animator.setInterpolator(new FastOutSlowInInterpolator());
                        animator.addListener(new SupportAnimator.AnimatorListener() {
                            @Override
                            public void onAnimationStart() {
                                myView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd() {

                            }

                            @Override
                            public void onAnimationCancel() {

                            }

                            @Override
                            public void onAnimationRepeat() {

                            }
                        });
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

            initSwipe();
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(type);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(null);

        return view;
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if(!((MainActivity)getContext()).currentType.equals("online")){
                    adapter.removeItem(position);
                }
                else{
                    Toast.makeText(getContext(),"Nope",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Get RecyclerView item from the ViewHolder
                    View itemView = viewHolder.itemView;

                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    Bitmap icon;
                    Paint p = new Paint();
                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.bookmark_check);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.magnify);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(BooksList);
    }

    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
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
