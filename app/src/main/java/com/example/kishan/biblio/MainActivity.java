package com.example.kishan.biblio;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kishan.biblio.Fragments.AdvancedSearchFragment;
import com.example.kishan.biblio.Fragments.CategoriesPage;
import com.example.kishan.biblio.Fragments.NavigationDrawerFragment;

public class MainActivity extends ActionBarActivity {

    public Toolbar myBar;
    DrawerLayout drawerLayout;

    public int fragInAni = 0;
    public int fragOutANi = 0;
    public int fragPopIn = 0;
    public int fragPopOut = 0;
    final String url = "https://www.googleapis.com/books/v1/volumes?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        setDefaultAnimationResources();
        setToolBar();
        setNavDrawer();

//        AdvancedSearchFragment asf = new AdvancedSearchFragment();
        CategoriesPage asf = new CategoriesPage();
//        BookDetailsList asf = new BookDetailsList();
//        Bundle bundle = new Bundle();
//        bundle.putString("URL", url+"q=King&printType=books");
//        bundle.putString("TYPE", "online");
//        asf.setArguments(bundle);

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        searchView
//                .setSearchableInfo(searchManager.
//                        getSearchableInfo(getComponentName()));
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

//        if (id == R.id.search) {
//            AdvancedSearchFragment myDialog = new AdvancedSearchFragment();
//
//            myDialog.show(getSupportFragmentManager().beginTransaction(),"My Dialog");
//        }

        return super.onOptionsItemSelected(item);
    }

}
