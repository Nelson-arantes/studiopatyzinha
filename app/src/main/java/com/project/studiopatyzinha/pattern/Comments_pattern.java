package com.project.studiopatyzinha.pattern;

public class Comments_pattern {
    String comment;
    String commented_at;
    String id;
    Accountpattern user;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommented_at() {
        return commented_at;
    }

    public void setCommented_at(String commented_at) {
        this.commented_at = commented_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Accountpattern getUser() {
        return user;
    }

    public void setUser(Accountpattern user) {
        this.user = user;
    }

    public Comments_pattern(String comment, String commented_at, String id, Accountpattern user) {
        this.comment = comment;
        this.commented_at = commented_at;
        this.id = id;
        this.user = user;
    }
}
