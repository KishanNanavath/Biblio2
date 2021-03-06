package com.example.kishan.biblio.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoriesPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoriesPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesPage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CategoriesPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriesPage.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoriesPage newInstance(String param1, String param2) {
        CategoriesPage fragment = new CategoriesPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories_page, container, false);

        String fictionQuery = "subject:\"Fiction\"";
        String educationQuery = "subject:\"Education\"";
        String horrorQuery = "subject:\"Horror\"";

        fictionBooksGetterArrayList = new ArrayList<>();
        CategoryAdapter categoryAdapter = new CategoryAdapter(fictionBooksGetterArrayList,getContext());
        fictionView = (RecyclerView)view.findViewById(R.id.rvFiction);

        SwipyRefreshLayout srl = (SwipyRefreshLayout)view.findViewById(R.id.srlCategoryScroll);
        new BooksAsyncTask(getActivity(),fictionBooksGetterArrayList,categoryAdapter,fictionView).execute(url+ URLEncoder.encode(fictionQuery),0,srl);

        fictionView.setAdapter(categoryAdapter);
        fictionView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
