package hu.szeba.hades.main.model.task.result;

import hu.szeba.hades.main.io.TabbedFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Result {

    private List<ResultLine> resultLines;
    private List<String> debugLines;

    public Result() {
        resultLines = new ArrayList<>();
        debugLines = new ArrayList<>();
    }

    public Result(File file) throws IOException {
        resultLines = new ArrayList<>();

        TabbedFile tabbedFile = new TabbedFile(file);
        for (int i = 0; i < tabbedFile.getLineCount(); i++) {
            ResultLine resultLine = new ResultLine(tabbedFile.getData(i, 0));
            resultLines.add(resultLine);
        }
    }

    public Result(Result other) {
        this.resultLines = new ArrayList<>();

        for (ResultLine resultLine : other.resultLines) {
            this.resultLines.add(new ResultLine(resultLine));
        }
    }

    public void addResultLine(ResultLine resultLine) {
        resultLines.add(resultLine);
    }

    public ResultLine getResultLineByIndex(int index) {
        return resultLines.get(index);
    }

    public int getResultLineCount() {
        return resultLines.size();
    }

    public void addDebugLine(String debugLine) {
        debugLines.add(debugLine);
    }

    public String getDebugLineByIndex(int index) {
        return debugLines.get(index);
    }

    public int getDebugLineCount() {
        return debugLines.size();
    }

    public boolean anyInputPresent() {
        return (resultLines.size() > 0 || debugLines.size() > 0);
    }

    public void clearAll() {
        resultLines.clear();
        debugLines.clear();
    }
}
