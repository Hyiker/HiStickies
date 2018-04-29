package com.hyiker.stickies.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 由sidhch于2018/4/29创建
 */
public class MenuFunctions {
    private static final Logger logger = LoggerFactory.getLogger(MenuFunctions.class);

    public void createNote() {
        logger.info("createNote");
    }

    public void exit() {
        logger.info("exit");

    }

    public void showNotes() {
        logger.info("showNotes");
    }
}
