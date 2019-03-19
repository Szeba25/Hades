package hu.szeba.hades.wizard.view;

import hu.szeba.hades.util.GridBagSetter;
import hu.szeba.hades.view.ViewableFrame;
import hu.szeba.hades.wizard.elements.AddEditDeletePanel;
import hu.szeba.hades.wizard.elements.DescriptiveElement;
import hu.szeba.hades.wizard.form.ModeEditorForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CourseEditorView extends JFrame implements ViewableFrame {

    private ViewableFrame parentView;

    private JPanel topPanel;
    private JTextField titleField;
    private JComboBox<String> languageBox;

    private JPanel centerPanel;
    private JList<DescriptiveElement> modeList;
    private AddEditDeletePanel modeModifier;

    private JList<DescriptiveElement> taskCollectionList;
    private AddEditDeletePanel taskCollectionModifier;

    private JList<DescriptiveElement> taskList;
    private AddEditDeletePanel taskModifier;

    public CourseEditorView(ViewableFrame parentView) {
        this.parentView = parentView;

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Wizard: Course editor");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(1000, 650));
        this.setResizable(false);

        initializeComponents();
        setupEvents();

        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);
        this.pack();
    }

    private void initializeComponents() {
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField();
        titleLabel.setLabelFor(titleField);

        JLabel languageLabel = new JLabel("Programming language:");
        languageBox = new JComboBox<>();
        languageBox.setEditable(true);
        languageLabel.setLabelFor(languageBox);

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(topPanel);

        gs.add(titleLabel,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(titleField,
                1,
                0,
                GridBagConstraints.HORIZONTAL,
                2,
                1,
                1,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(languageLabel,
                0,
                1,
                GridBagConstraints.BOTH,
                1,
                1,
                0,
                0,
                new Insets(5, 5, 0, 5));

        gs.add(languageBox,
                1,
                1,
                GridBagConstraints.HORIZONTAL,
                2,
                1,
                1,
                0,
                new Insets(5, 5, 0, 5));

        centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());

        modeList = new JList<>();
        JScrollPane modeListScroller = new JScrollPane(modeList);
        modeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        modeList.setFixedCellWidth(200);

        modeModifier = new AddEditDeletePanel();

        taskCollectionList = new JList<>();
        JScrollPane taskCollectionScroller = new JScrollPane(taskCollectionList);
        taskCollectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskCollectionList.setFixedCellWidth(200);

        taskCollectionModifier = new AddEditDeletePanel();

        taskList = new JList<>();
        JScrollPane taskListScroller = new JScrollPane(taskList);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFixedCellWidth(200);

        taskModifier = new AddEditDeletePanel();

        gs.setComponent(centerPanel);

        gs.add(new JLabel("All modes:"),
                0,
                0,
                GridBagConstraints.BOTH,
                2,
                1,
                0,
                0,
                new Insets(25, 5, 0, 0));

        gs.add(modeListScroller,
                0,
                1,
                GridBagConstraints.VERTICAL,
                1,
                1,
                0,
                1,
                new Insets(5, 5, 5, 5));

        gs.add(modeModifier,
                1,
                1,
                GridBagConstraints.VERTICAL,
                1,
                1,
                0,
                1,
                new Insets(5, 5, 5, 30));

        gs.add(new JLabel("All task collections:"),
                2,
                0,
                GridBagConstraints.BOTH,
                2,
                1,
                0,
                0,
                new Insets(25, 5, 0, 0));

        gs.add(taskCollectionScroller,
                2,
                1,
                GridBagConstraints.VERTICAL,
                1,
                1,
                0,
                1,
                new Insets(5, 5, 5, 5));

        gs.add(taskCollectionModifier,
                3,
                1,
                GridBagConstraints.VERTICAL,
                1,
                1,
                0,
                1,
                new Insets(5, 5, 5, 30));

        gs.add(new JLabel("All tasks:"),
                4,
                0,
                GridBagConstraints.BOTH,
                2,
                1,
                0,
                0,
                new Insets(25, 5, 0, 0));

        gs.add(taskListScroller,
                4,
                1,
                GridBagConstraints.VERTICAL,
                1,
                1,
                0,
                1,
                new Insets(5, 5, 5, 5));

        gs.add(taskModifier,
                5,
                1,
                GridBagConstraints.VERTICAL,
                1,
                1,
                0,
                1,
                new Insets(5, 5, 5, 30));
    }

    private void setupEvents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                CourseEditorView.this.dispose();
                parentView.showView();
            }
        });

        this.modeModifier.getAdd().addActionListener((e) -> {
            ModeEditorForm form = new ModeEditorForm();
            form.setLocationRelativeTo(this);
            form.setVisible(true);
        });
    }

    @Override
    public void showView() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.requestFocus();
    }

    @Override
    public void showViewMaximized() {
        this.showView();
    }

    @Override
    public void hideView() {
        this.setVisible(false);
    }

}
