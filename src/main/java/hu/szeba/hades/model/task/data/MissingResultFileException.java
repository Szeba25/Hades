package hu.szeba.hades.model.task.data;

public class MissingResultFileException extends Exception {

    public MissingResultFileException(String inputFileName) {
        super("Missing result file. Input file name: " + inputFileName + ".");
    }

}
