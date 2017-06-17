package com.example.sanyi.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sanyi on 17/06/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private static final String  LOG_TAG = BookAdapter.class.getSimpleName();
    public BookAdapter(@NonNull Context context,@NonNull List<Book> objects) {
        super(context,0,objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;

        if(listView==null){
            listView= LayoutInflater.from(getContext()).inflate(R.layout.book_element,parent,false);
        }
        Book currentbook=getItem(position);
        // Setting the views to the proper text/resources
        ImageView imageView=(ImageView) listView.findViewById(R.id.imageView);
        imageView.setImageBitmap(currentbook.getImage());

        TextView title=(TextView) listView.findViewById(R.id.TitleId);
        title.setText(currentbook.getTitle());

        TextView author=(TextView) listView.findViewById(R.id.authorId);
        author.setText(currentbook.getAuthor());

        TextView  year=(TextView) listView.findViewById(R.id.yearId);
        year.setText(currentbook.getPublishDate());

        TextView desc=(TextView) listView.findViewById(R.id.descId);
        desc.setText(currentbook.getDescription());

        return listView;
    }
}
