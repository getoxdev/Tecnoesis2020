package com.github.tenx.tecnoesis20.data.models;

import com.google.firebase.database.PropertyName;

public class FeedBody {
    @PropertyName("image")
    String image;

    @PropertyName("text")
    String text;

    public FeedBody() {
    }

    public FeedBody(String image, String text) {
        this.image = image;
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
