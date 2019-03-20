package hu.szeba.hades;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.view.ViewableFrame;
import hu.szeba.hades.wizard.view.GraphEditorTestView;

import javax.swing.*;
import java.io.IOException;

public class Main {

    private String[] args;
    private ViewableFrame mainForm;
    private ViewableFrame courseView;
    private ViewableFrame graphEditorTestView;

    private Main(String[] args) {
        this.args = args;
    }

    private void start() {
        try {
            Options.initialize();

            //mainForm = new MainForm();
            //mainForm.showView();

            //courseView = new CourseView();
            //courseView.showView();

            graphEditorTestView = new GraphEditorTestView();
            graphEditorTestView.showView();

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
