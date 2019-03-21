package hu.szeba.hades.wizard.view.components;

import hu.szeba.hades.main.util.GridBagSetter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DynamicButtonPanel extends JPanel {

    private List<JButton> buttons;

    public DynamicButtonPanel(String... buttonTitles) {
        this.setLayout(new GridBagLayout());

        buttons = new ArrayList<>();

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(this);

        for (int i = 0; i < buttonTitles.length; i++) {
            JButton button = new JButton(buttonTitles[i]);
            button.setFocusPainted(false);
            gs.add(button,
                    i,
                    0,
                    GridBagConstraints.HORIZONTAL,
                    1,
                    1,
                    1,
                    0,
                    new Insets(0, 0, 0, 5));
            buttons.add(button);
        }

    }

    public JButton getButton(int index) {
        return buttons.get(index);
    }

}
