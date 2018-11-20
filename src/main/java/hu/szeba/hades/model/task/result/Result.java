package hu.szeba.hades.model.task.result;

import hu.szeba.hades.io.TabbedFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Result {

    private List<ResultLine> resultLines;

    public Result() {
        resultLines = new ArrayList<>();
    }

    public Result(File file) throws IOException {
        resultLines = new ArrayList<>();

        TabbedFile tabbedFile = new TabbedFile(file);
        for (int i = 0; i < tabbedFile.getLineCount(); i++) {
            ResultLine resultLine = new ResultLine(tabbedFile.getData(i, 0));
            resultLines.add(resultLine);
        }
    }

    public void addResultLine(ResultLine resultLine) {
        resultLines.add(resultLine);
    }

    public ResultLine getResultLine(int index) {
        return resultLines.get(index);
    }

    public int getResultLineCount() {
        return resultLines.size();
    }

}
