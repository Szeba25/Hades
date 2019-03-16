package hu.szeba.hades.util;

import java.util.List;

public class HTMLUtilities {

    public static String generateTaskInfoHTML(String difficulty, String length, List<String> tags) {
        return "<html>" +
            "<style type=\"text/css\"> p {margin-bottom: 2px;  margin-top: 0px;} </style>" +
            "<p><b>Difficulty: </b>" + difficulty + "</p>" +
            "<p><b>Length: </b>" + length + "</p>" +
            "<p><b>Tags: </b>" + String.join(", ", tags) + "</p>" +
            "</html>";
    }

    public static String getEmptyTaskInfoHTML() {
        return "<html>" +
            "<style type=\"text/css\"> p {margin-bottom: 2px;  margin-top: 0px;} </style>" +
            "<p><b>Difficulty: </b>-</p>" +
            "<p><b>Length: </b>-</p>" +
            "<p><b>Tags: </b>-</p>" +
            "</html>";
    }

    public static String getEmptyTaskDescription() {
        return "<h2>No task selected...</h2>";
    }
}
