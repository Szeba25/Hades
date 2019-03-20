package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.TabbedFile;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.elements.DescriptiveElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WizardCourseDatabase {

    public List<MappedElement> courses;

    public WizardCourseDatabase() {
        courses = new ArrayList<>();
        for (String id : Options.getDatabasePath().list()) {
            try {
                TabbedFile metaFile = new TabbedFile(new File(Options.getDatabasePath(), id + "/title.dat"));
                courses.add(new DescriptiveElement(id, metaFile.getData(0, 0)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<MappedElement> getCourses() {
        return courses;
    }

}
