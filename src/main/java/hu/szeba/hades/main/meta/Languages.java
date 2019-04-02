package hu.szeba.hades.main.meta;

import hu.szeba.hades.main.io.DataFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Languages {

    private static Map<String, Map<String, String>> translatons;

    public static void initialize() throws IOException {
        translatons = new HashMap<>();
        File fileList = new File("config/language_files");
        for (File file : fileList.listFiles()) {
            DataFile dataFile = new DataFile(new File(file.getAbsolutePath()));
            Map<String, String> dict = new HashMap<>();
            translatons.put(file.getName(), dict);
            for (int i = 0; i < dataFile.getLineCount(); i++) {
                dict.put(dataFile.getData(i, 0), dataFile.getData(i, 1));
            }
        }
    }

    public static String translate(String originalText) {
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

    public static String getDisplayLanguage() {
        return Options.getConfigData("language") + ".lang";
    }

}
