package hu.szeba.hades.model.task.taskfactory;

import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.compiler.ProgramCompilerC;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.data.TaskDescription;
import hu.szeba.hades.model.task.data.TaskStory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.io.IOException;

public class TaskCFactory implements TaskFactory {

    @Override
    public Task getTask(User user, String courseName, String topicName, String taskId,
                        TaskDescription taskDescription, TaskStory taskStory, boolean continueTask)
            throws IOException, MissingResultFileException {

        return new Task(
                user,
                new ProgramCompilerC(),
                "C",
                RSyntaxTextArea.SYNTAX_STYLE_C,
                courseName,
                topicName,
                taskId,
                taskDescription,
                taskStory,
                continueTask);
    }

}
