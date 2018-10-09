package hu.szeba.hades.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskGraphFile {

    private File file;
    private List<TaskTuple> taskGraph;

    public TaskGraphFile(File file) throws IOException {
        this.file = file;
        this.taskGraph = new ArrayList<>();

        ArrayList<String> strings = new ArrayList<>();
        Files.lines(Paths.get(file.getAbsolutePath())).forEach(strings::add);

        for (String tuple : strings) {
            taskGraph.add(new TaskTuple(tuple));
        }
    }

    public void printContents() {
        for (TaskTuple taskTuple : taskGraph) {
            taskTuple.printTuple();
        }
    }

    private class TaskTuple {

        private String task1;
        private String task2;

        private TaskTuple(String tuple) {
            String[] splitted = tuple.split(",");
            switch (splitted.length) {
                case 2:
                    task1 = splitted[0].substring(1).trim();
                    task2 = splitted[1].substring(0, splitted[1].length()-1).trim();
                    break;
                case 1:
                    task1 = splitted[0].substring(1, splitted[0].length()-1).trim();
                    task2 = null;
                    break;
                default:
                    // Do nothing...
                    break;
            }
        }

        private void printTuple() {
            System.out.print(task1 + "/");
            if (task2 != null) {
                System.out.println(task2);
            } else {
                System.out.println("NULL");
            }
        }
    }

}
