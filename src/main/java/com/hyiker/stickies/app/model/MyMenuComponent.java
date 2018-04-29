package com.hyiker.stickies.app.model;

import javax.swing.*;

/**
 * 由sidhch于2018/4/29创建
 */
public class MyMenuComponent {
    private int level;
    private JMenuItem component;
    private String text;

    public MyMenuComponent(String text, int level) {
        this.text = text;
        this.level = level;
        if (level == 1) {
            component = new JMenu(text);
        } else {
            component = new JMenuItem(text);
        }
    }

    public MyMenuComponent createNext(String text) {
        MyMenuComponent mc = new MyMenuComponent(text, level + 1);
        component.add(mc.getComponent());
        return mc;
    }

    public JMenuItem getComponent() {
        return component;
    }
}
