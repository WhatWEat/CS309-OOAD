package com.example.projecthelper.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);
    public static final short INFO = 0;
    public static final short DEBUG = 1;
    public static final short WARN = 2;
    public static final short ERROR = 3;

    public static void log(String msg, short level){
        switch (level){
            case INFO -> logger.info(msg);
            case DEBUG -> logger.debug(msg);
            case WARN -> logger.warn(msg);
            case ERROR -> logger.error(msg);
        }
    }
}
