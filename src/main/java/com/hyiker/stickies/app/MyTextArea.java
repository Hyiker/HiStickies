package com.hyiker.stickies.app;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 由sidhch于2018/1/27创建
 */
class MyTextArea extends JTextPane {
    private String note_id;
    boolean is_locked;
    static int close_delay = 1500;


    public MyTextArea(int font_size, int height, String note_id) {
        this.note_id = note_id;
        //双击锁定
        //长按销毁窗体
        this.addMouseListener(new MyTextAreaListener());
    }

    private void modifyArea() {
        setEditable(is_locked);
        is_locked = !is_locked;
    }


    private class MyTextAreaListener extends MouseAdapter {
        private Timer t;
        private boolean has_released = false;

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                modifyArea();
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
            has_released = true;
        }
    }

    public String getNote_id() {
        return note_id;
    }
}
