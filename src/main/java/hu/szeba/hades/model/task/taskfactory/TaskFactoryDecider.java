package hu.szeba.hades.model.task.taskfactory;

import hu.szeba.hades.model.task.languages.UnsupportedProgrammingLanguageException;
import hu.szeba.hades.model.task.languages.SupportedLanguages;

public class TaskFactoryDecider {

    public static TaskFactory decideFactory(String language) throws UnsupportedProgrammingLanguageException {
        switch(language) {
            case SupportedLanguages.C:
                return new TaskCFactory();
            default:
                throw new UnsupportedProgrammingLanguageException(language);
        }
    }

}
