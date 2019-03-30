package hu.szeba.hades.main.model.task.taskfactory;

import hu.szeba.hades.main.io.DescriptionFile;
import hu.szeba.hades.main.meta.User;
import hu.szeba.hades.main.model.compiler.ProgramCompilerC;
import hu.szeba.hades.main.model.task.Task;
import hu.szeba.hades.main.model.task.data.MissingResultFileException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.io.IOException;

public class TaskCFactory implements TaskFactory {

    @Override
    public Task getTask(User user, String courseId, String modeId, String taskCollectionId, String taskId,
                        DescriptionFile taskDescription, boolean continueTask)
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
