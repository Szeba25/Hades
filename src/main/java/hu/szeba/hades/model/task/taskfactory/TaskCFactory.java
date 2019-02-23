package hu.szeba.hades.model.task.taskfactory;

import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.compiler.ProgramCompilerC;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.MissingResultFileException;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.model.task.data.TaskDescription;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.io.IOException;

public class TaskCFactory implements TaskFactory {

    @Override
    public Task getTask(User user, String courseName, String topicName, String taskId,
                        TaskDescription taskDescription, boolean continueTask)
            throws IOException, MissingResultFileException {

        TaskData taskData = new TaskData(user, courseName, topicName, taskId, taskDescription, continueTask,
                "C", RSyntaxTextArea.SYNTAX_STYLE_C);
        ProgramCompiler programCompiler = new ProgramCompilerC();
        return new Task(taskData, programCompiler, "C");
    }

}
