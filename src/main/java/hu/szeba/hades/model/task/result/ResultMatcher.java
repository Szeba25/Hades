package hu.szeba.hades.model.task.result;

import java.util.LinkedList;
import java.util.List;

public class ResultMatcher {

    private List<ResultDifference> differences;

    public ResultMatcher() {
        differences = new LinkedList<>();
    }

    public void match(Result referenceResult, Result desiredResult) {
        differences.clear();

        int referenceResultCount = referenceResult.getResultLineCount();
        int desiredResultCount = desiredResult.getResultLineCount();

        int runningIndex = Math.max(referenceResultCount, desiredResultCount);

        for (int i = 0; i < runningIndex; i++) {
            if (i > referenceResultCount - 1) {
                // No reference here
                differences.add(new ResultDifference(i,
                        new ResultLine("EMPTY"),
                        desiredResult.getResultLine(i)));
            } else if (i > desiredResultCount - 1) {
                // No desired result here
                differences.add(new ResultDifference(i,
                        referenceResult.getResultLine(i),
                        new ResultLine("EMPTY")));
            } else {
                if (!(referenceResult.getResultLine(i).matches(desiredResult.getResultLine(i)))) {
                    // No match in this line.
                    differences.add(new ResultDifference(i,
                            referenceResult.getResultLine(i),
                            desiredResult.getResultLine(i)));
                }
            }
        }
    }

    public ResultDifference getDifference(int index) {
        return differences.get(index);
    }

    public int getDifferencesSize() {
        return differences.size();
    }

}
