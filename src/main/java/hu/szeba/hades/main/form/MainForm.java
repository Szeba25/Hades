package hu.szeba.hades.main.form;

import hu.szeba.hades.main.meta.Languages;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.meta.User;
import hu.szeba.hades.main.model.course.CourseDatabase;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.components.JButtonGuarded;
import hu.szeba.hades.main.view.components.ViewableFrame;
import hu.szeba.hades.main.view.TaskSelectorView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;

public class MainForm extends JFrame implements ViewableFrame {

    private JPanel mainPanel;
    private JComboBox<String> userBox;
    private JPasswordField passwordField;
    private JButtonGuarded signInButton;

    public MainForm() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(520, 300));
        this.setTitle(Languages.translate("Welcome!"));
        this.setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        JLabel mainLabel = new JLabel(Languages.translate("Welcome! Please sign in!"));
        mainLabel.setFont(mainLabel.getFont().deriveFont(18f));

        userBox = new JComboBox<>();
        userBox.setEditable(true);

        passwordField = new JPasswordField();
        passwordField.setEnabled(false);

        signInButton = new JButtonGuarded(Languages.translate("Sign in"));
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

        gs.add(new JLabel(Languages.translate("User:")),
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

        gs.add(new JLabel(Languages.translate("Password:")),
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

        gs.add(new JLabel(Languages.translate("Development build")),
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
                if (signInButton.getActionGuard().isGuarded()) {
                    return;
                }
                signInButton.getActionGuard().guard();

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

    private void refreshUserBox() {
        userBox.removeAllItems();
        for (String userFolder : Options.getWorkingDirectoryPath().list()) {
            if (!userFolder.equals("DEFAULT")) {
                userBox.addItem(userFolder);
            }
        }
        userBox.addItem("DEFAULT");
        userBox.setSelectedIndex(0);
    }

    @Override
    public void showView() {
        refreshUserBox();

        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.requestFocus();
        signInButton.getActionGuard().reset();
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
