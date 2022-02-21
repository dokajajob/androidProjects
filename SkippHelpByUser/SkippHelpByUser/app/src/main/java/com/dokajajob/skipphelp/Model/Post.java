package com.dokajajob.skipphelp.Model;

public class Post {
    public String title;
    public String description;
    public String image;
    public String timestamp;
    public String user_id;

    public Post() {
    }


    public Post(String title, String desc, String image, String timestamp, String userid) {
        this.title = title;
        this.description = desc;
        this.image = image;
        this.timestamp = timestamp;
        this.user_id = userid;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserid() {
        return user_id;
    }

    public void setUserid(String userid) {
        this.user_id = userid;
    }



}
