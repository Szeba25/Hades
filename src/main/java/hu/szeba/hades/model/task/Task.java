package hu.szeba.hades.model.task;

import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.data.SourceFile;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.result.ResultMatcher;

import java.io.IOException;
import java.util.List;

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

    /*
     * Runs on worker thread!
     */
    public void compile() throws IOException, InterruptedException {
        program = programCompiler.compile(taskData.getSources(), taskData.getTaskWorkingDirectory());
    }

    /*
     * Runs on worker thread!
     */
    public List<String> getCompileMessages() {
        return program.getCompileMessages();
    }

    /*
     * Runs on worker thread!
     */
    public void saveFirstSource() throws IOException {
        taskData.getSources().get(0).save();
    }

    public void run() {}

    public ResultMatcher getResultMatcher() {
        return resultMatcher;
    }

    public String getFirstSourceContent() {
        return taskData.getSources().get(0).getData();
    }

    public void setFirstSourceContent(String data) {
        taskData.getSources().get(0).setData(data);
    }

    public String[] getSourceList() {
        String[] src = new String[taskData.getSources().size()];
        for (int i = 0; i < taskData.getSources().size(); i++) {
            src[i] = taskData.getSources().get(i).getName();
        }
        return src;
    }
}
