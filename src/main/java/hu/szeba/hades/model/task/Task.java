package hu.szeba.hades.model.task;

import hu.szeba.hades.control.task.TaskSolvingControl;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.model.task.program.Program;

import java.io.File;

public abstract class Task implements TaskSolvingControl {

    private TaskData taskData;
    private ProgramCompiler programCompiler;
    private Program program;

    public Task(TaskData taskData) {
        this.taskData = taskData;
        programCompiler = createCompiler(taskData.getTaskWorkingDirectory());
        program = null;
    }

    abstract ProgramCompiler createCompiler(File taskWorkingDirectory);

}
