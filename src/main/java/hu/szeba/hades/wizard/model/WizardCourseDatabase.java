package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.SingleDataFile;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.view.elements.MappedElement;
import hu.szeba.hades.wizard.view.elements.DescriptiveElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WizardCourseDatabase {

    public List<MappedElement> courses;

    public WizardCourseDatabase() throws IOException {
        courses = new ArrayList<>();
        for (String id : Options.getDatabasePath().list()) {
            SingleDataFile metaFile = new SingleDataFile(new File(Options.getDatabasePath(), id + "/title.dat"));
            courses.add(new DescriptiveElement(id, metaFile.getData(0, 0)));
        }
    }

    public List<MappedElement> getCourses() {
        return courses;
    }

}
