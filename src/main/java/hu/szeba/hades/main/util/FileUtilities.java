package hu.szeba.hades.main.util;

import hu.szeba.hades.main.meta.Options;

import java.io.File;
import java.io.IOException;
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

    public static boolean validFileName(String fileName) {
        File testFile = new File(Options.getWorkingDirectoryPath(), fileName);
        try {
            testFile.createNewFile();
            testFile.delete();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
