package hu.szeba.hades.util;

import java.util.regex.Pattern;

public class FileHandlingUtil {

    public static String getFileNameWithoutExtension(String fileName) {
        return fileName.split(Pattern.quote("."))[0];
    }

}
