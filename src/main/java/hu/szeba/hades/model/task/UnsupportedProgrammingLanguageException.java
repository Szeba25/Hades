package hu.szeba.hades.model.task;

public class UnsupportedProgrammingLanguageException extends Exception {

    public UnsupportedProgrammingLanguageException(String language) {
        super("Language is not supported: " + language);
    }
}
