package hu.szeba.hades.main.util;

import java.util.regex.Pattern;

public class FileUtilities {

    public static String getFileNameWithoutExtension(String fileName) {
        return fileName.split(Pattern.quote("."))[0];
    }

    public static String getFileNameExtension(String fileName) {
        String[] data = fileName.split(Pattern.quote("."));
        if (data.length > 1) {
            return data[1];
        } else {
            return "";
        }
    }

}
