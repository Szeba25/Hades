package hu.szeba.hades.model.task.result;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private List<ResultLine> resultLines;

    public Result() {
        resultLines = new ArrayList<>();
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
