package hu.szeba.hades.main.model.task;

import hu.szeba.hades.main.io.DescriptionFile;
import hu.szeba.hades.main.io.EditableTextFile;
import hu.szeba.hades.main.io.SingleDataFile;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.meta.TaskSolverAgent;
import hu.szeba.hades.main.model.compiler.ProgramCompiler;
import hu.szeba.hades.main.model.task.data.*;
import hu.szeba.hades.main.model.task.program.ProgramInput;
import hu.szeba.hades.main.model.task.result.Result;
import hu.szeba.hades.main.util.FileUtilities;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Task {

    private final TaskSolverAgent agent;
    private final ProgramCompiler programCompiler;
    private final String language;
    private final String syntaxStyle;

    private final String courseId;
    private final String modeId;
    private final String taskCollectionId;
    private final String taskId;

    private final File taskDirectory;
    private final File taskWorkingDirectory;

    private final DescriptionFile taskDescription;

    private List<InputResultPair> inputResultPairs;
    private Set<String> readonlySources;
    private List<EditableTextFile> sources;

    private final CompilerOutputRegister compilerOutputRegister;

    public Task(TaskSolverAgent agent,
                ProgramCompiler programCompiler,
                String language,
                String syntaxStyle,
                String courseId,
                String modeId,
                String taskCollectionId,
                String taskId,
                DescriptionFile taskDescription,
                boolean continueTask) throws IOException, MissingResultFileException {

        this.agent = agent;
        this.programCompiler = programCompiler;
        this.language = language;
        this.syntaxStyle = syntaxStyle;

        this.courseId = courseId;
        this.modeId = modeId;
        this.taskCollectionId = taskCollectionId;
        this.taskId = taskId;

        this.taskDirectory = new File(Options.getDatabasePath(), courseId + "/tasks/" + taskId);
        this.taskWorkingDirectory = agent.getCurrentTaskWorkingDirectoryPath();

        // If not continuing task, but folder exists, delete the folder first!
        if (!continueTask && taskWorkingDirectory.exists()) {
            System.out.println("Task: Reset!");
            FileUtils.deleteDirectory(taskWorkingDirectory);
        }

        // Copy sources if task directory does not exists...
        if (!taskWorkingDirectory.exists()) {
            System.out.println("Task: Copy!");
            FileUtils.forceMkdir(new File(taskWorkingDirectory, "sources"));
            FileUtils.copyDirectory(
                    new File(taskDirectory, "sources"),
                    new File(taskWorkingDirectory, "sources"));
            agent.markCurrentTaskAsStarted();
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
        List<File> inputFiles = new ArrayList<>(FileUtils.listFiles(
                new File(taskDirectory, "input_result_pairs"),
                extInput,
                false));

        for (int i = 0; i < inputFiles.size(); i++) {
            String inputFileName = inputFiles.get(i).getName();
            String resultFileName = FileUtilities.getFileNameWithoutExtension(inputFileName) + ".result";

            File resultFile = new File(taskDirectory, "input_result_pairs/" + resultFileName);

            if (!resultFile.exists()) {
                inputResultPairs.clear();
                throw new MissingResultFileException(inputFileName);
            }

            ProgramInput programInput = new ProgramInput(inputFiles.get(i));
            Result result = new Result(resultFile);

            inputResultPairs.add(new InputResultPair(programInput, result));
        }
    }

    private void makeSources() throws IOException {
        SingleDataFile file = new SingleDataFile(new File(taskDirectory, "readonly_sources.dat"));
        for (int i = 0; i < file.getLineCount(); i++) {
            readonlySources.add(file.getData(i, 0));
        }

        List<File> sourceFiles = new ArrayList<>(FileUtils.listFiles(
                new File(taskWorkingDirectory, "sources"),
                null,
                false));
        for (File sourceFile : sourceFiles) {
            sources.add(new EditableTextFile(
                    sourceFile,
                    readonlySources.contains(sourceFile.getName())));
        }
    }

    public TaskSolverAgent getAgent() {
        return agent;
    }

    public File copyTaskWorkingDirectory() {
        return new File(taskWorkingDirectory.getAbsolutePath());
    }

    public DescriptionFile getTaskDescription() {
        return taskDescription;
    }

    public String getLanguage() {
        return language;
    }

    public String getSyntaxStyle() {
        return syntaxStyle;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getModeId() {
        return modeId;
    }

    public String getTaskCollectionId() {
        return taskCollectionId;
    }

    public String getTaskId() {
        return taskId;
    }

    public List<InputResultPair> copyInputResultPairs() {
        List<InputResultPair> copy = new ArrayList<>();
        for (InputResultPair irp : inputResultPairs) {
            copy.add(new InputResultPair(irp));
        }
        return copy;
    }

    public List<EditableTextFile> getSources() {
        return sources;
    }

    public EditableTextFile getSourceByName(String name) {
        for (EditableTextFile src : sources) {
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
        for (EditableTextFile sf : sources) {
            sf.setData(codeAreas.get(sf.getName()).getText());
        }
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
        for (EditableTextFile sourceFile : sources) {
            sourceFile.save();
        }
    }

    public EditableTextFile addSource(String name) throws IOException {
        for (EditableTextFile src : sources) {
            if (src.getName().equals(name)) {
                return null;
            }
        }
        EditableTextFile source = new EditableTextFile(
                new File(taskWorkingDirectory + "/sources/" + name),
                readonlySources.contains(name));
        source.save();

        // Add the source file if its path is valid!
        sources.add(source);
        return source;
    }

    public void renameSource(String oldName, String newName) throws IOException {
        EditableTextFile src = getSourceByName(oldName);
        src.rename(newName);
    }

    public ProgramCompiler getProgramCompiler() {
        return programCompiler;
    }

    public CompilerOutputRegister getCompilerOutputRegister() {
        return compilerOutputRegister;
    }

}

