package hu.szeba.hades.model.task;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.compiler.ProgramCompilerC;
import hu.szeba.hades.model.task.data.TaskData;

import java.io.File;

public class TaskCFactory implements TaskFactory {

    @Override
    public Task getTask(File campaignDirectory, File campaignWorkingDirectory, String taskName) {

        File taskDirectory = new File(campaignDirectory, taskName);
        File taskWorkingDirectory = new File(campaignWorkingDirectory, taskName);
        TaskData taskData = new TaskData(taskDirectory, taskWorkingDirectory, taskName);
        ProgramCompiler programCompiler = new ProgramCompilerC(Options.getCompilerPath_C());

        return new Task(taskData, programCompiler);
    }

}
