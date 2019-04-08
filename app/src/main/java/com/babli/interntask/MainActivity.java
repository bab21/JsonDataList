package com.babli.interntask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import java.io.InputStream;
import java.util.HashMap;



public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
//    ImageView bindImage ;
//    public class contactlist{
//        String name;
//        String age;
//        String path;
//    }

    ArrayList<personDetails> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
//        bindImage = (ImageView) findViewById(R.id.imageView);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        String firstUpper(String s)
        {
            String a=s.substring(0,1);
            String b=s.substring(1,s.length());
            a=a.toUpperCase();
            s=a+b;
            return s;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://raw.githubusercontent.com/iranjith4/radius-intern-mobile/master/users.json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray results = jsonObj.getJSONArray("results");

                    // looping through All Contacts
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject c = results.getJSONObject(i);
                        JSONObject id = c.getJSONObject("name");
                        JSONObject dob = c.getJSONObject("dob");
                        JSONObject picture = c.getJSONObject("picture");
                        //ImageView bindImage=new ImageView;
                        personDetails details=new personDetails();
                        // JSONArray result = jsonObj.getJSONArray("results");

                        String title = id.getString("title");
                        String fname = id.getString("first");

                        String last = id.getString("last");
                        String age = dob.getString("age");
                        String pathToFile = picture.getString("thumbnail");
                        details.name=firstUpper(title)+" "+firstUpper(fname)+" "+firstUpper(last);
                        details.age=age;
                        details.imagePath=pathToFile;

                        try{
                            URL urls = new URL(details.imagePath);
                            HttpURLConnection connection = (HttpURLConnection) urls.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            Bitmap myBitmap = BitmapFactory.decodeStream(input);

                            details.bm=myBitmap;

                        }
                        catch (Exception e)
                        {
                            Log.d("ooiioouu",e+"");
                        }
                        //DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(bindImage);
                        //downloadTask.execute(pathToFile);
                        Log.d("ooiioouu", fname);
//                        String name = c.getString("name");
//                        String email = c.getString("email");
//                        String address = c.getString("address");
//                        String gender = c.getString("gender");
//
//                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
//                        String mobile = phone.getString("mobile");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");
//
//                        // tmp hash map for single contact
//                        HashMap<String, String> contact = new HashMap<>();
//
//                        // adding each child node to HashMap key => value
//                        contact.put("id", id);
//                        contact.put("name", name);
//                        contact.put("email", email);
//                        contact.put("mobile", mobile);
//
//                        // adding contact to contact list
                        contactList.add(details);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            myAdapter adapter = new myAdapter(MainActivity.this, R.layout.list_item, contactList
            );
            lv.setAdapter(adapter);
        }


    }
}


