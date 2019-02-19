package hu.szeba.hades.controller.task;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public interface SourceUpdaterForClosableTabs {

    void updateSourceFileDataFromCodeArea(String name, RSyntaxTextArea codeArea);

}
