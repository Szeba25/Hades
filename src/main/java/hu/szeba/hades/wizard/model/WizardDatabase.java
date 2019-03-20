package hu.szeba.hades.wizard.model;

import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.view.elements.MappedElement;
import hu.szeba.hades.wizard.elements.DescriptiveElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WizardDatabase {

    public List<MappedElement> possibleCourses;

    public WizardDatabase() {
        possibleCourses = new ArrayList<>();
        for (String id : Options.getDatabasePath().list()) {
            try {
                TabbedFile metaFile = new TabbedFile(new File(Options.getDatabasePath(), id + "/title.dat"));
                possibleCourses.add(new DescriptiveElement(id, metaFile.getData(0, 0)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<MappedElement> getPossibleCourses() {
        return possibleCourses;
    }

}
