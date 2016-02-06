package com.codepath.instasnap;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<InstasnapPhoto> photos;
    private InstasnapPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        photos = new ArrayList<>();
      //Create adapter and link to source
        aPhotos = new InstasnapPhotosAdapter(this, photos);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });

        swipeContainer.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark);
        
        //Set adapter to listview from layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);

        fetchPopularPhotos();
    }

    //Popular Media API endpoint
    public void fetchPopularPhotos() {
        String url ="https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
           // onSuccess

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray photosJSON = null;
                try {
                    photos.clear();
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i <photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);

                        //decode the attributes of json into photo model
                        InstasnapPhoto photo = new InstasnapPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photo.userImageUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.timeStamp = Long.valueOf(photoJSON.getString("created_time")) * 1000;

                        InstasnapComment first = new InstasnapComment();
                        JSONArray comments = photoJSON.getJSONObject("comments").getJSONArray("data");
                        JSONObject firstComment = comments.getJSONObject(0);
                        first.userName = firstComment.getJSONObject("from").getString("username");
                        first.comment = firstComment.getString("text");

                        InstasnapComment second = new InstasnapComment();
                        JSONObject secondComment = comments.getJSONObject(1);
                        second.userName = secondComment.getJSONObject("from").getString("username");
                        second.comment = secondComment.getString("text");

                        photo.firstComment = first;
                        photo.secondComment = second;

                        photos.add(photo);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //update adapter
                aPhotos.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            //onFailure
        });


    }




}
