package hu.szeba.hades.main.model.task.languages;

public class InvalidLanguageException extends Exception {

    public InvalidLanguageException(String language) {
        super("Programming language is not supported: " + language);
    }

}
