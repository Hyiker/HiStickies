package com.hyiker.stickies.app.model;

import com.hyiker.stickies.app.model.basic.BasicModel;

import java.util.List;

/**
 * 由sidhch于2018/4/21创建
 */
public class MenuInfo extends BasicModel {
    private String name;
    private String function;
    private String init;
    private List<MenuInfo> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public List<MenuInfo> getChildren() {
        return children;
    }

    public void setChildren(List<MenuInfo> children) {
        this.children = children;
    }


}
