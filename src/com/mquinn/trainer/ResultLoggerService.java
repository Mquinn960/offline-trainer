package com.mquinn.trainer;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ResultLoggerService {

    private Logger logger = Logger.getLogger("Results");
    private FileHandler fh;
    private boolean systemPrinting;

    private static ResultLoggerService instance = null;

    private ResultLoggerService(boolean printToSystem){
        systemPrinting = printToSystem;
    }

    public static ResultLoggerService getInstance(boolean printToSystem){
        if (instance == null){
            instance = new ResultLoggerService(printToSystem);
        }
        return instance;
    }

    public void setLogFile(String runDesc){
        try {
            fh = new FileHandler(runDesc + ".txt");
            logger.addHandler(fh);
            SimpleFormatter formatter = new ReducedLogFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void destroy(){
        logger.removeHandler(fh);
        fh.close();
        instance = null;
    }

    public void log(String toLog, boolean newLineAfter){

        String newline = "";
        if (newLineAfter)
            newline = "\r\n";

        logger.info(toLog + newline);

        if (systemPrinting){
            System.out.println(toLog);
            if (newLineAfter)
                System.out.println();
        }

    }

}
