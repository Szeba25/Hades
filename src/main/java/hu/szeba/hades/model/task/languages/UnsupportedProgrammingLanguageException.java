package hu.szeba.hades.model.task.languages;

public class UnsupportedProgrammingLanguageException extends Exception {

    public UnsupportedProgrammingLanguageException(String language) {
        super("Language is not supported: " + language);
    }

}
