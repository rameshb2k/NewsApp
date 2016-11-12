package com.example.android.newsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ramesh on 10/28/2016.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Activity context, ArrayList<News> newsArray) {
        super(context, 0, newsArray);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        News currentNews = getItem(position);

        //set the section information for the News
        String sectionString = currentNews.getSection();
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section_text_view);
        sectionTextView.setText(sectionString);

        //set the news title information for the News
        String titleString = currentNews.getTitle();
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        titleTextView.setText(titleString);

        //Set the Date published information for the News
        String dateString = currentNews.getDate();
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(dateString);

        return listItemView;
    }
}
