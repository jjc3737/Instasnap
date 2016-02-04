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

    private static class ViewHolder {
        TextView caption;
        ImageView photo;
    }
    public InstasnapPhotosAdapter(Context context, List<InstasnapPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    //Use template to display each photo


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstasnapPhoto photo = getItem(position);

        ViewHolder viewHolder;
        //Check if we are using a recycled view, if not inflate
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_photo, parent, false);
            viewHolder.caption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.ivPhoto);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.caption.setText(photo.caption);
        //Clear out imageview (because we could be using recycled view)
        viewHolder.photo.setImageResource(0);


        Picasso.with(getContext()).load(photo.imageUrl).into(viewHolder.photo);
       // Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.ic_launcher).into(ivPhoto);

        //Insert model data into view items

        return convertView;
    }
}
