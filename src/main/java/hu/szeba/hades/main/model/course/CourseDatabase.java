package hu.szeba.hades.main.model.course;

import hu.szeba.hades.main.io.SingleDataFile;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.meta.User;
import hu.szeba.hades.main.view.elements.MappedElement;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CourseDatabase {

    private User user;
    private List<MappedElement> possibleCourses;
    private Map<String, Course> courses;

    public CourseDatabase(User user) throws IOException {
        this.user = user;

        possibleCourses = new ArrayList<>();
        for (String id : Options.getDatabasePath().list()) {
            SingleDataFile metaFile = new SingleDataFile(new File(Options.getDatabasePath(), id + "/title.dat"));
            possibleCourses.add(new MappedElement(id, metaFile.getData(0, 0)));
        }

        courses = new HashMap<>();
    }

    public List<MappedElement> getPossibleCourses() {
        return possibleCourses;
    }

    public Course loadCourse(String courseId) throws IOException {
        if (courses.containsKey(courseId)) {
            return courses.get(courseId);
        } else {
            Course newCourse = new Course(user, courseId);
            courses.put(courseId, newCourse);
            return newCourse;
        }
    }

}
