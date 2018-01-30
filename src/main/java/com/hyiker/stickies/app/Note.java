package com.hyiker.stickies.app;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

/**
 * 由sidhch于2018/1/27创建
 */
public class Note extends MyFrame {
    /**
     * 便签对象
     * 每个便签独立存在，由NoteController管理
     * Controller向Note中传入text
     */

    static final int DEFAULT_HEIGHT = 210,
            DEFAULT_WIDTH = 360,
            MIN_HEIGHT = 100,
            MIN_WIDTH = 100, MARGIN = 5;

    private String id;
    public int RESIZE_STATUS = 0;
    private MyTextArea ta;

    private Point window_origin_position, mouse_origin_position;
    private Dimension window_origin_size;

    public String getId() {
        return id;
    }

    public MyTextArea getTa() {
        return ta;
    }

    public Note() {
        this(null, 0, 0, 0, 0, "", true);
    }

    public Note(String basic) {
        this(null, 0, 0, 0, 0, basic, true);
    }

    public Note(String id, int x, int y, int w, int h, String basic, Boolean visible) {
        //生成Note的ID

        if (id == null) {
            id = NoteController.getUUID();
        }
        System.out.println(id);
        this.id = id;
        w = w == 0 ? DEFAULT_WIDTH : w;
        h = h == 0 ? DEFAULT_HEIGHT : h;
        setBounds(x, y, w, h);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        //无边框
        setUndecorated(true);
        //空布局
        setLayout(null);
        //永远在最上层
        setAlwaysOnTop(true);

        setUpMenu();

        //设置TextPanel的显示
        ta = createTextArea(basic, id);
        setDragEnable(true);
        setResizable(true);

        getContentPane().add(ta);
        if (visible == null) {
            visible = true;
        }
        setVisible(visible);
    }

    private MyTextArea createTextArea(String b, String id) {
        MyTextArea mta = new MyTextArea(20, getHeight(), id);
        mta.setLocation(0, 0);
        mta.setSize(getWidth() + MARGIN, getHeight() + MARGIN);
        mta.setText(b);
        mta.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        return mta;
    }

    public void setDragEnable(boolean e) {
        if (e) {
            //ta被禁用后窗体可以被拖拽
            ta.addMouseListener(new DragAdapter());
            ta.addMouseMotionListener(new DragAdapter());
        }
    }

    @Override
    public void setResizable(boolean resizable) {
        if (resizable) {
            ta.addMouseListener(new Resizar());
            ta.addMouseMotionListener(new Resizar());
        }
    }

    public void fadeOut(int ms) {
        Thread t = new Thread(() -> {
            float opacity = 1;
            int dt = 30;
            float dps = opacity / dt;
            while (opacity > 0) {
                opacity = opacity - dps;
                if (opacity <= 0) {
                    dispose();
                    break;
                }
                setOpacity(opacity);
                try {
                    Thread.sleep((long) (ms / dt));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        t.start();
    }


    private class DragAdapter extends MouseAdapter implements Serializable {


        private static final long serialVersionUID = -5730465985313226745L;

        @Override
        public void mousePressed(MouseEvent e) {
            mouse_origin_position = e.getLocationOnScreen();
            window_origin_position = getLocationOnScreen();
            window_origin_size = getSize();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            //只有不在resizing以及被locked的情况下才可拖动窗体
            if (ta.is_locked && RESIZE_STATUS == 0) {
                int newX = (int) (window_origin_position.getX() + e.getXOnScreen() - mouse_origin_position.getX());
                int newY = (int) (window_origin_position.getY() + e.getYOnScreen() - mouse_origin_position.getY());
                setLocation(newX, newY);
            }
        }
    }

    private class Resizar extends MouseAdapter implements Serializable {
        private static final long serialVersionUID = 1343918047348470267L;
        private final int BOARD_DISTANCE = 10;
        private int newX, newY, newWidth, newHeight;
        private Point mouse_position;


        @Override
        //不同位置显示调整光标
        public void mouseMoved(MouseEvent e) {
            Point mouse_position = e.getLocationOnScreen(),
                    window_position = getLocationOnScreen();
            Dimension d = getSize();
            int mouseX = (int) mouse_position.getX(),
                    mouseY = (int) mouse_position.getY(),
                    windowX = (int) window_position.getX(),
                    windowY = (int) window_position.getY(),
                    windowW = (int) d.getWidth(),
                    windowH = (int) d.getHeight();


            if (Math.abs(mouseX - windowX) <= BOARD_DISTANCE && Math.abs(mouseY - windowY) <= BOARD_DISTANCE) {
                //左上角
                RESIZE_STATUS = 1;
                e.getComponent().setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
            } else if (Math.abs(mouseX - windowW - windowX) <= BOARD_DISTANCE && Math.abs(mouseY - windowH - windowY) <= BOARD_DISTANCE) {
                //右下角
                RESIZE_STATUS = 2;
                e.getComponent().setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
            } else {
                //正常
                RESIZE_STATUS = 0;
                if (ta.is_locked) {
                    //可移动
                    e.getComponent().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                } else {
                    //可编辑
                    e.getComponent().setCursor(new Cursor(Cursor.TEXT_CURSOR));
                }
            }

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mouse_position = e.getLocationOnScreen();
            if (RESIZE_STATUS == 0) {
                return;
            }
            if (RESIZE_STATUS == 1) {
                //左上角的情况
                newX = (int) mouse_position.getX();
                newY = (int) mouse_position.getY();
                //设置 x = 鼠标X, y = 鼠标y ,w = 初始w +初始鼠标x - 鼠标x,h = 初始h+初始鼠标y - 鼠标Y
                newWidth = (int) (window_origin_size.getWidth() + mouse_origin_position.getX() - mouse_position.getX());
                newHeight = (int) (window_origin_size.getHeight() + mouse_origin_position.getY() - mouse_position.getY());

                //设置窗口的坐标
                setBounds(newX, newY, newWidth, newHeight);
            } else if (RESIZE_STATUS == 2) {
                //右下角的情况
                //设置w = 当前鼠标x - 初始鼠标x + 初始窗口w h = 当前鼠标y - 初始鼠标y + 初始窗口h
                newWidth = (int) (mouse_position.getX() - mouse_origin_position.getX() + window_origin_size.getWidth());
                newHeight = (int) (mouse_position.getY() - mouse_origin_position.getY() + window_origin_size.getHeight());
                setSize(newWidth, newHeight);
            }
            //设置TextArea的大小
            ta.setSize(getWidth() + MARGIN, getHeight() + MARGIN);
        }
    }

}
