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
        JMenu hide = new JMenu("显示设置");
        JMenuItem hideItem = new JMenuItem("隐藏所有窗口");
        JMenuItem showItem = new JMenuItem("显示所有窗口");
        createItem.addActionListener(new MenuListener());
        exitItem.addActionListener(new MenuListener());
        hideItem.addActionListener(new MenuListener());
        showItem.addActionListener(new MenuListener());

        create.add(createItem);
        hide.add(hideItem);
        hide.add(showItem);
        exit.add(exitItem);


        jmb.add(create);
        jmb.add(exit);
        jmb.add(hide);
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
                    NoteController.getInstance().shutdown();
                    System.exit(-1);
                    break;
                case "隐藏所有窗口":
                    NoteController.getInstance().setVisibleForAll(false);
                    setVisible(false);
                    break;
                case "显示所有窗口":
                    NoteController.getInstance().setVisibleForAll(true);
                    setVisible(true);
                    break;
            }

        }
    }
}
