package com.babli.interntask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class myAdapter extends ArrayAdapter<personDetails> {

    public myAdapter( Context context, int resource,  List<personDetails> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        if(convertView==null)
        {
            convertView=((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_item,parent,false);

        }

        ImageView image=convertView.findViewById(R.id.img);

        TextView name=convertView.findViewById(R.id.name);
        TextView age=convertView.findViewById(R.id.age);

        personDetails details=getItem(position);
        name.setText(details.name);
        age.setText("Age: "+details.age);
        image.setImageBitmap(details.bm);

//        DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(image);
//        downloadTask.execute(details.imagePath);






        return convertView;
    }

    public class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageWithURLTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String pathToFile = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(pathToFile).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
