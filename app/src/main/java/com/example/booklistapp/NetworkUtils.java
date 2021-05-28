package com.example.booklistapp;

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
import java.util.ArrayList;

public class NetworkUtils {

    private NetworkUtils(){

    }

    public static URL construct_url(String url_to){
        URL url=null;
        try{
            url=new URL(url_to);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }

    public static String GET_JSON_RESPONSE(URL url){
        HttpURLConnection connection=null;
        InputStream inputStream=null;
        int response_code;
        String json_response="";

        try
        {
            connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            response_code=connection.getResponseCode();
            if(response_code==200)
            {
                inputStream=connection.getInputStream();
                json_response=DECODE_STREAM(inputStream);

            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {  if(connection!=null)
        {
            connection.disconnect();
        }
            if (inputStream!=null)
            {
                try {
                    inputStream.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }
        return json_response;


    }
    private static  String DECODE_STREAM(InputStream inputStream)throws  IOException{
      StringBuilder builder=new StringBuilder();
        InputStreamReader reader=new InputStreamReader(inputStream);
        BufferedReader bufferedReader=new BufferedReader(reader);
        String line=bufferedReader.readLine();
        while (line!=null){
            builder.append(line);
            line=bufferedReader.readLine();
        }
        return  builder.toString();
    }
    public static ArrayList<Book> DECODE_JSON(String response)
    {
        ArrayList<Book> books=new ArrayList<>();
        String title;
        String authorON;
        String image_url;
        String link;

        try {

            JSONObject head = new JSONObject(response);

            JSONArray items = head.getJSONArray("items");

            for (int c = 0; c < ((JSONArray) items).length(); ++c) {

                JSONObject book = items.getJSONObject(c);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                title = volumeInfo.getString("title");
                JSONArray author_list;

                if (volumeInfo.has("authors")) {
                    author_list = volumeInfo.getJSONArray("authors");
                    authorON = author_list.getString(0);

                } else {
                    authorON = "No Author";
                }


                if (volumeInfo.has("imageLinks")) {

                    JSONObject image_links = volumeInfo.getJSONObject("imageLinks");
                    image_url = image_links.getString("smallThumbnail");


                } else {
                    image_url = "NULL";

                }
                if (volumeInfo.has("previewLink")) {
                    link = volumeInfo.getString("previewLink");


                } else {
                    link = "NULL";
                }
              Book   book1=new Book(image_url,title,authorON,link);
                books.add(book1);

//                books.add(new Book(image_url, title, authorON, link));


            }
        }catch(JSONException e)
        {
            e.printStackTrace();
        }


        return  books;

    }


}
