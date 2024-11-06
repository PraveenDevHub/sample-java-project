package com.happiest.AdminService.utility;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class RBundle {

    public static String getFormattedKey(String name){
        ResourceBundle resourceBundle = ResourceBundle.getBundle("constant");
        return resourceBundle.getString(name);

    }

    public static String getKey(String name, Object... args) {
        String pattern =getFormattedKey(name);
        return MessageFormat.format(pattern, args);
    }
}
