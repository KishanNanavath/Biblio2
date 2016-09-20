package com.example.kishan.biblio.Tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.kishan.biblio.Database.BooksDatabase;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kishan on 2/23/2016.
 */
public class ImageLoadTask extends AsyncTask {

    Context con;

    public ImageLoadTask(Context con) {
        this.con = con;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        byte[] byteArray = null;
        String url = params[0].toString();
        int rowId = (int) params[1];
        String tableName = params[2].toString();

        try {
            URL furl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) furl.openConnection();

            InputStream is = connection.getInputStream();
            Bitmap bmp = BitmapFactory.decodeStream(is);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();

            BooksDatabase database = new BooksDatabase(con);
            database.open();
            database.setBlob(tableName,rowId,byteArray);
            database.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
