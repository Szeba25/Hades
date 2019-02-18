package hu.szeba.hades.model.task.data;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.program.ProgramInput;
import hu.szeba.hades.model.task.result.Result;
import hu.szeba.hades.util.FileUtilities;
import org.apache.commons.io.FileUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TaskData {

    private File taskDirectory;
    private File taskWorkingDirectory;

    private final String taskName;
    private final TaskDescription taskDescription;
    private final String language;
    private final String syntaxStyle;

    private List<InputResultPair> inputResultPairs;
    private List<SourceFile> sources;

    public TaskData(String taskName,
                    TaskDescription taskDescription,
                    boolean continueTask,
                    String language,
                    String syntaxStyle) throws IOException, MissingResultFileException {
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
        this.taskDescription = taskDescription;
        this.language = language;
        this.syntaxStyle = syntaxStyle;
        inputResultPairs = new ArrayList<>();
        sources = new ArrayList<>();
        makeInputResultPairs();
        makeSources();
    }

    private void makeInputResultPairs() throws IOException, MissingResultFileException {
        String[] extInput = { "input" };
        List<File> inputFiles = new LinkedList<>(FileUtils.listFiles(
                new File(taskDirectory, "input_result_pairs"),
                extInput,
                false));

        for (int i = 0; i < inputFiles.size(); i++) {
            String inputFileName = inputFiles.get(i).getName();
            String resultFileName = FileUtilities.getFileNameWithoutExtension(inputFileName) + ".result";

            File resultFile = new File(taskDirectory, "input_result_pairs/" + resultFileName);

            if (!resultFile.exists()) {
                inputResultPairs.clear();
                throw new MissingResultFileException(taskName, inputFileName);
            }

            ProgramInput programInput = new ProgramInput(inputFiles.get(i));
            Result result = new Result(resultFile);

            inputResultPairs.add(new InputResultPair(programInput, result));
        }
    }

    private void makeSources() throws IOException {
        List<File> sourceFiles = new LinkedList<>(FileUtils.listFiles(
                new File(taskWorkingDirectory, "sources/" + language),
                null,
                false));
        for (File sourceFile : sourceFiles) {
            sources.add(new SourceFile(sourceFile));
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

    public TaskDescription getTaskDescription() {
        return taskDescription;
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

    public SourceFile getSourceByName(String name) {
        for (SourceFile src : sources) {
            if (src.getName().equals(name)) {
                return src;
            }
        }
        return null;
    }

    public void setSourceContents(Map<String, RSyntaxTextArea> codeAreas) {
        sources.forEach((src) -> src.setData(codeAreas.get(src.getName()).getText()));
    }

    public String[] copySourceNamesWithPath() {
        String[] src = new String[sources.size()];
        for (int i = 0; i < sources.size(); i++) {
            src[i] = "sources/" + language + "/" + sources.get(i).getName();
        }
        return src;
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

    public SourceFile addSource(String name) throws IOException {
        for (SourceFile src : sources) {
            if (src.getName().equals(name)) {
                return null;
            }
        }
        SourceFile source = new SourceFile(new File(taskWorkingDirectory + "/sources/" + language + "/" + name));
        sources.add(source);
        return source;
    }

}
