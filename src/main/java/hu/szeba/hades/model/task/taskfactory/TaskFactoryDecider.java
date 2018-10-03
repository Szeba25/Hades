package hu.szeba.hades.model.task.taskfactory;

import hu.szeba.hades.model.task.languages.UnsupportedProgrammingLanguageException;

import java.util.HashMap;
import java.util.Map;

public class TaskFactoryDecider {

    private static Map<String, TaskFactory> factories = new HashMap<>();

    public static TaskFactory decideFactory(String language) throws UnsupportedProgrammingLanguageException {
        // Use reflection, and reuse factories!
        if (factories.containsKey(language)) {
            return factories.get(language);
        } else {
            try {
                Class taskClass = Class.forName("hu.szeba.hades.model.task.taskfactory.Task" + language + "Factory");
                TaskFactory newTaskFactory = (TaskFactory) taskClass.newInstance();
                factories.put(language, newTaskFactory);
                return newTaskFactory;
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                throw new UnsupportedProgrammingLanguageException(language);
            }
        }
    }

}
