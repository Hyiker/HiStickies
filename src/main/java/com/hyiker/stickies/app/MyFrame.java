package com.hyiker.stickies.app;

import com.hyiker.stickies.app.io.JSONDataHandler;
import com.hyiker.stickies.app.model.MenuInfo;
import com.hyiker.stickies.app.model.MyMenuComponent;
import com.hyiker.stickies.app.utils.Reflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 由sidhch于2018/1/29创建
 */
class MyFrame extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MyFrame.class);
    private static List<MenuInfo> menuInfoList = null;
    private MyFrame instance;

    public MyFrame() throws HeadlessException {
        this.instance = this;
    }

    static {
        JSONDataHandler dataReader = new JSONDataHandler();
        try {
            menuInfoList = dataReader.readMenuInfo();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setUpMenu() {
        JMenuBar jmb = new JMenuBar();
        for (MenuInfo mi : menuInfoList) {
            MyMenuComponent mmc = new MyMenuComponent(mi.getName(), 1);
            loopChildren(declareMenu(mmc, mi), mmc);
            jmb.add(mmc.getComponent());
        }
        setJMenuBar(jmb);
    }


    /**
     * @param children:子元素信息数组
     * @param father:父元素
     */
    private void loopChildren(List<MenuInfo> children, MyMenuComponent father) {
        if (children != null && father != null && children.size() != 0) {
            for (MenuInfo mi : children) {
                MyMenuComponent next = father.createNext(mi.getName());
                loopChildren(declareMenu(next, mi), next);
            }
        }
    }


    /**
     * @param item:要被修饰的对象
     * @param mi:修饰信息
     * @return 返回子元素
     */
    private List<MenuInfo> declareMenu(MyMenuComponent item, MenuInfo mi) {
        String function = mi.getFunction();
        String init = mi.getInit();
        List<MenuInfo> children = mi.getChildren();
        if (function != null) {
            Method funcMethod = Reflection.getMethodByName(function);
            item.getComponent().addActionListener(e -> Reflection.invokeMethod(funcMethod, (Object) null));
        }

        if (init != null) {
            Method m2 = Reflection.getMethodByName(init);
            Reflection.invokeMethod(m2, (Object) null);
        }
        return children;
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
