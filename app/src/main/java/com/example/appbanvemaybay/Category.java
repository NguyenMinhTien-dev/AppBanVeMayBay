package com.example.appbanvemaybay;

public class Category {
    String name, content;
    public Category (){}
    public Category (String name, String content){
        this.content = content;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
