package com.hyiker.stickies.app;

import javax.swing.*;
import java.awt.*;

/**
 * 由sidhch于2018/2/2创建
 */
public class MyJLabel extends JLabel {

    private AlphaComposite cmp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);

    private float opacity;

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        paintImmediately(getBounds());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(cmp.derive(opacity));
        super.paintComponent(g2d);
    }

    public int adjust(ImageIcon icon, int window_width, int window_height) {
        ImageIcon adjust = new ImageIcon();
        int img_width = icon.getIconWidth(),
                img_height = icon.getIconHeight();
        int width = window_width / 4;
        int height = width * img_height / img_width;
        adjust.setImage(icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        setIcon(adjust);
        setBounds((window_width - width) / 2, (window_height - height) / 2, width, height);

        return getY();
    }
}
