package com.mquinn.trainer;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ResultLogger {

    private Logger logger = Logger.getLogger("Results");
    private FileHandler fh;

    public ResultLogger(String runDesc){
        try {
            fh = new FileHandler("F:\\Hand Dataset\\Results\\" + runDesc + ".txt");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String toLog){

        logger.info(toLog);

    }

}
