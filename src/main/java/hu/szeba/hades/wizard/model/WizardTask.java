package hu.szeba.hades.wizard.model;

import hu.szeba.hades.main.io.DescriptionFile;
import hu.szeba.hades.main.model.task.data.SourceFile;
import hu.szeba.hades.main.util.FileUtilities;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WizardTask {

    private String taskId;
    private File taskPath;
    private DescriptionFile description;

    private SourceFile regExIncludeFile;
    private SourceFile regExExcludeFile;

    private Map<String, SourceFile> inputFiles;
    private Map<String, SourceFile> resultFiles;

    public WizardTask(String taskId, File taskPath, DescriptionFile description) throws IOException {
        this.taskId = taskId;
        this.taskPath = taskPath;
        this.description = description;
        this.regExIncludeFile = new SourceFile(new File(taskPath, "regex/include.txt"), false);
        this.regExExcludeFile = new SourceFile(new File(taskPath, "regex/exclude.txt"), false);

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
    }

    public void save() throws IOException {
        description.save();
        regExIncludeFile.save();
        regExExcludeFile.save();
        for (SourceFile sf : inputFiles.values()) {
            sf.save();
        }
        for (SourceFile sf : resultFiles.values()) {
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
        inputFiles.put(name, new SourceFile(new File(taskPath, "input_result_pairs/" + name + ".input"), false));
        resultFiles.put(name, new SourceFile(new File(taskPath, "input_result_pairs/" + name + ".result"), false));
    }

}
