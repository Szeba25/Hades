package hu.szeba.hades.main.view.components;

import javax.swing.*;

public class InputDialogFactory {

    public static String showCustomInputDialog(String title, String message, String ok, String cancel) {
        Object[] options = {ok, cancel};

        JTextField textField = new JTextField(15);

        JPanel panel = new JPanel();
        panel.add(new JLabel(title));
        panel.add(textField);

        int result = JOptionPane.showOptionDialog(null, panel, message, JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, null);
        if (result == JOptionPane.YES_OPTION) {
            return textField.getText();
        } else {
            return null;
        }
    }

}
