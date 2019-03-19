package hu.szeba.hades.main.view.entry;

import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.meta.User;
import hu.szeba.hades.main.model.course.CourseDatabase;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.JButtonGuarded;
import hu.szeba.hades.main.view.ViewableFrame;
import hu.szeba.hades.main.view.task.TaskSelectorView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;

public class MainView extends JFrame implements ViewableFrame {

    private JPanel mainPanel;
    private JComboBox<String> userBox;
    private JPasswordField passwordField;
    private JButtonGuarded signInButton;

    public MainView() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(520, 300));
        this.setTitle("Welcome!");
        this.setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        JLabel mainLabel = new JLabel("Welcome! Please sign in!");
        mainLabel.setFont(mainLabel.getFont().deriveFont(18f));

        userBox = new JComboBox<>();
        userBox.setEditable(true);
        for (String userFolder : Options.getWorkingDirectoryPath().list()) {
            userBox.addItem(userFolder);
        }
        userBox.setSelectedIndex(0);

        passwordField = new JPasswordField();
        passwordField.setEnabled(false);

        signInButton = new JButtonGuarded("Sign in");
        signInButton.setFocusPainted(false);
        signInButton.setPreferredSize(new Dimension(120, 25));

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(mainPanel);

        gs.add(mainLabel,
                0,
                0,
                GridBagConstraints.NONE,
                2,
                1,
                0,
                0,
                new Insets(15, 30, 25, 30));

        gs.add(new JLabel("User:"),
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(0, 30, 0, 30));

        gs.add(userBox,
                1,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(0, 30, 0, 30));

        gs.add(new JLabel("Password:"),
                0,
                2,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(15, 30, 0, 30));

        gs.add(passwordField,
                1,
                2,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                0,
                new Insets(15, 30, 0, 30));

        gs.add(signInButton,
                0,
                3,
                GridBagConstraints.NONE,
                2,
                1,
                1,
                0.5,
                new Insets(20, 30, 0, 30));

        gs.add(new JSeparator(JSeparator.HORIZONTAL),
                0,
                4,
                GridBagConstraints.HORIZONTAL,
                2,
                1,
                1.0,
                0,
                new Insets(15, 10, 40, 10));

        gs.add(new JLabel("Development build"),
                0,
                5,
                GridBagConstraints.NONE,
                2,
                1,
                1.0,
                0,
                new Insets(0, 0, 5, 5),
                GridBagConstraints.SOUTHEAST);

        signInButton.addActionListener((event) -> {
            try {
                User user = new User((String) userBox.getSelectedItem());
                TaskSelectorView taskSelectorView = new TaskSelectorView(this, new CourseDatabase(user));
                this.hideView();
                taskSelectorView.showView();
            } catch (IOException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        });

        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.pack();
    }

    @Override
    public void showView() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.requestFocus();
    }

    @Override
    public void showViewMaximized() {
        showView();
    }

    @Override
    public void hideView() {
        this.setVisible(false);
    }

}
