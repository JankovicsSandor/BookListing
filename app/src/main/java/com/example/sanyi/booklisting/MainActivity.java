package com.example.sanyi.booklisting;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    String Log_Tag="alma";
    private BookAdapter mAdapter;
    private static final int BOOK_LOADER_ID = 1;
    private TextView mEmptyStateTextView;
    private ProgressBar mProgressBar;
    private static final  String REQUEST_URL="https://www.googleapis.com/books/v1/volumes?q=android";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo active= cm.getActiveNetworkInfo();
        boolean isConnnected= active !=null && active.isConnectedOrConnecting();
        Log.e("Started the init",Log_Tag);
        mEmptyStateTextView=(TextView) findViewById(R.id.empty_view);
        ListView bookListView = (ListView) findViewById(R.id.list);
        bookListView.setEmptyView(mEmptyStateTextView);
        mProgressBar=(ProgressBar) findViewById(R.id.progressBar);
        if(isConnnected){
            getLoaderManager().initLoader(BOOK_LOADER_ID, null, this);
        }
        else{
            mProgressBar.setVisibility(View.GONE);

            mEmptyStateTextView.setText("No internet available");
        }

        mAdapter=new BookAdapter(this,new ArrayList<Book>());
        bookListView.setAdapter(mAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings){
            Intent setting=new Intent(this,Settings.class);
            startActivity(setting);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.e("Started the create","");
        SharedPreferences sharedPrefs= PreferenceManager.getDefaultSharedPreferences(this);
        String maxbook = sharedPrefs.getString(
                getString(R.string.settings_numberOfBooks_key),
                getString(R.string.settings_numberOfBooks_default));
        Uri uri =Uri.parse(REQUEST_URL);
        Log.e("Value of maxbook",maxbook);
        Uri.Builder uriBulider=uri.buildUpon();

        uriBulider.appendQueryParameter("maxResults", maxbook);

        return new BookLoader(this,uriBulider.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader,List<Book> data) {
        mProgressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText("No results to be shown");
        Log.e("Started the finish","");
        mAdapter.clear();
        if(data!= null && !data.isEmpty()){
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.e("Started the reset","");
        mAdapter.clear();
    }
}
