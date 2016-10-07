package com.example.kishan.biblio.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kishan.biblio.Database.BooksDatabase;
import com.example.kishan.biblio.Fragments.AdvancedSearchFragment;
import com.example.kishan.biblio.Fragments.BookDetailsList;
import com.example.kishan.biblio.Fragments.CategoriesPage;
import com.example.kishan.biblio.GenrePage;
import com.example.kishan.biblio.MainActivity;
import com.example.kishan.biblio.R;

import java.util.ArrayList;

/**
 * Created by Kishan on 2/17/2016.
 */
public class NavDrawAdapter extends RecyclerView.Adapter<NavDrawAdapter.MyViewHolder> {

    Context con;
    ArrayList<String> optionsArray;
    LayoutInflater layInf;
    FragmentManager fm;
    public DrawerLayout drawerLayout;
    public View navDrawView;

    int fragInAni = 0;
    int fragOutANi = 0;
    int fragPopIn = 0;
    int fragPopOut = 0;

    public NavDrawAdapter(Context con, ArrayList<String> optionsArray, FragmentManager fm) {
        this.con = con;
        this.optionsArray = optionsArray;
        layInf = LayoutInflater.from(con);
        this.fm = fm;

        fragInAni = ((MainActivity)con).fragInAni;
        fragOutANi = ((MainActivity)con).fragOutANi;
        fragPopIn = ((MainActivity)con).fragPopIn;
        fragPopOut = ((MainActivity)con).fragPopOut;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layInf.inflate(R.layout.row_rec_navdraw, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.option.setText(optionsArray.get(position));


        BooksDatabase database = new BooksDatabase(con);
        database.open();

        int numRows = 0;
        switch (optionsArray.get(position)) {
            case "Have Read":
                numRows = database.getNumRows(database.DATABASE_TABLE_HAVE_READ);
                holder.navIcon.setImageResource(R.drawable.book_plus);
                break;
            case "Reading":
                numRows = database.getNumRows(database.DATABASE_TABLE_READING);
                holder.navIcon.setImageResource(R.drawable.book_open_page_variant);
                break;
            case "Want to Read":
                numRows = database.getNumRows(database.DATABASE_TABLE_WANT_TO_READ);
                holder.navIcon.setImageResource(R.drawable.bookmark);
                break;
            case "Search Books":
                holder.navIcon.setImageResource(R.drawable.magnify);
                break;
            case "Categories":
                holder.navIcon.setImageResource(R.drawable.magnify);
                break;
            case "Close Library":
                break;
        }
        Log.d("NUm rows :", database.getNumRows(database.DATABASE_TABLE_HAVE_READ) + "");
        database.close();

        holder.numOfRows.setText(numRows + "");

        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(navDrawView);
                switch (holder.option.getText().toString()) {
                    case "Have Read":
                        ((MainActivity)con).currentType = "HaveRead";
                        callFragment("HaveRead");
                        break;
                    case "Reading":
                        ((MainActivity)con).currentType = "Reading";
                        callFragment("Reading");
                        break;
                    case "Want to Read":
                        ((MainActivity)con).currentType = "WantToRead";
                        callFragment("WantToRead");
                        break;
                    case "Advanced Search":
                        callASF();
                        break;
                    case "Categories":
                        callCategories();
                        break;
                    case "Close Library":
                        ((Activity)con).finish();
                        break;
                }
            }

            private void callASF() {
                String type = AdvancedSearchFragment.class.getName();

                Fragment upFrag = fm.findFragmentByTag(type);
                if(upFrag != null && upFrag.isVisible()){
                    Log.d("In ASF","FALSE");
                    return;
                }

                Log.d("In ASF","TRUE");

                if (fm.getBackStackEntryCount()>0 && type.equals(fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName()))
                    return;
                AdvancedSearchFragment asf = new AdvancedSearchFragment();

                boolean isFragmentInBackStack = fm.popBackStackImmediate(AdvancedSearchFragment.class.getName(), 0);
                if (!isFragmentInBackStack) {
                    FragmentTransaction ft = fm.beginTransaction();
                    //ft.setCustomAnimations(R.anim.a_come_in, R.anim.b_come_out, R.anim.b_come_in, R.anim.a_come_out);
//                    ft.setCustomAnimations(fragInAni,fragOutANi,fragPopIn,fragPopOut);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                    ft.replace(R.id.fInnerContainers, asf, AdvancedSearchFragment.class.getName());
                    ft.addToBackStack(AdvancedSearchFragment.class.getName());
                    ft.commit();
                }
            }

            private void callCategories() {
                String type = CategoriesPage.class.getName();

                Fragment upFrag = fm.findFragmentByTag(type);
                if(upFrag != null && upFrag.isVisible()){
                    Log.d("In ASF","FALSE");
                    return;
                }

                Log.d("In ASF","TRUE");

                if (fm.getBackStackEntryCount()>0 && type.equals(fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName()))
                    return;
                GenrePage asf = new GenrePage();

                boolean isFragmentInBackStack = fm.popBackStackImmediate(CategoriesPage.class.getName(), 0);
                if (!isFragmentInBackStack) {
                    FragmentTransaction ft = fm.beginTransaction();
                    //ft.setCustomAnimations(R.anim.a_come_in, R.anim.b_come_out, R.anim.b_come_in, R.anim.a_come_out);
//                    ft.setCustomAnimations(fragInAni,fragOutANi,fragPopIn,fragPopOut);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                    ft.replace(R.id.fInnerContainers, asf, CategoriesPage.class.getName());
                    ft.addToBackStack(CategoriesPage.class.getName());
                    ft.commit();
                }
            }

            private void callFragment(String type) {
                if (fm.getBackStackEntryCount() > 0 && type.equals(fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName()))
                    return;
                BookDetailsList bdl = new BookDetailsList();
                Bundle bundle = new Bundle();
                bundle.putString("URL", "");
                bundle.putString("TYPE", ((MainActivity)con).currentType);
                bdl.setArguments(bundle);

                boolean isFragmentInBackStack = fm.popBackStackImmediate(type, 0);
                if (!isFragmentInBackStack) {
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

//                    ft.setCustomAnimations(R.anim.a_come_in, R.anim.b_come_out, R.anim.b_come_in, R.anim.a_come_out);
                    //ft.setCustomAnimations(fragInAni,fragOutANi,fragPopIn,fragPopOut);
                    ft.replace(R.id.fInnerContainers, bdl, type);
                    ft.addToBackStack(type);
                    ft.commit();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return optionsArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView option;
        TextView numOfRows;
        ImageView navIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            option = (TextView) itemView.findViewById(R.id.tvOptions);
            numOfRows = (TextView) itemView.findViewById(R.id.tvNumRows);
            navIcon = (ImageView) itemView.findViewById(R.id.ivNavIcon);
        }
    }
}

