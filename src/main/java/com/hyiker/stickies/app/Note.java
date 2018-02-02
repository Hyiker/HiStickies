package com.hyiker.stickies.app;

import com.hyiker.stickies.app.model.NoteData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;

/**
 * 由sidhch于2018/1/27创建
 */
public class Note extends MyFrame {
    private static final Logger logger = LoggerFactory.getLogger(Note.class);
    /**
     * 便签对象
     * 每个便签独立存在，由NoteController管理
     * Controller向Note中传入text
     */

    static final int DEFAULT_HEIGHT = 210,
            DEFAULT_WIDTH = 360,
            MIN_HEIGHT = 100,
            MIN_WIDTH = 100, MARGIN = 3, DEFAULT_X = 20, DEFAULT_Y = 80, BOARD_DISTANCE = 15;

    private String id;
    public int RESIZE_STATUS = 0;
    private MyTextArea ta;

    private Point window_origin_position, mouse_origin_position;
    private Dimension window_origin_size;
    private DragAdapter da;
    private Resizer resizer;
    private static final String BASIC_TEXT = "双击锁定/解锁\n双击长按" + ((float) (MyTextArea.close_delay / 1000)) + "秒关闭\n可拖动左上角或者右下角改变大小";


    public String getId() {
        return id;
    }

    public MyTextArea getTa() {
        return ta;
    }

    public Note() {
        this(null, new Rectangle(DEFAULT_X, DEFAULT_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT), BASIC_TEXT, true, false, 0);
    }

    public Note(int x, int y) {
        this(null, new Rectangle(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT), BASIC_TEXT, true, false, 0);
    }

    public Note(NoteData data) {

        this(data.getId(), data.getBounds(), data.getText(), data.getVisibility() == null ? false : data.getVisibility(), data.getIs_locked(), data.getScroll_position());
    }

    public Note(String id, Rectangle ra, String basic, Boolean visible, Boolean is_locked, Integer scroll_position) {
        //生成Note的ID

        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        this.id = id;
        if (ra == null) {
            ra = new Rectangle(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
        setBounds(ra);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        //无边框
        setUndecorated(true);
        //空布局
        setLayout(null);
        //永远在最上层
        setAlwaysOnTop(true);

        setUpMenu();

        //设置TextPanel的显示
        ta = createTextArea(basic, id, is_locked == null ? false : is_locked, scroll_position == null ? 0 : scroll_position);
        setDragEnable(true);
        setResizable(true);

        getContentPane().add(ta);
        //System.out.println("=======");

        //防止死锁...
        // TODO: 2018/2/2 搞懂这里的原理.. DEBUG了好久才找出来
        SwingUtilities.invokeLater(() -> setVisible(visible));
    }

    private MyTextArea createTextArea(String b, String id, boolean is_locked, int scroll_position) {
        MyTextArea mta = new MyTextArea(20, getWidth() + MARGIN, getHeight() + MARGIN, id, is_locked, scroll_position);
        mta.setLocation(-2, -2);
        mta.setText(b);
        mta.setTextFont(new Font("微软雅黑", Font.PLAIN, 20));
        return mta;
    }

    public void setDragEnable(boolean e) {
        if (e) {
            if (da == null) {
                da = new DragAdapter();
            }
            //ta被禁用后窗体可以被拖拽
            ta.getTextPane().addMouseListener(da);
            ta.getTextPane().addMouseMotionListener(da);
        } else {
            if (da != null) {
                ta.removeMouseMotionListener(da);
                ta.removeMouseListener(da);
            }
        }
    }

    @Override
    public void setResizable(boolean resizable) {
        if (resizable) {
            if (resizer == null) {
                resizer = new Resizer();
            }
            ta.getTextPane().addMouseListener(resizer);
            ta.getTextPane().addMouseMotionListener(resizer);
        } else {
            if (resizer != null) {
                ta.removeMouseListener(resizer);
                ta.removeMouseMotionListener(resizer);
            }
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

    public NoteData getNoteData() {
        NoteData nd = new NoteData();
        nd.setId(this.getId());
        nd.setBounds(this.getBounds());
        nd.setFont(ta.getFont().getFontName());
        nd.setFont_size(ta.getFont().getSize());
        nd.setVisibility(this.isVisible());
        nd.setText(ta.getText());
        nd.setIs_locked(ta.is_locked());
        nd.setScroll_position(ta.getVerticalScrollBar().getValue());
        return nd;
    }

    private class DragAdapter extends MouseAdapter {


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

    private class Resizer extends MouseAdapter {
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
