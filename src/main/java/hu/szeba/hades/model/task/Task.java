package hu.szeba.hades.model.task;

import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.result.ResultMatcher;

import java.io.IOException;

public class Task {

    private TaskData taskData;
    private ProgramCompiler programCompiler;
    private Program program;
    private ResultMatcher resultMatcher;

    public Task(TaskData taskData, ProgramCompiler programCompiler) {
        this.taskData = taskData;
        this.programCompiler = programCompiler;
        program = null;
        resultMatcher = new ResultMatcher();
    }

    public void compile() throws IOException, InterruptedException {
        program = programCompiler.compile(taskData.getSources(), taskData.getTaskWorkingDirectory());
    }

    public void run() {}

    public ResultMatcher getResultMatcher() {
        return resultMatcher;
    }
}
