package com.hanaa.confessions;


import java.util.ArrayList;
import java.util.List;

public class Confession {

    private String confession;
    private String mKey;
    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    private ArrayList<String> favList = new ArrayList<String>();

    private ArrayList<String> commentsList = new ArrayList<String>();

    public ArrayList<String> getFavList() {
        return favList;
    }

    public void setFavList(ArrayList<String> favList) {

        this.favList = favList;

    }


    private String comment;
    private String confUserEmail;

    public String getConfUserEmail() {
        return confUserEmail;
    }

    public void setConfUserEmail(String confUserEmail) {
        this.confUserEmail = confUserEmail;
    }

    public List<String> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(ArrayList<String> commentsList) {
        this.commentsList = commentsList;
    }


    public void setConfession(String confession) {
        this.confession = confession;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Confession()
    {}

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }


    public String getConfession() {
        return confession;
    }
}
