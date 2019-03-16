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
        c.gridx = gridx;
        c.gridy = gridy;
        c.fill = fill;
        c.gridwidth = gridwidth;
        c.gridheight = gridheight;
        c.weightx = weightx;
        c.weighty = weighty;
        c.insets = insets;
        dest.add(other, c);
    }

}
