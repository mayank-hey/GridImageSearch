package com.mchacko.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mchacko.gridimagesearch.R;
import com.mchacko.gridimagesearch.adapters.ImageResultsAdapter;
import com.mchacko.gridimagesearch.listeners.EndlessScrollListener;
import com.mchacko.gridimagesearch.models.ImageResult;
import com.mchacko.gridimagesearch.models.ImageSearchSettings;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends ActionBarActivity {
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;
    private ImageSearchSettings imageSearchSettings;
    private static final int SETTINGS_REQUEST_CODE = 22;
    private static String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);
        imageSearchSettings = new ImageSearchSettings();
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        AsyncHttpClient client = new AsyncHttpClient();
        if (query.equals(""))
            return;
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0" + "&q=" +
                query + "&rsz=8" + "&start=" + offset*8;
        searchUrl += imageSearchSettings.getQueryParams();
        Log.d("URL", searchUrl);
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Log.d("DEBUG", response.toString());
                JSONArray imageResultJson = null;
                try {
                    imageResultJson = response.getJSONObject("responseData").getJSONArray("results");
                    //imageResults.clear(); // only for new search
                    //imageResults.addAll(ImageResult.fromJSONArray(imageResultJson));
                    // aImageResults.notifyDataSetChanged(); -- std way of updating
                    // When you make changes to the adapter it does modify the underlying data
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("INFO", imageResults.toString());
            }
        });
    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch the image display activity
                    // creating an intent
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                    // get the image to display
                ImageResult result = imageResults.get(position);
                    // pass the image result to the intent
                i.putExtra("result", result);
                    // Launch the new activity
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateSearchResults() {
        AsyncHttpClient client = new AsyncHttpClient();
        if (query.equals("")) {
            Toast.makeText(this, "Enter a term to Search", Toast.LENGTH_SHORT).show();
            return;
        }
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0" + "&q=" +
                query + "&rsz=8";
        searchUrl += imageSearchSettings.getQueryParams();
        Log.d("URL", searchUrl);
        client.get(searchUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Log.d("DEBUG", response.toString());
                JSONArray imageResultJson = null;
                try {
                    imageResultJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear(); // only for new search
                    //imageResults.addAll(ImageResult.fromJSONArray(imageResultJson));
                    // aImageResults.notifyDataSetChanged(); -- std way of updating
                    // When you make changes to the adapter it does modify the underlying data
                    aImageResults.addAll(ImageResult.fromJSONArray(imageResultJson));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("INFO", imageResults.toString());
                customLoadMoreDataFromApi(1);
            }
        });
    }

    public void onImageSearch(View view) {
        query = etQuery.getText().toString();
        updateSearchResults();
    }

    public void onInvokingSettings(MenuItem item) {
        // Launch the settings activity
        // creating an intent
        Intent i = new Intent(this, SettingsActivity.class);
        // pass the current settings
        i.putExtra("imageSearchSettings", imageSearchSettings);
        // Launch the new activity
        startActivityForResult(i, SETTINGS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                this.imageSearchSettings = (ImageSearchSettings) data.getSerializableExtra("imageSearchSettings");
                updateSearchResults();
                etQuery.setText(query);
                etQuery.setSelection(etQuery.getText().length());
            }
        }
    }
}
