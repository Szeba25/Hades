package hu.szeba.hades.controller.task;

import hu.szeba.hades.meta.Options;
import hu.szeba.hades.model.task.Task;
import hu.szeba.hades.model.task.data.TaskData;
import hu.szeba.hades.view.task.BuildMenuWrapper;
import hu.szeba.hades.view.task.TaskSolvingView;
import hu.szeba.hades.view.task.TerminalArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskSolvingController {

    private Task task;
    private AtomicBoolean stopFlag;

    public TaskSolvingController(Task task) {
        this.task = task;
        this.stopFlag = new AtomicBoolean(false);
    }

    public void setSourceList(TaskSolvingView taskSolvingView) {
        TaskData data = task.getData();
        taskSolvingView.setTaskInstructions(data.getTaskDescription().getLongDescription());
        taskSolvingView.setSourceList(data.copySourceNames(), data.getSyntaxStyle());
        taskSolvingView.setCodeAreaContents(data.getSources());
    }

    public void compile(Map<String, RSyntaxTextArea> codeAreas,
                        TerminalArea terminalArea, BuildMenuWrapper buildMenuWrapper) throws IOException {
        // Set the sources content and save sources on EDT
        TaskData data = task.getData();
        data.setSourceContents(codeAreas);
        data.saveSources();

        // Clear terminal, and disable build menu
        terminalArea.clear();
        buildMenuWrapper.setBuildEnabled(false);
        buildMenuWrapper.setBuildAndRunEnabled(false);
        buildMenuWrapper.setRunEnabled(false);

        // Start a worker thread to compile the task!
        TaskCompilerWorker taskCompilerWorker = new TaskCompilerWorker(
                task.getCompilerOutputRegister(),
                task.getProgramCompiler(),
                data.copySourceNamesWithPath(),
                data.copyTaskWorkingDirectory(),
                buildMenuWrapper,
                terminalArea);
        taskCompilerWorker.execute();
    }

    public void compileAndRun(Map<String, RSyntaxTextArea> codeAreas,
                              TerminalArea terminalArea, BuildMenuWrapper buildMenuWrapper) throws IOException {
        // Set the sources content and save sources on EDT
        TaskData data = task.getData();
        data.setSourceContents(codeAreas);
        data.saveSources();

        // Clear terminal, and disable build menu
        terminalArea.clear();
        buildMenuWrapper.setBuildEnabled(false);
        buildMenuWrapper.setBuildAndRunEnabled(false);
        buildMenuWrapper.setRunEnabled(false);
        buildMenuWrapper.setStopEnabled(true);
        stopFlag.set(false);

        // Start a worker thread to compile the task!
        TaskCompilerAndRunnerWorker taskCompilerAndRunnerWorker = new TaskCompilerAndRunnerWorker(
                task.getCompilerOutputRegister(),
                task.getProgramCompiler(),
                data.copyInputResultPairs(),
                data.copySourceNamesWithPath(),
                data.copyTaskWorkingDirectory(),
                buildMenuWrapper,
                terminalArea,
                Options.getConfigIntData("max_stream_byte_count"),
                stopFlag);
        taskCompilerAndRunnerWorker.execute();
    }

    public void run(TerminalArea terminalArea, BuildMenuWrapper buildMenuWrapper) {
        // Clear terminal, and disable build menu
        terminalArea.clear();
        buildMenuWrapper.setBuildEnabled(false);
        buildMenuWrapper.setBuildAndRunEnabled(false);
        buildMenuWrapper.setRunEnabled(false);
        buildMenuWrapper.setStopEnabled(true);
        stopFlag.set(false);

        // Start a worker thread to run the task!
        TaskRunnerWorker taskRunnerWorker = new TaskRunnerWorker(
                task.getCompilerOutputRegister().getCompilerOutput().getProgram(),
                task.getData().copyInputResultPairs(),
                buildMenuWrapper,
                terminalArea,
                Options.getConfigIntData("max_stream_byte_count"),
                stopFlag);
        taskRunnerWorker.execute();
    }

    public void stopCurrentProcess(TerminalArea terminalArea) {
        if (!stopFlag.get()) {
            stopFlag.set(true);
            terminalArea.add("> Stopping running process...\n");
        }
    }

}
