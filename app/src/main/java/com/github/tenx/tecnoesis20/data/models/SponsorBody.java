package com.github.tenx.tecnoesis20.data.models;

import com.google.firebase.database.PropertyName;

public class SponsorBody {
    @PropertyName("image")
    String image;

    @PropertyName("title")
    String title;

    @PropertyName("description")
    String description;


    public SponsorBody() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
