package hu.szeba.hades.main.view;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class TerminalArea {

    private JTextPane textPane;
    private SimpleAttributeSet defaultAttribute;
    private SimpleAttributeSet outputAttribute;
    private SimpleAttributeSet debugAttribute;
    private SimpleAttributeSet errorAttribute;

    public TerminalArea(JTextPane textPane) {
        this.textPane = textPane;
        textPane.setEditable(false);
        textPane.setBackground(new Color(225, 225, 225));

        defaultAttribute = new SimpleAttributeSet();
        StyleConstants.setForeground(defaultAttribute, Color.BLACK);
        StyleConstants.setFontFamily(defaultAttribute, "Monospaced");
        StyleConstants.setFontSize(defaultAttribute, 14);

        outputAttribute = new SimpleAttributeSet();
        StyleConstants.setForeground(outputAttribute, new Color(28, 102, 19));
        StyleConstants.setFontFamily(outputAttribute, "Monospaced");
        StyleConstants.setFontSize(outputAttribute, 14);
        StyleConstants.setBold(outputAttribute, true);

        debugAttribute = new SimpleAttributeSet();
        StyleConstants.setForeground(debugAttribute, new Color(33, 42, 145));
        StyleConstants.setFontFamily(debugAttribute, "Monospaced");
        StyleConstants.setFontSize(debugAttribute, 14);
        StyleConstants.setBold(debugAttribute, true);

        errorAttribute = new SimpleAttributeSet();
        StyleConstants.setForeground(errorAttribute, Color.RED);
        StyleConstants.setFontFamily(errorAttribute, "Monospaced");
        StyleConstants.setFontSize(errorAttribute, 14);
        StyleConstants.setItalic(errorAttribute, true);
    }

    public void clear() {
        textPane.setText("");
    }

    public void add(String line) {
        StyledDocument document = textPane.getStyledDocument();
        try {
            if (line.length() > 0) {
                if (line.charAt(0) == '~') {
                    document.insertString(document.getLength(), line.substring(1), errorAttribute);
                } else if (line.charAt(0) == '@') {
                    document.insertString(document.getLength(), line.substring(1), debugAttribute);
                } else if (line.charAt(0) == '#') {
                    document.insertString(document.getLength(), line.substring(1), outputAttribute);
                } else {
                    document.insertString(document.getLength(), line, defaultAttribute);
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

}
