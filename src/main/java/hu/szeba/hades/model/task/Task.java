package hu.szeba.hades.model.task;

import hu.szeba.hades.model.compiler.CompilerOutput;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.data.SourceFile;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.result.Result;
import hu.szeba.hades.model.task.result.ResultMatcher;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Task {

    private TaskData taskData;
    private ProgramCompiler programCompiler;
    private CompilerOutput compilerOutput;
    private ResultMatcher resultMatcher;

    public Task(TaskData taskData, ProgramCompiler programCompiler) {
        this.taskData = taskData;
        this.programCompiler = programCompiler;
        this.compilerOutput = programCompiler.getCached(taskData.getTaskWorkingDirectory());
        this.resultMatcher = new ResultMatcher();
    }

    /*
     * Runs on worker thread!
     */
    public void compile() throws IOException, InterruptedException {
        compilerOutput = programCompiler.compile(taskData.getSources(), taskData.getTaskWorkingDirectory());
    }

    /*
     * Runs on worker thread!
     */
    public List<String> getCompileMessages() {
        return compilerOutput.getCompileMessages();
    }

    /*
     * Will run on worker thread!
     */
    public Result run() throws IOException, InterruptedException {
        return compilerOutput.getProgram().run(null);
    }

    public void saveSources() throws IOException {
        for (SourceFile sourceFile : taskData.getSources()) {
            sourceFile.save();
        }
    }

    public ResultMatcher getResultMatcher() {
        return resultMatcher;
    }

    public String[] getSourceFileNameList() {
        String[] src = new String[taskData.getSources().size()];
        for (int i = 0; i < taskData.getSources().size(); i++) {
            src[i] = taskData.getSources().get(i).getName();
        }
        return src;
    }

    public List<SourceFile> getSourceFiles() {
        return taskData.getSources();
    }

    public void setSourceContents(Map<String, RSyntaxTextArea> codeAreas) {
        List<SourceFile> sources = taskData.getSources();
        sources.forEach((src) -> src.setData(codeAreas.get(src.getName()).getText()));
    }

    public String getSyntaxStyle() {
        return taskData.getSyntaxStyle();
    }

    public String getTaskName() {
        return taskData.getTaskName();
    }

    public boolean isProgramReady() {
        return compilerOutput.isReady();
    }
}

