package com.example.sanyi.booklisting;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Sanyi on 17/06/2017.
 */

public final class Query {

    private static final String  LOG_TAG = Query.class.getSimpleName();
    private static String imageLink;

    private Query(){}

    private static URL createUrl(String stringUrl){
        URL url=null;
        try {
            url=new URL(stringUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error with creating url",e);
        }
        return url;
    }
    //Gathering up the infromations needed for the query
    public static ArrayList<Book> fetchBookData(String requestUrl){
        Log.e("Request URL",requestUrl);
        URL url= createUrl(requestUrl);
        String response=null;
        try {
            response=makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error with fetching data",e);
        }
        ArrayList<Book> books=extractFeatures(response);
        return books;
    }

//Requesting for a response
    private static String makeHttpRequest(URL url) throws IOException{
        String response="";
        if (url==null){return response;}
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        try {
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return response;
    }
// Reading from the response
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output=new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line!=null){
                output.append(line);
                line=reader.readLine();
            }
        }
        return output.toString();
    }
    private static Bitmap bmp=null;
    private static JSONArray author;
    private static String authorName;
    private static String publishDate;
    private static ArrayList<Book> extractFeatures(String json) {
        ArrayList<Book> books=new ArrayList<>();
        if (TextUtils.isEmpty(json)){
            return null;
        }
        try {
            // Try to parse the json responses
            JSONObject baseJson = new JSONObject(json);
            JSONArray items=baseJson.getJSONArray("items");
            String description="";
            for (int i=0;i<items.length();i++){
                JSONObject item=items.getJSONObject(i);
                JSONObject volumeInfo=item.getJSONObject("volumeInfo");

                // Checking whether the API contains the name of the Author
                if (volumeInfo.has("authors")){
                    author=volumeInfo.getJSONArray("authors");
                    authorName=author.getString(0);
                }
                else {
                    authorName="Unknown";
                }
                if(volumeInfo.has("publishedDate")){
                    publishDate=volumeInfo.getString("publishedDate");
                }
                else {
                    publishDate="Unknown";
                }
                // Checking whether the API contains the description of the book
                if(!item.has("searchInfo")){
                    description="No available description";
                }
                else{
                    JSONObject search=item.getJSONObject("searchInfo");
                    description=search.getString("textSnippet");
                }
                // Checking whether the API contains the picture of the book
                if(volumeInfo.has("imageLinks")){
                    JSONObject image=volumeInfo.getJSONObject("imageLinks");
                    imageLink=image.getString("smallThumbnail");
                    try {
                        URL url=new URL(imageLink);
                        bmp= BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    }
                    catch (MalformedURLException e){
                        Log.e(LOG_TAG,"Error making the imagUrl",e);
                    }
                    catch (IOException e){
                        Log.e(LOG_TAG,"Error making the bmp",e);
                    }
                }
                else{
                    bmp=BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.ic_settings_black_24dp);
                }
                books.add(new Book(bmp,volumeInfo.getString("title"),authorName,publishDate,description,volumeInfo.getString("previewLink")));
            }
        }
        catch (JSONException e){
            Log.e(LOG_TAG, "Problem parsing the Book JSON result",e);
        }
        return books;
    }

}
