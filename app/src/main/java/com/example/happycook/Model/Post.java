package com.example.happycook.Model;

public class Post {
    private String postid;
    private String postimage;
    private String description;
    private String publisher;
    String nlieu;
    String buoc1, buoc2, buoc3;

    public Post(String postid, String postimage, String description, String publisher, String nlieu, String buoc1, String buoc2, String buoc3) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
        this.nlieu = nlieu;
        this.buoc1 = buoc1;
        this.buoc2 = buoc2;
        this.buoc3 = buoc3;
    }

    public Post() {
    }

    public String getNlieu() {
        return nlieu;
    }

    public void setNlieu(String nlieu) {
        this.nlieu = nlieu;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getBuoc1() {
        return buoc1;
    }

    public void setBuoc1(String buoc1) {
        this.buoc1 = buoc1;
    }

    public String getBuoc2() {
        return buoc2;
    }

    public void setBuoc2(String buoc2) {
        this.buoc2 = buoc2;
    }

    public String getBuoc3() {
        return buoc3;
    }

    public void setBuoc3(String buoc3) {
        this.buoc3 = buoc3;
    }
}
