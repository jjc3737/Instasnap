package com.codepath.instasnap;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JaneChung on 2/2/16.
 */
public class InstasnapPhotosAdapter extends ArrayAdapter<InstasnapPhoto> {

    private Context context;

    private static class ViewHolder {
        TextView caption;
        ImageView photo;
        ImageView userPhoto;
        TextView userName;
        TextView timeStamp;
        TextView likes;
        TextView commentOne;
        TextView commentTwo;
        Button comments;

    }
    public InstasnapPhotosAdapter(Context context, List<InstasnapPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
    }

    //Use template to display each photo


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstasnapPhoto photo = getItem(position);

        DeviceDimensionsHelper helper = new DeviceDimensionsHelper();
        int widthPixels = helper.getDisplayWidth(getContext());

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
            viewHolder.comments = (Button) convertView.findViewById(R.id.btComments);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.caption.setText(photo.caption);
        viewHolder.userName.setText(photo.username);
        viewHolder.likes.setText(NumberFormat.getInstance().format((double) photo.likesCount) + " likes");
        viewHolder.timeStamp.setText(DateUtils.getRelativeTimeSpanString(photo.timeStamp, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));

        final ArrayList<InstasnapComment> comments = getCommentsArray(photo.comments);
        viewHolder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add fragment with comments
                PhotosActivity activity = (PhotosActivity) context;
                FragmentTransaction t = activity.getFragmentManager().beginTransaction();
                CommentsFragment fragment = CommentsFragment.newInstance(comments);
                t.add(R.id.your_placeholder, fragment);
                t.commit();

            }
        });

        //Clear out imageview (because we could be using recycled view)
        viewHolder.photo.setImageResource(0);
        viewHolder.userPhoto.setImageResource(0);

        SpannableStringBuilder build = new SpannableStringBuilder();
        String firstUserName = photo.firstComment.userName;
        SpannableString blueSpannable = new SpannableString(firstUserName);
        blueSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#125688")), 0, firstUserName.length(),0);
        build.append(blueSpannable);

        String firstComment = " " + photo.firstComment.comment;
        SpannableString blackSpannable = new SpannableString(firstComment);
        blackSpannable.subSequence(0, firstComment.length());
        build.append(blackSpannable);

        viewHolder.commentOne.setText(build, TextView.BufferType.SPANNABLE);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        String secondUserName = photo.secondComment.userName;
        SpannableString blue = new SpannableString(secondUserName);
        blue.setSpan(new ForegroundColorSpan(Color.parseColor("#125688")), 0, secondUserName.length(),0);
        builder.append(blue);

        String secondComment = " " + photo.secondComment.comment;
        SpannableString black = new SpannableString(secondComment);
        black.subSequence(0, secondComment.length());
        builder.append(black);

        viewHolder.commentTwo.setText(builder, TextView.BufferType.SPANNABLE);

        Picasso.with(getContext()).load(photo.imageUrl).resize(0, widthPixels).placeholder(R.drawable.placeholder).into(viewHolder.photo);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.parseColor("#556270"))
                        .borderWidthDp(2)
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

    public ArrayList<InstasnapComment> getCommentsArray(JSONArray comments) {
        ArrayList<InstasnapComment> commentsArray = new ArrayList<>();

        try {
            for (int i = 0; i < comments.length(); i++) {
                InstasnapComment comment = new InstasnapComment();
                JSONObject object = comments.getJSONObject(i);

                comment.userName =  object.getJSONObject("from").getString("username");
                comment.comment = object.getString("text");
                comment.imageUrl = object.getJSONObject("from").getString("profile_picture");
                commentsArray.add(i, comment);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return commentsArray;
    }


}
