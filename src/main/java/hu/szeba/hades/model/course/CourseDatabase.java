package hu.szeba.hades.model.course;

import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CourseDatabase {

    private User user;
    private List<String> possibleCourseIds;
    private List<String> possibleCourseTitles;
    private Map<String, String> courseTitleToId;
    private Map<String, Course> courses;

    public CourseDatabase(User user) {
        this.user = user;
        possibleCourseIds = new ArrayList<>();
        possibleCourseIds.addAll(Arrays.asList(Options.getDatabasePath().list()));

        possibleCourseTitles = new ArrayList<>();
        courseTitleToId = new HashMap<>();

        possibleCourseIds.forEach((id) -> {
            try {
                TabbedFile metaFile = new TabbedFile(new File(Options.getDatabasePath(), id + "/title.dat"));
                possibleCourseTitles.add(metaFile.getData(0, 0));
                courseTitleToId.put(metaFile.getData(0, 0), id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        courses = new HashMap<>();
    }

    public List<String> getPossibleCourseTitles() {
        return possibleCourseTitles;
    }

    public String titleToId(String courseTitle) {
        return courseTitleToId.get(courseTitle);
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
