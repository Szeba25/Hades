package hu.szeba.hades.model.task;

public class TaskFactoryDecider {

    public class SupportedLanguages {
        public static final String C = "C";
        public static final String CPP = "CPP";
        public static final String JAVA = "JAVA";
    }

    public static TaskFactory decideFactory(String language) throws UnsupportedProgrammingLanguageException {
        switch(language) {
            case SupportedLanguages.C:
                return new TaskCFactory();
            default:
                throw new UnsupportedProgrammingLanguageException();
        }
    }

}
