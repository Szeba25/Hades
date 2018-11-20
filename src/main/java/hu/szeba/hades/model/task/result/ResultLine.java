package hu.szeba.hades.model.task.result;

public class ResultLine {

    private String data;

    public ResultLine(String data) {
        this.data = data;
    }

    public ResultLine(ResultLine other) {
        this.data = other.data;
    }

    public String getData() {
        return data;
    }

    public boolean matches(ResultLine otherLine) {
        return data.equals(otherLine.data);
    }

}
