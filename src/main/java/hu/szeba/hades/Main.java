package hu.szeba.hades;

import hu.szeba.hades.gui.MainGUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI mainGUI = new MainGUI();
        });
    }

}
