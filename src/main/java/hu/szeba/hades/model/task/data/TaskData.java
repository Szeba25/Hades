package hu.szeba.hades.model.task.data;

import hu.szeba.hades.io.DataFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.program.ProgramInput;
import hu.szeba.hades.model.task.result.Result;
import org.apache.commons.io.FileUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskData {

    private File taskDirectory;
    private File taskWorkingDirectory;

    private final String taskName;
    private final String language;
    private final String syntaxStyle;

    private List<InputResultPair> inputResultPairs;
    private List<SourceFile> sources;

    public TaskData(String taskName, boolean continueTask, String language, String syntaxStyle) throws IOException {
        this.taskDirectory = getTaskDirectory(taskName);
        this.taskWorkingDirectory = getTaskWorkingDirectory(taskName);

        // If not continuing task, but folder exists, delete everything first!
        if (!continueTask && taskWorkingDirectory.exists()) {
            System.out.println("Reset task!");
            FileUtils.deleteDirectory(taskWorkingDirectory);
        }
        // Copy everything if directory does not exists...
        if (!taskWorkingDirectory.exists()) {
            System.out.println("Copy TaskData!");
            FileUtils.forceMkdir(taskWorkingDirectory);
            FileUtils.copyDirectory(taskDirectory, taskWorkingDirectory);
        }

        this.taskName = taskName;
        this.language = language;
        this.syntaxStyle = syntaxStyle;
        inputResultPairs = new ArrayList<>();
        sources = new ArrayList<>();
        makeInputResultPairs();
        makeSources();
    }

    private void makeInputResultPairs() throws IOException {
        DataFile irList = new DataFile(new File(taskWorkingDirectory,
                ".meta/solutions_map.dat"),
                "->");
        for (int i = 0; i < irList.getLineCount(); i++) {
            String inputFileName = irList.getData(i, 0);
            String solutionFileName = irList.getData(i, 1);
            ProgramInput programInput = new ProgramInput(new File(taskWorkingDirectory,
                    ".meta/input_result_pairs/" + inputFileName));
            Result result = new Result(new File(taskWorkingDirectory,
                    ".meta/input_result_pairs/" + solutionFileName));
            inputResultPairs.add(new InputResultPair(programInput, result));
        }
    }

    private void makeSources() throws IOException {
        DataFile sourceList = new DataFile(new File(taskWorkingDirectory, ".meta/sources.dat"));
        for (int i = 0; i < sourceList.getLineCount(); i++) {
            sources.add(new SourceFile(new File(taskWorkingDirectory, sourceList.getData(i, 0))));
        }
    }

    private File getTaskDirectory(String taskName) {
        return new File(Options.getDatabasePath(), "tasks/" + taskName);
    }

    private File getTaskWorkingDirectory(String taskName) {
        return new File(Options.getWorkingDirectoryPath(), "tasks/" + taskName);
    }

    public File getTaskDirectory() {
        return taskDirectory;
    }

    public File getTaskWorkingDirectory() {
        return taskWorkingDirectory;
    }

    public File copyTaskWorkingDirectory() {
        return new File(taskWorkingDirectory.getAbsolutePath());
    }

    public String getTaskName() {
        return taskName;
    }

    public String getSyntaxStyle() {
        return syntaxStyle;
    }

    public List<InputResultPair> copyInputResultPairs() {
        List<InputResultPair> copy = new ArrayList<>();
        for (InputResultPair sol : inputResultPairs) {
            copy.add(new InputResultPair(sol));
        }
        return copy;
    }

    public List<SourceFile> getSources() {
        return sources;
    }

    public void setSourceContents(Map<String, RSyntaxTextArea> codeAreas) {
        sources.forEach((src) -> src.setData(codeAreas.get(src.getName()).getText()));
    }

    public String[] copySourceNames() {
        String[] src = new String[sources.size()];
        for (int i = 0; i < sources.size(); i++) {
            src[i] = sources.get(i).getName();
        }
        return src;
    }

    public void saveSources() throws IOException {
        for (SourceFile sourceFile : sources) {
            sourceFile.save();
        }
    }

}
