package hu.szeba.hades.model.task;

import hu.szeba.hades.io.TabbedFile;
import hu.szeba.hades.meta.Options;
import hu.szeba.hades.meta.User;
import hu.szeba.hades.model.compiler.ProgramCompiler;
import hu.szeba.hades.model.task.data.*;
import hu.szeba.hades.model.task.program.ProgramInput;
import hu.szeba.hades.model.task.result.Result;
import hu.szeba.hades.util.FileUtilities;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Task {

    private final User user;
    private final ProgramCompiler programCompiler;
    private final String language;
    private final String syntaxStyle;

    private final String courseName;
    private final String taskCollectionName;
    private final String taskId;

    private final File taskDirectory;
    private final File taskWorkingDirectory;

    private final TaskDescription taskDescription;

    private List<InputResultPair> inputResultPairs;
    private Set<String> readonlySources;
    private List<SourceFile> sources;

    private final CompilerOutputRegister compilerOutputRegister;

    public Task(User user,
                ProgramCompiler programCompiler,
                String language,
                String syntaxStyle,
                String courseName,
                String taskCollectionName,
                String taskId,
                TaskDescription taskDescription,
                boolean continueTask) throws IOException, MissingResultFileException {

        this.user = user;
        this.programCompiler = programCompiler;
        this.language = language;
        this.syntaxStyle = syntaxStyle;

        this.courseName = courseName;
        this.taskCollectionName = taskCollectionName;
        this.taskId = taskId;

        this.taskDirectory = new File(Options.getDatabasePath(), courseName + "/tasks/" + taskId);
        this.taskWorkingDirectory = new File(user.getUserWorkingDirectoryPath(), courseName + "/" + taskCollectionName + "/" + taskId);

        // If not continuing task, but folder exists, delete the folder first!
        if (!continueTask && taskWorkingDirectory.exists()) {
            System.out.println("Reset task!");
            FileUtils.deleteDirectory(taskWorkingDirectory);
        }

        // Copy sources if task directory does not exists...
        if (!taskWorkingDirectory.exists()) {
            System.out.println("Copy task!");
            FileUtils.forceMkdir(new File(taskWorkingDirectory, "sources"));
            FileUtils.copyDirectory(
                    new File(taskDirectory, "sources"),
                    new File(taskWorkingDirectory, "sources"));
        }

        this.taskDescription = taskDescription;

        this.inputResultPairs = new ArrayList<>();
        this.readonlySources = new HashSet<>();
        this.sources = new ArrayList<>();

        this.compilerOutputRegister = new CompilerOutputRegister();
        this.compilerOutputRegister.setCompilerOutput(programCompiler.getCached(taskWorkingDirectory));

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
                throw new MissingResultFileException(taskId, inputFileName);
            }

            ProgramInput programInput = new ProgramInput(inputFiles.get(i));
            Result result = new Result(resultFile);

            inputResultPairs.add(new InputResultPair(programInput, result));
        }
    }

    private void makeSources() throws IOException {
        TabbedFile file = new TabbedFile(new File(taskDirectory, "readonly_sources.txt"));
        for (int i = 0; i < file.getLineCount(); i++) {
            readonlySources.add(file.getData(i, 0));
        }

        List<File> sourceFiles = new LinkedList<>(FileUtils.listFiles(
                new File(taskWorkingDirectory, "sources"),
                null,
                false));
        for (File sourceFile : sourceFiles) {
            sources.add(new SourceFile(
                    sourceFile,
                    readonlySources.contains(sourceFile.getName())));
        }
    }

    public User getUser() {
        return user;
    }

    public File copyTaskWorkingDirectory() {
        return new File(taskWorkingDirectory.getAbsolutePath());
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTaskCollectionName() {
        return taskCollectionName;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskIdentifierString() {
        return courseName + "/" + taskCollectionName + "/" + taskId;
    }

    public TaskDescription getTaskDescription() {
        return taskDescription;
    }

    public String getLanguage() {
        return language;
    }

    public String getSyntaxStyle() {
        return syntaxStyle;
    }

    public List<InputResultPair> copyInputResultPairs() {
        List<InputResultPair> copy = new ArrayList<>();
        for (InputResultPair irp : inputResultPairs) {
            copy.add(new InputResultPair(irp));
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

    public void deleteSourceByName(String name) throws IOException {
        for (int i = sources.size()-1; i >= 0; i--) {
            if (sources.get(i).getName().equals(name) && !sources.get(i).isReadonly()) {
                sources.remove(i).delete();
                break;
            }
        }
    }

    public void setSourceContents(Map<String, JTextArea> codeAreas) {
        sources.forEach((src) -> src.setData(codeAreas.get(src.getName()).getText()));
    }

    public String[] copySourceNamesWithPath() {
        String[] src = new String[sources.size()];
        for (int i = 0; i < sources.size(); i++) {
            src[i] = "sources/" + sources.get(i).getName();
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

    public Set<String> copyReadonlySources() {
        return new HashSet<>(readonlySources);
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
        SourceFile source = new SourceFile(
                new File(taskWorkingDirectory + "/sources/" + name),
                readonlySources.contains(name));
        source.save();

        // Add the source file if its path is valid!
        sources.add(source);
        return source;
    }

    public ProgramCompiler getProgramCompiler() {
        return programCompiler;
    }

    public CompilerOutputRegister getCompilerOutputRegister() {
        return compilerOutputRegister;
    }

}

