package hu.szeba.hades.model.course;

import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;
import hu.szeba.hades.view.elements.MappedElement;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CourseDatabase {

    private User user;
    private List<MappedElement> possibleCourses;
    private Map<String, Course> courses;

    public CourseDatabase(User user) {
        this.user = user;

        possibleCourses = new ArrayList<>();
        for (String id : Options.getDatabasePath().list()) {
            try {
                TabbedFile metaFile = new TabbedFile(new File(Options.getDatabasePath(), id + "/title.dat"));
                possibleCourses.add(new MappedElement(id, metaFile.getData(0, 0)));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
