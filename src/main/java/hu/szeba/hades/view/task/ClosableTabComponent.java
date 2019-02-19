package hu.szeba.hades.view.task;

import hu.szeba.hades.controller.task.SourceUpdaterForClosableTabs;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;

public class ClosableTabComponent extends JPanel {

    private final JTabbedPane pane;
    private final SourceUpdaterForClosableTabs sourceUpdaterForClosableTabs;

    public ClosableTabComponent(final JTabbedPane pane, final SourceUpdaterForClosableTabs sourceUpdaterForClosableTabs) {
        // Unset default FlowLayout gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        this.sourceUpdaterForClosableTabs = sourceUpdaterForClosableTabs;
        setOpaque(false);

        // Make JLabel read titles from JTabbedPane
        JLabel label = new JLabel() {
            public String getText() {
                int i = pane.indexOfTabComponent(ClosableTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };

        add(label);
        // Add more space between the label and the button
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        // Tab button
        JButton button = new TabButton(sourceUpdaterForClosableTabs);
        add(button);
        // Add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    private class TabButton extends JButton implements ActionListener {

        private SourceUpdaterForClosableTabs controller;

        public TabButton(SourceUpdaterForClosableTabs controller) {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close");
            // Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            // Make it transparent
            setContentAreaFilled(false);
            // No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            // Making nice rollover effect
            // We use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            // Close the proper tab by clicking the button
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ClosableTabComponent.this);
            if (i != -1) {
                // Before we remove the tab, save it's data!
                //RTextScrollPane scrollPane = (RTextScrollPane) pane.getComponentAt(i);
                //controller.updateSourceFileDataFromCodeArea();
                pane.remove(i);
            }
        }

        // We don't want to update UI for this button
        public void updateUI() { }

        // Paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            // Shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.GRAY);
            if (getModel().isRollover()) {
                g2.setColor(Color.BLACK);
            }
            int delta = 5;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }

    }

    private final static MouseListener buttonMouseListener = new MouseAdapter() {

        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }

    };

}
