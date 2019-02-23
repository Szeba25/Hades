package hu.szeba.hades.model.course;

import hu.szeba.hades.meta.User;

import java.util.HashMap;
import java.util.Map;

public class CourseDatabase {

    private User user;
    private Map<String, Course> courses;

    public CourseDatabase(User user) {
        this.user = user;
        courses = new HashMap<>();
    }

    public Course loadCourse(String courseName) {
        if (courses.containsKey(courseName)) {
            return courses.get(courseName);
        } else {
            Course newCourse = new Course(user, courseName);
            courses.put(courseName, newCourse);
            return newCourse;
        }
    }

}
