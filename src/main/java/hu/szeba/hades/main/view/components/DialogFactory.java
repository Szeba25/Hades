package hu.szeba.hades.main.view.components;

import javax.swing.*;

public class DialogFactory {

    public static void showCustomMessage(String message, String title) {
        JOptionPane.showMessageDialog(
                new JFrame(), message, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void showCustomWarning(String message, String title) {
        JOptionPane.showMessageDialog(
                new JFrame(), message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void showCustomError(String message, String title) {
        JOptionPane.showMessageDialog(
                new JFrame(), message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static String showCustomInputDialog(String initial, String message, String title, String ok, String cancel) {
        Object[] options = {ok, cancel};

        JTextField textField = new JTextField(15);

        JPanel panel = new JPanel();
        panel.add(new JLabel(message));
        panel.add(textField);
        textField.setText(initial);

        int result = JOptionPane.showOptionDialog(null, panel, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        if (result == JOptionPane.YES_OPTION) {
            return textField.getText();
        } else {
            return null;
        }
    }

    public static int showCustomChoiceDialog(String message, String title, String ok, String cancel) {
        Object[] obj = {ok, cancel};
        return JOptionPane.showOptionDialog(new JFrame(), message, title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                obj, obj[0]);
    }

}
