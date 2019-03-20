package hu.szeba.hades.model.course;

import hu.szeba.hades.io.ConfigFile;
import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;
import hu.szeba.hades.view.elements.MappedElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {

    private User user;
    private String courseId;
    private List<MappedElement> possibleModes;
    private Map<String, Mode> modes;

    // Language cannot change!
    private final String language;

    public Course(User user, String courseId) throws IOException {
        this.user = user;
        this.courseId = courseId;

        possibleModes = new ArrayList<>();
        File pathFile = new File(Options.getDatabasePath(), courseId + "/modes");
        for (String id : pathFile.list()) {
            TabbedFile metaFile = new TabbedFile(new File(pathFile, id + "/title.dat"));
            possibleModes.add(new MappedElement(id, metaFile.getData(0, 0)));
        }

        this.modes = new HashMap<>();

        ConfigFile courseMetaFile = new ConfigFile(new File(Options.getDatabasePath(), courseId  + "/meta.conf"));
        this.language = courseMetaFile.getData(0, 1);
    }

    public List<MappedElement> getPossibleModes() {
        return possibleModes;
    }

    public Mode loadMode(String modeId) throws IOException {
        if (modes.containsKey(modeId)) {
            return modes.get(modeId);
        } else {
            Mode newMode = new Mode(user, courseId, modeId, language);
            modes.put(modeId, newMode);
            return newMode;
        }
    }

}
