package hu.szeba.hades;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.view.entry.MainView;

import javax.swing.*;
import java.io.IOException;

public class Main {

    private MainView view;

    private Main() {
        try {
            Options.initialize();
            view = new MainView();
            view.showView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

}
