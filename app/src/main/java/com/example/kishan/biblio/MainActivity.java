package com.example.kishan.biblio;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.kishan.biblio.Fragments.AdvancedSearchFragment;
import com.example.kishan.biblio.Fragments.BookDetailsList;
import com.example.kishan.biblio.Fragments.CategoriesPage;
import com.example.kishan.biblio.Fragments.NavigationDrawerFragment;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

    public Toolbar myBar;
    DrawerLayout drawerLayout;
    SearchView searchView;
    private Menu mMenu;
    public String currentType;
    Toolbar getMyBar;
    CollapsingToolbarLayout ctl;


    public int fragInAni = 0;
    public int fragOutANi = 0;
    public int fragPopIn = 0;
    public int fragPopOut = 0;
    final String url = "https://www.googleapis.com/books/v1/volumes?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentType = "";

//        setContentView(R.layout.coord_layout);
//        getMyBar = (Toolbar)findViewById(R.id.MyToolbar);
//        setSupportActionBar(getMyBar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        ctl = (CollapsingToolbarLayout)findViewById(R.id.collapse_toolbar);
//        ctl.setTitle("Yo Homies");
//
//        ctl.setContentScrimColor(ContextCompat.getColor(getApplicationContext(),R.color.accentColor));


        setContentView(R.layout.activity_main);
        setDefaultAnimationResources();
        setToolBar();
        setNavDrawer();

        AdvancedSearchFragment asf = new AdvancedSearchFragment();
//        CategoriesPage asf = new CategoriesPage();

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        //ft.setCustomAnimations(R.anim.a_come_in, R.anim.b_come_out, R.anim.b_come_in, R.anim.a_come_out);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.add(R.id.fInnerContainers, asf, AdvancedSearchFragment.class.getName());
        //ft.addToBackStack(AdvancedSearchFragment.class.getName());
        ft.commit();

        getBaseContext().setTheme(R.style.AppTheme_BaseMy);
        //recreate();
    }

    private void setDefaultAnimationResources() {
        fragInAni = R.anim.slide_frag_in;
        fragOutANi = R.anim.slide_frag_out;
        fragPopIn = R.anim.slide_frag_pop_in;
        fragPopOut = R.anim.slide_frag_pop_out;
    }

    private void setToolBar() {
        myBar = (Toolbar) findViewById(R.id.tbBar);
        setSupportActionBar(myBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setNavDrawer() {
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.ndNavDraw);
        drawerLayout = (DrawerLayout) findViewById(R.id.dlMainContainer);
        drawerFragment.setUp(R.id.ndNavDraw, drawerLayout, myBar);
    }

    @Override
    public void onBackPressed() {

         if(this.drawerLayout.isDrawerOpen(GravityCompat.START))
            this.drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        searchView = (SearchView)MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        Log.d("Status",searchView.toString());
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.miFlipAnimation) {
            fragInAni = R.anim.slide_frag_in;
            fragOutANi = R.anim.slide_frag_out;
            fragPopIn = R.anim.slide_frag_pop_in;
            fragPopOut = R.anim.slide_frag_pop_out;
        }

        if (id == R.id.miSlideAnimation) {
            fragInAni = R.anim.a_come_in;
            fragOutANi = R.anim.b_come_out;
            fragPopIn = R.anim.b_come_in;
            fragPopOut = R.anim.a_come_out;
        }

        if (id == R.id.search) {
//            Toast.makeText(getApplicationContext(),"start",Toast.LENGTH_SHORT).show();
//            int cx = (myBar.getLeft() + myBar.getRight())/2;
//            int cy = (myBar.getTop() + myBar.getBottom())/2;
//
//            // get the final radius for the clipping circle
//            int dx = Math.max(cx, myBar.getWidth() - cx);
//            int dy = Math.max(cy, myBar.getHeight() - cy);
//            float finalRadius = (float) Math.hypot(dx, dy);
//
//            int dur = 2000;
//            SupportAnimator animator =
//                    ViewAnimationUtils.createCircularReveal(myBar, cx, cy, finalRadius,0 );
//            animator.setInterpolator(new FastOutSlowInInterpolator());
//            animator.addListener(new SupportAnimator.AnimatorListener() {
//                @Override
//                public void onAnimationStart() {
//                    myBar.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onAnimationEnd() {
//                    myBar.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onAnimationCancel() {
//
//                }
//
//                @Override
//                public void onAnimationRepeat() {
//
//                }
//            });
//            animator.setDuration(dur);
//            animator.start();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.setQuery("", false);
        searchView.setIconified(true);

        MenuItemCompat.collapseActionView(mMenu.findItem(R.id.search));

        try {
            searchQuery(query);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.d("Text Submit : ",query);
        return false;
    }

    private void searchQuery(String query) throws UnsupportedEncodingException {
        this.currentType = "online";

        String url = "https://www.googleapis.com/books/v1/volumes?q=";

        String searchTerms = URLEncoder.encode(query,"UTF-8")+"&maxResults=40&printType=books&orderBy=newest";
        Log.d("Query :",searchTerms);

        BookDetailsList bdl = new BookDetailsList();
        Bundle bundle = new Bundle();
        bundle.putString("URL", url + searchTerms);
        bundle.putString("TYPE", "online");
        bdl.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fInnerContainers, bdl);
        ft.addToBackStack(bdl.getClass().getName());
        ft.commit();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("Text Change : ",newText);
        return false;
    }
}
