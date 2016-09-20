package com.example.kishan.biblio.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.example.kishan.biblio.Adapters.BooksDetailsAdapter;
import com.example.kishan.biblio.BuildConfig;
import com.example.kishan.biblio.Getters.BooksGetter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class BooksAsyncTask extends AsyncTask {
    ProgressDialog bar;
    Context con;
    int endPos;
    BooksDetailsAdapter mBookAdapet;
    ArrayList<BooksGetter> mBookArray;
    RecyclerView mRecView;
    int nItems;
    SwipeRefreshLayout srl;
    int startPos;

    public BooksAsyncTask(Context c, ArrayList<BooksGetter> myBooksArray, BooksDetailsAdapter myAdapter, RecyclerView myRecView) {
        this.nItems = 0;
        this.startPos = 0;
        this.endPos = 0;
        this.con = c;
        this.mRecView = myRecView;
        this.mBookAdapet = myAdapter;
        this.mBookArray = myBooksArray;
    }

    protected void onPreExecute() {
        this.bar = new ProgressDialog(this.con);
        this.bar.setTitle("Getting the Books");
        this.bar.setMessage("Loading...");
        this.bar.setIndeterminate(true);
        this.bar.show();
    }

    protected Object doInBackground(Object[] params) {
        String Data = BuildConfig.FLAVOR;
        this.startPos = ((Integer) params[1]).intValue();
        this.srl = (SwipeRefreshLayout) params[2];
        try {
            HttpClient client = new DefaultHttpClient();
            HttpParams httpParams = client.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
            HttpConnectionParams.setSoTimeout(httpParams, 5000);
            HttpGet get = new HttpGet((String) params[0]);
            Data = (String) params[0];
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                getJsonData(EntityUtils.toString(response.getEntity()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    private void getJsonData(String data) throws JSONException {
        JSONArray itemsArray = new JSONObject(data).getJSONArray("items");
        this.nItems = itemsArray.length();
        for (int i = 0; i < itemsArray.length(); i++) {
            int j;
            JSONObject volInfoObject = itemsArray.getJSONObject(i).getJSONObject("volumeInfo");
            String title = "-";
            String publishDate = "-";
            String rating = BuildConfig.FLAVOR;
            String language = "-";
            String description = "-";
            if (volInfoObject.has("title")) {
                title = volInfoObject.getString("title");
            }
            if (volInfoObject.has("publishedDate")) {
                publishDate = volInfoObject.getString("publishedDate");
            }
            if (volInfoObject.has("averageRating")) {
                rating = volInfoObject.getString("averageRating");
            }
            if (volInfoObject.has("language")) {
                language = volInfoObject.getString("language");
            }
            if (volInfoObject.has("description")) {
                description = volInfoObject.getString("description");
            }
            String sAuthors = BuildConfig.FLAVOR;
            if (volInfoObject.has("authors")) {
                JSONArray authorsArray = volInfoObject.getJSONArray("authors");
                for (j = 0; j < authorsArray.length(); j++) {
                    sAuthors = sAuthors + authorsArray.getString(j) + "~";
                }
            }
            String sCategories = BuildConfig.FLAVOR;
            if (volInfoObject.has("categories")) {
                JSONArray categoryArray = volInfoObject.getJSONArray("categories");
                for (j = 0; j < categoryArray.length(); j++) {
                    sCategories = sCategories + categoryArray.getString(j) + "~";
                }
            }
            String sImageLinks = BuildConfig.FLAVOR;
            if (volInfoObject.has("imageLinks")) {
                JSONObject imageLinksObject = volInfoObject.getJSONObject("imageLinks");
                if (imageLinksObject.has("smallThumbnail")) {
                    sImageLinks = sImageLinks + imageLinksObject.getString("smallThumbnail") + "~";
                }
                if (imageLinksObject.has("thumbnail")) {
                    sImageLinks = sImageLinks + imageLinksObject.getString("thumbnail");
                }
            }
            this.mBookArray.add(new BooksGetter(0, title, sAuthors, rating, publishDate, sCategories, sImageLinks, language, description, null));
        }
    }

    protected void onPostExecute(Object o) {
        this.bar.dismiss();
        this.mBookAdapet.notifyItemRangeInserted(this.startPos, this.startPos + this.nItems);
        this.mRecView.invalidate();
        if (this.srl.isRefreshing()) {
            this.srl.setRefreshing(false);
        }
    }
}