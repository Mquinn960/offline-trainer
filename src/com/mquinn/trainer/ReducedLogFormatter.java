package com.mquinn.trainer;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class ReducedLogFormatter extends SimpleFormatter {
    public String format(LogRecord record){
        if(record.getLevel() == Level.INFO){
            return record.getMessage();
        }else{
            return super.format(record);
        }
    }
}
