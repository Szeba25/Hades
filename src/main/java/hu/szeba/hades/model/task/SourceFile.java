package hu.szeba.hades.model.task;

public class SourceFile {

    private String name;
    private String data;

    public SourceFile(String name) {
        this.name = name;
        this.data = "";
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void save() {
        System.out.println("NYI source file saving");
    }

}
