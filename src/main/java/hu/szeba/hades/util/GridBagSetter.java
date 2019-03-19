package hu.szeba.hades.util;

import javax.swing.*;
import java.awt.*;

public class GridBagSetter {

    private JComponent dest;
    private GridBagConstraints c;

    public GridBagSetter() {
        c = new GridBagConstraints();
    }

    public void setComponent(JComponent dest) {
        this.dest = dest;
    }

    public void add(JComponent other, int gridx, int gridy, int fill, int gridwidth, int gridheight, double weightx, double weighty, Insets insets) {
        this.add(other, gridx, gridy, fill, gridwidth, gridheight, weightx, weighty, insets, c.anchor = GridBagConstraints.CENTER);
    }

    public void add(JComponent other, int gridx, int gridy, int fill, int gridwidth, int gridheight, double weightx, double weighty, Insets insets, int anchor) {
        c.gridx = gridx;
        c.gridy = gridy;
        c.fill = fill;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.weightx = weightx;
        c.weighty = weighty;
        c.insets = insets;
        c.anchor = anchor;
        dest.add(other, c);
    }

}
