package hu.szeba.hades.control.task;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.compiler.ProgramCompilerC;
import hu.szeba.hades.model.compiler.ProgramCompilerCPP;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.program.Program;

public class TaskSolvingControl {

    private Options options;
    private Task currentTask;
    private Program compiledProgram;
    private ProgramCompiler compiler;

    public TaskSolvingControl(Options options, Task currentTask) {
        this.options = options;
        this.currentTask = currentTask;
        compiledProgram = null;
        setCompilerByTask();
    }

    private void setCompilerByTask() {
        switch(currentTask.getTaskType()) {
            case Task.TYPE_C:
                compiler = new ProgramCompilerC(options.getCompilerPath_C(),
                        currentTask.getTaskWorkingDirectory());
                break;
            case Task.TYPE_CPP:
                compiler = new ProgramCompilerCPP(options.getCompilerPath_CPP(),
                        currentTask.getTaskWorkingDirectory());
                break;
        }
    }

    private void compile() {
        compiledProgram = compiler.compile(currentTask);
    }

}
