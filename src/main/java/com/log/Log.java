package com.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {
    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static FileHandler fileHandler = null;

    public static Logger getLogger() {


        try {
            fileHandler = new FileHandler("server.log",true);
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            return logger;
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
