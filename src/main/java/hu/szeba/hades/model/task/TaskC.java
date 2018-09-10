package hu.szeba.hades.model.task;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.compiler.ProgramCompilerC;
import hu.szeba.hades.model.task.data.TaskData;

import java.io.File;

public class TaskC extends Task {

    public TaskC(TaskData taskData) {
        super(taskData);
    }

    @Override
    ProgramCompiler createCompiler(File taskWorkingDirectory) {
        return new ProgramCompilerC(Options.getCompilerPath_C(), taskWorkingDirectory);
    }

    @Override
    public void testMethod() {

    }
}
