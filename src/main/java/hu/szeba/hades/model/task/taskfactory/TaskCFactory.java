package hu.szeba.hades.model.task.taskfactory;

import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.compiler.ProgramCompilerC;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.data.TaskDescription;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.io.IOException;

public class TaskCFactory implements TaskFactory {

    @Override
    public Task getTask(User user, String courseId, String modeId, String taskCollectionId, String taskId,
                        TaskDescription taskDescription, boolean continueTask)
            throws IOException, MissingResultFileException {

        return new Task(
                user,
                new ProgramCompilerC(),
                "C",
                RSyntaxTextArea.SYNTAX_STYLE_C,
                courseId,
                modeId,
                taskCollectionId,
                taskId,
                taskDescription,
                continueTask);
    }

}
