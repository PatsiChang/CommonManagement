package com.common.utils;

public class EmailHelper {
    public static String generateEmailTemplate(String body, String recipientName){
        return "Dear "+recipientName+",\n\n"+body+"\n\nWarm Regards,\nSmart Home";
    }
}
