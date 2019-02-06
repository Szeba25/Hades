package hu.szeba.hades.view.task;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class TerminalArea {

    private JTextPane textPane;
    private SimpleAttributeSet outputAttribute;
    private SimpleAttributeSet debugAttribute;
    private SimpleAttributeSet errorAttribute;

    public TerminalArea(JTextPane textPane) {
        this.textPane = textPane;
        textPane.setEditable(false);
        textPane.setBackground(new Color(225, 225, 225));

        outputAttribute = new SimpleAttributeSet();
        StyleConstants.setForeground(outputAttribute, Color.BLACK);
        StyleConstants.setFontFamily(outputAttribute, "Monospaced");
        StyleConstants.setFontSize(outputAttribute, 14);

        debugAttribute = new SimpleAttributeSet();
        StyleConstants.setForeground(debugAttribute, new Color(20, 90, 20));
        StyleConstants.setFontFamily(debugAttribute, "Monospaced");
        StyleConstants.setFontSize(debugAttribute, 14);

        errorAttribute = new SimpleAttributeSet();
        StyleConstants.setForeground(errorAttribute, Color.RED);
        StyleConstants.setFontFamily(errorAttribute, "Monospaced");
        StyleConstants.setFontSize(errorAttribute, 14);
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
                } else {
                    document.insertString(document.getLength(), line, outputAttribute);
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

}
