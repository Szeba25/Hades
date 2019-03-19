package hu.szeba.hades.main.util;

import java.util.regex.Pattern;

public class FileUtilities {

    public static String getFileNameWithoutExtension(String fileName) {
        return fileName.split(Pattern.quote("."))[0];
    }

}
