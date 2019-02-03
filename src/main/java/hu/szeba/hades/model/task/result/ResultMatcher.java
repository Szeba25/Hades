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
                differences.add(new ResultDifference(i + 1,
                        new ResultLine(""),
                        desiredResult.getResultLineByIndex(i)));
            } else if (i > desiredResultCount - 1) {
                // No desired result here
                differences.add(new ResultDifference(i + 1,
                        referenceResult.getResultLineByIndex(i),
                        new ResultLine("")));
            } else {
                if (!(referenceResult.getResultLineByIndex(i).matches(desiredResult.getResultLineByIndex(i)))) {
                    // No match in this line.
                    differences.add(new ResultDifference(i + 1,
                            referenceResult.getResultLineByIndex(i),
                            desiredResult.getResultLineByIndex(i)));
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
