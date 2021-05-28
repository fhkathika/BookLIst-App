package com.example.booklistapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final int LOADER_ID = 1;
    private static final String LOG_TAG = MainActivity.class.getName();

    private EditText searchBox;
    private Button searchButton;
    private ListView List;
    private ProgressBar loadingCircle;
    private TextView error_Message;
    private BookListAdapter bookListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST: EarthQuakeActvity onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBox = findViewById(R.id.search_box);
        searchButton = findViewById(R.id.search_buttton);
        List = findViewById(R.id.list);
        loadingCircle = findViewById(R.id.progressIndicator);
        error_Message = findViewById(R.id.empty_view);
        ArrayList<Book> books = new ArrayList<>();


        bookListAdapter = new BookListAdapter(this, new ArrayList<Book>());
        List.setAdapter(bookListAdapter);
//        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Book current_book = (Book) List.getAdapter();
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(current_book.getPreviewLink()));
//                startActivity(intent);
//            }
//        });
        LoaderManager loaderManager = getLoaderManager();
        Log.i(LOG_TAG, "TEST:calling initloadr");
        loaderManager.initLoader(LOADER_ID, null, this);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }


    private boolean Read_network_state(Context context) {
        Log.i(LOG_TAG, "TEST:Read_network method called");
        boolean is_connected;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        is_connected = info != null && info.isConnectedOrConnecting();
        return is_connected;
    }

    private void search() {
        Log.i(LOG_TAG, "TEST:search initor  called");

        String search_query = searchBox.getText().toString();
        boolean is_connected = Read_network_state(this);
        if (!is_connected) {
            error_Message.setText(R.string.Failed_to_Load_data);
            List.setVisibility(View.INVISIBLE);
            error_Message.setVisibility(View.VISIBLE);
            return;
        }

        if (search_query.equals("")) {
            Toast.makeText(this, "PLease enter query", Toast.LENGTH_SHORT).show();
            return;
        }
        String final_query = search_query.replace(" ", "+");
        Uri uri = Uri.parse(BASE_URL + final_query);
        Uri.Builder builder = uri.buildUpon();

        Bundle args = new Bundle();
        args.putString("SEARCH_URL", builder.toString());
        LoaderManager loaderManager = getLoaderManager();
        Loader loader = loaderManager.getLoader(LOADER_ID);
        if (loader == null) {
            loaderManager.initLoader(LOADER_ID, args, this);
        } else {
            loaderManager.restartLoader(LOADER_ID, args, this);
        }
    }

    @Override
    public Loader<ArrayList<Book>> onCreateLoader(int id, final Bundle args) {
        Log.i(LOG_TAG, "TEST: MainActivity  onLOadercreate() called");
        return new AsyncTaskLoader<ArrayList<Book>>(this) {

            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                error_Message.setVisibility(View.INVISIBLE);
                loadingCircle.setVisibility(View.VISIBLE);
                List.setVisibility(View.INVISIBLE);
                forceLoad();
            }

            @Override
            public ArrayList<Book> loadInBackground() {
                String url = args.getString("SEARCH_URL");
                URL urll = NetworkUtils.construct_url(url);
                String JSON_RESPONSE = NetworkUtils.GET_JSON_RESPONSE(urll);
                return NetworkUtils.DECODE_JSON(JSON_RESPONSE);
            }
        };


    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> data) {
        Log.i(LOG_TAG, "TEST: MainActivity  onLoadFinished called");

        loadingCircle.setVisibility(View.INVISIBLE);
        List.setVisibility(View.VISIBLE);
        bookListAdapter.clear();
        if (data != null && !data.isEmpty()) {
            bookListAdapter.addAll(data);
        }
//bookListAdapter.add_new(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        bookListAdapter.clear();
    }
}