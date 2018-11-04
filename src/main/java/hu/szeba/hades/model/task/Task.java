package hu.szeba.hades.model.task;

import hu.szeba.hades.model.compiler.CompilerOutput;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.data.SourceFile;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.model.task.result.ResultMatcher;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Task implements CompilerOutputRegister {

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

    public ProgramCompiler getProgramCompiler() {
        return programCompiler;
    }

    public CompilerOutput getCompilerOutput() {
        return compilerOutput;
    }

    public File getTaskWorkingDirectoryCopy() {
        return new File(taskData.getTaskWorkingDirectory().getAbsolutePath());
    }

    public String getSyntaxStyle() {
        return taskData.getSyntaxStyle();
    }

    public String getTaskName() {
        return taskData.getTaskName();
    }

    @Override
    public void registerCompilerOutput(CompilerOutput compilerOutput) {
        this.compilerOutput = compilerOutput;
    }

}

