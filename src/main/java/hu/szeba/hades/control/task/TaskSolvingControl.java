package hu.szeba.hades.control.task;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.compiler.ProgramCompilerC;
import hu.szeba.hades.model.compiler.ProgramCompilerCPP;
import hu.szeba.hades.model.compiler.ProgramCompilerFactory;
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
        compiler = ProgramCompilerFactory.getProgramCompilerByTask(options, currentTask);
    }

    private void compile() {
        compiledProgram = compiler.compile(currentTask);
    }

}
