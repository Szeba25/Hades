package hu.szeba.hades.model.task;

import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.data.SourceFile;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.model.task.program.Program;
import hu.szeba.hades.model.task.result.ResultMatcher;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
     * Will run on worker thread!
     */
    public void run() {}

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

    public String getLanguage() {
        return taskData.getLanguage();
    }

    public String getSyntaxStyle() {
        return "text/" + taskData.getLanguage().toLowerCase();
    }
}

