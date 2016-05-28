package com.stvjbz.androidsqlitesample2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(this);

        /**
         * CRUD Operations
         * */
        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addContact(new Contact("Ravi", "9100000000"));
        db.addContact(new Contact("Srinivas", "9199999999"));
        db.addContact(new Contact("Tommy", "9522222222"));
        db.addContact(new Contact("Karthik", "9533333333"));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

        Request request = new Request.Builder()
                //http://api.openweathermap.org/data/2.5/weather?APPID=<自身のAPPID>&q=Tokyo
                .url("http://api.openweathermap.org/data/2.5/weather?APPID=6d8c3db06cd827052dee0e743082fdf1&q=Tokyo")
                .get()
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                //.addNetworkInterceptor(new StethoInterceptor())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d("okhttp3",call.toString());
                //Log.d("okhttp3",response.body().toString());
                try {
                    String result = response.body().string();
                    JSONObject resJson = new JSONObject(result);
                    JSONArray weathers = resJson.getJSONArray("weather");
                    JSONObject weather = weathers.getJSONObject(0);
                    String description = weather.getString("description");

                    //res = description;
//                    Log.d("okhttp3", "resJson: " + resJson.toString());
//                    Log.d("okhttp3", "weathers: " + weathers.toString());
//                    Log.d("okhttp3", "weather: " + weather.toString());
                    Log.d("okhttp3", "description: " + description);

                } catch(JSONException e) {
                    e.printStackTrace();
                }

                // Gson
                Gson gson = new Gson();

                // JSONからStringへの変換
                String str = gson.fromJson("\"hello\"", String.class);
                System.out.println("String: " + str);


                // JSONから配列への変換
                int[] array = gson.fromJson("[1, 2, 3]", int[].class);
                System.out.println("int[]: " + array[0] + ", " + array[1] + ", " + array[2]);

                // JSONからListへの変換
                List list = gson.fromJson("[\"hello\", \"hellohello\",\"hellohellohello\"]", List.class);
                System.out.println("List: " + list.get(0) + ", " + list.get(1) + ", " + list.get(2));
                System.out.println("List: " + list.get(0) + ", " + list.get(1) + ", " + list.get(2));


            }
        });
    }

}

