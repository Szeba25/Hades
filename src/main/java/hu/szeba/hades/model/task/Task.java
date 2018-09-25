package hu.szeba.hades.model.task;

import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.result.ResultMatcher;

public class Task {

    protected TaskData taskData;
    protected ProgramCompiler programCompiler;
    protected Program program;
    protected ResultMatcher resultMatcher;

    public Task(TaskData taskData, ProgramCompiler programCompiler) {
        this.taskData = taskData;
        this.programCompiler = programCompiler;
        program = null;
        resultMatcher = new ResultMatcher();

        // TODO: Remove this!
        System.out.println("Task successfully created!");
        System.out.println("Task directory: " + taskData.getTaskDirectory().getAbsolutePath());
        System.out.println("Task working directory: " + taskData.getTaskWorkingDirectory().getAbsolutePath());
    }

}
