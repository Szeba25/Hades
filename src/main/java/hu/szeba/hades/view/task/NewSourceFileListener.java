package hu.szeba.hades.view.task;

import java.io.IOException;

public interface NewSourceFileListener {

    void addNewSourceFileTrigger(String name) throws IOException;

}
