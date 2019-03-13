package hu.szeba.hades.model.course;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;

public class CourseDatabase {

    private User user;
    private List<String> possibleCourses;
    private Map<String, Course> courses;

    public CourseDatabase(User user) {
        this.user = user;
        possibleCourses = new ArrayList<>();
        possibleCourses.addAll(Arrays.asList(Options.getDatabasePath().list()));
        courses = new HashMap<>();
    }

    public List<String> getPossibleCourses() {
        return possibleCourses;
    }

    public Course loadCourse(String courseId) {
        if (courses.containsKey(courseId)) {
            return courses.get(courseId);
        } else {
            Course newCourse = new Course(user, courseId);
            courses.put(courseId, newCourse);
            return newCourse;
        }
    }

}
