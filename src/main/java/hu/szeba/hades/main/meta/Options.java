package hu.szeba.hades.main.meta;

import hu.szeba.hades.main.io.ConfigFile;
import hu.szeba.hades.main.io.DataFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Options {

    private static Map<String, String> config;
    private static Map<String, File> paths;

    private static Map<String, Map<String, String>> translatons;

    public static void initialize() throws IOException {
        config = new HashMap<>();
        DataFile configFile = new ConfigFile(new File("config/main.conf"));
        for (int i = 0; i < configFile.getLineCount(); i++) {
            config.put(configFile.getData(i, 0), configFile.getData(i, 1));
        }

        paths = new HashMap<>();
        DataFile pathsFile = new ConfigFile(new File("config/paths.conf"));
        for (int i = 0; i < pathsFile.getLineCount(); i++) {
            paths.put(pathsFile.getData(i, 0),
                    new File(pathsFile.getData(i, 1)));
        }

        translatons = new HashMap<>();
        File fileList = new File("config/language_files");
        for (File file : fileList.listFiles()) {
            DataFile dataFile = new DataFile(new File(file.getAbsolutePath()));
            Map<String, String> dict = new HashMap<>();
            translatons.put(file.getName(), dict);
            for (int i = 0; i < dataFile.getLineCount(); i++) {
                System.out.println(i);
                System.out.println(dataFile.getData(i,0) + "/" + dataFile.getData(i, 1));
                dict.put(dataFile.getData(i, 0), dataFile.getData(i, 1));
            }
        }
        checkPaths();
    }

    public String translate(String originalText) {
        if(translatons.containsKey(getDisplayLanguage())) {
            if (translatons.get(getDisplayLanguage()).containsKey(originalText)) {
                return translatons.get(getDisplayLanguage()).get(originalText);
            } else {
                return "?" + originalText + "?";
            }
        } else {
            return originalText;
        }
    }

    public String getDisplayLanguage() {
        return config.get("language");
    }

    public static String getConfigData(String configDataIdentifier) {
        return config.get(configDataIdentifier);
    }

    public static int getConfigIntData(String configDataIdentifier) {
        return Integer.parseInt(config.get(configDataIdentifier));
    }

    public static boolean getConfigBooleanData(String configDataIdentifier) {
        return Boolean.parseBoolean(config.get(configDataIdentifier));
    }

    public static File getPathTo(String location) {
        return paths.get(location);
    }

    public static File getDatabasePath() {
        return paths.get("database");
    }

    public static File getWorkingDirectoryPath() { return paths.get("working_directory"); }

    private static void checkPaths() {
        for (String key : paths.keySet()) {
            System.out.println("Check -> " + key + " exists: " + paths.get(key).exists());
        }
    }

}
