package hu.szeba.hades.main.model.task.result;

public class ResultDifference {

    private int lineNumber;
    private ResultLine firstLine;
    private ResultLine secondLine;

    public ResultDifference(int lineNumber, ResultLine firstLine, ResultLine secondLine) {
        this.lineNumber = lineNumber;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public ResultLine getFirstLine() {
        return firstLine;
    }

    public ResultLine getSecondLine() {
        return secondLine;
    }
}
