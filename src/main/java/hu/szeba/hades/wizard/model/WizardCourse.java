package hu.szeba.hades.wizard.model;

import hu.szeba.hades.view.elements.MappedElement;

import java.util.List;

public class WizardCourse {

    private String courseId;
    private String courseTitle;
    private String language;

    private List<MappedElement> modes;
    private List<MappedElement> taskCollections;
    private List<MappedElement> tasks;

    public WizardCourse(String courseId) {
        this.courseId = courseId;



    }

}
