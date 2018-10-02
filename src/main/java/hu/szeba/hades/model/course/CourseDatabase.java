package hu.szeba.hades.model.course;

import java.util.HashMap;
import java.util.Map;

public class CourseDatabase {

    private Map<String, Course> courses;

    public CourseDatabase() {
        courses = new HashMap<>();
    }

    public Course loadCourse(String courseName) {

        if (courses.containsKey(courseName)) {
            return courses.get(courseName);
        } else {
            Course newCourse = new Course(courseName);
            courses.put(courseName, newCourse);
            return newCourse;
        }

    }

}
