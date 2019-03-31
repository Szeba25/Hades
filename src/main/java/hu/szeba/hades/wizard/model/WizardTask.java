package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.DescriptionFile;
import hu.szeba.hades.main.io.TabbedFile;
import hu.szeba.hades.main.model.task.data.SourceFile;
import hu.szeba.hades.main.util.FileUtilities;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WizardTask {

    private String taskId;
    private File taskPath;
    private DescriptionFile description;

    private SourceFile readonlySources;
    private SourceFile regExIncludeFile;
    private SourceFile regExExcludeFile;

    private Map<String, SourceFile> inputFiles;
    private Map<String, SourceFile> resultFiles;

    private Map<String, SourceFile> sourceFiles;
    private Map<String, SourceFile> solutionFiles;

    public WizardTask(String taskId, File taskPath, DescriptionFile description) throws IOException {
        this.taskId = taskId;
        this.taskPath = taskPath;
        this.description = description;

        this.readonlySources = new SourceFile(new File(taskPath, "readonly_sources.txt"), false);
        this.regExIncludeFile = new SourceFile(new File(taskPath, "regex/include.txt"), false);
        this.regExExcludeFile = new SourceFile(new File(taskPath, "regex/exclude.txt"), false);

        // Load input result pairs
        this.inputFiles = new HashMap<>();
        this.resultFiles = new HashMap<>();

        File inputResultFolder = new File(taskPath, "input_result_pairs");
        for (String fileName : inputResultFolder.list()) {
            String pureFileName = FileUtilities.getFileNameWithoutExtension(fileName);
            SourceFile finalFile = new SourceFile(new File(taskPath, "input_result_pairs/" + fileName), false);
            if (FileUtilities.getFileNameExtension(fileName).equals("input")) {
                inputFiles.put(pureFileName, finalFile);
            } else {
                resultFiles.put(pureFileName, finalFile);
            }
        }

        // Load source files
        this.sourceFiles = new HashMap<>();
        File sourceFilesFolder = new File(taskPath, "sources");
        for (String fileName : sourceFilesFolder.list()) {
            sourceFiles.put(fileName, new SourceFile(new File(taskPath, "sources/" + fileName), false));
        }

        // Load solution files
        this.solutionFiles = new HashMap<>();
        File solutionFilesFolder = new File(taskPath, "solutions");
        for (String fileName : solutionFilesFolder.list()) {
            solutionFiles.put(fileName, new SourceFile(new File(taskPath, "solutions/" + fileName), false));
        }
    }

    public void save() throws IOException {
        description.save();

        readonlySources.save();
        regExIncludeFile.save();
        regExExcludeFile.save();

        // First delete all changeable data
        File inputResultFolder = new File(taskPath, "input_result_pairs");
        for (File file : inputResultFolder.listFiles()) {
            file.delete();
        }
        File sourceFilesFolder = new File(taskPath, "sources");
        for (File file : sourceFilesFolder.listFiles()) {
            file.delete();
        }
        File solutionFilesFolder = new File(taskPath, "solutions");
        for (File file : solutionFilesFolder.listFiles()) {
            file.delete();
        }

        // And save them all again
        for (SourceFile sf : inputFiles.values()) {
            sf.save();
        }
        for (SourceFile sf : resultFiles.values()) {
            sf.save();
        }
        for (SourceFile sf : sourceFiles.values()) {
            sf.save();
        }
        for (SourceFile sf : solutionFiles.values()) {
            sf.save();
        }
    }

    public String getTitle() {
        return description.getTitle();
    }

    public String getDifficulty() {
        return description.getDifficulty();
    }

    public String getLength() {
        return description.getLength();
    }

    public String getTags() {
        return String.join("\n", description.getTags());
    }

    public String getReadonlySourcesData() {
        return readonlySources.getData();
    }

    public String getRegExIncludeData() {
        return regExIncludeFile.getData();
    }

    public String getRegExExcludeData() {
        return regExExcludeFile.getData();
    }

    public String getInputFileData(String name) {
        return inputFiles.get(name).getData();
    }

    public String getResultFileData(String name) {
        return resultFiles.get(name).getData();
    }

    public List<String> getInputResultFileNames() {
        List<String> list = new ArrayList<>(inputFiles.keySet());
        list.sort(Comparator.naturalOrder());
        return list;
    }

    public boolean isInputResultFileExists(String name) {
        return inputFiles.containsKey(name) && resultFiles.containsKey(name);
    }

    public void setTitle(String title) {
        description.setTitle(title);
    }

    public void setDifficulty(String difficulty) {
        description.setDifficulty(difficulty);
    }

    public void setLength(String length) {
        description.setLength(length);
    }

    public void setTags(String text) {
        String[] tags = text.split("\n");
        description.getTags().clear();
        description.getTags().addAll(Arrays.asList(tags));
    }

    public void setReadonlySourcesData(String data) {
        readonlySources.setData(data);
    }

    public void setRegExIncludeData(String data) {
        regExIncludeFile.setData(data);
    }

    public void setRegExExcludeData(String data) {
        regExExcludeFile.setData(data);
    }

    public void setInputFileData(String name, String data) {
        inputFiles.get(name).setData(data);
    }

    public void setResultFileData(String name, String data) {
        resultFiles.get(name).setData(data);
    }

    public void addInputResultFile(String name) throws IOException {
        SourceFile inputSource = new SourceFile(new File(taskPath, "input_result_pairs/" + name + ".input"), false);
        SourceFile resultSource = new SourceFile(new File(taskPath, "input_result_pairs/" + name + ".result"), false);
        inputFiles.put(name, inputSource);
        resultFiles.put(name, resultSource);
    }

    public void removeInputResultFile(String name) {
        inputFiles.remove(name);
        resultFiles.remove(name);
    }

    public void renameInputResultFile(String name, String newName) throws IOException {
        inputFiles.put(newName, inputFiles.remove(name));
        resultFiles.put(newName, resultFiles.remove(name));
        inputFiles.get(newName).rename(newName + ".input", false);
        resultFiles.get(newName).rename(newName + ".result", false);
    }

    public Map<String, SourceFile> getSourceFiles() {
        return sourceFiles;
    }

    public Map<String, SourceFile> getSolutionFiles() {
        return solutionFiles;
    }

    public File getTaskPath() {
        return taskPath;
    }

}
