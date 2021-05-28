package com.example.booklistapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class BookListAdapter extends ArrayAdapter<Book> {
    private final Context mContext;
    private ArrayList<Book> bookList;
    public BookListAdapter(@NonNull Context context, ArrayList<Book>extractBookList) {
        super(context, 0,extractBookList);
        this.mContext=context;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View bookListItemView=convertView;
        if (bookListItemView==null){
            bookListItemView= LayoutInflater.from(getContext()).inflate(
                    R.layout.booklist,parent,false);
        }
        Book book=getItem(position);
        ImageView bookCoverIV=bookListItemView.findViewById(R.id.book_image);
//        String coverImage=book.getImageUrl();
//        bookCoverIV.setImageURI(Uri.parse(coverImage));
        bookCoverIV.setImageURI(Uri.parse(book.getImageUrl()));

        TextView bookTitleTv=bookListItemView.findViewById(R.id.book_title);
        bookTitleTv.setText(book.getTitle());
//        String  bookTitle=book.getTitle();
//       bookCoverIV.setText(bookTitle);
        TextView bookAutherTv=bookListItemView.findViewById(R.id.auther_name);
        bookAutherTv.setText(book.getAuther());



        return bookListItemView;


    }
//    ArrayList<Book> bookList;
//    public void add_new(ArrayList<Book> data)
//    {
//        bookList.addAll(data);
//        notifyDataSetChanged();
//    }

}
