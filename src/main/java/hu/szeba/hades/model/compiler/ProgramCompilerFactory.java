package hu.szeba.hades.model.compiler;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.Task;

public class ProgramCompilerFactory {

    public static ProgramCompiler getProgramCompilerByTask(Options options, Task currentTask) {
        switch(currentTask.getTaskType()) {
            case Task.TYPE_C:
                return new ProgramCompilerC(options.getCompilerPath_C(),
                        currentTask.getTaskWorkingDirectory());
            case Task.TYPE_CPP:
                return new ProgramCompilerCPP(options.getCompilerPath_CPP(),
                        currentTask.getTaskWorkingDirectory());
            default:
                return null;
        }
    }

}
