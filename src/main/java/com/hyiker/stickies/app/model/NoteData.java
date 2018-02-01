package com.hyiker.stickies.app.model;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.awt.*;
import java.util.Arrays;

/**
 * 由sidhch于2018/2/1创建
 */
public class NoteData {
    private String id;
    @JsonProperty("bounds")
    private Integer[] bounds_array;
    @JsonIgnore
    private Rectangle bounds;
    private Integer font_size;
    private String font;
    private String text;
    private Boolean visibility;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer[] getBounds_array() {
        return bounds_array;
    }

    public void setBounds_array(Integer[] bounds_array) {
        this.bounds_array = bounds_array;
    }

    public void setBounds(Rectangle ra) {
        Integer[] b = new Integer[4];
        b = Arrays.asList((int) ra.getX(), (int) ra.getY(), (int) ra.getWidth(), (int) ra.getHeight()).toArray(b);
        this.bounds_array = b;
    }

    public Rectangle getBounds() {
        if (bounds_array != null) {
            bounds = new Rectangle(bounds_array[0], bounds_array[1], bounds_array[2], bounds_array[3]);
        }
        return bounds;
    }

    public Integer getFont_size() {
        return font_size;
    }

    public void setFont_size(Integer font_size) {
        this.font_size = font_size;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
