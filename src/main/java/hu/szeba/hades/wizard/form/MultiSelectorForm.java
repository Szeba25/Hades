package hu.szeba.hades.wizard.form;

import hu.szeba.hades.main.meta.Languages;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.elements.MappedElement;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MultiSelectorForm extends JDialog {

    private JPanel mainPanel;
    private JList<MappedElement> list;
    private JButton okButton;

    public MultiSelectorForm(String title) {
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setTitle(title);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(300, 500));
        this.setResizable(true);
        this.setModal(true);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        list = new JList<>();
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setModel(new DefaultListModel<>());
        JScrollPane taskCollectionListScroll = new JScrollPane(list);

        okButton = new JButton(Languages.translate("Select"));
        okButton.setFocusPainted(false);

        okButton.addActionListener((e) -> {
            setVisible(false);
        });

        GridBagSetter gs = new GridBagSetter();

        gs.setComponent(mainPanel);

        gs.add(taskCollectionListScroll,
                0,
                0,
                GridBagConstraints.BOTH,
                1,
                1,
                1,
                1,
                new Insets(5, 5, 5, 5));

        gs.add(okButton,
                0,
                1,
                GridBagConstraints.NONE,
                1,
                1,
                0,
                0,
                new Insets(0, 5, 5, 5));

        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    public void setListContents(List<MappedElement> elements) {
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) list.getModel();
        model.removeAllElements();

        for (MappedElement element : elements) {
            model.addElement(element);
        }
    }

    public List<MappedElement> getSelectedElements() {
        return list.getSelectedValuesList();
    }

    public void changeTitleById(String id, String title) {
        DefaultListModel<MappedElement> model = (DefaultListModel<MappedElement>) list.getModel();
        for (int i = 0; i < model.size(); i++) {
            if (model.getElementAt(i).getId().equals(id)) {
                model.getElementAt(i).setTitle(title);
                break;
            }
        }
    }

}
