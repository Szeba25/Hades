package hu.szeba.hades.model.task.data;

public class MissingResultFileForProgramInputException extends Exception {

    public MissingResultFileForProgramInputException(String taskName, String inputFileName) {
        super("Missing result file. Task name: " + taskName + ". Input file name: "
                + inputFileName + ".");
    }

}
