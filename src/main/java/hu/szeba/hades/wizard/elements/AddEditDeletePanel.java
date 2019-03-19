package hu.szeba.hades.wizard.elements;

import hu.szeba.hades.util.GridBagSetter;

import javax.swing.*;
import java.awt.*;

public class AddEditDeletePanel extends JPanel {

    private JButton add;
    private JButton edit;
    private JButton delete;

    public AddEditDeletePanel() {
        this.setLayout(new GridBagLayout());

        add = new JButton("Add");
        add.setFocusPainted(false);
        edit = new JButton("Edit");
        edit.setFocusPainted(false);
        delete = new JButton("Delete");
        delete.setFocusPainted(false);

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(this);

        gs.add(add,
                0,
                0,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                1,
                0,
                new Insets(0, 0, 5, 0));

        gs.add(edit,
                0,
                1,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                1,
                0,
                new Insets(0, 0, 5, 0));

        gs.add(delete,
                0,
                2,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                1,
                0,
                new Insets(0, 0, 0, 0));
    }

    public JButton getAdd() {
        return add;
    }

    public JButton getEdit() {
        return edit;
    }

    public JButton getDelete() {
        return delete;
    }

}
