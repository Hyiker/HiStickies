package com.hyiker.stickies.app.utils;

import com.hyiker.stickies.app.MenuFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 由sidhch于2018/4/29创建
 */
public class Reflection {
    private static final Logger logger = LoggerFactory.getLogger(Reflection.class);

    private static MenuFunctions mf = new MenuFunctions();

    private static Class mfClass = mf.getClass();

    public static Method getMethodByName(String name, Class<?>... pt) {
        Method mtd = null;
        try {
            mtd = mfClass.getMethod(name, pt);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return mtd;
    }

    public static Object invokeMethod(Method mtd, Object... args) {
        Object result = null;
        if (mtd != null) {
            try {
                logger.info(mtd.getName());
                result = mtd.invoke(mf);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            logger.info("传入 invokeMethod(Method mtd, Object... args) 的方法为空");
        }
        return result;
    }
}
