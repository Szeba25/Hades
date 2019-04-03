package hu.szeba.hades.wizard.view.components;

import hu.szeba.hades.main.meta.Languages;
import hu.szeba.hades.main.util.GridBagSetter;
import hu.szeba.hades.main.view.components.JButtonGuarded;

import javax.swing.*;
import java.awt.*;

public class AddEditDeletePanel extends JPanel {

    private JButtonGuarded add;
    private JButtonGuarded edit;
    private JButtonGuarded delete;

    public AddEditDeletePanel() {
        this.setLayout(new GridBagLayout());

        add = new JButtonGuarded(Languages.translate("Add"));
        add.setFocusPainted(false);
        edit = new JButtonGuarded(Languages.translate("Edit"));
        edit.setFocusPainted(false);
        delete = new JButtonGuarded(Languages.translate("Delete"));
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

    public JButtonGuarded getAdd() {
        return add;
    }

    public JButtonGuarded getEdit() {
        return edit;
    }

    public JButtonGuarded getDelete() {
        return delete;
    }

}
