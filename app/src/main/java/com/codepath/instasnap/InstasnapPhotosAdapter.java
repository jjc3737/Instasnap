package com.codepath.instasnap;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by JaneChung on 2/2/16.
 */
public class InstasnapPhotosAdapter extends ArrayAdapter<InstasnapPhoto> {

    private static class ViewHolder {
        TextView caption;
        ImageView photo;
        ImageView userPhoto;
        TextView userName;
        TextView timeStamp;
        TextView likes;
        TextView commentOne;
        TextView commentTwo;

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
            viewHolder.userPhoto = (ImageView) convertView.findViewById(R.id.ivUser);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.timeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
            viewHolder.likes = (TextView) convertView.findViewById(R.id.tvLikes);
            viewHolder.commentOne = (TextView) convertView.findViewById(R.id.tvCommentOne);
            viewHolder.commentTwo = (TextView) convertView.findViewById(R.id.tvCommentTwo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.caption.setText(photo.caption);
        viewHolder.userName.setText(photo.username);
        viewHolder.likes.setText(NumberFormat.getInstance().format((double) photo.likesCount) + " likes");
        viewHolder.timeStamp.setText(DateUtils.getRelativeTimeSpanString(photo.timeStamp, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));


        //Clear out imageview (because we could be using recycled view)
        viewHolder.photo.setImageResource(0);
        viewHolder.userPhoto.setImageResource(0);

        viewHolder.commentOne.setText(photo.firstComment.userName + ": " + photo.firstComment.comment);
        viewHolder.commentTwo.setText(photo.secondComment.userName + ": " + photo.secondComment.comment);

        Picasso.with(getContext()).load(photo.imageUrl).into(viewHolder.photo);
       // Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.ic_launcher).into(ivPhoto);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLUE)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.with(getContext())
                .load(photo.userImageUrl)
                .fit()
                .transform(transformation)
                .into(viewHolder.userPhoto);

        //Insert model data into view items

        return convertView;
    }
}
