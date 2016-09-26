package com.example.kishan.biblio.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kishan.biblio.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdvancedSearchFragment extends DialogFragment implements View.OnClickListener, TextView.OnEditorActionListener {
    EditText title;
    EditText authors;
    EditText keywords;
    EditText isbn;
    Button bSearch;

    View view;

    final String url = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            if (Build.VERSION.SDK_INT <= 11)
                ((ViewGroup) view.getParent()).removeView(view);
        } else {
            getDialog().setTitle("Advanced Search");
            view = inflater.inflate(R.layout.fragment_advanced_search, container, false);

            title = (EditText) view.findViewById(R.id.etSearchTitle);
            title.setOnEditorActionListener(this);

            authors = (EditText) view.findViewById(R.id.etSearchAuthor);
            authors.setOnEditorActionListener(this);

            keywords = (EditText) view.findViewById(R.id.etSearchKeywords);
            keywords.setOnEditorActionListener(this);

            isbn = (EditText) view.findViewById(R.id.etIsbn);
            isbn.setOnEditorActionListener(this);

            /*
            bSearch = (Button) view.findViewById(R.id.bSearchFields);
            bSearch.setOnClickListener(this);
            */
        }
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Library");
        ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle(null);
        return view;
    }

    @Override
    public void onClick(View v) {
        /*
        if (v.getId() == R.id.bSearchFields) {
            setFragment();
        }
        */
    }

    public void setFragment() throws UnsupportedEncodingException {
        ArrayList<String> searchArray = new ArrayList<>();

        if (!keywords.getText().toString().equals("")) {
            searchArray.add(keywords.getText().toString()+ "\"");
            keywords.setText("");
        }
        if (!title.getText().toString().equals("")) {
            searchArray.add("intitle:" + "\""+ title.getText().toString()+ "\"");
            title.setText("");
        }
        if (!authors.getText().toString().equals("")) {
            searchArray.add("inauthor:" + "\""+ authors.getText().toString()+ "\"");
            authors.setText("");
        }
        if (!isbn.getText().toString().equals("")) {
            searchArray.add("isbn:" + "\""+ isbn.getText().toString()+ "\"");
            isbn.setText("");
        }

        if (searchArray.size() == 0) {
            Toast.makeText(getActivity(), "Please fill any field", Toast.LENGTH_LONG).show();
            return;
        }

        String searchTerms = "";
        searchTerms = URLEncoder.encode(TextUtils.join("+", searchArray),"UTF-8")+"&maxResults=40&printType=books";
        Log.d("Query :",searchTerms);

        BookDetailsList bdl = new BookDetailsList();
        Bundle bundle = new Bundle();
        bundle.putString("URL", url + searchTerms);
        bundle.putString("TYPE", "online");
        bdl.setArguments(bundle);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fInnerContainers, bdl);
        ft.addToBackStack(bdl.getClass().getName());
        ft.commit();
        getDialog().dismiss();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        hideKeyboard(getActivity());
        boolean ret = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            try {
                setFragment();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ret = true;
        }
        return ret;
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
