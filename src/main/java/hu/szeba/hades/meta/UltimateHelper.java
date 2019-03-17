package hu.szeba.hades.meta;

import hu.szeba.hades.io.DataFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

public class UltimateHelper {

    private Random random;
    private DataFile links;

    public UltimateHelper() {
        try {
            random = new Random();
            links = new DataFile(new File("config/helper.txt"));
        } catch (IOException e) {
            links = null;
        }
    }

    public void help() throws IOException {
        if (links != null) {
            int id = random.nextInt(links.getLineCount());
            String link = links.getData(id, 0);

            System.out.println("UltimateHelper: " + link);

            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(URI.create(link));
            }
        }
    }

}
