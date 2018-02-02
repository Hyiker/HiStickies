package com.hyiker.stickies.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 由sidhch于2018/1/27创建
 */
class MyTextArea extends JScrollPane {
    private static final Logger logger = LoggerFactory.getLogger(MyTextArea.class);
    private String note_id;
    boolean is_locked;
    static int close_delay = 1500;
    private final String IMAGE_FILE_NAME = this.getClass().getClassLoader().getResource("assets/imgs/lock.png").getPath();
    private JTextPane textPane;
    private MyJLabel locker;
    private int orgY;

    public void setOrgY(int orgY) {
        this.orgY = orgY;
    }

    public boolean is_locked() {
        return is_locked;
    }

    public MyTextArea(int font_size, int width, int height, String note_id, boolean is_locked, int scroll_position) {
        this.note_id = note_id;
        this.is_locked = !is_locked;
        textPane = new JTextPane();
        //将textpane放入scrollpane
        setViewportView(textPane);
        setSize(width, height);
        //初始化锁图标
        //双击锁定
        ImageIcon background = new ImageIcon(IMAGE_FILE_NAME);

        //设置居中的背景
        locker = setCenteredBackground(background);
        textPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                orgY = locker.adjust(new ImageIcon(IMAGE_FILE_NAME), getWidth(), getHeight());
            }

        });

        JScrollBar jsb = getVerticalScrollBar();
        jsb.setValue(scroll_position);
        jsb.addAdjustmentListener(e -> locker.setLocation(locker.getX(), e.getValue() + orgY));
        getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        modifyArea();
        //长按销毁窗体
        textPane.addMouseListener(new MyTextAreaListener());
    }

    private void modifyArea() {
        is_locked = !is_locked;
        textPane.setEnabled(!is_locked);
        textPane.setEditable(!is_locked);
        locker.setVisible(is_locked);
    }


    public void setText(String text) {
        textPane.setText(text);
    }

    public String getText() {
        return textPane.getText();
    }

    public void setTextFont(Font f) {
        textPane.setFont(f);
    }


    @Override
    public Component add(Component comp) {
        //super.add(comp);
        return textPane.add(comp);
    }

    //在设置size的时候
    public MyJLabel setCenteredBackground(ImageIcon icon) {

        // 锁的图片大小根据窗口大小调整
        MyJLabel background = new MyJLabel();
        background.setIcon(icon);
        background.setOpacity(0.5f);
        orgY = background.adjust(icon, getWidth(), getHeight());
        background.setVisible(is_locked);

        textPane.add(background);
        return background;
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    private class MyTextAreaListener extends MouseAdapter {
        private Timer t;
        private boolean has_released = false;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                modifyArea();
                has_released = true;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

            //System.out.println(e.getClickCount());
            if (e.getClickCount() == 2) {
                has_released = false;
                if (t == null) {
                    t = new Timer();
                }
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!has_released) {
                            NoteController.getInstance().destroyNote(note_id);
                        }
                    }
                }, close_delay);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            logger.info("clickcount===" + e.getClickCount());
            has_released = true;
        }
    }

    public String getNote_id() {
        return note_id;
    }
}
