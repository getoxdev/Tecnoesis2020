package com.github.tenx.tecnoesis20.data.models;

import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.List;

public class EventBody {

    @PropertyName("name")
    String name;

    @PropertyName("description")
    String description;

    @PropertyName("rules")
    List<String> rules;

    @PropertyName("image")
    String image;

    @PropertyName("date")
    String date;

    @PropertyName("registerLink")
    String registerLink;

    @PropertyName("website")
    String website;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public EventBody() {
        rules = new ArrayList<>();
        registerLink = "";
        date  = "";
        image = "";
        description = "";
        name = "";
        website = "";
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


    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRegisterLink() {
        return registerLink;
    }

    public void setRegisterLink(String registerLink) {
        this.registerLink = registerLink;
    }
}