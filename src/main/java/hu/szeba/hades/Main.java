package hu.szeba.hades;

import hu.szeba.hades.main.form.MainForm;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.view.components.ViewableFrame;
import hu.szeba.hades.wizard.view.CourseView;

import javax.swing.*;
import java.io.IOException;

public class Main {

    private String[] args;
    private ViewableFrame mainForm;
    private ViewableFrame courseView;

    private Main(String[] args) {
        this.args = args;
    }

    private void start() {
        try {
            Options.initialize();
            if (args.length > 0 && args[0].equals("wizard")) {
                courseView = new CourseView();
                courseView.showView();
            } else {
                mainForm = new MainForm();
                mainForm.showView();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main(args);
            main.start();
        });
    }

}
