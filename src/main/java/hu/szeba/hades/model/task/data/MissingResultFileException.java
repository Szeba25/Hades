package hu.szeba.hades.model.task.data;

public class MissingResultFileException extends Exception {

    public MissingResultFileException(String taskName, String inputFileName) {
        super("Missing result file. Task name: " + taskName + ". Input file name: "
                + inputFileName + ".");
    }

}
