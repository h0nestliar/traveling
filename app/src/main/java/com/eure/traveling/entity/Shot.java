package com.eure.traveling.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Shot extends RealmObject implements Serializable{

    private int id;
    private String title;
    private String type = Type.ANIMATED.name();
    @Ignore
    private Type typeAsEnum;
    @JsonProperty("likes_count")
    private int likesCount;
    @JsonProperty("images")
    private Image image;
    @JsonProperty("user")
    private Designer designer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Type getTypeAsEnum() {
        return Type.valueOf(getType());
    }

    public void setTypeAsEnum(Type typeAsEnum) {
        setType(typeAsEnum.name());
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Designer getDesigner() {
        return designer;
    }

    public void setDesigner(Designer designer) {
        this.designer = designer;
    }
}
