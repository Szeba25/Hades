package hu.szeba.hades.wizard.view.components;

import hu.szeba.hades.main.util.GridBagSetter;

import javax.swing.*;
import java.awt.*;

public class PlusMinusPanel extends JPanel {

    private JButton plus;
    private JButton minus;

    public PlusMinusPanel() {
        this.setLayout(new GridBagLayout());

        plus = new JButton("+");
        plus.setFocusPainted(false);
        minus = new JButton("-");
        minus.setFocusPainted(false);

        GridBagSetter gs = new GridBagSetter();
        gs.setComponent(this);

        gs.add(plus,
                0,
                0,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                1,
                0,
                new Insets(0, 0, 5, 0));

        gs.add(minus,
                0,
                1,
                GridBagConstraints.HORIZONTAL,
                1,
                1,
                1,
                0,
                new Insets(0, 0, 0, 0));
    }

    public JButton getPlus() {
        return plus;
    }

    public JButton getMinus() {
        return minus;
    }

}
