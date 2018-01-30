package com.hyiker.stickies.app;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 由sidhch于2018/1/29创建
 */
class MyFrame extends JFrame {

    private static final long serialVersionUID = -5084820520550046468L;

    void setUpMenu() {
        //针对苹果的菜单设置
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        JMenuBar jmb = new JMenuBar();
        JMenu create = new JMenu("创建");
        JMenuItem createItem = new JMenuItem("创建");
        JMenu exit = new JMenu("退出");
        JMenuItem exitItem = new JMenuItem("退出");
        createItem.addActionListener(new MenuListener());
        exitItem.addActionListener(new MenuListener());

        create.add(createItem);
        exit.add(exitItem);


        jmb.add(create);
        jmb.add(exit);
        setJMenuBar(jmb);
    }

    private class MenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem mi = (JMenuItem) e.getSource();
            switch (mi.getText()) {
                case "创建":
                    NoteController nc = NoteController.getInstance();
                    nc.instantiateRandomNote();
                    break;
                case "退出":
                    NoteController.getInstance().end();
                    System.exit(-1);
                    break;
            }

        }
    }
}
