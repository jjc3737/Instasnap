package com.codepath.instasnap;

import org.json.JSONArray;

/**
 * Created by JaneChung on 2/2/16.
 */
public class InstasnapPhoto {

    public String username;
    public String caption;
    public String imageUrl;
    public int imageHeight;
    public int likesCount;
    public Long timeStamp;
    public String userImageUrl;
    public InstasnapComment firstComment;
    public InstasnapComment secondComment;
    public JSONArray comments;

}
