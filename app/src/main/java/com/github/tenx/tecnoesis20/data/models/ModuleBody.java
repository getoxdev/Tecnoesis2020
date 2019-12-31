package com.github.tenx.tecnoesis20.data.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.List;


@IgnoreExtraProperties
public class ModuleBody {

    @PropertyName("name")
    String name;

    @PropertyName("description")
    String description;

    @PropertyName("image")
    String image;

    @PropertyName("events")
    List<EventBody> events;

    @PropertyName("website")
    String website;




    public ModuleBody() {
        events = new ArrayList<>();
        image = "";
        description = "";
        name = "";
        website = "";
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<EventBody> getEvents() {
        return events;
    }

    public void setEvents(List<EventBody> events) {

        this.events = events;
    }




    @Override
    public String toString() {
        return name + "\n" + description + "\n";
    }
}
