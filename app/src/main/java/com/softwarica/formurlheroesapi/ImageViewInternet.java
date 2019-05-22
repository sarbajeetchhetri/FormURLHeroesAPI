package com.softwarica.formurlheroesapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageViewInternet extends AppCompatActivity implements View.OnClickListener {
    private Button btnOk;
    private ImageView image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageview);
        init();
        loadImage();
    }

    private void loadImage() {
        String url = "https://www.savethekoala.com/sites/savethekoala.com/files/uploads/25.jpg";
        new BackgroundTask(image).execute(url);
    }

    private void init() {
        btnOk = findViewById(R.id.btnOk);
        image = findViewById(R.id.imageView);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnOk){
            startActivity(new Intent(ImageViewInternet.this,MainActivity.class));
        }
    }


    private class BackgroundTask extends AsyncTask<String,String,Bitmap>{
        private ImageView imageView;
        private static final String TAG = "BackgroundTask";
        public BackgroundTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String...params) {
            Bitmap bit = null;
            try {
                URL url = new URL(params[0]);
                bit = BitmapFactory.decodeStream((InputStream) url.getContent());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bit;
        }

        @Override
        protected void onPostExecute(Bitmap bt) {
            imageView.setImageBitmap(bt);
        }
    }
}
