package hu.szeba.hades.wizard.view;

import hu.szeba.hades.view.ViewableFrame;
import hu.szeba.hades.wizard.components.GraphEditorPanel;

import javax.swing.*;
import java.awt.*;

public class GraphEditorTestView extends JFrame implements ViewableFrame {

    private GraphEditorPanel graphEditor;

    public GraphEditorTestView() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Graph editor");
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(800, 600));
        this.setResizable(false);

        graphEditor = new GraphEditorPanel();

        this.getContentPane().add(graphEditor, BorderLayout.CENTER);

        this.pack();
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
