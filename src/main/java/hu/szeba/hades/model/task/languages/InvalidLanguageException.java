package hu.szeba.hades.model.task.languages;

public class InvalidLanguageException extends Exception {

    public InvalidLanguageException(String language) {
        super("Programming language is not supported: " + language);
    }

}
