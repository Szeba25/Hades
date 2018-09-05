package hu.szeba.hades;

import hu.szeba.hades.gui.task.TaskSelector;
import hu.szeba.hades.meta.Options;

import javax.swing.*;
import java.io.File;

public class Main {

    private Options options;
    private TaskSelector taskSelector;

    private Main() {
        options = new Options(
                new File("D:/Egyetem/MinGW/bin"),
                new File("D:/Egyetem/Szakdolgozat/hades_Campaigns"),
                new File("D:/Egyetem/Szakdolgozat/hades_WorkingDirectory"));
        taskSelector = new TaskSelector();
        showTaskSelector();
    }

    private void showTaskSelector() {
        taskSelector.show();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

}
