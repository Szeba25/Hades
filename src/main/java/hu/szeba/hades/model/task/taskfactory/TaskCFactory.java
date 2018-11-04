package hu.szeba.hades.model.task.taskfactory;

import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.compiler.ProgramCompilerC;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.TaskData;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.io.IOException;

public class TaskCFactory implements TaskFactory {

    @Override
    public Task getTask(String taskName) throws IOException {
        TaskData taskData = new TaskData(taskName, RSyntaxTextArea.SYNTAX_STYLE_C);
        ProgramCompiler programCompiler = new ProgramCompilerC();
        return new Task(taskData, programCompiler);
    }

}
