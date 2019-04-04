package hu.szeba.hades.main.controller;

import hu.szeba.hades.main.meta.AudioPlayer;
import hu.szeba.hades.main.meta.Languages;
import hu.szeba.hades.main.meta.Options;
import hu.szeba.hades.main.meta.TaskSolverAgent;
import hu.szeba.hades.main.model.task.data.InputResultPair;
import hu.szeba.hades.main.model.task.program.Program;
import hu.szeba.hades.main.model.task.result.Result;
import hu.szeba.hades.main.model.task.result.ResultDifference;
import hu.szeba.hades.main.model.task.result.ResultMatcher;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskRunnerWork implements Work {

    private TaskSolverAgent agent;
    private Program program;
    private List<InputResultPair> inputResultPairs;
    private int maxByteCount;
    private AtomicBoolean stopFlag;

    public TaskRunnerWork(TaskSolverAgent agent,
                          Program program,
                          List<InputResultPair> inputResultPairs,
                          int maxByteCount,
                          AtomicBoolean stopFlag) {
        this.agent = agent;
        this.program = program;
        this.inputResultPairs = inputResultPairs;
        this.maxByteCount = maxByteCount;
        this.stopFlag = stopFlag;
    }

    @Override
    public void execute(Publisher publisher) throws IOException, InterruptedException {
        publisher.customPublish(">>> " + Languages.translate("Running program...") + "\n\n");

        ResultMatcher matcher = new ResultMatcher();

        for (InputResultPair inputResultPair : inputResultPairs) {
            publisher.customPublish(">>> " + Languages.translate("Using input:") + " " + inputResultPair.getProgramInput().getFile().getName() + "\n");
            Result result = program.run(inputResultPair.getProgramInput(), maxByteCount, stopFlag);

            if (stopFlag.get()) {
                publisher.customPublish("~> " + Languages.translate("Process force closed!") + "\n");
                return;
            }

            if (!result.anyInputPresent()) {
                matcher.noResponseHappened();
                publisher.customPublish("~> " + Languages.translate("No response...") + "\n\n");
            } else {
                if (result.getDebugLineCount() > 0) {
                    publisher.customPublish("\n> " + Languages.translate("Debug log:") + "\n");
                }
                for (int i = 0; i < result.getDebugLineCount(); i++) {
                    publisher.customPublish("@" + (i + 1) + ". " + result.getDebugLineByIndex(i) + "\n");
                }
                publisher.customPublish("\n> " + Languages.translate("Output:") + "\n");
                for (int i = 0; i < result.getResultLineCount(); i++) {
                    publisher.customPublish("#" + (i + 1) + ". " + result.getResultLineByIndex(i).getData() + "\n");
                }
                publisher.customPublish("\n");
                matcher.match(result, inputResultPair.getDesiredResult());
                for (int i = 0; i < matcher.getDifferencesSize(); i++) {
                    ResultDifference diff = matcher.getDifference(i);
                    publisher.customPublish("~* " + Languages.translate("difference at line:") + " " + diff.getLineNumber() +
                            ". \"" + diff.getFirstLine().getData() + "\"" + Languages.translate("should be") + " \""
                            + diff.getSecondLine().getData() + "\"\n");
                }
                if (matcher.getDifferencesSize() > 0) {
                    publisher.customPublish("\n");
                }
            }
        }

        if (agent.isCurrentTaskCompleted()) {
            publisher.customPublish("#> " + Languages.translate("Task was already completed...") + " (" + matcher.getAllDifferencesCount() +
                    " " + Languages.translate("differences, and") +
                    " " + matcher.getAllNoResponsesCount() + " " + Languages.translate("no responses") + ")\n\n");
        } else if (matcher.getAllDifferencesCount() == 0 && matcher.getAllNoResponsesCount() == 0) {
            if (Options.getConfigBooleanData("wow")) {
                new AudioPlayer(new File("resources/wow.wav")).play();
            }
            agent.markCurrentTaskAsCompleted();
            publisher.customPublish("#> " + Languages.translate("Task successfully COMPLETED!") +
                    " " + Languages.translate("(no errors)") + "\n\n");
        } else {
            publisher.customPublish("~> " + Languages.translate("There were a total of") + " " + matcher.getAllDifferencesCount() +
                    " " + Languages.translate("differences, and") +
                    " " + matcher.getAllNoResponsesCount() + " " + Languages.translate("no responses") + "\n\n");
        }

        publisher.customPublish(Languages.translate("... End of running!"));
    }

}
