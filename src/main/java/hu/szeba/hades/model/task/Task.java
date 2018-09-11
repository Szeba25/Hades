package hu.szeba.hades.model.task;

import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.model.task.program.Program;

public abstract class Task {

    protected TaskData taskData;
    protected ProgramCompiler programCompiler;
    protected Program program;

    public Task(TaskData taskData) {
        this.taskData = taskData;
        programCompiler = createCompiler();
        program = null;

        // TODO: Remove this!
        System.out.println("Task successfully created!");
    }

    abstract ProgramCompiler createCompiler();

}
