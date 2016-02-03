package com.codepath.instasnap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by JaneChung on 2/2/16.
 */
public class InstasnapPhotosAdapter extends ArrayAdapter<InstasnapPhoto> {

    public InstasnapPhotosAdapter(Context context, List<InstasnapPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    //Use template to display each photo


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstasnapPhoto photo = getItem(position);
        //Check if we are using a recycled view, if not inflate
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        //Lookup the views for populating the data (image, caption)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);

        tvCaption.setText(photo.caption);
        //Clear out imageview (because we could be using recycled view)
        ivPhoto.setImageResource(0);

        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
       // Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.ic_launcher).into(ivPhoto);

        //Insert model data into view items

        return convertView;
    }
}
