package hu.szeba.hades.wizard.model;

import hu.szeba.hades.io.ConfigFile;
import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.view.elements.MappedElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WizardCourse {

    private String courseId;
    private String courseTitle;
    private String language;

    private List<MappedElement> modes;
    private List<MappedElement> taskCollections;
    private List<MappedElement> tasks;

    public WizardCourse(String courseId, String courseTitle) throws IOException {
        this.courseId = courseId;
        this.courseTitle = courseTitle;

        ConfigFile courseMetaFile = new ConfigFile(new File(Options.getDatabasePath(), courseId  + "/meta.conf"));
        this.language = courseMetaFile.getData(0, 1);

        modes = new ArrayList<>();
        taskCollections = new ArrayList<>();
        tasks = new ArrayList<>();

        for (String modeId : new File(Options.getDatabasePath(), courseId + "/modes").list()) {
            TabbedFile metaFile = new TabbedFile(new File(Options.getDatabasePath(), "/modes/" + modeId + "/title.dat"));
            modes.add(new MappedElement(modeId, metaFile.getData(0, 0)));
        }

    }

}
