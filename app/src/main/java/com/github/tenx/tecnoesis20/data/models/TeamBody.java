package com.github.tenx.tecnoesis20.data.models;

import com.google.firebase.database.PropertyName;

import java.util.List;

public class TeamBody {


    @PropertyName("name")
    String name;


    @PropertyName("members")
    List<MemberBody> members;


    public TeamBody() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MemberBody> getMembers() {
        return members;
    }

    public void setMembers(List<MemberBody> members) {
        this.members = members;
    }

    public static class MemberBody {
        @PropertyName("name")
        String name;

        @PropertyName("designation")
        String designation;

        @PropertyName("image")
        String image;


        public MemberBody() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
