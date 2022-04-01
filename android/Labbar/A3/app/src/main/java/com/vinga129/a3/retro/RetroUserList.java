package com.vinga129.a3.retro;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RetroUserList {
    @SerializedName("medlemmar")
    ArrayList<RetroUser> items;

    public ArrayList<RetroUser> getItems() {
        return items;
    }

    public void setItems(ArrayList<RetroUser> items) {
        this.items = items;
    }

    public static class RetroUser {
        @SerializedName("namn")
        private String name;
        @SerializedName("epost")
        private String email;
        @SerializedName("svarade")
        private String answered;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAnswered() {
            return answered;
        }

        public void setAnswered(String answered) {
            this.answered = answered;
        }
    }
}